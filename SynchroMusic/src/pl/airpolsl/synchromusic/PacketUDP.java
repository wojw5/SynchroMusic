package pl.airpolsl.synchromusic;

import pl.airpolsl.synchromusic.ConnectionDetails.SendMode;

public abstract class PacketUDP extends Packet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7453170668113735269L;

	abstract void setRawData();

	@Override
	void setConnectionDetails() {
		connectionDetails = new ConnectionDetails("UDP",SendMode.Unicast);
	}


}
