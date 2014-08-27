package pl.airpolsl.synchromusic;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Queue;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Class allows to manage Tracklist.
 * @author Wojciech
 *
 */
public class TracksListFragment extends ListFragment {

	static Tracks tracks = new Tracks();
	static TrackArrayAdapter adapter;
	static Queue<MediaPlayer> players;
	static Context appContext;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		adapter = new TrackArrayAdapter(getActivity(),tracks);  
        appContext = getActivity().getApplicationContext();
 
        setListAdapter(adapter);
    }
	@Override
	  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	    inflater.inflate(R.menu.list_tracks, menu);
	  } 
	
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.add_track_item:
	    	Intent intent = new Intent();
	    	intent.setType("audio/*");
	    	intent.setAction(Intent.ACTION_GET_CONTENT);
	    	startActivityForResult(intent,1);


	      break;
	    default:
	      break;
	    }

	    return true;
	  } 
	
	@Override
	public void onActivityResult(int requestCode,int resultCode,Intent data){

	  if(requestCode == 1){

	    if(resultCode == Activity.RESULT_OK){

	        Uri uri = data.getData();
	        String fullUri=null;
			try {
				fullUri = ip().getHostAddress() + ":3125/" +data.getDataString();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
	        mediaMetadataRetriever.setDataSource(getActivity(),uri);
	        String title = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
	        String artist = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
	        if (title==null) title = "Unknown";
	        if (artist==null) artist = "Unknown";
	        Track track = new Track(title, artist,
	        		Long.parseLong(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)),
	        		fullUri);
	        tracks.add(track);
	        adapter.notifyDataSetChanged();
	        ((ServerMainActivity) getActivity()).connectionHandler.sendToClients(track);
	    }
	  }
	  super.onActivityResult(requestCode, resultCode, data);
	}
	
	public static void addTrack(Track track){
		tracks.add(track);
		tracks.initPlayers(appContext);
		adapter.notifyDataSetChanged();
	}
	
	static InetAddress ip() throws SocketException {
	    Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
	    NetworkInterface ni;
	    while (nis.hasMoreElements()) {
	        ni = nis.nextElement();
	        if (!ni.isLoopback()/*not loopback*/ && ni.isUp()/*it works now*/) {
	            for (InterfaceAddress ia : ni.getInterfaceAddresses()) {
	                //filter for ipv4/ipv6
	                if (ia.getAddress().getAddress().length == 4) {
	                    //4 for ipv4, 16 for ipv6
	                    return ia.getAddress();
	                }
	            }
	        }
	    }
	    return null;
	}
}
