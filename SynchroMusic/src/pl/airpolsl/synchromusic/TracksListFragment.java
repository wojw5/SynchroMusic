package pl.airpolsl.synchromusic;

import java.util.ArrayList;
import java.util.List;

import android.app.ListFragment;
import android.os.Bundle;

public class TracksListFragment extends ListFragment {


	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        List<Track> tracks = new ArrayList<Track>();
        tracks.add(new Track("Przyk³adowy tytu³", "Jakiœ Artysta", "3:45", "String nStreamerName"));
        tracks.add(new Track("Przyk³adowy tytu³", "Jakiœ Artysta", "3:45", "String nStreamerName"));
        tracks.add(new Track("Przyk³adowy tytu³", "Jakiœ Artysta", "3:45", "String nStreamerName"));
        tracks.add(new Track("Przyk³adowy tytu³", "Jakiœ Artysta", "3:45", "String nStreamerName"));
        tracks.add(new Track("Przyk³adowy tytu³", "Jakiœ Artysta", "3:45", "String nStreamerName"));
        tracks.add(new Track("Przyk³adowy tytu³", "Jakiœ Artysta", "3:45", "String nStreamerName"));
        tracks.add(new Track("Przyk³adowy tytu³", "Jakiœ Artysta", "3:45", "String nStreamerName"));
        tracks.add(new Track("Przyk³adowy tytu³", "Jakiœ Artysta", "3:45", "String nStreamerName"));
        tracks.add(new Track("Przyk³adowy tytu³", "Jakiœ Artysta", "3:45", "String nStreamerName"));
        tracks.add(new Track("Przyk³adowy tytu³", "Jakiœ Artysta", "3:45", "String nStreamerName"));
        tracks.add(new Track("Przyk³adowy tytu³", "Jakiœ Artysta", "3:45", "String nStreamerName"));
        tracks.add(new Track("Przyk³adowy tytu³", "Jakiœ Artysta", "3:45", "String nStreamerName"));
        
        TrackArrayAdapter adapter = new TrackArrayAdapter(getActivity(),tracks);
 
        setListAdapter(adapter);
    }
}
