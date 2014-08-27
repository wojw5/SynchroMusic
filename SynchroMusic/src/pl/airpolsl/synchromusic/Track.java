package pl.airpolsl.synchromusic;

import java.io.Serializable;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
/**
 * Represents one audio file with parameters
 * @author Wojciech Widenka
 *
 */
/**
 * Represents one audio file with parameters
 * @author Wojciech Widenka
 *
 */
public class Track implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4782592970852158068L;
	private String title;
	private String artist;
	private String genere;
	private Long length;
	private String streamerName;
	private Boolean played = false;
	private Boolean loaded = false;
	private String uri;
	private MediaPlayer mediaPlayer = null;
	public enum Priority {
	    Now, High, Normal, Low
	}
	
	/**
	 * Create new track
	 * @param nTitle
	 * @param nArtist
	 * @param nlength
	 * @param nUri
	 */
	public Track(String nTitle, String nArtist, Long nlength, String nUri)
	{
		title=nTitle;
		artist=nArtist;
		length=nlength;
		uri=nUri;
	}
	
	/**
	 * initialize media player for this track. this mean also start buffering song.
	 * @param context
	 */
	public void initPlayer(Context context){
		if(mediaPlayer==null){
			try {
				mediaPlayer = MediaPlayer.create(context, Uri.parse("http://" + uri));
				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer mp) {
						mp.release();
						TracksListFragment.tracks.remove(0);
						TracksListFragment.adapter.notifyDataSetChanged();
					}
				});
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * Start playing
	 * @param time start time in ms
	 */
	public void play(int time){
		if(mediaPlayer!=null) {
			mediaPlayer.seekTo(time);
			mediaPlayer.start();
		}
	}
	
	/**
	 * Start playing from last state
	 */
	public void play(){
		if(mediaPlayer!=null) {
			mediaPlayer.start();
		}
	}
	
	/**
	 * Pause and seek to time
	 * @param time in ms
	 */
	public void pause(int time){
		if(mediaPlayer!=null && mediaPlayer.isPlaying()) {
			mediaPlayer.seekTo(time);
			mediaPlayer.pause();
		}
	}
	
	/**
	 * Pause track
	 */
	public void pause(){
		if(mediaPlayer!=null && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
		}
	}
	
	
	public String getTitle()
	{
		return title;
	}
	public String getArtist()
	{
		return artist;
	}
	
	public Long getLength()
	{
		return length;
	}
	
	public String getUri()
	{
		return uri;
	}
	
	public String getTime()
	{
		long tLength = length/1000;
		String sLength = tLength/60 + ":";
	    int sec = (int) (tLength%60);
	    if(sec<10) sLength+="0";
	    sLength+=sec;
	    return sLength;
	}
	
	public Boolean isPlayed(){
		return played;
	}
	
	public Boolean isLoaded(){
		return loaded;
	}
	
	public void setPlayed(Boolean value){
		played = value;
	}
	
	public void setLoaded(Boolean value){
		loaded = value;
	}
	
	public void setPlayed(){
		played = true;
	}
	
	public void setLoaded(){
		loaded = true;
	}

	public boolean isPlaying() {
		if(mediaPlayer==null) return false;
		return mediaPlayer.isPlaying();
	}
}
