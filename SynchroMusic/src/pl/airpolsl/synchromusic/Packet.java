package pl.airpolsl.synchromusic;
/**
 * Base packet object.
 * @author Wojciech
 *
 */
public abstract class Packet {

	protected ConnectionDetails connectionDetails;
	
	abstract void setRawData();
	abstract void setConnectionDetails();
	
	
}
