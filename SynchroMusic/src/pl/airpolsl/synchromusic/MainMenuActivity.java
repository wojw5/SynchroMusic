package pl.airpolsl.synchromusic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Main menu android activity
 *
 */
public class MainMenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
	}
	
	/**
	 * start server action
	 * @param view
	 */
	public void goToServer(View view)
	{
		startActivity( new Intent(this,ServerMainActivity.class));
	}
	
	/**
	 * start client action
	 * @param view
	 */
	public void goToClient(View view)
	{
		startActivity( new Intent(this,ClientMainActivity.class));
	}
	
	/**
	 * go to settings action
	 * @param view
	 */
	public void goToSettings(View view)
	{
		startActivity( new Intent(this,SettingsActivity.class));
	}
}
