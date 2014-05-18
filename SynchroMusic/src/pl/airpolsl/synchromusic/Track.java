package pl.airpolsl.synchromusic;

public class Track {
	private String title;
	private String artist;
	private String genere;
	private String length;
	private String streamerName;
	private Boolean played = false;
	
	
	public Track(String nTitle, String nArtist, String nlength, String nStreamerName)
	{
		title=nTitle;
		artist=nArtist;
		length=nlength;
		streamerName=nStreamerName;
	}
	
	public String getTitle()
	{
		return title;
	}
	public String getArtist()
	{
		return artist;
	}
	
	public String getLength()
	{
		return length;
	}
	
}
