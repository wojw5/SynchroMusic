package pl.airpolsl.synchromusic;

public class StartPlayingPacket extends PacketTCP {

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

}
