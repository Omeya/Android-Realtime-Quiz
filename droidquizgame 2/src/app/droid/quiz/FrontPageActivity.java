package app.droid.quiz;

import static app.droid.quiz.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static app.droid.quiz.CommonUtilities.EXTRA_MESSAGE;
import static app.droid.quiz.CommonUtilities.SENDER_ID;
import static app.droid.quiz.CommonUtilities.SERVER_URL;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.Facebook;
import com.google.android.gcm.GCMRegistrar;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.util.Log;

import android.view.View;

import com.revmob.RevMob;
import com.revmob.ads.banner.RevMobBanner;


public class FrontPageActivity extends Activity {

	private Button startGameButton;
	private Handler toastHandler = new Handler();
	private boolean wait;
	public static final String mAPP_ID = "164121927106630";
	public Facebook mFacebook = new Facebook(mAPP_ID);
	private AsyncTask<Void, Void, Void> mRegisterTask;
	private boolean soundsOn = true;
	private MediaPlayer mp;
	private RevMob revmob;
	
	public void onCreate(Bundle savedInstanceState) {
		// Configure Chartboost
		
        
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		checkNotNull(SERVER_URL, "SERVER_URL");
		checkNotNull(SENDER_ID, "SENDER_ID");
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);
		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);
		strtoast = "Checked!";
		toastHandler.post(toast);
		setContentView(R.layout.front_screen);
		
		revmob = RevMob.start(this);
		
		revmob.showFullscreen(this);
        
		RevMobBanner banner = revmob.createBanner(this);
        ViewGroup view = (ViewGroup) findViewById(R.id.banner);
        view.addView(banner);
        
        RevMobBanner banner1 = revmob.createBanner(this);
        ViewGroup view1 = (ViewGroup) findViewById(R.id.banner1);
        view1.addView(banner1);
        
        RevMobBanner banner2 = revmob.createBanner(this);
        ViewGroup view2 = (ViewGroup) findViewById(R.id.banner2);
        view2.addView(banner2);
        
        
		
		startGameButton = (Button) findViewById(R.id.startGameButton);

		startGameButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LinearLayout syncLinearLayout = (LinearLayout) findViewById(R.id.syncLinearLayout);
				LinearLayout linearLayoutFirstScreen = (LinearLayout) findViewById(R.id.linearLayoutFirstScreen);
				syncLinearLayout.setVisibility(View.VISIBLE);
				linearLayoutFirstScreen.setVisibility(View.GONE);
				timeTextView = (TextView) findViewById(R.id.timeTextView);
				new SyncTask().execute("");
			}

		});

		// Button optionsButton = (Button) findViewById(R.id.optionsButton);
		// optionsButton.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Intent option = new Intent(getApplicationContext(),
		// LogInOptionsActivity.class);
		// startActivity(option);
		// finish();
		//
		// }
		// });

		Button statisticsButton = (Button) findViewById(R.id.leaderboardButton);
		statisticsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent option = new Intent(getApplicationContext(),
						StatisticsActivity.class);
				startActivity(option);
				finish();

			}
		});

		Button profileButton = (Button) findViewById(R.id.profileButton);
		profileButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent option = new Intent(getApplicationContext(),
						ProfileActivity.class);
				startActivity(option);
				finish();

			}
		});
		
		Button helpButton = (Button) findViewById(R.id.helpButton);
		helpButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent option = new Intent(getApplicationContext(),
						HelpActivity.class);
				startActivity(option);
				finish();

			}
		});
		
		Button tncLink = (Button) findViewById(R.id.tncLink);
		tncLink.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent option = new Intent(getApplicationContext(),
						TermsAndConditions.class);
				//option.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
	
	
	private void startGameClick() {
		wait = true;
		// new LoadPlayTask().execute("");
//		LinearLayout syncLinearLayout = (LinearLayout) findViewById(R.id.syncLinearLayout);
//		LinearLayout linearLayoutFirstScreen = (LinearLayout) findViewById(R.id.linearLayoutFirstScreen);
//		syncLinearLayout.setVisibility(View.VISIBLE);
//		linearLayoutFirstScreen.setVisibility(View.GONE);
//		timeTextView = (TextView) findViewById(R.id.timeTextView);
		timeSyncToStart = getTime();
		new getAllQuestionstask().execute("");
		mp = playSound(R.raw.waitin_tym_synchronising_winnerwait);
		mp.setLooping(true);
		StartThread startThread = new StartThread();
		startThread.start();
		System.out.println("d5alll leeeeeeeeeeeeeeeeeeeeeh");
		if (timeSyncToStart < 50) {
			registerReceiver(mHandleMessageReceiver, new IntentFilter(
					DISPLAY_MESSAGE_ACTION));
			System.out.println("d5alll leeeeeeeeeeeeeeeeeeeeeh2");
			final String regId = GCMRegistrar
					.getRegistrationId(getApplicationContext());
			if (regId.equals("")) {
				// Automatically registers application on startup.
				GCMRegistrar.register(getApplicationContext(),
						SENDER_ID);
				strtoast = "called register inside if!";
				toastHandler.post(toast);
			} else {
				// Device is already registered on GCM, check server.
				if (GCMRegistrar
						.isRegisteredOnServer(getApplicationContext())) {
					strtoast = "is registered!";
					toastHandler.post(toast);
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
							ServerUtilities.unregister(context, regId);
							boolean registered = ServerUtilities
									.register(context, regId);
							if (!registered) {
								strtoast = "hoppa!";
								toastHandler.post(toast);
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
	}
	
	class IamHeretask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
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
			IAmHere();
			return true;
		}

	}
	
	class SyncTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
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
			startGameClick();
			return true;
		}

	}
	
	private MediaPlayer playSound(int sound) {
		MediaPlayer mp = MediaPlayer.create(getApplicationContext(), sound);
		if (soundsOn) {
			try {
				mp.prepare();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mp.start();
		}
		return mp;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem item = menu.add("Log Out");
		item.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			public boolean onMenuItemClick(MenuItem item) {
				try {
					mFacebook.logout(FrontPageActivity.this);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				InsureUtility.saveuserlogincredentials(getApplicationContext(),
						"", "", "N");
//				Intent intent = new Intent(getApplicationContext(),
//						LoginActivity.class);
//				startActivity(intent);
				finish();
//				android.os.Process.killProcess(android.os.Process.myPid());
				return false;
			}
		});
		return super.onCreateOptionsMenu(menu);
	}

	private TextView timeTextView;
	public static int timeSyncToStart;
	boolean syncCount = true;
	private Handler toastHandler2 = new Handler();
	// handler to show the toast
	private Runnable toast2 = new Runnable() {

		public void run() {
			// TODO
			// syncCount = true;
			// while (syncCount) {
			timeTextView.setText(timeSyncToStart + "");
			timeTextView.invalidate();
			// try {
			// Thread.sleep(1000);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			// timeSyncToStart--;
			// if (timeSyncToStart == -1)
			// syncCount = false;
			// }
			// if (currentTime == -1)
			// timeLeftTextView.setText("Time Left        0:0");
			// else
			// timeLeftTextView.setText("Time Left        0:" + currentTime);
			// System.out.println(currentTime + "");
			// timeLeftTextView.postInvalidate();
		}
	};
	
	
	private Handler toastHandler3 = new Handler();
	// handler to show the toast
	private Runnable toast3 = new Runnable() {

		public void run() {
//			startGameClick();
		}
	};

	private class StartThread extends Thread {
		public void run() {
			syncCount = true;
			while (syncCount) {
				if (timeSyncToStart == 50) {
					registerReceiver(mHandleMessageReceiver, new IntentFilter(
							DISPLAY_MESSAGE_ACTION));
					System.out.println("d5alll leeeeeeeeeeeeeeeeeeeeeh2");
					final String regId = GCMRegistrar
							.getRegistrationId(getApplicationContext());
					if (regId.equals("")) {
						// Automatically registers application on startup.
						GCMRegistrar.register(getApplicationContext(),
								SENDER_ID);
						strtoast = "called register inside if!";
						toastHandler.post(toast);
					} else {
						// Device is already registered on GCM, check server.
						if (GCMRegistrar
								.isRegisteredOnServer(getApplicationContext())) {
							strtoast = "is registered!";
							toastHandler.post(toast);
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
									ServerUtilities.unregister(context, regId);
									boolean registered = ServerUtilities
											.register(context, regId);
									// At this point all attempts to register
									// with
									// the app
									// server failed, so we need to unregister
									// the
									// device
									// from GCM - the app will try to register
									// again
									// when
									// it is restarted. Note that GCM will send
									// an
									// unregistered callback upon completion,
									// but
									// GCMIntentService.onUnregistered() will
									// ignore
									// it.
									if (!registered) {
										strtoast = "hoppa!";
										toastHandler.post(toast);
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
				toastHandler2.post(toast2);

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				timeSyncToStart--;
				if (timeSyncToStart == -1){
					syncCount = false;
					
					MainScreen.messagesArray = questionsArray;
					MainScreen.runForFirstTime = true;
					// score = 0;
					wait = false;
					System.out.println("elmafrod 5alas");
					System.out.println("5alasna wait");
					syncCount = false;
					mp.stop();
					Intent option = new Intent(getApplicationContext(),
							MainScreen.class);
					startActivity(option);
					finish();
				}
			}
		}
	}

	// class LoadPlayTask extends AsyncTask<String, Void, Boolean> {
	//
	// // private ProgressDialog pdialog = null;
	//
	// @Override
	// protected void onPreExecute() {
	// super.onPreExecute();
	// LinearLayout syncLinearLayout = (LinearLayout)
	// findViewById(R.id.syncLinearLayout);
	// LinearLayout linearLayoutFirstScreen = (LinearLayout)
	// findViewById(R.id.linearLayoutFirstScreen);
	// syncLinearLayout.setVisibility(View.VISIBLE);
	// linearLayoutFirstScreen.setVisibility(View.GONE);
	// timeTextView = (TextView) findViewById(R.id.timeTextView);
	// timeSyncToStart = getTime();
	// StartThread startThread = new StartThread();
	// startThread.start();
	// // pdialog = new ProgressDialog(FrontPageActivity.this);
	// // pdialog.setTitle("Waiting to Sync");
	// // pdialog.setMessage("Please Wait...");
	// // pdialog.show();
	// }
	//
	// @Override
	// protected void onCancelled() {
	// super.onCancelled();
	// // pdialog.dismiss();
	// // pdialog = null;
	// }
	//
	// @Override
	// protected void onPostExecute(Boolean result) {
	// super.onPostExecute(result);
	// // pdialog.dismiss();
	// // pdialog = null;
	// // playButton.setVisibility(View.GONE);
	// // linearLayout1.setVisibility(View.VISIBLE);
	// System.out.println("elmafrod 5alas");
	// System.out.println("5alasna wait");
	// syncCount = false;
	//
	// Intent unregIntent = new Intent(
	// "com.google.android.c2dm.intent.UNREGISTER");
	// unregIntent.putExtra("app", PendingIntent.getBroadcast(
	// getApplicationContext(), 0, new Intent(), 0));
	// startService(unregIntent);
	// GCMRegistrar.unregister(getApplicationContext());
	// unregisterReceiver(mHandleMessageReceiver);
	// GCMRegistrar.onDestroy(getApplicationContext());
	// Intent option = new Intent(getApplicationContext(),
	// MainScreen.class);
	// startActivity(option);
	// finish();
	//
	// }
	//
	// // private void showAlertDialog(String title, String message) {
	// // new AlertDialog.Builder(LoginActivity.this)
	// // .setTitle(title)
	// // .setMessage(message)
	// // .setNeutralButton("Ok",
	// // new DialogInterface.OnClickListener() {
	// //
	// // public void onClick(DialogInterface dialog,
	// // int which) {
	// // // TODO Auto-generated method stub
	// // if (loginResult) {
	// //
	// // }
	// // }
	// // }).show();
	//
	// // }
	//
	// @Override
	// protected Boolean doInBackground(String... params) {
	//
	// while (wait)
	// ;
	// return true;
	// }
	//
	// }

	private CharSequence strtoast;
	// handler to show the toast
	private Runnable toast = new Runnable() {

		public void run() {
			// Toast.makeText(getApplicationContext(), strtoast,
			// Toast.LENGTH_SHORT).show();
		}
	};
	private JSONArray questionsArray;

	private boolean IAmHere() {
		System.out.println("ana hennnaa");
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

	public int getTime() {
		System.out.println("get timees");
		HttpParams postParams = new BasicHttpParams();
		HttpConnectionParams.setSoTimeout(postParams, 3000);
		HttpConnectionParams.setConnectionTimeout(postParams, 3000);
		postParams.setParameter("your_name", "John Smith");
		HttpClient client = new DefaultHttpClient(postParams);
		HttpPost httpGet = new HttpPost(SERVER_URL + "/RemainingTimeServlet");
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
		return 180;
	}

	class getAllQuestionstask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
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
	
	private void getAllQuestions(){
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			if (newMessage.equalsIgnoreCase(CommonUtilities.getMessage))
				return;
			else
				CommonUtilities.getMessage = newMessage;
			System.out.println("THE MESAGEE from front zefttaaa:  "
					+ newMessage);
			if (newMessage.equals("Are you here?")) {
				strtoast = "Are you here!";
				toastHandler.post(toast);
				System.out
						.println("get are you here frm front page Activity!!!!!!!!!!!");
//				IAmHere();
				new IamHeretask().execute("");
				
//				new getAllQuestionstask().execute("");
				
			} else if (newMessage.startsWith("winner")) {
				// winnerUser = newMessage.substring(6);
				// waitWinner = false;
			} else {
				try {
					JSONObject jsonObject = new JSONObject(newMessage);
					strtoast = "ZZZZZZZZZZ!";
					toastHandler.post(toast);
					if (jsonObject.getInt("index") == 1) {
						System.out.println("5aliha false");
						// unregisterReceiver(mHandleMessageReceiver);
						// Intent unregIntent = new Intent(
						// "com.google.android.c2dm.intent.UNREGISTER");
						// unregIntent.putExtra("app",
						// PendingIntent.getBroadcast(
						// getApplicationContext(), 0, new Intent(), 0));
						// startService(unregIntent);
						// GCMRegistrar.unregister(getApplicationContext());
						// GCMRegistrar.onDestroy(getApplicationContext());
//						MainScreen.newMessage = newMessage;
//						MainScreen.runForFirstTime = true;
//						// score = 0;
//						wait = false;
//						System.out.println("elmafrod 5alas");
//						System.out.println("5alasna wait");
//						syncCount = false;
//						mp.stop();
//						Intent option = new Intent(getApplicationContext(),
//								MainScreen.class);
//						startActivity(option);
//						finish();
						// questionOne
						// .setBackgroundResource(R.drawable.question_not_answered);
						// questionTwo
						// .setBackgroundResource(R.drawable.question_not_answered);
						// questionThree
						// .setBackgroundResource(R.drawable.question_not_answered);
						// questionFour
						// .setBackgroundResource(R.drawable.question_not_answered);
						// questionFive
						// .setBackgroundResource(R.drawable.question_not_answered);
						// questionSix
						// .setBackgroundResource(R.drawable.question_not_answered);
						// questionSeven
						// .setBackgroundResource(R.drawable.question_not_answered);
						// questionEight
						// .setBackgroundResource(R.drawable.question_not_answered);
						// questionNine
						// .setBackgroundResource(R.drawable.question_not_answered);
						// questionTen
						// .setBackgroundResource(R.drawable.question_not_answered);
						// questionOne.invalidate();
						// questionTwo.invalidate();
						// questionThree.invalidate();
						// questionFour.invalidate();
						// questionFive.invalidate();
						// questionSix.invalidate();
						// questionSeven.invalidate();
						// questionEight.invalidate();
						// questionNine.invalidate();
						// questionTen.invalidate();
					}
					// try {
					// if (answered == Integer.parseInt(rightAnswer)) {
					// score++;
					// System.out.println("sccc   " + score);
					// }
					// } catch (Exception e) {
					// }
					// System.out.println("!!!!!!!!!!!!!");
					// canAnswer = true;
					// answered = -1;
					// currentTime = 10;
					// destroy = false;
					// running = true;
					// // TODO
					// // timeLeftTextView.setText("Time Left        0:"
					// // + currentTime);
					// StartThread startThread = new StartThread();
					// startThread.start();
					// index = jsonObject.getInt("index");
					// // remainingQuestionsTextView.setText("Q: " + index +
					// // "/10");
					// switch (index) {
					// case 2:
					// questionOne
					// .setBackgroundResource(R.drawable.question_answered);
					// questionOne.invalidate();
					// break;
					// case 3:
					// questionTwo
					// .setBackgroundResource(R.drawable.question_answered);
					// questionTwo.invalidate();
					// break;
					// case 4:
					// questionThree
					// .setBackgroundResource(R.drawable.question_answered);
					// questionThree.invalidate();
					// break;
					// case 5:
					// questionFour
					// .setBackgroundResource(R.drawable.question_answered);
					// questionFour.invalidate();
					// break;
					// case 6:
					// questionFive
					// .setBackgroundResource(R.drawable.question_answered);
					// questionFive.invalidate();
					// break;
					// case 7:
					// questionSix
					// .setBackgroundResource(R.drawable.question_answered);
					// questionSix.invalidate();
					// break;
					// case 8:
					// questionSeven
					// .setBackgroundResource(R.drawable.question_answered);
					// questionSeven.invalidate();
					// break;
					// case 9:
					// questionEight
					// .setBackgroundResource(R.drawable.question_answered);
					// questionEight.invalidate();
					// break;
					// case 10:
					// questionNine
					// .setBackgroundResource(R.drawable.question_answered);
					// questionNine.invalidate();
					// break;
					//
					// default:
					// break;
					// }
					// questionTextView.setText(jsonObject.getString("question"));
					// answerATextView.setText(jsonObject.getString("answer_A"));
					// answerATextView.setBackgroundResource(R.drawable.a_answer);
					// answerBTextView.setText(jsonObject.getString("answer_B"));
					// answerBTextView.setBackgroundResource(R.drawable.b_answer);
					// answerCTextView.setText(jsonObject.getString("answer_C"));
					// answerCTextView.setBackgroundResource(R.drawable.c_answer);
					// answerDTextView.setText(jsonObject.getString("answer_D"));
					// answerDTextView.setBackgroundResource(R.drawable.d_answer);
					// rightAnswer = jsonObject.getString("right_answer");
					// // remainingQuestionsTextView.invalidate();
					// questionTextView.invalidate();
					// answerATextView.invalidate();
					// answerBTextView.invalidate();
					// answerCTextView.invalidate();
					// answerDTextView.invalidate();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			// mDisplay.append(newMessage + "\n");
		}
	};

	private void checkNotNull(Object reference, String name) {
		if (reference == null) {
			throw new NullPointerException(getString(R.string.error_config,
					name));
		}
	}

	public void onBackPressed() {
		// running = false;
		// destroy = true;
		 // If an interstitial is on screen, close it. Otherwise continue as normal.
	   
		
		try {
			mp.stop();
		} catch (Exception e) {
		}
		try {
			System.out.println("7777777777777777");
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

		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		// running = false;
		// destroy = true;
		try {
			mp.stop();
		} catch (Exception e) {
		}
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

		super.onDestroy();
	}

}
