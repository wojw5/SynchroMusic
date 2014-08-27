package pl.airpolsl.synchromusic;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.nsd.NsdServiceInfo;
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
	
	
	public static final String SERVICE_TYPE = "_synchromusic._tcp.";
	public static final String TAG = "SynchroMusicClient";
	public String mServiceName = "SynchroMusic";
	public ConnectionHandler connectionHandler;
	public ConnectivityMethod conn;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TracksListFragment.tracks.clear();
		setContentView(R.layout.activity_client_main);
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this); //get preferences
		String connectionType = sharedPref.getString("pref_connection_mode", "0");			//get connection type
		switch (Integer.parseInt(connectionType)) {
		case 0: // Network service discovery over existing wifi connection
			try {
				conn = new NSDWiFi(this);
			} catch (Exception e1) {
				Log.d(TAG, "Cannot create NSDWiFi: " + e1.getMessage());
				Toast.makeText(getBaseContext(), "First connect to your network",
						Toast.LENGTH_LONG).show();
				Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
				startActivity(intent);
				finish();
				return;
			}
			try {
				availibleServices = conn.discoverServices();
			} catch (Exception e) {
				Log.d(TAG, "Cannot discover service: " + e.getMessage());
				Toast.makeText(getBaseContext(), "Cannot discover service: " +
						e.getMessage(), Toast.LENGTH_LONG).show();
			}
			if (availibleServices.isEmpty()) {
				Toast.makeText(getBaseContext(), "Services not found :(",
						Toast.LENGTH_LONG).show();
				finish();
				return;
			}
			else {
				DialogFragment newFragment =
						new AvailibleServicesDialogFragment(availibleServices);	//generate dialog with availible services
				newFragment.show(getFragmentManager(), "services");				//display that dialog
			}
			break;
			
			
		case 1: // Network service discovery over not yet existing wifi p2p connection
			//use WiFiP2P when bugfix will be confirmed
		    break;

		default:
			Log.d(TAG, "Wrong setting");
			Toast.makeText(getBaseContext(), "Wrong setting", Toast.LENGTH_LONG).show();
			finish();
			break;
		}	
	}
	
	public void onResume() {
        super.onResume();
        //registerReceiver(mReceiver, mIntentFilter);
        
    }

    @Override
    public void onPause() {
        super.onPause();
        //unregisterReceiver(mReceiver);
        conn.tearDown();
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
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Select Service");
			builder.setItems(list, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					selectedService=availibleServices.get(which);
					Toast.makeText(getBaseContext(), "choosen: " +
							selectedService.getServiceName(), Toast.LENGTH_LONG).show();
					Log.d(TAG,"Selected service: " +selectedService.getServiceName() +
							" on " +selectedService.getHost().getHostAddress() +
							":" +selectedService.getPort());
					connectionHandler = new ConnectionHandler(conn.getServerSocket(), getActivity());
					connectionHandler.setBoss(selectedService.getHost(), selectedService.getPort());
				}
			});
			return builder.create(); // Create the AlertDialog object and return it
		}
	}
}
