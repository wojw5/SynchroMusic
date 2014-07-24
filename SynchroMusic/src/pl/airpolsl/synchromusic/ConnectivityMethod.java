package pl.airpolsl.synchromusic;

import java.util.List;

import android.net.nsd.NsdServiceInfo;

public interface ConnectivityMethod {
	public static final String SERVICE_TYPE = "_synchromusic._tcp.";
	public static final String SERVICE_NAME = "SynchroMusic";
	public void registerService() throws Exception;
	
	public List<NsdServiceInfo> discoverServices() throws Exception;
	
	public void tearDown();
}
