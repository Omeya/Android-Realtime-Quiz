package app.droid.quiz;

import static app.droid.quiz.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static app.droid.quiz.CommonUtilities.EXTRA_MESSAGE;
import static app.droid.quiz.CommonUtilities.SENDER_ID;
import static app.droid.quiz.CommonUtilities.SERVER_URL;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gcm.GCMRegistrar;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import app.droid.quiz.FrontPageActivity.getAllQuestionstask;

import com.revmob.RevMob;
import com.revmob.ads.banner.RevMobBanner;
import android.view.View;

public class WinnerActivity extends Activity {

	private String winnerUser;
	public static int score;
	public static String answers;
	private boolean syncCount;
	private boolean waitWinner;
	private boolean wait;
	private Handler toastHandler = new Handler();
	private AsyncTask<Void, Void, Void> mRegisterTask;
	private boolean soundsOn = true;
	private MediaPlayer mp;
	public static boolean firstTime;
	private String Winner;
	private RevMob revmob;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.winner);
		
		revmob = RevMob.start(this);
		revmob.showFullscreen(this);
		//RevMob revmob = RevMob.start(this, "522c5d8ae698f00f77000030");
        RevMobBanner banner1 = revmob.createBanner(this);
        ViewGroup view = (ViewGroup) findViewById(R.id.banner1);
        view.addView(banner1);
        RevMobBanner banner2 = revmob.createBanner(this);
        ViewGroup view1 = (ViewGroup) findViewById(R.id.banner2);
        view1.addView(banner2);
		// LinearLayout syncLinearLayout = (LinearLayout)
		// findViewById(R.id.syncLinearLayout);
		// LinearLayout winnLinearLayout = (LinearLayout)
		// findViewById(R.id.winnerLinearLayout);
		// TextView winnerTextView = (TextView)
		// findViewById(R.id.winnerTextView);
		// TextView scoreTextView = (TextView) findViewById(R.id.scoreTextView);
		// syncLinearLayout.setVisibility(View.GONE);
		// winnLinearLayout.setVisibility(View.VISIBLE);
		// winnerTextView.setText(winnerUser);
		// scoreTextView.setText("YOUR SCORE " + score + " POINTS");

		if (firstTime) {
			new sendScoretask().execute("");
		}
		new getTimeTask().execute("");
	
		mp = playSound(R.raw.waitin_tym_synchronising_winnerwait);
		mp.setLooping(true);
		toastHandler3.post(toast3);
		
		Button startLink = (Button) findViewById(R.id.conti);
		startLink.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent option = new Intent(getApplicationContext(),FrontPageActivity.class);
				startActivity(option);
				//finish();

			}
			
		});	
		
		Button startLink1 = (Button) findViewById(R.id.continue_btn);
		startLink1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent option = new Intent(getApplicationContext(),FrontPageActivity.class);
				startActivity(option);
				//finish();

			}
			
		});	
		
	}

	class sendScoretask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			firstTime = false;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			sendScore();
			return true;
		}

	}

	class getTimeTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
		}

		@Override
		protected Boolean doInBackground(String... params) {
			timeSyncToStart = getTime();
			return true;
		}

	}
	
	class getWinnerTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
		}

		@Override
		protected Boolean doInBackground(String... params) {
			Winner = getWinner();
			return true;
		}

	}

	private MediaPlayer playSound(int sound) {
		final MediaPlayer mp = MediaPlayer.create(getApplicationContext(), sound);
		if (soundsOn) {
			mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			    @Override
			    public void onPrepared(MediaPlayer arg0) {
			        mp.start();

			    }
			});
		}
		return mp;
	}

	private Handler toastHandler3 = new Handler();
	private TextView timeTextView;
	private int timeSyncToStart;

	private Handler toastHandler4 = new Handler();
	// handler to show the toast
	private Runnable toast4 = new Runnable() {

		public void run() {
			timeTextView.setText(timeSyncToStart + "");
			timeTextView.invalidate();
		}
	};
	
	private Handler toastHandler5 = new Handler();
	// handler to show the toast
	private Runnable toast5 = new Runnable() {

		public void run() {
			waitWinner = false;
			syncCount = false;
			LinearLayout syncLinearLayout = (LinearLayout) findViewById(R.id.syncLinearLayout);
			LinearLayout winnLinearLayout = (LinearLayout) findViewById(R.id.winnerLinearLayout);
			TextView winnerTextView = (TextView) findViewById(R.id.winnerTextView);
			TextView scoreTextView = (TextView) findViewById(R.id.scoreTextView);
			syncLinearLayout.setVisibility(View.GONE);
			winnLinearLayout.setVisibility(View.VISIBLE);
			winnerTextView.setText(winn);
			scoreTextView.setText("YOUR SCORE " + score + " POINTS");
			mp.stop();
			playSound(R.raw.winner_display_page);
			Button reviewAnswersButton = (Button) findViewById(R.id.reviewAnswersButton);
			reviewAnswersButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					LinearLayout reviewAnswersLinearLayout = (LinearLayout) findViewById(R.id.reviewAnswersLinearLayout);
					LinearLayout winnLinearLayout = (LinearLayout) findViewById(R.id.winnerLinearLayout);
					reviewAnswersLinearLayout.setVisibility(View.VISIBLE);
					winnLinearLayout.setVisibility(View.GONE);
					TextView answersTextView = (TextView) findViewById(R.id.answersTextView);
					answersTextView.setText(answers);
				}
			});
			syncCount = false;
		}
	};
	private String winn;
	private class StartThread2 extends Thread {


		public void run() {
			syncCount = true;
			while (syncCount) {
				toastHandler4.post(toast4);

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				timeSyncToStart--;
				if (timeSyncToStart == 15) {
					registerReceiver(mHandleMessageReceiver, new IntentFilter(
							DISPLAY_MESSAGE_ACTION));
					final String regId = GCMRegistrar
							.getRegistrationId(getApplicationContext());
					if (regId.equals("")) {
						// Automatically registers application on startup.
						GCMRegistrar.register(getApplicationContext(),
								SENDER_ID);
						strtoast = "called register inside if!";
						toastHandler.post(toast);
						System.out.println(strtoast);
					} else {
						// Device is already registered on GCM, check server.
						if (GCMRegistrar
								.isRegisteredOnServer(getApplicationContext())) {
							strtoast = "is registered!";
							toastHandler.post(toast);
							System.out.println(strtoast);
							// Skips registration.
							// mDisplay.append(getString(R.string.already_registered)
							// +
							// "\n");
						} else {
							// Try to register again, but not in the UI thread.
							// It's also necessary to cancel the thread
							// onDestroy(),
							// hence the use of AsyncTask instead of a raw
							// thread.
							final Context context = getApplicationContext();
							mRegisterTask = new AsyncTask<Void, Void, Void>() {

								@Override
								protected Void doInBackground(Void... params) {
									strtoast = "register backgrounddd!";
									toastHandler.post(toast);
									System.out.println(strtoast);
									ServerUtilities.unregister(context, regId);
									boolean registered = ServerUtilities
											.register(context, regId);
									if (!registered) {
										strtoast = "hoppa!";
										toastHandler.post(toast);
										System.out.println(strtoast);
										GCMRegistrar.unregister(context);
									}
									return null;
								}

								@Override
								protected void onPostExecute(Void result) {
									mRegisterTask = null;
								}

							};
							mRegisterTask.execute(null, null, null);
						}
					}
				}
				if (timeSyncToStart == -1){
					winn = getWinner();
					toastHandler5.post(toast5);
				}
			}
		}
	}

	private Runnable toast3 = new Runnable() {

		public void run() {
			// questionTen.setImageResource(R.drawable.question_answered);
			// questionTen.invalidate();
			// LinearLayout syncLinearLayout = (LinearLayout)
			// findViewById(R.id.syncLinearLayout);
			// LinearLayout linearLayoutFirstScreen = (LinearLayout)
			// findViewById(R.id.linearLayout1);
			timeTextView = (TextView) findViewById(R.id.timeTextView);
			// syncLinearLayout.setVisibility(View.VISIBLE);
			// linearLayoutFirstScreen.setVisibility(View.GONE);
			// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			timeSyncToStart = 30;
			StartThread2 startThread2 = new StartThread2();
			startThread2.start();
			// waitWinner = true;
			// new LoadWinnerTask().execute("");

		}
	};

	public int getTime() {
		System.out.println("get timees");
		HttpParams postParams = new BasicHttpParams();
		HttpConnectionParams.setSoTimeout(postParams, 3000);
		HttpConnectionParams.setConnectionTimeout(postParams, 3000);
		postParams.setParameter("your_name", "John Smith");
		HttpClient client = new DefaultHttpClient(postParams);
		HttpPost httpGet = new HttpPost(SERVER_URL
				+ "/RemainingTimeWinnerServlet");
		// HttpResponse httpResponse;
		try {
			HttpResponse httpResponse = client.execute(httpGet);
			HttpEntity entity = httpResponse.getEntity();
			String response = EntityUtils.toString(entity);
			System.out.println(response);
			JSONObject jObject = new JSONObject(response);
			return jObject.getInt("remainingTime");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 30;
	}
	
	public String getWinner() {
		System.out.println("get timees");
		HttpParams postParams = new BasicHttpParams();
		HttpConnectionParams.setSoTimeout(postParams, 3000);
		HttpConnectionParams.setConnectionTimeout(postParams, 3000);
		postParams.setParameter("your_name", "John Smith");
		HttpClient client = new DefaultHttpClient(postParams);
		HttpPost httpGet = new HttpPost(SERVER_URL
				+ "/getWinner");
		// HttpResponse httpResponse;
		try {
			HttpResponse httpResponse = client.execute(httpGet);
			HttpEntity entity = httpResponse.getEntity();
			String response = EntityUtils.toString(entity);
			System.out.println(response);
			return response;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Non Winner";
	}

	private void sendScore() {
		HttpParams postParams = new BasicHttpParams();
		HttpConnectionParams.setSoTimeout(postParams, 3000);
		HttpConnectionParams.setConnectionTimeout(postParams, 3000);
		HttpClient client = new DefaultHttpClient(postParams);
		HttpPost httpGet = new HttpPost(SERVER_URL + "/GetScore");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
		if (Fields.userName == null)
			Fields.userName = "-no name-";
		nameValuePairs.add(new BasicNameValuePair("name", Fields.email));
		nameValuePairs.add(new BasicNameValuePair("score", score + ""));
		nameValuePairs.add(new BasicNameValuePair("id", Fields.id));
		System.out.println("IDDD  " + Fields.id);
		try {
			httpGet.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			client.execute(httpGet);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private CharSequence strtoast;
	// handler to show the toast
	private Runnable toast = new Runnable() {

		public void run() {
			// Toast.makeText(getApplicationContext(), strtoast,
			// Toast.LENGTH_SHORT).show();
		}
	};

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			if (newMessage.equalsIgnoreCase(CommonUtilities.getMessage))
				return;
			else
				CommonUtilities.getMessage = newMessage;
			System.out.println("THE MESAGEE:  " + newMessage);
			if (newMessage.equals("Are you here?")) {
				strtoast = "Are you here!";
				toastHandler.post(toast);
				System.out
						.println("get are you here from winner Activity!!!!!!!!!!!");
				IAmHere();
			} else if (newMessage.startsWith("winner")) {
				winnerUser = newMessage.substring(6);
//				waitWinner = false;
//				syncCount = false;
//				LinearLayout syncLinearLayout = (LinearLayout) findViewById(R.id.syncLinearLayout);
//				LinearLayout winnLinearLayout = (LinearLayout) findViewById(R.id.winnerLinearLayout);
//				TextView winnerTextView = (TextView) findViewById(R.id.winnerTextView);
//				TextView scoreTextView = (TextView) findViewById(R.id.scoreTextView);
//				syncLinearLayout.setVisibility(View.GONE);
//				winnLinearLayout.setVisibility(View.VISIBLE);
//				winnerTextView.setText(winnerUser);
//				scoreTextView.setText("YOUR SCORE " + score + " POINTS");
//				mp.stop();
//				playSound(R.raw.winner_display_page);
//				Button reviewAnswersButton = (Button) findViewById(R.id.reviewAnswersButton);
//				reviewAnswersButton.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						LinearLayout reviewAnswersLinearLayout = (LinearLayout) findViewById(R.id.reviewAnswersLinearLayout);
//						LinearLayout winnLinearLayout = (LinearLayout) findViewById(R.id.winnerLinearLayout);
//						reviewAnswersLinearLayout.setVisibility(View.VISIBLE);
//						winnLinearLayout.setVisibility(View.GONE);
//						TextView answersTextView = (TextView) findViewById(R.id.answersTextView);
//						answersTextView.setText(answers);
//					}
//				});
			} else {
				// try {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(newMessage);

					strtoast = "ZZZZZZZZZZ!";
					toastHandler.post(toast);
					if (jsonObject.getInt("index") == 1) {
						MainScreen.messagesArray = questionsArray;
						MainScreen.runForFirstTime = true;
						// score = 0;
						wait = false;
						System.out.println("elmafrod 5alas");
						System.out.println("5alasna wait");
						syncCount = false;
						Intent option = new Intent(getApplicationContext(),
								MainScreen.class);
						startActivity(option);
						finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				System.out.println("!!!!!!!!!!!!!");
				// timeLeftTextView.setText("Time Left        0:"
				// + currentTime);
				// mDisplay.append(newMessage + "\n");
			}
		}
	};
	private JSONArray questionsArray;

	class getAllQuestionstask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
		}

		@Override
		protected Boolean doInBackground(String... params) {
			getAllQuestions();
			return true;
		}

	}

	private void getAllQuestions() {
		System.out.println("ana hennnaa");
		HttpParams postParams = new BasicHttpParams();
		HttpConnectionParams.setSoTimeout(postParams, 3000);
		HttpConnectionParams.setConnectionTimeout(postParams, 3000);
		postParams.setParameter("your_name", "John Smith");
		HttpClient client = new DefaultHttpClient(postParams);
		HttpPost httpGet = new HttpPost(SERVER_URL + "/GetAllQuestions");
		// HttpResponse httpResponse;
		try {
			HttpResponse httpResponse = client.execute(httpGet);
			HttpEntity entity = httpResponse.getEntity();
			String response = EntityUtils.toString(entity);
			System.out.println(response);
			questionsArray = new JSONArray(response);

			// return Boolean.parseBoolean(jObject.getString("msg"));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public boolean IAmHere() {
		System.out.println("HEREEEEEEEEEEEEE");
		HttpParams postParams = new BasicHttpParams();
		HttpConnectionParams.setSoTimeout(postParams, 3000);
		HttpConnectionParams.setConnectionTimeout(postParams, 3000);
		postParams.setParameter("your_name", "John Smith");
		HttpClient client = new DefaultHttpClient(postParams);
		HttpPost httpGet = new HttpPost(SERVER_URL + "/IAmHereServlet");
		// HttpResponse httpResponse;
		try {
			client.execute(httpGet);
			// HttpEntity entity = httpResponse.getEntity();
			// String response = EntityUtils.toString(entity);
			// System.out.println(response);
			// JSONObject jObject = new JSONObject(response);
			// return Boolean.parseBoolean(jObject.getString("msg"));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void onBackPressed() {
		Intent ActivityIntent = new Intent(WinnerActivity.this,
				FrontPageActivity.class);
		startActivity(ActivityIntent);
		finish();
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		try {
			unregisterReceiver(mHandleMessageReceiver);
			Intent unregIntent = new Intent(
					"com.google.android.c2dm.intent.UNREGISTER");
			unregIntent.putExtra("app",
					PendingIntent.getBroadcast(this, 0, new Intent(), 0));
			startService(unregIntent);
			GCMRegistrar.unregister(getApplicationContext());
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
		}
		// MainScreen.this.finish();
		super.onDestroy();
	}

}