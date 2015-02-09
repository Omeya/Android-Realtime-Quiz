package app.droid.quiz;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import app.droid.quiz.LoginActivity.UserLoginFaceBookTask;

public class LogInOptionsActivity extends Activity {

	public static final String mAPP_ID = "164121927106630";
	public Facebook mFacebook = new Facebook(mAPP_ID);

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.log_in_options);
		Button signUpFaceBookButton = (Button) findViewById(R.id.signUpFacebookButton);
		Button signUpEmailButton = (Button) findViewById(R.id.signUpEmailButton);
		Button logInButton = (Button) findViewById(R.id.signInButton);
		signUpEmailButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent option = new Intent(getApplicationContext(),
						SignUpEmailActivity.class);
				startActivity(option);
				finish();

			}
		});
		logInButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent option = new Intent(getApplicationContext(),
						LoginActivity.class);
				startActivity(option);
				finish();

			}
		});
		signUpFaceBookButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!mFacebook.isSessionValid()) {
					// Toast.makeText(LogInOptionsActivity.this, "Authorizing",
					// Toast.LENGTH_SHORT).show();
					mFacebook
							.authorize(LogInOptionsActivity.this,
									new String[] { "email" },
									new LoginDialogListener());
				} else {
					Toast.makeText(LogInOptionsActivity.this,
							"Please sign out first to sign up",
							Toast.LENGTH_SHORT).show();
					// try {
					// JSONObject json =
					// Util.parseJson(mFacebook.request("me"));
					// String facebookID = json.getString("id");
					// String firstName = json.getString("first_name");
					// String lastName = json.getString("last_name");
					// System.out.println(facebookID+"   ");
					// Toast.makeText(LogInOptionsActivity.this,
					// "You already have a valid session, " + firstName + " " +
					// lastName + ". No need to re-authorize.",
					// Toast.LENGTH_SHORT).show();
					// }
					// catch( Exception error ) {
					// Toast.makeText( LogInOptionsActivity.this,
					// error.toString(),
					// Toast.LENGTH_SHORT).show();
					// }
				}

			}
		});
		// SessionStore.restore(mFacebook, this);
	}

//	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuItem item = menu.add("Log Out");
//		item.setOnMenuItemClickListener(new OnMenuItemClickListener() {
//
//			public boolean onMenuItemClick(MenuItem item) {
//				try {
//					mFacebook.logout(LogInOptionsActivity.this);
//				} catch (Exception e) {
//					// e.printStackTrace();
//				}
//				InsureUtility.saveuserlogincredentials(getApplicationContext(),
//						"", "", "N");
//				Intent intent = new Intent(getApplicationContext(),
//						LoginActivity.class);
//				startActivity(intent);
//				finish();
//				return false;
//			}
//		});
//		return super.onCreateOptionsMenu(menu);
//	}

	class LoadPlayTask extends AsyncTask<String, Void, Boolean> {

		private ProgressDialog pdialog = null;
		private String response;
		private Object photo;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pdialog = new ProgressDialog(LogInOptionsActivity.this);
			pdialog.setTitle("Sign Up");
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
			System.out.println("the ress  " + response + "1");
			// try {
			// mFacebook.logout(LogInOptionsActivity.this);
			// } catch (MalformedURLException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			if (response.trim().equalsIgnoreCase("exist")) {
				showAlertDialog("Error", "The Account already exists");
			} else if (response.trim().equalsIgnoreCase("done")) {
				Intent option = new Intent(getApplicationContext(),
						FrontPageActivity.class);
				startActivity(option);
				finish();
			}  else
				showAlertDialog("Error", response);
		}

		private void showAlertDialog(String title, String message) {
			new AlertDialog.Builder(LogInOptionsActivity.this)
					.setTitle(title)
					.setMessage(message)
					.setNeutralButton("Ok",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									// if (loginResult) {
									//
									// }
								}
							}).show();

		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			String b64Image = "";
			// if (!photo.equals("")) {

			try {
				URL img_value = null;
				img_value = new URL("http://graph.facebook.com/"
						+ json.getString("id") + "/picture?type=large");
				Bitmap mIcon1 = BitmapFactory.decodeStream(img_value
						.openConnection().getInputStream());

				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				mIcon1.compress(Bitmap.CompressFormat.PNG, 100, stream);
				mIcon1.recycle();
				byte[] byteArray = stream.toByteArray();
				stream.close();
				stream = null;
				b64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// }
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("firstName", ifNull(json.getString("first_name")));
				jsonObject.put("lastName", ifNull(json.getString("last_name")));
				jsonObject.put("email", ifNull(json.getString("email")));
				jsonObject.put("id", ifNull(json.getString("id")));
				jsonObject.put("password", "0");
				jsonObject.put("phone", "");
				jsonObject.put("country", "");
				jsonObject.put("photo", ifNull(b64Image));
				jsonObject.put("is_facebook", 1);
				jsonObject.put("friends", ifNull(friends));
				HttpParams postParams = new BasicHttpParams();
				HttpClient client = new DefaultHttpClient(postParams);
				HttpPost httpGet = new HttpPost(CommonUtilities.SERVER_URL
						+ "/SignUpEmail");
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						5);
				nameValuePairs.add(new BasicNameValuePair("object", jsonObject
						.toString()));
				httpGet.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse httpResponse = client.execute(httpGet);
				HttpEntity entity = httpResponse.getEntity();
				response = EntityUtils.toString(entity);
				System.out.println(response);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}

	}

	private String ifNull(String str){
		if(str == null)
			return "";
		else
			return str;
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mFacebook.authorizeCallback(requestCode, resultCode, data);
		System.out.println("on ressssssssssss");
	}

	private JSONObject json;

	private String friends;

	public final class LoginDialogListener implements DialogListener {

		

		public void onComplete(Bundle values) {
			new UserLoginFaceBookTask().execute("");
		}

		public void onFacebookError(FacebookError error) {
			error.printStackTrace();
			Toast.makeText(LogInOptionsActivity.this,
					"Something went wrong. Please try again.",
					Toast.LENGTH_LONG).show();
		}

		public void onError(DialogError error) {
			error.printStackTrace();
			Toast.makeText(LogInOptionsActivity.this,
					"Something went wrong. Please try again.",
					Toast.LENGTH_LONG).show();
		}

		public void onCancel() {
			System.out.println("cancelna");
			Toast.makeText(LogInOptionsActivity.this,
					"Something went wrong. Please try again.",
					Toast.LENGTH_LONG).show();
		}
	}
	class UserLoginFaceBookTask extends AsyncTask<String, Void, Boolean> {
		private JSONObject json2;
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			new LoadPlayTask().execute("");
		}
		//
		@Override
		protected Boolean doInBackground(String... arg0) {
			try {
				// The user has logged in, so now you can query and use their
				// Facebook info
				json = Util.parseJson(mFacebook.request("me"));
				System.out.println(json.toString());
				String facebookID = json.getString("id");
				String firstName = json.getString("first_name");
				String lastName = json.getString("last_name");

				json2 = Util.parseJson(mFacebook.request("me/friends"));
				System.out.println(json2.toString());
				JSONArray jsonArray = json2.getJSONArray("data");
				friends = "";
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					friends += jsonObject.getString("id");
					if (i != jsonArray.length() - 1)
						friends += ",";
				}
				// Toast.makeText( LogInOptionsActivity.this,
				// "Thank you for Logging In, " + firstName + " " + lastName +
				// "!", Toast.LENGTH_SHORT).show();
				// SessionStore.save(mFacebook, LogInOptionsActivity.this);
				// SessionStore.clear(LogInOptionsActivity.this);
				
			} catch (Exception error) {
				Toast.makeText(LogInOptionsActivity.this, error.toString(),
						Toast.LENGTH_SHORT).show();
			}
			return null;
		}
		
	}
}
