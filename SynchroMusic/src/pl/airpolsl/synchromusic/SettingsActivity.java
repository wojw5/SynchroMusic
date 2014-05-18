package pl.airpolsl.synchromusic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
	}
	
	public void goToMainMenu(View view)
	{
		startActivity( new Intent(this,MainMenuActivity.class));
		finish();
	}
}
