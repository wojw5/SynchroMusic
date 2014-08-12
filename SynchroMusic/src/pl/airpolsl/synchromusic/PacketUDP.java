package pl.airpolsl.synchromusic;

import pl.airpolsl.synchromusic.ConnectionDetails.SendMode;

public abstract class PacketUDP extends Packet {
	@Override
	void setConnectionDetails() {
		connectionDetails = new ConnectionDetails("UDP",SendMode.Unicast);
	}


}
