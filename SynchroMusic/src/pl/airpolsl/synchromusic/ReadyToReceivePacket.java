package pl.airpolsl.synchromusic;

public class ReadyToReceivePacket extends AnswerToPreparePacket {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8494370011032977450L;

	public ReadyToReceivePacket(int nId){
		id=nId;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
}
