package app.droid.quiz;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.revmob.RevMob;
import com.revmob.ads.banner.RevMobBanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import app.droid.quiz.LogInOptionsActivity.LoadPlayTask;
import app.droid.quiz.LogInOptionsActivity.LoginDialogListener;
import com.revmob.ads.banner.RevMobBanner;
import android.view.View;

import com.revmob.RevMob;

public class LoginActivity extends Activity implements OnClickListener {

	Button btn_login, btn_register, btn_login_facebook;
	EditText et_uname, et_pass;
	CheckBox chk_remember;
	Intent ActivityIntent;
	String TAG = "LoginActivity";
	public static final String mAPP_ID = "164121927106630";
	public Facebook mFacebook = new Facebook(mAPP_ID);
	boolean loginResult = false;

	private RevMob revmob;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// Configure Chartboost
	
		String appId = "5221fa1b16ba472a49000000";
		String appSignature = "f9d82b8403c09e44a109a30afc33d5a0364a0085";
	

		// Show an interstitial

		revmob = RevMob.start(this);
		
		//this.cb.showMoreApps();
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.login);


        RevMobBanner banner = revmob.createBanner(this);
        ViewGroup view = (ViewGroup) findViewById(R.id.banner);
        view.addView(banner);
		chk_remember = (CheckBox) findViewById(R.id.img_checkbox);
		et_uname = (EditText) findViewById(R.id.et_uname);
		et_pass = (EditText) findViewById(R.id.et_pass);

		btn_login = (Button) findViewById(R.id.btn_login);
		btn_register = (Button) findViewById(R.id.btn_register);
		btn_login_facebook = (Button) findViewById(R.id.btn_login_facebook);
		btn_register.setOnClickListener(this);
		btn_login.setOnClickListener(this);

		InsureUtility.getUsernamePasswordFromPreferenes(LoginActivity.this);
		et_uname.setText(InsureUtility.Username);
		et_pass.setText(InsureUtility.Password);
		if (InsureUtility.Remember.equals("Y")) {
			chk_remember.setChecked(true);
			if(et_uname.getText().toString().length()> 0){
				if (InsureUtility.isInternetAvailable(LoginActivity.this)) {
					if (et_uname.getText().toString().length() > 0
							&& et_pass.getText().toString().length() > 0) {

						new UserLoginTask().execute("");
						/*
						 * Intent menu = new
						 * Intent(LoginActivity.this,MenuActivity.class);
						 * startActivity(menu);
						 */
					} else {
						Toast.makeText(this,
								"Username or Password cannot be empty",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(
							this,
							"Network is not available check your network settings.",
							Toast.LENGTH_LONG).show();
				}
//				if(mFacebook.isSessionValid())
//					mFacebook.authorize(LoginActivity.this,
//							new String[] { "email" }, new LoginDialogListener());
			}
		} else {
			et_uname.setText("");
			et_pass.setText("");
			chk_remember.setChecked(false);
		}

		btn_login_facebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				mFacebook.authorize(LoginActivity.this,
						new String[] { "email" }, new LoginDialogListener());

			}
		});
		
		
		Button tncLink = (Button) findViewById(R.id.tncLink);
		tncLink.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent option = new Intent(getApplicationContext(),
						TermsAndConditions.class);
				startActivity(option);
				//finish();

			}
		});	
	}
	
	@Override
	protected void onStart() {
	    super.onStart();

	    // Notify the beginning of a user session. Must not be dependent on user actions or any prior network requests.
	  
	}   

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mFacebook.authorizeCallback(requestCode, resultCode, data);
		System.out.println("on ressssssssssss");
	}

	private JSONObject json;

	class UserLoginFaceBookTask extends AsyncTask<String, Void, Boolean> {

		private ProgressDialog pdialog = null;
		private String response = "none";

		String url;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pdialog = new ProgressDialog(LoginActivity.this);
			pdialog.setTitle("Working");
			pdialog.setMessage("Please Wait...");
			pdialog.show();
			
			//String firstName = json.getString("first_name");
			//String lastName = json.getString("last_name");
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			pdialog.dismiss();
			pdialog = null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pdialog.dismiss();
			pdialog = null;
			loginResult = result;
			System.out.println(response + "1");
//			try {
//				mFacebook.logout(LoginActivity.this);
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			if (response.startsWith("exist")) {
				System.out.println("ezay");
				InsureUtility.saveuserlogincredentials(
						getApplicationContext(), facebookID, "0", "Y");
//				if (chk_remember.isChecked()) {
//					InsureUtility.saveuserlogincredentials(
//							getApplicationContext(), facebookID, "0", "Y");
//				} else {
//					InsureUtility.saveuserlogincredentials(
//							getApplicationContext(), facebookID, "0", "N");
//				}
				String rest = response.substring(5);
				System.out.println(rest+"    rstt");
				Fields.userName = rest.split(",")[0];
				Fields.id = rest.split(",")[1];
				System.out.println("" +Fields.id);
				
				ActivityIntent = new Intent(LoginActivity.this,
						FrontPageActivity.class);
				startActivity(ActivityIntent);
				LoginActivity.this.finish();
				InsureUtility.getimei(LoginActivity.this);

			} else {
				showAlertDialog("Login", response);
			}

		}

		private void showAlertDialog(String title, String message) {
			new AlertDialog.Builder(LoginActivity.this)
					.setTitle(title)
					.setMessage(message)
					.setNeutralButton("Ok",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									if (loginResult) {

									}
								}
							}).show();

		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			String TAG_RESULTS = "result";
			try {
				json = Util.parseJson(mFacebook.request("me"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(json.toString());
			try {
				facebookID = json.getString("id");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("id", facebookID);

				jsonObject.put("password", "0");
				HttpParams postParams = new BasicHttpParams();
				HttpClient client = new DefaultHttpClient(postParams);
				HttpPost httpGet = new HttpPost(CommonUtilities.SERVER_URL
						+ "/LogIn");
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						5);
				nameValuePairs.add(new BasicNameValuePair("object", jsonObject
						.toString()));
				httpGet.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse httpResponse = client.execute(httpGet);
				HttpEntity entity = httpResponse.getEntity();
				response = EntityUtils.toString(entity);
				System.out.println(response);

				// InsureUtility
				// .saveusernames(LoginActivity.this,
				// c.getString("FirstName"),
				// c.getString("LastName"),
				// c.getString("MidName"));
				Fields.userID = facebookID.toString();
				// JsonParser jParser = new JsonParser();
				//
				// JSONObject json = jParser.getJSONFromUrl(url);
				// try {
				// if (json != null) {
				// results = json.getJSONArray(TAG_RESULTS);
				// Log.e("result length", "" + results.length());
				// JSONObject c = results.getJSONObject(0);
				// System.out.println("C:  " + c);
				// if (c.getString("ResultID").equals("3007")) {
				// response = c.getString("ResultMsg");
				//
				//
				// InsureUtility
				// .saveusernames(LoginActivity.this,
				// c.getString("FirstName"),
				// c.getString("LastName"),
				// c.getString("MidName"));
				// Fields.userID = et_uname.getText().toString();
				// InsureUtility.Username = et_uname.getText().toString();
				// // Fields.lastName = c.getString("LastName");
				// // Fields.middleName = c.getString("MidName");
				// // Fields.firstName = c.getString("FirstName");
				// // Fields.dOB = c.getString("DoB");
				// // ShowLog.i(TAG_RESULTS, "request success 3007");
				// // ShowLog.i("ResultMsg", c.getString("ResultMsg"));
				// // ShowLog.i("username", username);
				// // ShowLog.i("password", password);
				// return Boolean.TRUE;
				// } else {
				// response = c.getString("ResultMsg");
				// return Boolean.FALSE;
				// }

				// }
				// } catch (JSONException e) {
				// e.printStackTrace();
				// }

				if (response.equals("none")) {
					return Boolean.FALSE;
				} else {
					return Boolean.TRUE;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return Boolean.FALSE;
		}

	}

	private String facebookID;

	public final class LoginDialogListener implements DialogListener {

		public void onComplete(Bundle values) {
			try {
				// The user has logged in, so now you can query and use their
				// Facebook info
				
				

				// Toast.makeText( LogInOptionsActivity.this,
				// "Thank you for Logging In, " + firstName + " " + lastName +
				// "!", Toast.LENGTH_SHORT).show();
				// SessionStore.save(mFacebook, LogInOptionsActivity.this);
				// SessionStore.clear(LogInOptionsActivity.this);
				new UserLoginFaceBookTask().execute("");
			} catch (Exception error) {
				Toast.makeText(LoginActivity.this, error.toString(),
						Toast.LENGTH_SHORT).show();
			}
		}

		public void onFacebookError(FacebookError error) {
			error.printStackTrace();
			Toast.makeText(LoginActivity.this,
					"Something went wrong. Please try again.",
					Toast.LENGTH_LONG).show();
		}

		public void onError(DialogError error) {
			error.printStackTrace();
			Toast.makeText(LoginActivity.this,
					"Something went wrong. Please try again.",
					Toast.LENGTH_LONG).show();
		}

		public void onCancel() {
			System.out.println("cancelna");
			Toast.makeText(LoginActivity.this,
					"Something went wrong. Please try again.",
					Toast.LENGTH_LONG).show();
		}
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub

		Log.e("remember me status", "" + chk_remember.isChecked());

		switch (v.getId()) {

		case R.id.btn_login:
			if (InsureUtility.isInternetAvailable(LoginActivity.this)) {
				if (et_uname.getText().toString().length() > 0
						&& et_pass.getText().toString().length() > 0) {

					new UserLoginTask().execute("");
					/*
					 * Intent menu = new
					 * Intent(LoginActivity.this,MenuActivity.class);
					 * startActivity(menu);
					 */
				} else {
					Toast.makeText(this,
							"Username or Password cannot be empty",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(
						this,
						"Network is not available check your network settings.",
						Toast.LENGTH_LONG).show();
			}
			break;

		case R.id.btn_register:
			Intent register = new Intent(LoginActivity.this,
					LogInOptionsActivity.class);
			startActivity(register);
			finish();
			// new
			// UserRegisterTask().execute(insureUtility.registerationUrl+"fname=Mike&lname=Smith&addr=Maryland%20USA&phoneid=667890&tel=12345678901&email=youremail@yourdomain.com&photo=image/mypic.jpg&note=Please%20Send%20all%20documentss.&work=IT%20Consultant");
			break;

		default:
			break;
		}
	}

	class UserLoginTask extends AsyncTask<String, Void, Boolean> {

		private ProgressDialog pdialog = null;
		private String response = "none";
		private String username, password;

		String url;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pdialog = new ProgressDialog(LoginActivity.this);
			pdialog.setTitle("Working");
			pdialog.setMessage("Please Wait...");
			pdialog.show();
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			pdialog.dismiss();
			pdialog = null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pdialog.dismiss();
			pdialog = null;
			loginResult = result;
			System.out.println(response + "1");

			if (response.startsWith("exist")) {
				System.out.println("ezay");
				if (chk_remember.isChecked()) {
					InsureUtility.saveuserlogincredentials(
							getApplicationContext(), et_uname.getText()
									.toString(), et_pass.getText().toString(),
							"Y");
				} else {
					InsureUtility.saveuserlogincredentials(
							getApplicationContext(), et_uname.getText()
									.toString(), et_pass.getText().toString(),
							"N");
				}
				String rest = response.substring(5);
				Fields.userName = rest.split(",")[0];
				Fields.id = rest.split(",")[1];
				Fields.email = rest.split(",")[2];
				ActivityIntent = new Intent(LoginActivity.this,
						FrontPageActivity.class);
				startActivity(ActivityIntent);
				LoginActivity.this.finish();
				InsureUtility.getimei(LoginActivity.this);

			} else {
				showAlertDialog("Login", response);
			}

		}

		private void showAlertDialog(String title, String message) {
			new AlertDialog.Builder(LoginActivity.this)
					.setTitle(title)
					.setMessage(message)
					.setNeutralButton("Ok",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									if (loginResult) {

									}
								}
							}).show();

		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			String TAG_RESULTS = "result";

			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("id", et_uname.getText().toString());

				jsonObject.put("password", et_pass.getText().toString());
				HttpParams postParams = new BasicHttpParams();
				HttpClient client = new DefaultHttpClient(postParams);
				HttpPost httpGet = new HttpPost(CommonUtilities.SERVER_URL
						+ "/LogIn");
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						5);
				nameValuePairs.add(new BasicNameValuePair("object", jsonObject
						.toString()));
				httpGet.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse httpResponse = client.execute(httpGet);
				HttpEntity entity = httpResponse.getEntity();
				response = EntityUtils.toString(entity);
				System.out.println(response);

				// InsureUtility
				// .saveusernames(LoginActivity.this,
				// c.getString("FirstName"),
				// c.getString("LastName"),
				// c.getString("MidName"));
				Fields.userID = et_uname.getText().toString();
				// JsonParser jParser = new JsonParser();
				//
				// JSONObject json = jParser.getJSONFromUrl(url);
				// try {
				// if (json != null) {
				// results = json.getJSONArray(TAG_RESULTS);
				// Log.e("result length", "" + results.length());
				// JSONObject c = results.getJSONObject(0);
				// System.out.println("C:  " + c);
				// if (c.getString("ResultID").equals("3007")) {
				// response = c.getString("ResultMsg");
				//
				//
				// InsureUtility
				// .saveusernames(LoginActivity.this,
				// c.getString("FirstName"),
				// c.getString("LastName"),
				// c.getString("MidName"));
				// Fields.userID = et_uname.getText().toString();
				// InsureUtility.Username = et_uname.getText().toString();
				// // Fields.lastName = c.getString("LastName");
				// // Fields.middleName = c.getString("MidName");
				// // Fields.firstName = c.getString("FirstName");
				// // Fields.dOB = c.getString("DoB");
				// // ShowLog.i(TAG_RESULTS, "request success 3007");
				// // ShowLog.i("ResultMsg", c.getString("ResultMsg"));
				// // ShowLog.i("username", username);
				// // ShowLog.i("password", password);
				// return Boolean.TRUE;
				// } else {
				// response = c.getString("ResultMsg");
				// return Boolean.FALSE;
				// }

				// }
				// } catch (JSONException e) {
				// e.printStackTrace();
				// }

				if (response.equals("none")) {
					return Boolean.FALSE;
				} else {
					return Boolean.TRUE;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return Boolean.FALSE;
		}

	}

}
