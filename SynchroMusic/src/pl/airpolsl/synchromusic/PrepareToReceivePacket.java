package pl.airpolsl.synchromusic;

import java.util.Random;

/**
 * Packet with track information
 * @author Wojciech
 *
 */
public class PrepareToReceivePacket extends PacketTCP {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5272863896765882894L;
	private Track track;
	private int id;

	/**
	 * Create packet with track info.
	 * @param nTrack
	 */
	public PrepareToReceivePacket(Track nTrack){
		track=nTrack;
		Random generator = new Random();
		id=generator.nextInt();
	}
	
	public Track getTrack(){
		return track;
	}


	@Override
	public String toString() {
		return "Prepare: ID: " + id + " Info: " + track.getArtist() + " - " + track.getTitle();
	}

}
