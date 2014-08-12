package pl.airpolsl.synchromusic;

public class RefuseReceivingPacket extends AnswerToPreparePacket {


	Error error;

	
	public RefuseReceivingPacket(int nId, Error nError){
		id=nId;
		error=nError;
	}
}
