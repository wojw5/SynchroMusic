package pl.airpolsl.synchromusic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.util.Log;

public class ServerBroadcastReceiver extends BroadcastReceiver {
	public ServerBroadcastReceiver() {
	}

	private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private ServerMainActivity mActivity;

    public ServerBroadcastReceiver(WifiP2pManager manager, Channel channel,
    		ServerMainActivity activity) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;
    }
    
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Determine if Wifi P2P mode is enabled or not, alert
            // the Activity.
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                //activity.setIsWifiP2pEnabled(true);
            	Log.d("ServerBroadcast", "Wifi on");
            } else {
                //activity.setIsWifiP2pEnabled(false);
            	Log.d("ServerBroadcast", "Wifi off");
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            // The peer list has changed!  We should probably do something about
            // that.
        	Log.d("ServerBroadcast", "peers Changed action");
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            // Connection state changed!  We should probably do something about
            // that.
        	Log.d("ServerBroadcast", "Con Changed");

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
           // DeviceListFragment fragment = (DeviceListFragment) activity.getFragmentManager()
            //        .findFragmentById(R.id.frag_list);
            //fragment.updateThisDevice((WifiP2pDevice) intent.getParcelableExtra(
              ///      WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));
        	Log.d("ServerBroadcast", "Dev Changed");
        }
        else throw new UnsupportedOperationException("Not yet implemented" + action);
	}
}
