package pl.airpolsl.synchromusic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import android.util.Log;

/**
 * Contains information about particular client connection.
 * Class beside fields implements also separate threads to handle client socket connection i background.
 * @author Wojciech Widenka
 *
 */
public class Client extends User {
	
	private InetAddress address;
	private int port;
	
	private Socket clientSocket;

    private Thread mSendThread;
    private Thread mRecThread;
    
    private BlockingQueue<Packet> mPacketQueue=null; // TODO needed?
    
    private static final String TAG = "User";
    
    public Client(InetAddress a, int p) {

    	this.port = p;
    	this.address = a;
        Log.d(TAG, "Creating singleClient");

        mSendThread = new Thread(new SendingThread());
        mSendThread.start();
    }
    
    public Client(Socket client) {

    	clientSocket = client;
    	this.port = clientSocket.getPort();
    	this.address = clientSocket.getInetAddress();
        Log.d(TAG, "Creating singleClient");

        mSendThread = new Thread(new SendingThread());
        mSendThread.start();
    }

    private class SendingThread implements Runnable {

        
        private int QUEUE_CAPACITY = 10;

        public SendingThread() {
        	mPacketQueue = new ArrayBlockingQueue<Packet>(QUEUE_CAPACITY);
        }

        @Override
        public void run() {
        	try {
				clientSocket = new Socket(address,port);
				Log.d(TAG,"Sending thread socket created.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	
        	
        	
            mRecThread = new Thread(new ReceivingThread());
            mRecThread.start();

            while (true) {
                try {
                    Packet msg = mPacketQueue.take();
                    Log.d(TAG,"Sending packet:" + msg.toString());
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

                    Packet receivedPacket = null;
                    receivedPacket = (Packet) input.readObject();
                    if (receivedPacket != null) {
                        Log.d(TAG, "Read from the stream: " + receivedPacket.toString());
                        //TODO process packed method
                    } else {
                        Log.d(TAG, "The nulls! The nulls!");
                        break;
                    }
                }
                input.close();
                
            } catch (IOException e) {
                Log.e(TAG, "Server loop error: ", e);
            } catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
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

    private void sendPacket(Packet packet) {
        ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(clientSocket.getOutputStream());
			out.writeObject(packet);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Log.d(TAG, "Message sent: " + packet.toString());
    }
    
    public void send(Packet packet){
    	if (mPacketQueue!=null) mPacketQueue.add(packet);
    }
}