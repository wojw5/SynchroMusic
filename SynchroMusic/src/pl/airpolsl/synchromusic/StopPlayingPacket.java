package pl.airpolsl.synchromusic;

public class StopPlayingPacket extends PacketTCP {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8013914923799606004L;
	Track track;
	Long time;

	
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

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
