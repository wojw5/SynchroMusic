package pl.airpolsl.synchromusic;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import android.content.Context;
import android.util.Log;
/**
 * Class is responsible for connection. Implements thread for incoming connections.
 * @author Wojciech Widenka
 *
 */
public class ConnectionHandler {

	private ServerSocket serverSocket=null;
	private Socket clientSocket=null;
	private Thread serverThread=null;
	private Client boss=null;
	private Clients clients=null;
	//private Thread sendMulticast=null;
	
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
			try {
				SynchroMusicProtocol.sendWelcome(boss, context);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * Send packet to SynchroMusic server
	 * @param packet
	 */
	public void sendToBoss(Packet packet){
		try {
			boss.send(packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendToClients(Track track){
		for (Client client : clients.getList()) {
			try {
				SynchroMusicProtocol.sendTrack(client, track);
			} catch (Exception e) {
				clients.remove(client);
				e.printStackTrace();
			}
		}
	}
	public void sendToClients(Packet packet){
		// if (packet instanceof StartPlayingPacket) SendMulticast.start(); wait for bugfixes
		for (Client client : clients.getList()) {
			try {
				SynchroMusicProtocol.sendPacket(packet,client);
			} catch (Exception e) {
				clients.remove(client);
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Start server thread
	 */
	private void init(){
		//When UDS will be working (wait for bugfixes?)
		//WifiManager wifi = (WifiManager)context.getSystemService( Context.WIFI_SERVICE );
		//if(wifi != null)
		//{
		//	WifiManager.MulticastLock lock = wifi.createMulticastLock("WifiDevices");
		//	lock.acquire();
		//}
		
		Log.d(TAG,"Starting connection handler with server socket: "+ serverSocket.getInetAddress() +":"+serverSocket.getLocalPort());
		clients = new Clients();
		serverThread = new Thread(new ServerThread());
		serverThread.start();
		//sendMulticast = new Thread(new MultiCastThread());
		//sendMulticast.start();
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
					e.printStackTrace();
				}
				Log.d(TAG, "Connected.");
				if(boss==null) clients.addNew(new Client(clientSocket,context));
			}
		}
	}
}
