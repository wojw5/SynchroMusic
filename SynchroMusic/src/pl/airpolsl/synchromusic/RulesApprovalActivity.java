package pl.airpolsl.synchromusic;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;

/**
 * Class indicates app rules and agreements notification. 
 * @author Wojciech
 *
 */
public class RulesApprovalActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rules_approval);
	}
	
	public void onCheckboxClicked(View view) {
	    findViewById(R.id.accept_button).setEnabled(((CheckBox) view).isChecked());
	}
	
	/*
	 * Save approval and go to settings activity.
	 */
	public void goToSettings(View view)
	{
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this );
		SharedPreferences.Editor prefEditor = settings.edit();
		prefEditor.putBoolean("rolesApproved", true);
		prefEditor.commit();
		startActivity( new Intent(this,SettingsActivity.class));
		finish();
	}
}
