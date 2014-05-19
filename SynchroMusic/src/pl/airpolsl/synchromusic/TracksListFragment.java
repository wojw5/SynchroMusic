package pl.airpolsl.synchromusic;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class TracksListFragment extends ListFragment {

	List<Track> tracks = new ArrayList<Track>();
	TrackArrayAdapter adapter;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		adapter = new TrackArrayAdapter(getActivity(),tracks);  
        
 
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
	        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
	        mediaMetadataRetriever.setDataSource(getActivity(),uri);
	        tracks.add(new Track(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE),
	        		mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST),
	        		Long.parseLong(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)),
	        		uri));
	        adapter.notifyDataSetChanged();
	    }
	  }
	  super.onActivityResult(requestCode, resultCode, data);
	}
}
