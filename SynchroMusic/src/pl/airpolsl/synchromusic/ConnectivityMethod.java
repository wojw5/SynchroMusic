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
	public void registerService();
	
	public List<NsdServiceInfo> discoverServices() throws Exception;
	
	public void tearDown();

	public void resumeServer();

	public void pauseServer();
	
	public ServerSocket getServerSocket();
}
