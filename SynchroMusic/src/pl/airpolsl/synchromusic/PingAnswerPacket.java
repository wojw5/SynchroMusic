package pl.airpolsl.synchromusic;

public class PingAnswerPacket extends PacketUDP {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2232697495819936012L;
	int id;

	
	public PingAnswerPacket(int nId){
		id=nId;
	}
}
