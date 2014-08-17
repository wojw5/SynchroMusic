package pl.airpolsl.synchromusic;

import pl.airpolsl.synchromusic.ConnectionDetails.SendMode;

public abstract class PacketUDP extends Packet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 119104841674753747L;

	@Override
	void setConnectionDetails() {
		connectionDetails = new ConnectionDetails("UDP",SendMode.Unicast);
	}


}
