package pl.airpolsl.synchromusic;

public class RefuseReceivingPacket extends AnswerToPreparePacket {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8443896486067918145L;
	Error error;

	
	public RefuseReceivingPacket(int nId, Error nError){
		id=nId;
		error=nError;
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
}
