package pl.airpolsl.synchromusic;

public class StartPlayingPacket extends PacketTCP {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5245508448396681505L;
	Track track;
	Long time;

	
	public StartPlayingPacket(Track nTrack, Long nTime){
		track=nTrack;
		time=nTime;
	}
	
	public StartPlayingPacket(Track nTrack){
		track=nTrack;
		time=0L;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
