package pl.airpolsl.synchromusic;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Player buttons for server
 * @author Wojciech
 *
 */
public class SerwerPlayerButtonsFragment extends Fragment {

	public SerwerPlayerButtonsFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_serwer_player_buttons,
				container, false);
	}
}
