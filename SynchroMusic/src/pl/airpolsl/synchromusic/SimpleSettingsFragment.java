package pl.airpolsl.synchromusic;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class SimpleSettingsFragment extends PreferenceFragment {

	public SimpleSettingsFragment() {
		// Required empty public constructor
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

}
