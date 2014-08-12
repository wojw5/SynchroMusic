package pl.airpolsl.synchromusic;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

/**
 * Main Activity for SynchroMusic as server.
 *
 */
public class ServerMainActivity extends Activity
{
	
	public static final String SERVICE_TYPE = "_synchromusic._tcp.";
    public static final String TAG = "SynchroMusicServer";
    public String mServiceName = "SynchroMusic"; //TODO private?
    
    private ConnectivityMethod conn;
	
    /**
     * Initialize appropriate ConnectivityMethod based on settings.
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server_main);
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		String connectionType = sharedPref.getString("pref_connection_mode", "0");
		
		switch (Integer.parseInt(connectionType)) {
		case 0:
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
			break;
			
		case 1:
			conn = new NSDP2P(this);
			break;

		default:
			break;
		}
		
	    try {
			conn.registerService();
		} catch (Exception e) {
			Log.d(TAG, "Cannot register service: " + e.getMessage());
        	Toast.makeText(getBaseContext(), "Cannot register service: " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
	    new ConnectionHandler(conn.getServerSocket()); //TODO shouldn't be public assigned?
	}
	
	/** register the BroadcastReceiver with the intent values to be matched *///TODO remove comment?
    @Override
    public void onResume() {
        super.onResume();
        
    }

    @Override
    public void onPause() {
        super.onPause();
        //conn.pauseServer();
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	if (conn!=null) conn.pauseServer();
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.server_main, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
}

