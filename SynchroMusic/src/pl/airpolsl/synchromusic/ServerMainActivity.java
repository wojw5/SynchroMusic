package pl.airpolsl.synchromusic;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

/**
 * Main Activity for SynchroMusic as server.
 *
 */
public class ServerMainActivity extends Activity
{
	
	public static final String SERVICE_TYPE = "_synchromusic._tcp.";
    public static final String TAG = "SynchroMusicServer";
    public String mServiceName = "SynchroMusic"; //TODO private?
    
    public ConnectivityMethod conn;
    public ConnectionHandler connectionHandler;
    private FileServer fileServer;
    private MediaPlayer player;
	
    /**
     * Initialize appropriate ConnectivityMethod based on settings.
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server_main);
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		String connectionType = sharedPref.getString("pref_connection_mode", "0");
		
		switch (Integer.parseInt(connectionType)) {
		case 0:
			try {
				conn = new NSDWiFi(this);
			} catch (Exception e1) {
				Log.d(TAG, "Cannot create NSDWiFi: " +e1.getMessage());
				Toast.makeText(getBaseContext(), "First connect to your network", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
				startActivity(intent);
				finish();
				return;
			}
			break;
			
		case 1:
			conn = new NSDP2P(this);
			break;

		default:
			break;
		}
		
	    try {
			conn.registerService();
		} catch (Exception e) {
			Log.d(TAG, "Cannot register service: " + e.getMessage());
        	Toast.makeText(getBaseContext(), "Cannot register service: " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
	    connectionHandler = new ConnectionHandler(conn.getServerSocket(),this); //TODO shouldn't be public assigned?
	    
	    fileServer = new FileServer(this);
	    try {
			fileServer.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** register the BroadcastReceiver with the intent values to be matched *///TODO remove comment?
    @Override
    public void onResume() {
        super.onResume();
        
    }

    @Override
    public void onPause() {
        super.onPause();
        //conn.pauseServer();
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	if (conn!=null) conn.pauseServer();
    	if (fileServer!=null) fileServer.stop();
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.server_main, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	public void startPlaying(View view){
		startPlaying();
	}
	
	public void playNext(View view){
		
		if (TracksListFragment.tracks.size()>1){
			Track currentTrack = TracksListFragment.tracks.get(0);
			if(player!=null)
			{
				if(player.isPlaying()){
					connectionHandler.sendToClients(new StopPlayingPacket(currentTrack,player.getCurrentPosition()));
					player.stop();
				}
				player.release();
			}
			TracksListFragment.tracks.remove(0);
			currentTrack = TracksListFragment.tracks.get(0);
	        player = null;
			player = MediaPlayer.create(this, Uri.parse("http://"+currentTrack.getUri()));
			player.setOnCompletionListener(new
				    OnCompletionListener() {        
		        @Override
		        public void onCompletion(MediaPlayer mp) {
		        mp.release();
		        TracksListFragment.tracks.remove(0);
		        player = null;
				TracksListFragment.adapter.notifyDataSetChanged();
				startPlaying();
			    }
		        
			});
			connectionHandler.sendToClients(new StartPlayingPacket(currentTrack));
			TracksListFragment.adapter.notifyDataSetChanged();
			player.start();
		}
	}
	
	public void startPlaying(){
		if (TracksListFragment.tracks.size()>0){
			Track currentTrack = TracksListFragment.tracks.get(0);
			if(player!=null)
			{
				if(player.isPlaying()){
					connectionHandler.sendToClients(new StopPlayingPacket(currentTrack,player.getCurrentPosition()));
					player.pause();
				}
				else {
					connectionHandler.sendToClients(new StartPlayingPacket(currentTrack,player.getCurrentPosition()));
					player.seekTo(player.getCurrentPosition()-50); //approximate latency
					player.start();
				}
			}
			else{
				player = MediaPlayer.create(this, Uri.parse("http://"+currentTrack.getUri()));
				player.setOnCompletionListener(new
					    OnCompletionListener() {        
			        @Override
			        public void onCompletion(MediaPlayer mp) {
			        mp.release();
			        TracksListFragment.tracks.remove(0);
			        player = null;
					TracksListFragment.adapter.notifyDataSetChanged();
					startPlaying();
			    }
			});
				connectionHandler.sendToClients(new StartPlayingPacket(currentTrack));
				player.seekTo(player.getCurrentPosition()-50); //approximate latency
				player.start();
			}
		}
	}
}

