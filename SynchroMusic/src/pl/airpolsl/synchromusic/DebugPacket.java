package pl.airpolsl.synchromusic;

public class DebugPacket extends PacketTCP {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1662962384780402807L;

	Error error;
	String description;
	@Override
	void setRawData() {
		// TODO Auto-generated method stub

	}
	
	public DebugPacket(Error nError, String nDescription)
	{
		error=nError;
		description=nDescription;
	}
	
	public DebugPacket(Error nError)
	{
		error=nError;
	}
	
	public DebugPacket(String nDescription)
	{
		description=nDescription;
	}

}
