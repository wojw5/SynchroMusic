package pl.airpolsl.synchromusic;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

/**
 * Contains pethods for packet processing.
 * @author Wojciech
 *
 */
public class SynchroMusicProtocol {
	private static final String TAG = "SynchroMusicProtocol";
	
	/**
	 * Send info about Client availibility
	 * @param client
	 * @param context
	 * @throws Exception
	 */
	static void sendWelcome(Client client, Context context) throws Exception{
		sendPacket(new WelcomePacket(context), client);
	};
	
	/**
	 * Send info about new track to client
	 * @param client
	 * @param track
	 * @throws Exception
	 */
	static void sendTrack(Client client, Track track) throws Exception{
		sendPacket(new PrepareToReceivePacket(track), client);
	};
	
	/**
	 * send packet to client
	 * @param packet
	 * @param client
	 * @throws Exception
	 */
	static void sendPacket(Packet packet,Client client) throws Exception{
		client.send(packet);
	};
	
	/**
	 * check incoming packet and react on its content
	 * @param packet
	 * @param context
	 * @param from
	 * @throws Exception
	 */
	static void processPacket(final Packet packet,Context context,Client from) throws Exception {
		Log.d(TAG, "R: " + packet.toString());
		
		if (packet instanceof StartPlayingPacket) {
			String uri = ((StartPlayingPacket) packet).uri;
			int time = ((StartPlayingPacket) packet).time;
			String trackUri = TracksListFragment.tracks.get(0).getUri();
			while (!TracksListFragment.tracks.isEmpty() && trackUri.compareTo(uri) != 0)
			{
				TracksListFragment.tracks.remove(0);
				TracksListFragment.tracks.initPlayers(context);
				trackUri = TracksListFragment.tracks.get(0).getUri();
				((Activity)context).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						TracksListFragment.adapter.notifyDataSetChanged();
					}
				});
			}
			if (!TracksListFragment.tracks.isEmpty())
				TracksListFragment.tracks.get(0).play(time);
		}
		else if (packet instanceof PrepareToReceivePacket) {
			((Activity)context).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					TracksListFragment.addTrack(((PrepareToReceivePacket)packet).getTrack());
				}
			});
		}
		else if (packet instanceof WelcomePacket) {
			for (Track track : TracksListFragment.tracks) {
				sendTrack(from, track);
			}
		}
		else if (packet instanceof StopPlayingPacket) {
			int time = ((StopPlayingPacket) packet).time;
			if (!TracksListFragment.tracks.isEmpty()
					|| TracksListFragment.tracks.get(0).isPlaying())
				TracksListFragment.tracks.get(0).pause(time);
		}
	};

	/**
	 * check incoming message form multicast and react on its content
	 * @param msg
	 */
	public static void processUDP(String msg) {
		if (msg=="play" && !TracksListFragment.tracks.isEmpty())
			TracksListFragment.tracks.get(0).play();
	};
}
