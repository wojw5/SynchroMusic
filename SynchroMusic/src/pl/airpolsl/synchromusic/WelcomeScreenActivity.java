package pl.airpolsl.synchromusic;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;


public class WelcomeScreenActivity extends Activity {
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome_screen);

        new Handler().postDelayed(new Runnable() {
        	@Override
        	public void run(){
        		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(WelcomeScreenActivity.this);
        		if(settings.getBoolean("rolesApproved", false))
        			startActivity(new Intent(WelcomeScreenActivity.this,MainMenuActivity.class));
        		else
        			startActivity(new Intent(WelcomeScreenActivity.this,RulesApprovalActivity.class));
        		finish();
        	}

        }, 3000);
        
    }

}
