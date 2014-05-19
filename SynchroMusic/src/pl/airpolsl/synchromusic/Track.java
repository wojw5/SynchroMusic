package pl.airpolsl.synchromusic;

import android.net.Uri;

public class Track {
	private String title;
	private String artist;
	private String genere;
	private Long length;
	private String streamerName;
	private Boolean played = false;
	private Uri uri;
	public enum Priority {
	    Now, High, Normal, Low
	}
	
	
	public Track(String nTitle, String nArtist, Long nlength, Uri nUri)
	{
		title=nTitle;
		artist=nArtist;
		length=nlength;
		uri=nUri;
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
	
	public String getTime()
	{
		String sLength = length/60 + ":";
	    int sec = (int) (length%60);
	    if(sec<10) sLength+="0";
	    sLength+=sec;
	    return sLength;
	}
	
}
