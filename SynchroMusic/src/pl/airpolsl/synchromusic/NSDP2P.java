package pl.airpolsl.synchromusic;

import java.net.ServerSocket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class NSDP2P implements ConnectivityMethod {
	
	private IntentFilter mIntentFilter;
	private WifiP2pManager mManager;
	private WifiP2pManager.Channel mChannel;
	private ServerBroadcastReceiver mReceiver;

	private Context context;
	public String mServiceName;
	
	private static final String TAG = "NSDP2P";
	
	public NSDP2P(Context context){
		this.context=context;
	    mManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
	    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		String username = sharedPref.getString("pref_username", "anonymous");
		mServiceName=username;
	}
	
	@Override
	public void registerService() {
		mIntentFilter = new IntentFilter();
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

	    
	    
	    mChannel = mManager.initialize(context, context.getMainLooper(), null);
	    
	    mReceiver = new ServerBroadcastReceiver(mManager, mChannel, (ServerMainActivity)context);
	    startRegistration();

	}

	@Override
	public List<NsdServiceInfo> discoverServices() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void tearDown() {
		// TODO Auto-generated method stub

	}
	
private void startRegistration() {
        
        mManager.clearLocalServices(mChannel, new ActionListener() {

			@Override
			public void onFailure(int arg0) {
				Log.d(TAG, "cannot clear local services " + arg0);
			}

			@Override
			public void onSuccess() {
				Log.d("Server Main Activity", "local services cleared.");
				//  Create a string map containing information about your service.
		        Map<String,String> record = new HashMap<String,String>();
		        record.put("listenport", "0");
		        record.put("buddyname", "John Doe" + (int) (Math.random() * 1000));
		        record.put("available", "visible");
		        record.put("type", "server");
		        record.put("version", "1.0");
		
		        // Service information.  Pass it an instance name, service type
		        // _protocol._transportlayer , and the map containing
		        // information other devices will want once they connect to this one.
		        WifiP2pDnsSdServiceInfo serviceInfo =
		                WifiP2pDnsSdServiceInfo.newInstance(mServiceName, SERVICE_TYPE, record);
		
		        // Add the local service, sending the service info, network channel,
		        // and listener that will be used to indicate success or failure of
		        // the request.
				mManager.addLocalService(mChannel, serviceInfo,
						new ActionListener() {
		            @Override
		            public void onSuccess() {
		                // Command successful! Code isn't necessarily needed here,
		                // Unless you want to update the UI or add logging statements.
		            	Log.d(TAG, "local service added");
		            	Toast.makeText(context, "local service added.", Toast.LENGTH_LONG).show();
		            }
		
		            @Override
		            public void onFailure(int arg0) {
		                // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
		            	Log.d(TAG, "Adding local service failure: " + arg0);
		            	Toast.makeText(context, "faliture " + arg0, Toast.LENGTH_LONG).show();
		            }
		        });
			}
        });
        
	}

@Override
public void resumeServer() {
	mReceiver = new ServerBroadcastReceiver(mManager, mChannel, (ServerMainActivity)context);
    context.registerReceiver(mReceiver, mIntentFilter);
}

@Override
public void pauseServer() {
    context.unregisterReceiver(mReceiver);
	
}

@Override
public ServerSocket getServerSocket() {
	// TODO Auto-generated method stub
	return null;
}

}
