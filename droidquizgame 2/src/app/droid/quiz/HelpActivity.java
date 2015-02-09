package app.droid.quiz;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import app.droid.quiz.ProfileActivity.LoadStatisticsTask;

public class HelpActivity extends Activity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.help);
		
	}

	@Override
	public void onBackPressed() {
		Intent ActivityIntent = new Intent(HelpActivity.this,
				FrontPageActivity.class);
		startActivity(ActivityIntent);
		finish();
		// MainScreen.this.finish();
		super.onDestroy();
	}
}
