package pl.airpolsl.synchromusic;

public class StopPlayingPacket extends PacketTCP {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7752091743733339286L;
	Track track;
	Long time;
	@Override
	void setRawData() {
		// TODO Auto-generated method stub

	}
	
	public StopPlayingPacket(Track nTrack, Long nTime){
		track=nTrack;
		time=nTime;
	}
	
	public StopPlayingPacket(Track nTrack){
		track=nTrack;
		time=0L;
	}
	
	public StopPlayingPacket(){
		time=0L;
	}

}
