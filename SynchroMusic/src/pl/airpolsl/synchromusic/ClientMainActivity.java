package pl.airpolsl.synchromusic;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdServiceResponseListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdTxtRecordListener;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Bundle;
import android.util.Log;

public class ClientMainActivity extends Activity {

	

	private IntentFilter mIntentFilter;
	private WifiP2pManager mManager;
	private WifiP2pManager.Channel mChannel;
	private ClientBroadcastReceiver mReceiver;
	
	public static final String SERVICE_TYPE = "_synchromusic._tcp.";
    public static final String TAG = "SynchroMusicClient";
    public String mServiceName = "SynchroMusic";
    
    final HashMap<String, String> buddies = new HashMap<String, String>();
    
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_main);

		mIntentFilter = new IntentFilter();
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
	    
	    mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
	    
	    mChannel = mManager.initialize(this, getMainLooper(), null);
	    
	    mReceiver = new ClientBroadcastReceiver(mManager, mChannel, this);
	    discoverService();
	}
	
	public void onResume() {
        super.onResume();
        mReceiver = new ClientBroadcastReceiver(mManager, mChannel, this);
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }
    
    
    private void discoverService() {
	    DnsSdTxtRecordListener txtListener = new DnsSdTxtRecordListener() {
	        @Override
	        /* Callback includes:
	         * fullDomain: full domain name: e.g "printer._ipp._tcp.local."
	         * record: TXT record dta as a map of key/value pairs.
	         * device: The device running the advertised service.
	         */

	        public void onDnsSdTxtRecordAvailable(
	                String fullDomain, Map<String,String> record, WifiP2pDevice device) {
	                Log.d(TAG, "DnsSdTxtRecord available -" + record.toString());
	                buddies.put(device.deviceAddress, record.get("buddyname"));
	            }
	        };
	        
	        DnsSdServiceResponseListener servListener = new DnsSdServiceResponseListener() {
	            @Override
	            public void onDnsSdServiceAvailable(String instanceName, String registrationType,
	                    WifiP2pDevice resourceType) {

	                    // Update the device name with the human-friendly version from
	                    // the DnsTxtRecord, assuming one arrived.
	                    resourceType.deviceName = buddies
	                            .containsKey(resourceType.deviceAddress) ? buddies
	                            .get(resourceType.deviceAddress) : resourceType.deviceName;

	                    // Add to the custom adapter defined specifically for showing
	                    // wifi devices.
	                    /*NetworkListFragmentFragment fragment = (NetworkListFragmentFragment) getFragmentManager()
	                            .findFragmentById(R.id.);
	                    fragment.
	                    WiFiDevicesAdapter adapter = ((WiFiDevicesAdapter) fragment
	                            .getListAdapter());

	                    adapter.add(resourceType);
	                    adapter.notifyDataSetChanged();*/
	                    Log.d("Server Main Activity", "onBonjourServiceAvailable " + instanceName);
	            }
	        };

	        mManager.setDnsSdResponseListeners(mChannel, servListener, txtListener);
	        WifiP2pDnsSdServiceRequest serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
	        mManager.addServiceRequest(mChannel,
	                serviceRequest,
	                new ActionListener() {
	                    @Override
	                    public void onSuccess() {
	                        // Success!
	                    }

	                    @Override
	                    public void onFailure(int code) {
	                        // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
	                    }
	                });
	        
	        mManager.discoverServices(mChannel, new ActionListener() {

	            @Override
	            public void onSuccess() {
	                // Success!
	            	Log.d("Server Main Activity", "discover services success ");
	            }

	            @Override
	            public void onFailure(int code) {
	                // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
	                if (code == WifiP2pManager.P2P_UNSUPPORTED) {
	                    Log.d("Server Main Activity", "discover services failure " + code);
	            }
	            }
	        });
	}
}
