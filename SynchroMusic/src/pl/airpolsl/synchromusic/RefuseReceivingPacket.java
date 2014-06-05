package pl.airpolsl.synchromusic;

public class RefuseReceivingPacket extends AnswerToPreparePacket {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4083581759688296567L;

	Error error;
	@Override
	void setRawData() {
		// TODO Auto-generated method stub

	}
	
	public RefuseReceivingPacket(int nId, Error nError){
		id=nId;
		error=nError;
	}
}
