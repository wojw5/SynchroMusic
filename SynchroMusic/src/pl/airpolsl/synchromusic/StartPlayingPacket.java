package pl.airpolsl.synchromusic;

public class StartPlayingPacket extends PacketTCP {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5245508448396681505L;
	String uri;
	Long time;

	
	public StartPlayingPacket(String nTrack, Long nTime){
		uri=nTrack;
		time=nTime;
	}
	
	public StartPlayingPacket(Track nTrack, Long nTime){
		uri=nTrack.getUri();
		time=nTime;
	}
	
	public StartPlayingPacket(String nTrack){
		uri=nTrack;
		time=0L;
	}

	@Override
	public String toString() {
		return "Start at " + time + "media: " + uri ;
	}

}
