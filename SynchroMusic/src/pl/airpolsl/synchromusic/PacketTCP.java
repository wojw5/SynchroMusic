package pl.airpolsl.synchromusic;


public abstract class PacketTCP extends Packet {


	private static final long serialVersionUID = -676812801949949374L;
	
	/**
	 * returns formatted string containing packet information
	 */
	public abstract String toString();

}
