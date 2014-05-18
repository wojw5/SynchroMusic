package pl.airpolsl.synchromusic;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

public class MainMenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
	}
	
	public void goToServer(View view)
	{
		startActivity( new Intent(this,ServerMainActivity.class));
	}
	
	public void goToSettings(View view)
	{
		startActivity( new Intent(this,SettingsActivity.class));
	}
}
