package pl.airpolsl.synchromusic;

public class ReadyToReceivePacket extends AnswerToPreparePacket {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5070420817724154302L;

	@Override
	void setRawData() {
		// TODO Auto-generated method stub

	}
	public ReadyToReceivePacket(int nId){
		id=nId;
	}
}
