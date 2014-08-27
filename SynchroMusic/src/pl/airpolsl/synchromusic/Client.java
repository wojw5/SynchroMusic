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
	
	/**
	 * Creates new client connection in network. New socket will be created.
	 * @param a client address
	 * @param p client port
	 * @param nContext android context
	 */
	public Client(InetAddress a, int p, Context nContext) {
		this.context = nContext;
		this.port = p;
		this.address = a;
	    Log.d(TAG, "Creating singleClient");
	
	    mSendThread = new Thread(new SendingThread());
	    mSendThread.start();
	}
	
	/**
	 *  Creates new client connection in network. Based on passed socket.
	 * @param client existing socket
	 * @param nContext android context
	 */
	public Client(Socket client, Context nContext) {
		this.context = nContext;
		clientSocket = client;
		this.port = clientSocket.getPort();
		this.address = clientSocket.getInetAddress();
	    Log.d(TAG, "Creating singleClient");
	
	    mSendThread = new Thread(new SendingThread());
	    mSendThread.start();
	}
	
	/**
	 * To be used in separate threads in internal thread classes.
	 * @return this Client object
	 */
	private Client getSelfClient(){
		return this;
	}
	
	/**
	 * Class for send messages to Client in background.
	 */
	private class SendingThread implements Runnable {
	
		private int QUEUE_CAPACITY = 10;
		
		/**
		 * Initializes packet queue.
		 */
		public SendingThread() {
			mPacketQueue = new ArrayBlockingQueue<Object>(QUEUE_CAPACITY);
		}
		
		@Override
		/**
		 * Opens socket to client if needed and sending messages from queue.
		 */
		public void run(){
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
					break;
				} catch (Exception e) {
					break;
				}
			}
		}
	}
	
	/**
	 * Class for receive messages from Client in background.
	 */
	private class ReceivingThread implements Runnable {
		
		@Override
		/**
		 * Opens input stream and getting a messages from client
		 */
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
				mPacketQueue=null;
				return;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }
	
	/**
	 * Close client connection.
	 */
	public void tearDown() {
		try {
			clientSocket.close();
		} catch (IOException ioe) {
			Log.e(TAG, "Error when closing client socket.");
		}
	}
	//TODO move to send thread?
	/**
	 * Opens stream and sending packet to client.
	 * @param packet any serializable object to send
	 * @throws Exception
	 */
    private void sendPacket(Object packet) throws Exception{
		if (out==null){
				try {
					out = new ObjectOutputStream(clientSocket.getOutputStream());
				} catch (IOException e) {
					throw e;
				}
		}

		try {
			out.writeObject(packet);
		} catch (IOException e) {
			throw e;
		}
	}
	
    /**
     * Adds object to sending queue
     * @param packet
     * @throws Exception
     */
	public void send(Object packet) throws Exception{
		if (mPacketQueue!=null) mPacketQueue.add(packet);
		else {
			Log.d(TAG,packet.toString() +" to " + address.getHostAddress());
			throw new Exception("Link broken");
		}
	}
}
