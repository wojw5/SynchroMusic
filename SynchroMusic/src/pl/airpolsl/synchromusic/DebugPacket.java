package pl.airpolsl.synchromusic;

public class DebugPacket extends PacketTCP {

	/**
	 * Packet containing debug information
	 */
	private static final long serialVersionUID = -8215147270708845235L;
	Error error;
	String description;
	
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

	@Override
	public String toString() {
		return "Debug: " + error + ":" + description;
	}

}
