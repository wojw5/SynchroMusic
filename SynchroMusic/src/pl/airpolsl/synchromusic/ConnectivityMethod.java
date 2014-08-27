package pl.airpolsl.synchromusic;

import java.net.ServerSocket;
import java.util.List;

import android.net.nsd.NsdServiceInfo;
/**
 * Interface defines required methods for all type of connections.
 * @author Wojciech Widenka
 *
 */
public interface ConnectivityMethod {
	public static final String SERVICE_TYPE = "_synchromusic._tcp.";
	public static final String SERVICE_NAME = "SynchroMusic";
	
	/**
	 * Make server service to be able for discover
	 */
	public void registerService();
	
	/**
	 * Finds availible server services
	 * @return Fist of found services
	 * @throws Exception
	 */
	public List<NsdServiceInfo> discoverServices() throws Exception;
	
	/**
	 * Turn off service discovery and registration
	 */
	public void tearDown();
	
	/**
	 * resume server tasks
	 */
	public void resumeServer();

	/**
	 * pause server tasks
	 */
	public void pauseServer();
	
	public ServerSocket getServerSocket();
}
