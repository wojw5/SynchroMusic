package pl.airpolsl.synchromusic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
/**
 * Activity for preferences management.
 * @author Wojciech
 *
 */
public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
	}
	
	public void goToMainMenu(View view)
	{
		Intent intent = new Intent(this,MainMenuActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
		finish();
	}
}
