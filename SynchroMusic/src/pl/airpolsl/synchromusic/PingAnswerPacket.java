package pl.airpolsl.synchromusic;

public class PingAnswerPacket extends PacketUDP {

	int id;

	
	public PingAnswerPacket(int nId){
		id=nId;
	}
}
