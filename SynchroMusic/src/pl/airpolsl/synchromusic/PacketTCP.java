package pl.airpolsl.synchromusic;

import pl.airpolsl.synchromusic.ConnectionDetails.SendMode;

public abstract class PacketTCP extends Packet {


	/**
	 * 
	 */
	private static final long serialVersionUID = -676812801949949374L;

	@Override
	void setConnectionDetails() {
		connectionDetails = new ConnectionDetails("TCP",SendMode.Unicast);
	}


}
