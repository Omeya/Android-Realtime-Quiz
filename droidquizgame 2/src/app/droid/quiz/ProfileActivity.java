package app.droid.quiz;

import static app.droid.quiz.CommonUtilities.SERVER_URL;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

import com.google.android.gcm.GCMRegistrar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends Activity {

	private TextView nameTextView, totalPointsTextView, lastGameTextView,
			wonTextView;

	private ImageView profileImageView, badge1ImageView, badge2ImageView,
			badge3ImageView, badge4ImageView, badge5ImageView, badge6ImageView,
			badge7ImageView, badge8ImageView, badge9ImageView,
			badge10ImageView, badge11ImageView, badge12ImageView,
			badge13ImageView, badge14ImageView, badge15ImageView,
			badge16ImageView, badge17ImageView, badge18ImageView,
			badge19ImageView, badge20ImageView;

	private Uri targetUri;

	private Bitmap bitmap;

	private String photo;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.profile);
		new LoadStatisticsTask().execute("");
		nameTextView = (TextView) findViewById(R.id.nameTextView);
		totalPointsTextView = (TextView) findViewById(R.id.totalPointsTextView);
		lastGameTextView = (TextView) findViewById(R.id.lastGameTextView);
		wonTextView = (TextView) findViewById(R.id.wonTextView);
		profileImageView = (ImageView) findViewById(R.id.profileImageView);
		badge1ImageView = (ImageView) findViewById(R.id.imageView01);
		badge2ImageView = (ImageView) findViewById(R.id.imageView02);
		badge3ImageView = (ImageView) findViewById(R.id.imageView03);
		badge4ImageView = (ImageView) findViewById(R.id.imageView04);
		badge5ImageView = (ImageView) findViewById(R.id.imageView05);
		badge6ImageView = (ImageView) findViewById(R.id.imageView06);
		badge7ImageView = (ImageView) findViewById(R.id.imageView07);
		badge8ImageView = (ImageView) findViewById(R.id.imageView08);
		badge9ImageView = (ImageView) findViewById(R.id.imageView09);
		badge10ImageView = (ImageView) findViewById(R.id.imageView10);
		badge11ImageView = (ImageView) findViewById(R.id.imageView11);
		badge12ImageView = (ImageView) findViewById(R.id.imageView12);
		badge13ImageView = (ImageView) findViewById(R.id.imageView13);
		badge14ImageView = (ImageView) findViewById(R.id.imageView14);
		badge15ImageView = (ImageView) findViewById(R.id.imageView15);
		badge16ImageView = (ImageView) findViewById(R.id.imageView16);
		badge17ImageView = (ImageView) findViewById(R.id.imageView17);
		badge18ImageView = (ImageView) findViewById(R.id.imageView18);
		badge19ImageView = (ImageView) findViewById(R.id.imageView19);
		badge20ImageView = (ImageView) findViewById(R.id.imageView20);
		profileImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, 0);
				finish();
			}
		});
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
					profileImageView.setImageBitmap(bitmap);
				} catch (FileNotFoundException e) {
					// block
					e.printStackTrace();
				}
				photo = targetUri.toString();
				new LoadImageTask().execute("");
				// Toast.makeText(getApplicationContext(), photo, 10).show();
			}
		}
	}

	class LoadStatisticsTask extends AsyncTask<String, Void, Boolean> {

		private ProgressDialog pdialog = null;
		private String response;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pdialog = new ProgressDialog(ProfileActivity.this);
			pdialog.setTitle("Loading");
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
			try {
				pdialog.dismiss();
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			pdialog = null;
			System.out.println("the ress  " + response + "1");
			try {
				JSONObject jsonObject = new JSONObject(response);
				nameTextView.setText(jsonObject.getString("name"));

				totalPointsTextView.setText("Total Points :"
						+ jsonObject.getInt("scores") + "/"
						+ (jsonObject.getInt("games_played") * 10));
				lastGameTextView.setText("Last Game    :"
						+ jsonObject.getInt("last_game_score") + "/" + "10");
				wonTextView.setText("$ Won        :"
						+ jsonObject.getInt("times_won"));
				totalPointsTextView.invalidate();
				lastGameTextView.invalidate();
				wonTextView.invalidate();
				loadImage(jsonObject.getString("photo"), profileImageView);
				JSONArray jsonArray = jsonObject.getJSONArray("badges");
				if (jsonArray.length() > 0)
					loadImage2(jsonArray.getString(0), badge1ImageView);
				if (jsonArray.length() > 1)
					loadImage2(jsonArray.getString(1), badge2ImageView);
				if (jsonArray.length() > 2)
					loadImage2(jsonArray.getString(2), badge3ImageView);
				if (jsonArray.length() > 3)
					loadImage2(jsonArray.getString(3), badge4ImageView);
				if (jsonArray.length() > 4)
					loadImage2(jsonArray.getString(4), badge5ImageView);
				if (jsonArray.length() > 5)
					loadImage2(jsonArray.getString(5), badge6ImageView);
				if (jsonArray.length() > 6)
					loadImage2(jsonArray.getString(6), badge7ImageView);
				if (jsonArray.length() > 7)
					loadImage2(jsonArray.getString(7), badge8ImageView);
				if (jsonArray.length() > 8)
					loadImage2(jsonArray.getString(8), badge9ImageView);
				if (jsonArray.length() > 9)
					loadImage2(jsonArray.getString(9), badge10ImageView);
				if (jsonArray.length() > 10)
					loadImage2(jsonArray.getString(10), badge11ImageView);
				if (jsonArray.length() > 11)
					loadImage2(jsonArray.getString(11), badge12ImageView);
				if (jsonArray.length() > 12)
					loadImage2(jsonArray.getString(12), badge13ImageView);
				if (jsonArray.length() > 13)
					loadImage2(jsonArray.getString(13), badge4ImageView);
				if (jsonArray.length() > 14)
					loadImage2(jsonArray.getString(14), badge15ImageView);
				if (jsonArray.length() > 15)
					loadImage2(jsonArray.getString(15), badge16ImageView);
				if (jsonArray.length() > 16)
					loadImage2(jsonArray.getString(16), badge17ImageView);
				if (jsonArray.length() > 17)
					loadImage2(jsonArray.getString(17), badge18ImageView);
				if (jsonArray.length() > 18)
					loadImage2(jsonArray.getString(18), badge19ImageView);
				if (jsonArray.length() > 19)
					loadImage2(jsonArray.getString(19), badge20ImageView);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		private void loadImage2(String string, ImageView badgeImageView) {
			int imageInt = Integer.parseInt(string);
			int id = 100;
			switch (imageInt) {
			case 1:
				// badgeImageView.setImageResource(R.drawable.p1);
				id = R.drawable.p1;
				break;
			case 2:
				// badgeImageView.setImageResource(R.drawable.p2);
				id = R.drawable.p2;
				break;
			case 3:
				// badgeImageView.setImageResource(R.drawable.p3);
				id = R.drawable.p3;
				break;
			case 4:
				// badgeImageView.setImageResource(R.drawable.p4);
				id = R.drawable.p4;
				break;
			case 5:
				// badgeImageView.setImageResource(R.drawable.p5);
				id = R.drawable.p5;
				break;
			case 6:
				// badgeImageView.setImageResource(R.drawable.p6);
				id = R.drawable.p6;
				break;
			case 7:
				// badgeImageView.setImageResource(R.drawable.p7);
				id = R.drawable.p7;
				break;
			case 8:
				// badgeImageView.setImageResource(R.drawable.p8);
				id = R.drawable.p8;
				break;
			case 9:
				// badgeImageView.setImageResource(R.drawable.p9);
				id = R.drawable.p9;
				break;
			case 10:
				// badgeImageView.setImageResource(R.drawable.p10);
				id = R.drawable.p10;
				break;
			case 11:
				// badgeImageView.setImageResource(R.drawable.p11);
				id = R.drawable.p11;
				break;
			case 12:
				// badgeImageView.setImageResource(R.drawable.p12);
				id = R.drawable.p12;
				break;
			case 13:
				// badgeImageView.setImageResource(R.drawable.p13);
				id = R.drawable.p13;
				break;
			case 14:
				// badgeImageView.setImageResource(R.drawable.p14);
				id = R.drawable.p14;
				break;
			case 15:
				// badgeImageView.setImageResource(R.drawable.p15);
				id = R.drawable.p15;
				break;
			case 16:
				// badgeImageView.setImageResource(R.drawable.p16);
				id = R.drawable.p16;
				break;
			// case 17:
			// badgeImageView.setImageResource(R.drawable.p17);
			// break;
			case 18:
				// badgeImageView.setImageResource(R.drawable.p18);
				id = R.drawable.p18;
				break;
			case 19:
				// badgeImageView.setImageResource(R.drawable.p19);
				id = R.drawable.p19;
				break;
			case 20:
				// badgeImageView.setImageResource(R.drawable.p20);
				id = R.drawable.p20;
				break;
			case 100:
				// badgeImageView.setImageResource(R.drawable.p100);
				id = R.drawable.p100;
				break;
			default:
				break;
			}
			// badgeImageView.setMaxHeight(83);
			// badgeImageView.setMaxWidth(69);

			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);

			bitmap = Bitmap.createScaledBitmap(bitmap, 69, 83, true);
			badgeImageView.setImageBitmap(bitmap);
			badgeImageView.setVisibility(View.VISIBLE);
			badgeImageView.invalidate();
		}

		private void loadImage(String imageText, ImageView imageView) {
			byte[] byteArray = Base64.decode(imageText, Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
					byteArray.length);

			bitmap = Bitmap.createScaledBitmap(bitmap, 69, 83, true);
			imageView.setImageBitmap(bitmap);
			imageView.setVisibility(View.VISIBLE);
			imageView.invalidate();
		}

		private void showAlertDialog(String title, String message) {
			new AlertDialog.Builder(ProfileActivity.this)
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
			HttpParams postParams = new BasicHttpParams();
			// HttpConnectionParams.setSoTimeout(postParams, 3000);
			// HttpConnectionParams.setConnectionTimeout(postParams, 3000);
			HttpClient client = new DefaultHttpClient(postParams);
			HttpPost httpGet = new HttpPost(SERVER_URL + "/Profile");
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
			nameValuePairs.add(new BasicNameValuePair("id", Fields.id));
			try {
				httpGet.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse httpResponse;

				httpResponse = client.execute(httpGet);

				HttpEntity entity = httpResponse.getEntity();
				response = EntityUtils.toString(entity);
				System.out.println(response);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}

	}

	
	class LoadImageTask extends AsyncTask<String, Void, Boolean> {

		private ProgressDialog pdialog = null;
		private String response;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pdialog = new ProgressDialog(ProfileActivity.this);
			pdialog.setTitle("Uploading");
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
		}

		@Override
		protected Boolean doInBackground(String... params) {
			HttpParams postParams = new BasicHttpParams();
			// HttpConnectionParams.setSoTimeout(postParams, 3000);
			// HttpConnectionParams.setConnectionTimeout(postParams, 3000);
			HttpClient client = new DefaultHttpClient(postParams);
			HttpPost httpGet = new HttpPost(SERVER_URL + "/ChangePic");
			
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
				jsonObject.put("id", Fields.id);
				jsonObject.put("photo", b64Image);
			}catch (Exception e) {
			}
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
			nameValuePairs.add(new BasicNameValuePair("object", jsonObject.toString()));
			try {
				httpGet.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse httpResponse;

				httpResponse = client.execute(httpGet);

				HttpEntity entity = httpResponse.getEntity();
				response = EntityUtils.toString(entity);
				System.out.println(response);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}

	}

	@Override
	public void onBackPressed() {
		Intent ActivityIntent = new Intent(ProfileActivity.this,
				FrontPageActivity.class);
		startActivity(ActivityIntent);
		finish();
		// MainScreen.this.finish();
		super.onDestroy();
	}

}
