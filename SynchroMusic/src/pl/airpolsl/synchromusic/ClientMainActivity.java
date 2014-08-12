package pl.airpolsl.synchromusic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdServiceResponseListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdTxtRecordListener;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

/**
 * Main Activity for SynchroMusic as client.
 *
 */
public class ClientMainActivity extends Activity{

	private NsdServiceInfo selectedService;
	private List<NsdServiceInfo> availibleServices;
	private IntentFilter mIntentFilter;
	private WifiP2pManager mManager;
	private WifiP2pManager.Channel mChannel;
	private ClientBroadcastReceiver mReceiver;
	
	public static final String SERVICE_TYPE = "_synchromusic._tcp.";
    public static final String TAG = "SynchroMusicClient";
    public String mServiceName = "SynchroMusic";
    ConnectionHandler connectionHandler;
    ConnectivityMethod conn;
    
    final HashMap<String, String> buddies = new HashMap<String, String>();
    
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_client_main);
		/*SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		String connectionType = sharedPref.getString("pref_connection_mode", "0");
		switch (Integer.parseInt(connectionType)) {
		case 0:
			NSDWiFi conn = new NSDWiFi(this);
			ProgressDialog progress;
			progress = ProgressDialog.show(this, "Wait",
				    "Searching services", true);
			try {
				availibleServices = conn.discoverServices();
			} catch (Exception e) {
				Log.d(TAG, "Cannot discover service: " + e.getMessage());
            	Toast.makeText(getBaseContext(), "Cannot discover service: " + e.getMessage(), Toast.LENGTH_LONG).show();
			}
			progress.dismiss();
			if(availibleServices.isEmpty()){
				Toast.makeText(getBaseContext(), "services not found", Toast.LENGTH_LONG).show();
			}
			else {
				    DialogFragment newFragment = new AvailibleServicesDialogFragment(availibleServices);
				    newFragment.show(getFragmentManager(), "services");
				}
			break;
			
			
		case 1:
			mIntentFilter = new IntentFilter();
		    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
		    
		    mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		    
		    mChannel = mManager.initialize(this, getMainLooper(), null);
		    
		    mReceiver = new ClientBroadcastReceiver(mManager, mChannel, this);
		    discoverService();
		    break;

		default:
			break;
		}*/
	}
	
	public void onResume() {
        super.onResume();
        //mReceiver = new ClientBroadcastReceiver(mManager, mChannel, this); TODO delete?
        //registerReceiver(mReceiver, mIntentFilter);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this); //get preferences
		String connectionType = sharedPref.getString("pref_connection_mode", "0");			//get connection type
		switch (Integer.parseInt(connectionType)) {
		case 0: // Network service discovery over existing wifi connection
			try {
				conn = new NSDWiFi(this);
			} catch (Exception e1) {
				Log.d(TAG, "Cannot create NSDWiFi: " +e1.getMessage());
				Toast.makeText(getBaseContext(), "First connect to your network", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
				startActivity(intent);
				finish();
				return;
			}
			ProgressDialog progress; // TODO would be nice if working ;/
			progress = ProgressDialog.show(this, "Wait",
				    "Searching services", true);
			try {
				availibleServices = conn.discoverServices();
			} catch (Exception e) {
				Log.d(TAG, "Cannot discover service: " + e.getMessage());
            	Toast.makeText(getBaseContext(), "Cannot discover service: " + e.getMessage(), Toast.LENGTH_LONG).show(); // TODO use strring, make more friendly or delete
			}
			progress.dismiss();
			if(availibleServices.isEmpty()){
				Toast.makeText(getBaseContext(), "services not found", Toast.LENGTH_LONG).show(); // TODO use strring, make more friendly get back to main menu
			}
			else {
				    DialogFragment newFragment = new AvailibleServicesDialogFragment(availibleServices); //generate dialog with availible services
				    newFragment.show(getFragmentManager(), "services");
				}
			break;
			
			
		case 1: // Network service discovery over not yet existing wifi p2p connection
			mIntentFilter = new IntentFilter();
		    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
		    
		    mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		    
		    mChannel = mManager.initialize(this, getMainLooper(), null);
		    
		    mReceiver = new ClientBroadcastReceiver(mManager, mChannel, this);
		    discoverService();
		    break;

		default:
			break;
		}
    }

    @Override
    public void onPause() {
        super.onPause();
        //unregisterReceiver(mReceiver);
        conn.tearDown();
    }
    
    // TODO repair & move to ndsp2p class
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
    
    
    /**
     * Indicates a dialog with list of services form availibleServices param
     * @param availibleServices
     */
    public void showAvailibleServicesDialog(List<NsdServiceInfo> availibleServices) {
        DialogFragment dialog = new AvailibleServicesDialogFragment(availibleServices);
        dialog.show(getFragmentManager(), "AvailibleServicesDialogFragment");
    }
    
    /**
     * Class defines how to build dialog with availible services for connection.
     *
     */
	public class AvailibleServicesDialogFragment extends DialogFragment {
		
		private String[] list;
		
		/**
		 * Construct list with names of services.
		 * @param availibleServices
		 */
		public AvailibleServicesDialogFragment(List<NsdServiceInfo> availibleServices){
			int length = availibleServices.size();
			list=new String[length];
			for(int i=0;i<length;i++){
				list[i]=availibleServices.get(i).getServiceName();
			}
		}
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setTitle("Select Service");
			builder.setItems(list, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) { //TODO move somewhere?
	                   selectedService=availibleServices.get(which);
	                   Toast.makeText(getBaseContext(), "choosen: " + selectedService.getServiceName(), Toast.LENGTH_LONG).show();
	                   Log.d(TAG,"Selected service: " +selectedService.getServiceName()
	                		   + " on " +selectedService.getHost().getHostAddress()
	                		   + ":" +selectedService.getPort()
	                		);
	                   //TODO connect to selected, check if working
	                   connectionHandler = new ConnectionHandler(conn.getServerSocket());
	                   connectionHandler.setBoss(selectedService.getHost(), selectedService.getPort());
	                   connectionHandler.sendToBoss(new DebugPacket("DUPA")); //TODO
	               }
			});
	        // Create the AlertDialog object and return it
	        return builder.create();
	        
	    }
	}

    
}
