package pl.airpolsl.synchromusic;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class SynchroMusicProtocol {
	private static final String TAG = "SynchroMusicProtocol";
	
	
	static void sendWelcome(Client client, Context context){
		sendPacket(new WelcomePacket(context), client);
	};
	
	static void sendTrack(Client client, Track track){
		sendPacket(new PrepareToReceivePacket(track), client);
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
	static void processPacket(final Packet packet,Context context){
		Log.d(TAG, "R: " + packet.toString());
		if (packet instanceof WelcomePacket) {
			
		}
		else if (packet instanceof PrepareToReceivePacket) {
			((Activity)context).runOnUiThread(new Runnable() {
			     @Override
			     public void run() {

			    	 TracksListFragment.addTrack(((PrepareToReceivePacket)packet).getTrack());
			    }
			});
			
		}
		else if (packet instanceof StartPlayingPacket) {
			String uri = ((StartPlayingPacket) packet).uri;
			Long time = ((StartPlayingPacket) packet).time;
			while (!TracksListFragment.tracks.isEmpty() && TracksListFragment.tracks.get(0).getUri() != uri)
			{
				TracksListFragment.tracks.remove(0);
			}
			
			if (!TracksListFragment.tracks.isEmpty()) TracksListFragment.tracks.get(0).play(time);
			
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
