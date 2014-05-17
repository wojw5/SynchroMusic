package pl.airpolsl.synchromusic;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
//import android.view.Menu;
//import android.view.MenuItem;

public class RulesApprovalActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rules_approval);
	}
	
	public void onCheckboxClicked(View view) {
	    findViewById(R.id.accept_button).setEnabled(((CheckBox) view).isChecked());
	}
}
