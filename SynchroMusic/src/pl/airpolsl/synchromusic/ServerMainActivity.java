package pl.airpolsl.synchromusic;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

public class ServerMainActivity extends Activity
{
	private IntentFilter mIntentFilter;
	private WifiP2pManager mManager;
	private WifiP2pManager.Channel mChannel;
	private ServerBroadcastReceiver mReceiver;
	
	public static final String SERVICE_TYPE = "_synchromusic._tcp.";
    public static final String TAG = "SynchroMusicServer";
    public String mServiceName = "SynchroMusic";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server_main);
		
		mIntentFilter = new IntentFilter();
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

	    mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
	    
	    mChannel = mManager.initialize(this, getMainLooper(), null);
	    
	    mReceiver = new ServerBroadcastReceiver(mManager, mChannel, this);
	    startRegistration();
	    
	    
	}
	
	/** register the BroadcastReceiver with the intent values to be matched */
    @Override
    public void onResume() {
        super.onResume();
        mReceiver = new ServerBroadcastReceiver(mManager, mChannel, this);
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.server_main, menu);
	    return super.onCreateOptionsMenu(menu);
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
		            	Toast.makeText(getBaseContext(), "local service added.", Toast.LENGTH_LONG).show();
		            }
		
		            @Override
		            public void onFailure(int arg0) {
		                // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
		            	Log.d(TAG, "Adding local service failure: " + arg0);
		            	Toast.makeText(getBaseContext(), "faliture " + arg0, Toast.LENGTH_LONG).show();
		            }
		        });
			}
        });
        
	}
}

