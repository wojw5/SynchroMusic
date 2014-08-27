package pl.airpolsl.synchromusic;

import java.net.ServerSocket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdServiceResponseListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdTxtRecordListener;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
/**
 * Implements Network Servis Discovery P2P
 * Bugged in some android devices.
 * @author Wojciech
 *
 */
public class NSDP2P implements ConnectivityMethod {
	
	private IntentFilter mIntentFilter;
	private WifiP2pManager mManager;
	private WifiP2pManager.Channel mChannel;
	private ServerBroadcastReceiver mReceiver;


	final HashMap<String, String> buddies = new HashMap<String, String>();

	private Context context;
	public String mServiceName;
	
	private static final String TAG = "NSDP2P";
	
	public NSDP2P(Context context){
		this.context=context;
	    mManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
	    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		String username = sharedPref.getString("pref_username", "anonymous");
		mServiceName=username;
		
		mIntentFilter = new IntentFilter();
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
	    
	    mManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
	    
	    mChannel = mManager.initialize(context, context.getMainLooper(), null);
	    
	    //mReceiver = new ClientBroadcastReceiver(mManager, mChannel, this);
	    discoverService();
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
	
	 public void discoverService() {
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
