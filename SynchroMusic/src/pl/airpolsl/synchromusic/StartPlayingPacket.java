package pl.airpolsl.synchromusic;

public class StartPlayingPacket extends PacketTCP {

	/**
	 * 
	 */
	private static final long serialVersionUID = 547048593957305858L;

	Track track;
	Long time;
	@Override
	void setRawData() {
		// TODO Auto-generated method stub

	}
	
	public StartPlayingPacket(Track nTrack, Long nTime){
		track=nTrack;
		time=nTime;
	}
	
	public StartPlayingPacket(Track nTrack){
		track=nTrack;
		time=0L;
	}

}
