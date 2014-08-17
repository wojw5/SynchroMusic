package pl.airpolsl.synchromusic;

import java.io.File;


import android.content.Context;
import android.util.Log;

public class SynchroMusicProtocol {
	private static final String TAG = "SynchroMusicProtocol";
	
	
	static void sendWelcome(Client client, Context context){
		sendPacket(new WelcomePacket(context), client);
	};
	
	static void sendTrack(Client client, Track track){
		sendPacket(new PrepareToReceivePacket(track), client);
		File file = new File(track.getUri());
		sendFile(file,client);
	};
	
	static void sendPacket(Packet packet,Client client){
		client.send(packet);
	};
	
	static void sendFile(File packet,Client client){
		client.send(packet);
	};
	
	void sendPacket(Packet packet,Clients client){
		
	};
	void sendPing(Client client){
		
	};
	void receivePing(){
		
	};
	static void processPacket(Packet packet){
		Log.d(TAG, "R: " + packet.toString());
		if (packet instanceof WelcomePacket) {
			
		}
	};
	
	static void processFile(File file){
		Log.d(TAG, "R: " + file.toString());
		//File toSave = new File(context.getFilesDir(), filename);
		//Files.
	};
	void sendPacketMulticast(){
		
	};
}
