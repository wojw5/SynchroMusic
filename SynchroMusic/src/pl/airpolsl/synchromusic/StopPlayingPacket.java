package pl.airpolsl.synchromusic;

public class StopPlayingPacket extends PacketTCP {

	/**
	 * Packet that informs about which song and in what time should stop playing.
	 */
	private static final long serialVersionUID = -8013914923799606004L;
	Track track;
	int time;

	
	public StopPlayingPacket(Track nTrack, int nTime){
		track=nTrack;
		time=nTime;
	}
	
	public StopPlayingPacket(Track nTrack){
		track=nTrack;
		time=0;
	}
	
	public StopPlayingPacket(){
		time=0;
	}

	@Override
	public String toString() {
		return "Pause in " + track.getArtist() + " - " + track.getTitle() + " at " + time + "ms";
	}

}
