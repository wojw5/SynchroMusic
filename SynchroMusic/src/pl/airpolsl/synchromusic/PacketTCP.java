package pl.airpolsl.synchromusic;

import pl.airpolsl.synchromusic.ConnectionDetails.SendMode;

public abstract class PacketTCP extends Packet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1971857379253371248L;

	abstract void setRawData();

	@Override
	void setConnectionDetails() {
		connectionDetails = new ConnectionDetails("TCP",SendMode.Unicast);
	}


}
