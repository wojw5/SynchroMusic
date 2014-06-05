package pl.airpolsl.synchromusic;

public class PingAnswerPacket extends PacketUDP {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2650417019792490023L;
	int id;
	@Override
	void setRawData() {
		// TODO Auto-generated method stub

	}
	
	public PingAnswerPacket(int nId){
		id=nId;
	}
}
