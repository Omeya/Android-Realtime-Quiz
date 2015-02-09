package app.droid.quiz;

import static app.droid.quiz.CommonUtilities.SENDER_ID;
import static app.droid.quiz.CommonUtilities.SERVER_URL;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;

public class TermsAndConditions extends Activity {
	
	private WebView webView;
	 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	
		setContentView(R.layout.tandc);
		
		webView = (WebView) findViewById(R.id.webView1);
		//webView.getSettings().setJavaScriptEnabled(true);
		//webView.loadUrl("http://www.google.com");
		//String customHtml = "<html><body><h1>Hello, WebView</h1></body></html>";
		webView.loadUrl("file:///android_asset/tNc.html");
	 
	}
	
//	@Override
//	public void onBackPressed() {
//		//Intent ActivityIntent = new Intent(TermsAndConditions.this,FrontPageActivity.class);
//		//startActivity(ActivityIntent);
//		this.finish();
//		// MainScreen.this.finish();
//		super.onDestroy();
//	}
}
