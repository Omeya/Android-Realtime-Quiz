package app.droid.quiz;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class SignUpEmailActivity extends Activity {

	private ImageView img_profile;
	private Uri targetUri;
	private Bitmap bitmap;
	private String photo = "";
	private Button joinQuizButton;
	private EditText firstNameEditText, lastNameEditText, emailEditText,
			passwordEditText, phoneEditText, countryEditText;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.sign_up_email);
		img_profile = (ImageView) findViewById(R.id.galleryImageView);
		img_profile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, 0);
				finish();
			}
		});

		firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
		lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
		emailEditText = (EditText) findViewById(R.id.emailEditText);
		passwordEditText = (EditText) findViewById(R.id.passwordEditText);
		phoneEditText = (EditText) findViewById(R.id.phoneEditText);
		countryEditText = (EditText) findViewById(R.id.countryEditText);
		joinQuizButton = (Button) findViewById(R.id.joinQuizButton);
		joinQuizButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (firstNameEditText.getText().toString().equals("")) {
					strtoast = "Please fill in first name";
					toastHandler.post(toast);
				} else if (lastNameEditText.getText().toString().equals("")) {
					strtoast = "Please fill in last name";
					toastHandler.post(toast);
				} else if (emailEditText.getText().toString().equals("")) {
					strtoast = "Please fill in email";
					toastHandler.post(toast);
				} else if (passwordEditText.getText().toString().equals("")) {
					strtoast = "Please fill in password";
					toastHandler.post(toast);
				} else if (phoneEditText.getText().toString().equals("")) {
					strtoast = "Please fill in phone";
					toastHandler.post(toast);
				} else if (countryEditText.getText().toString().equals("")) {
					strtoast = "Please fill in country";
					toastHandler.post(toast);
				} else {
					new LoadPlayTask().execute("");
				}

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

	private Handler toastHandler = new Handler();

	private CharSequence strtoast;
	// handler to show the toast
	private Runnable toast = new Runnable() {

		public void run() {
			Toast.makeText(getApplicationContext(), strtoast,
					Toast.LENGTH_SHORT).show();
		}
	};

	class LoadPlayTask extends AsyncTask<String, Void, Boolean> {

		private ProgressDialog pdialog = null;
		private String response;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pdialog = new ProgressDialog(SignUpEmailActivity.this);
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
			if (response.trim().equalsIgnoreCase("exist")) {
				showAlertDialog("Error", "The email already exists");
			} else if (response.trim().equalsIgnoreCase("done")) {
				Intent option = new Intent(getApplicationContext(),
						FrontPageActivity.class);
				startActivity(option);
				finish();
			} else
				showAlertDialog("Error", response);
		}

		private void showAlertDialog(String title, String message) {
			new AlertDialog.Builder(SignUpEmailActivity.this)
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
			if (!photo.equals("")) {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				bitmap.recycle();
				byte[] byteArray = stream.toByteArray();
				try {
					stream.close();
					stream = null;
					b64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("firstName", firstNameEditText.getText()
						.toString());
				jsonObject.put("lastName", lastNameEditText.getText()
						.toString());
				jsonObject.put("email", emailEditText.getText().toString());
				jsonObject.put("password", passwordEditText.getText()
						.toString());
				jsonObject.put("phone", phoneEditText.getText().toString());
				jsonObject.put("country", countryEditText.getText().toString());
				jsonObject.put("photo", b64Image);
				jsonObject.put("is_facebook", 0);
				jsonObject.put("friends", null);
				jsonObject.put("id", emailEditText.getText().toString());
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

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (data == null)
			Log.e("onActivityresult", "data null");
		else {
			super.onActivityResult(requestCode, resultCode, data);

			if (resultCode == RESULT_OK && requestCode == 0) {
				targetUri = data.getData();
				System.out.println(targetUri);
				// textTargetUri.setText(targetUri.toString());
				try {
					bitmap = BitmapFactory.decodeStream(getContentResolver()
							.openInputStream(targetUri));
					
					bitmap = Bitmap.createScaledBitmap(bitmap, 69, 83, true);
					img_profile.setImageBitmap(bitmap);
				} catch (FileNotFoundException e) {
					// block
					e.printStackTrace();
				}
				photo = targetUri.toString();
				// Toast.makeText(getApplicationContext(), photo, 10).show();
			}
		}
	}

}
