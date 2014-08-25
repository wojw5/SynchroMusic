package pl.airpolsl.synchromusic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import android.content.Context;
import android.util.Log;

/**
 * Contains information about particular client connection.
 * Class beside fields implements also separate threads to handle client socket connection i background.
 * @author Wojciech Widenka
 *
 */
public class Client {
	
	private InetAddress address;
	private int port;
	
	private Socket clientSocket;

	private Thread mSendThread;
	private Thread mRecThread;
	
	private BlockingQueue<Object> mPacketQueue=null;
	private ObjectOutputStream out;
	private static final String TAG = "Client";
	private Context context;
	
	public Client(InetAddress a, int p, Context nContext) {
		this.context = nContext;
		this.port = p;
		this.address = a;
	    Log.d(TAG, "Creating singleClient");
	
	    mSendThread = new Thread(new SendingThread());
	    mSendThread.start();
	}
	
	public Client(Socket client, Context nContext) {
		this.context = nContext;
		clientSocket = client;
		this.port = clientSocket.getPort();
		this.address = clientSocket.getInetAddress();
	    Log.d(TAG, "Creating singleClient");
	
	    mSendThread = new Thread(new SendingThread());
	    mSendThread.start();
	}
	
	private Client getSelfClient(){
		return this;
	}
	
	private class SendingThread implements Runnable {
	
	    
	    private int QUEUE_CAPACITY = 10;
	
	    public SendingThread() {
	    	mPacketQueue = new ArrayBlockingQueue<Object>(QUEUE_CAPACITY);
	    }
	
	    @Override
	    public void run() {
	    	try {
				if (clientSocket == null) clientSocket = new Socket(address,port);
				Log.d(TAG,"Sending thread socket created.");
			} catch (IOException e) {
				e.printStackTrace();
			}

			mRecThread = new Thread(new ReceivingThread());
			mRecThread.start();

			while (true) {
				try {
					Object msg = mPacketQueue.take();
					Log.d(TAG,"T:" + msg.toString());
					sendPacket(msg);
				} catch (InterruptedException ie) {
					Log.d(TAG, "Message sending loop interrupted, exiting");
				}
			}
		}
	}

	private class ReceivingThread implements Runnable {
		
		@Override
		public void run() {
			ObjectInputStream input;
		
			try {
				input = new ObjectInputStream(clientSocket.getInputStream());
				
				while (!Thread.currentThread().isInterrupted()) {
				
					Object receivedPacket = null;
					receivedPacket =  input.readObject();
					if (receivedPacket != null) {
						if (receivedPacket instanceof Packet)
							SynchroMusicProtocol.processPacket(
									(Packet) receivedPacket,context,getSelfClient());
						else Log.d(TAG,"Received Object: " + receivedPacket.toString());
					} else {
						Log.d(TAG, "The nulls! The nulls!");
						break;
					}
				}
				input.close();

			} catch (IOException e) {
				Log.e(TAG, "Server loop error: ", e);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
    }

	public void tearDown() {
		try {
			clientSocket.close();
		} catch (IOException ioe) {
			Log.e(TAG, "Error when closing client socket.");
		}
	}

    private void sendPacket(Object packet) {
		if (out==null){
				try {
					out = new ObjectOutputStream(clientSocket.getOutputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		try {
			out.writeObject(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(Object packet){
		if (mPacketQueue!=null) mPacketQueue.add(packet);
	}
}
