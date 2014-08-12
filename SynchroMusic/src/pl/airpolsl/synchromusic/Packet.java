package pl.airpolsl.synchromusic;

import java.io.Serializable;

/**
 * Base packet object.
 * @author Wojciech
 *
 */
public abstract class Packet implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8143699352848595328L;
	protected ConnectionDetails connectionDetails;
	
	abstract void setConnectionDetails();
	
	
}
