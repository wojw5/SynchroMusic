package pl.airpolsl.synchromusic;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class NSDWiFi implements ConnectivityMethod {
	
	private NsdManager mNsdManager;
	private NsdManager.RegistrationListener mRegistrationListener;
	private NsdManager.DiscoveryListener mDiscoveryListener;
	private NsdManager.ResolveListener mResolveListener;
	
	
	private Context context;
	private ServerSocket mServerSocket;
	private int mLocalPort;
	private List<NsdServiceInfo> availibleServices;
	private NsdServiceInfo resolvedService;
	
	private static final String TAG = "NSDWiFi";

    public String mServiceName;
	
	public NSDWiFi(Context context){
		this.context=context;
	    mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
	    availibleServices= new ArrayList<NsdServiceInfo>();
	    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		String username = sharedPref.getString("pref_username", "anonymous");
		mServiceName=username;
	}
	
	@Override
	public void registerService() throws Exception {
		
		if(!isConnected(context)) throw new Exception("Not connected");

		mServerSocket = null;
		try{
			mServerSocket = new ServerSocket(0);
		}
		catch (IOException e){
			throw e;
		}
		

	    // Store the chosen port.
	    mLocalPort =  mServerSocket.getLocalPort();
	    
	    // Create the NsdServiceInfo object, and populate it.
	    NsdServiceInfo serviceInfo  = new NsdServiceInfo();

	    // The name is subject to change based on conflicts
	    // with other services advertised on the same network.
	    serviceInfo.setServiceName(mServiceName);
	    serviceInfo.setServiceType(SERVICE_TYPE);
	    serviceInfo.setPort(mLocalPort);
	    

	    initializeRegistrationListener();
	    
	    mNsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
	}
	
	@Override
	public List<NsdServiceInfo> discoverServices() throws Exception {
		if(!isConnected(context)) throw new Exception("Not connected");
		initializeDiscoveryListener();
		initializeResolveListener();
		mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
		Thread.sleep(2000);
		mNsdManager.stopServiceDiscovery(mDiscoveryListener);
		return availibleServices;
	}
	
	private static boolean isConnected(Context context) {
	    ConnectivityManager connectivityManager = (ConnectivityManager)
	        context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = null;
	    if (connectivityManager != null) {
	        networkInfo =
	            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    }
	    return networkInfo == null ? false : networkInfo.isConnected();
	}
	
	private void initializeRegistrationListener() {
	    mRegistrationListener = new NsdManager.RegistrationListener() {

	        @Override
	        public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
	            // Save the service name.  Android may have changed it in order to
	            // resolve a conflict, so update the name you initially requested
	            // with the name Android actually used.
	            mServiceName = NsdServiceInfo.getServiceName();
	            Log.d(TAG, "Service registered as: " + mServiceName);
	        }

	        @Override
	        public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
	            // Registration failed!  Put debugging code here to determine why.
	        	Log.d(TAG, "Registration failed! " + errorCode);
	        }

	        @Override
	        public void onServiceUnregistered(NsdServiceInfo arg0) {
	            // Service has been unregistered.  This only happens when you call
	            // NsdManager.unregisterService() and pass in this listener.
	        	Log.d(TAG, "Service " + mServiceName + " unregistered.");
	        }

	        @Override
	        public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
	            // Unregistration failed.  Put debugging code here to determine why.
	        	Log.d(TAG, "Unegistration failed! " + errorCode);
	        }
	    };
	}

	private void initializeDiscoveryListener() {

	    // Instantiate a new DiscoveryListener
	    mDiscoveryListener = new NsdManager.DiscoveryListener() {

	        //  Called as soon as service discovery begins.
	        @Override
	        public void onDiscoveryStarted(String regType) {
	            Log.d(TAG, "Service discovery started");
	            availibleServices.clear();
	        }

	        @Override
	        public void onServiceFound(NsdServiceInfo service) {
	        	
	            // A service was found!  Do something with it.
	            Log.d(TAG, "Service discovery success" + service);
	            if (!service.getServiceType().equals(SERVICE_TYPE)) {
	                // Service type is the string containing the protocol and
	                // transport layer for this service.
	                Log.d(TAG, "Unknown Service Type: " + service.getServiceType());
	            } else if (service.getServiceName().equals(mServiceName)) {
	                // The name of the service tells the user what they'd be
	                // connecting to. It could be "Bob's Chat App".
	            	
	                Log.d(TAG, "Same machine: " + mServiceName);
	            } else {
	                mNsdManager.resolveService(service, mResolveListener);
	                availibleServices.add(service);
	            }
	        }

	        @Override
	        public void onServiceLost(NsdServiceInfo service) {
	            // When the network service is no longer available.
	            // Internal bookkeeping code goes here.
	            Log.e(TAG, "service lost" + service);
	        }

	        @Override
	        public void onDiscoveryStopped(String serviceType) {
	            Log.i(TAG, "Discovery stopped: " + serviceType);
	        }

	        @Override
	        public void onStartDiscoveryFailed(String serviceType, int errorCode) {
	            Log.e(TAG, "Discovery failed: Error code:" + errorCode);
	            mNsdManager.stopServiceDiscovery(this);
	        }

	        @Override
	        public void onStopDiscoveryFailed(String serviceType, int errorCode) {
	            Log.e(TAG, "Discovery failed: Error code:" + errorCode);
	            mNsdManager.stopServiceDiscovery(this);
	        }
	    };
	}

	private void initializeResolveListener() {
	    mResolveListener = new NsdManager.ResolveListener() {

	        @Override
	        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
	            // Called when the resolve fails.  Use the error code to debug.
	            Log.e(TAG, "Resolve failed" + errorCode);
	        }

	        @Override
	        public void onServiceResolved(NsdServiceInfo serviceInfo) {
	            Log.e(TAG, "Resolve Succeeded. " + serviceInfo);

	            if (serviceInfo.getServiceName().equals(mServiceName)) {
	                Log.d(TAG, "Same IP.");
	                return;
	            }
	            resolvedService = serviceInfo;
	        }
	    };
	}
	
	public void tearDown() {
        mNsdManager.unregisterService(mRegistrationListener);
        mNsdManager.stopServiceDiscovery(mDiscoveryListener);
    }
}
