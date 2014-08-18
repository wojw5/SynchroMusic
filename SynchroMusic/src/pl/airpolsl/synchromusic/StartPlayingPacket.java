package pl.airpolsl.synchromusic;

public class StartPlayingPacket extends PacketTCP {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5293205630117854181L;
	String uri;
	int time;

	
	public StartPlayingPacket(String nTrack, int nTime){
		uri=nTrack;
		time=nTime;
	}
	
	public StartPlayingPacket(Track nTrack, int nTime){
		uri=nTrack.getUri();
		time=nTime;
	}
	
	public StartPlayingPacket(String nTrack){
		uri=nTrack;
		time=0;
	}

	public StartPlayingPacket(Track track) {
		uri=track.getUri();
		time=0;
	}

	@Override
	public String toString() {
		return "Start at " + time + "media: " + uri ;
	}

}
