package pl.airpolsl.synchromusic;

import java.util.Random;

public class PrepareToReceivePacket extends PacketTCP {
	Track track;
	int id;

	
	public PrepareToReceivePacket(Track nTrack){
		track=nTrack;
		Random generator = new Random();
		id=generator.nextInt();
	}

}
