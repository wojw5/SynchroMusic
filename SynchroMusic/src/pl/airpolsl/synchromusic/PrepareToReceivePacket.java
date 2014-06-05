package pl.airpolsl.synchromusic;

import java.util.Random;

public class PrepareToReceivePacket extends PacketTCP {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7800093165187215874L;
	Track track;
	int id;
	@Override
	void setRawData() {
		// TODO Auto-generated method stub

	}
	
	public PrepareToReceivePacket(Track nTrack){
		track=nTrack;
		Random generator = new Random();
		id=generator.nextInt();
	}

}
