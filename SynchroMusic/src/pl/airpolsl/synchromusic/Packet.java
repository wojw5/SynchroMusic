package pl.airpolsl.synchromusic;

public abstract class Packet implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8455241140318755771L;
	private String rawData;
	protected ConnectionDetails connectionDetails;
	
	abstract void setRawData();
	abstract void setConnectionDetails();
	
	
}
