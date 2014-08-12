package pl.airpolsl.synchromusic;

public class StopPlayingPacket extends PacketTCP {

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

}
