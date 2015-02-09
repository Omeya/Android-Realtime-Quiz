package app.droid.quiz;

import static app.droid.quiz.CommonUtilities.SERVER_URL;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class StatisticsActivity extends Activity {

	private JSONArray scoresArray;
	private JSONArray friendsScoresArray;
	private JSONArray lastGameScores;
	private JSONArray lastGameFriendsScores;

	private Button friendsButton, usersButton, lastGameButton, allgamesButton;
	private boolean friendsButtonClicked, lastGameButtonClicked;
	private TextView firstPointsTextView, secondPointsTextView,
			thirdPointsTextView, fourthPointsTextView, fifthPointsTextView;
	private TextView firstNameTextView, secondNameTextView, thirdNameTextView,
			fourthNameTextView, fifthNameTextView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.statistics);
		new LoadStatisticsTask().execute("");

		friendsButton = (Button) findViewById(R.id.friendsButton);
		usersButton = (Button) findViewById(R.id.usersButton);
		lastGameButton = (Button) findViewById(R.id.lastGameButton);
		allgamesButton = (Button) findViewById(R.id.allGamesButton);

		firstNameTextView = (TextView) findViewById(R.id.firstNameTextView);
		firstPointsTextView = (TextView) findViewById(R.id.firstPointsTextView);
		secondNameTextView = (TextView) findViewById(R.id.secondNameTextView);
		secondPointsTextView = (TextView) findViewById(R.id.secondPointsTextView);
		thirdNameTextView = (TextView) findViewById(R.id.thirdNameTextView);
		thirdPointsTextView = (TextView) findViewById(R.id.thirdPointsTextView);
		fourthNameTextView = (TextView) findViewById(R.id.fourthNameTextView);
		fourthPointsTextView = (TextView) findViewById(R.id.fourthPointsTextView);
		fifthNameTextView = (TextView) findViewById(R.id.fifthNameTextView);
		fifthPointsTextView = (TextView) findViewById(R.id.fifthPointsTextView);
		friendsButtonClicked = true;
		lastGameButtonClicked = true;
		friendsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!friendsButtonClicked) {
					friendsButtonClicked = true;
					friendsButton
							.setBackgroundResource(R.drawable.friends_clicked);
					friendsButton.invalidate();
					usersButton
							.setBackgroundResource(R.drawable.users_unclicked);
					usersButton.invalidate();
					if (lastGameButtonClicked)
						loadStat(lastGameFriendsScores);
					else
						loadStat(friendsScoresArray);
				}
			}
		});
		usersButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (friendsButtonClicked) {
					friendsButtonClicked = false;
					friendsButton
							.setBackgroundResource(R.drawable.friends_unclicked);
					friendsButton.invalidate();
					usersButton.setBackgroundResource(R.drawable.users_clicked);
					usersButton.invalidate();
					if (lastGameButtonClicked)
						loadStat(lastGameScores);
					else
						loadStat(scoresArray);
				}
			}
		});
		lastGameButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!lastGameButtonClicked) {
					lastGameButtonClicked = true;
					lastGameButton
							.setBackgroundResource(R.drawable.last_game_clicked);
					lastGameButton.invalidate();
					allgamesButton
							.setBackgroundResource(R.drawable.all_games_unclicked);
					allgamesButton.invalidate();
					if (friendsButtonClicked)
						loadStat(lastGameFriendsScores);
					else
						loadStat(lastGameScores);
				}
			}
		});

		allgamesButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (lastGameButtonClicked) {
					lastGameButtonClicked = false;
					lastGameButton
							.setBackgroundResource(R.drawable.last_game_unclicked);
					lastGameButton.invalidate();
					allgamesButton
							.setBackgroundResource(R.drawable.all_games_clicked);
					allgamesButton.invalidate();
					if (friendsButtonClicked)
						loadStat(friendsScoresArray);
					else
						loadStat(scoresArray);
				}
			}
		});
	}

	private void loadStat(JSONArray jsonArray) {
		firstPointsTextView.setText("");
		firstNameTextView.setText("");
		secondNameTextView.setText("");
		secondPointsTextView.setText("");
		thirdNameTextView.setText("");
		thirdPointsTextView.setText("");
		fourthNameTextView.setText("");
		fourthPointsTextView.setText("");
		fifthNameTextView.setText("");
		fifthPointsTextView.setText("");
		if (jsonArray != null) {
			if (jsonArray.length() > 0) {
				try {
					JSONObject jsonObject = jsonArray.getJSONObject(0);
					firstNameTextView.setText("1. "
							+ jsonObject.getString("name"));
					firstPointsTextView.setText(jsonObject.getString("score")
							+ " POINTS");
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			if (jsonArray.length() > 1) {
				try {
					JSONObject jsonObject = jsonArray.getJSONObject(1);
					secondNameTextView.setText("2. "
							+ jsonObject.getString("name"));
					secondPointsTextView.setText(jsonObject.getString("score")
							+ " POINTS");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			if (jsonArray.length() > 2) {
				try {
					JSONObject jsonObject = jsonArray.getJSONObject(2);
					thirdNameTextView.setText("3. "
							+ jsonObject.getString("name"));
					thirdPointsTextView.setText(jsonObject.getString("score")
							+ " POINTS");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			if (jsonArray.length() > 3) {
				try {
					JSONObject jsonObject = jsonArray.getJSONObject(3);
					fourthNameTextView.setText("4. "
							+ jsonObject.getString("name"));
					fourthPointsTextView.setText(jsonObject.getString("score")
							+ " POINTS");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			if (jsonArray.length() > 4) {
				try {
					JSONObject jsonObject = jsonArray.getJSONObject(4);
					fifthNameTextView.setText("5. "
							+ jsonObject.getString("name"));
					fifthPointsTextView.setText(jsonObject.getString("score")
							+ " POINTS");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		firstPointsTextView.invalidate();
		firstNameTextView.invalidate();
		secondNameTextView.invalidate();
		secondPointsTextView.invalidate();
		thirdNameTextView.invalidate();
		thirdPointsTextView.invalidate();
		fourthNameTextView.invalidate();
		fourthPointsTextView.invalidate();
		fifthNameTextView.invalidate();
		fifthPointsTextView.invalidate();
	}

	class LoadStatisticsTask extends AsyncTask<String, Void, Boolean> {

		private ProgressDialog pdialog = null;
		private String response;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pdialog = new ProgressDialog(StatisticsActivity.this);
			pdialog.setTitle("calculating");
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
			try {
				JSONObject jsonObject = new JSONObject(response);
				scoresArray = jsonObject.getJSONArray("scores");
				lastGameScores = jsonObject.getJSONArray("last_game_scores");
				friendsScoresArray = jsonObject.getJSONArray("scores_friends");
				lastGameFriendsScores = jsonObject
						.getJSONArray("last_game_scores_friends");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			loadStat(lastGameFriendsScores);
		}

		private void showAlertDialog(String title, String message) {
			new AlertDialog.Builder(StatisticsActivity.this)
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
			HttpPost httpGet = new HttpPost(SERVER_URL + "/getStatistics");
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
	
	@Override
	public void onBackPressed() {
		Intent ActivityIntent = new Intent(StatisticsActivity.this,
				FrontPageActivity.class);
		startActivity(ActivityIntent);
		finish();
//		MainScreen.this.finish();
		super.onDestroy();
	}

}
