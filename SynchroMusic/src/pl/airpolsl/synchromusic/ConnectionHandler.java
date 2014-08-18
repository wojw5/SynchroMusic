package pl.airpolsl.synchromusic;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
/**
 * Class is responsible for connection. Implements thread for incoming connections.
 * @author Wojciech Widenka
 *
 */
public class ConnectionHandler {

	private Handler handler=null;
	private ServerSocket serverSocket=null;
	private Socket clientSocket=null;
	private Thread serverThread=null;
	private Client boss=null;
	private Clients clients=null;
	
	private Context context;
	private static final String TAG = "ConnectionHandler";
	
	/*
	 * Construct device ServerSocket to be able to receive data.
	 */
	public ConnectionHandler(Context nContext){
		context = nContext;
		try {
			serverSocket = new ServerSocket(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		init();
	}
	
	/*
	 * Take control over existing server socket to manage connections.
	 */
	public ConnectionHandler(ServerSocket serverSocket,Context nContext){
		context = nContext;
		this.serverSocket=serverSocket;
		init();
	}
	
	/*
	 * Set SynchroMusic server. 
	 */
	public void setBoss(InetAddress address, int port){
			boss = new Client(address,port,context);
			SynchroMusicProtocol.sendWelcome(boss, context);
	}
	
	/**
	 * Send packet to SynchroMusic server
	 * @param packet
	 */
	public void sendToBoss(Packet packet){
		boss.send(packet);
	}
	
	public void sendToClients(Track track){
		for (Client client : clients.getList()) {
			SynchroMusicProtocol.sendTrack(client, track);
		}
	}
	public void sendToClients(Packet packet){
		for (Client client : clients.getList()) {
			SynchroMusicProtocol.sendPacket(packet,client);
		}
	}
	
	/*
	 * Start server thread
	 */
	private void init(){
		Log.d(TAG,"Starting connection handler with server socket: "+ serverSocket.getInetAddress() +":"+serverSocket.getLocalPort());
		clients = new Clients();
		serverThread = new Thread(new ServerThread());
		serverThread.start();
	}
	
	public Clients getClients(){
		return clients;
	}
	
	/**
	 * Thread for incoming connections.
	 * @author Wojciech Widenka
	 *
	 */
	private class ServerThread implements Runnable {

        @Override
        public void run() {
                
            while (!Thread.currentThread().isInterrupted()) {
                Log.d(TAG, "ServerSocket Created, awaiting connection");
                try {
					clientSocket=serverSocket.accept();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                Log.d(TAG, "Connected.");
                if(boss==null) clients.addNew(new Client(clientSocket,context)); //TODO what with that client? maybe add to clients list.
                
                //else
                
            }
        }
    }

}
