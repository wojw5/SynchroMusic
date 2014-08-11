package pl.airpolsl.synchromusic;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
/**
 * Class allows to prepare list of tracks for user.
 * @author Wojciech
 *
 */
public class TrackArrayAdapter extends ArrayAdapter<Track> {
	private final Context context;
	private final List<Track> tracks;

  public TrackArrayAdapter(Context context, List<Track> tracks) {
    super(context, R.layout.row_list_tracks, tracks);
    this.context = context;
    this.tracks = tracks;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.row_list_tracks, parent, false);
    TextView title = (TextView) rowView.findViewById(R.id.title_textView);
    TextView artist = (TextView) rowView.findViewById(R.id.artist_textView);
    TextView time = (TextView) rowView.findViewById(R.id.time_textView);
    title.setText(tracks.get(position).getTitle());
    artist.setText(tracks.get(position).getArtist());
    time.setText(tracks.get(position).getTime());
    
    return rowView;
  }
}
