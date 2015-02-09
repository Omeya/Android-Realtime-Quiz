package app.droid.quiz;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gcm.GCMRegistrar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import static app.droid.quiz.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static app.droid.quiz.CommonUtilities.EXTRA_MESSAGE;
import static app.droid.quiz.CommonUtilities.SENDER_ID;
import static app.droid.quiz.CommonUtilities.SERVER_URL;

import com.revmob.ads.banner.RevMobBanner;
import android.view.View;

import com.revmob.RevMob;

public class MainScreen extends Activity {

	// public static String newMessage;
	public static JSONArray messagesArray;
	private AsyncTask<Void, Void, Void> mRegisterTask;
	private ImageView timeleftImageView;
	private LinearLayout linearLayout1;
	private TextView questionTextView, answerATextView, answerBTextView,
			answerCTextView, answerDTextView;
	private ImageView questionOne, questionTwo, questionThree, questionFour,
			questionFive, questionSix, questionSeven, questionEight,
			questionNine, questionTen;
	private Button playButton;
	private boolean wait;
	private Handler toastHandler = new Handler();
	private String rightAnswer = "";
	private boolean canAnswer = true;
	private int answered;

	private int score = 0;

	private boolean running = true;
	private int currentTime;
	private boolean destroy = false;
	private boolean waitWinner;
	private String winnerUser;
	private int index;

	private String answers = "";
	private boolean soundsOn = true;
	private MediaPlayer mp;
	private int currentQuestion;
	public static boolean runForFirstTime;
	private RevMob revmob;
	// TextView mDisplay;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// checkNotNull(SERVER_URL, "SERVER_URL");
		// checkNotNull(SENDER_ID, "SENDER_ID");
		// // Make sure the device has the proper dependencies.
		// GCMRegistrar.checkDevice(this);
		// // Make sure the manifest was properly set - comment out this line
		// // while developing the app, then uncomment it when it's ready.
		// GCMRegistrar.checkManifest(this);
		// strtoast = "Checked!";
		// toastHandler.post(toast);
		setContentView(R.layout.main_screen);
		linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
		timeleftImageView = (ImageView) findViewById(R.id.timeLeftImage);
		questionTextView = (TextView) findViewById(R.id.questionTextView);
		answerATextView = (TextView) findViewById(R.id.answer_A);
		answerBTextView = (TextView) findViewById(R.id.answer_b);
		answerCTextView = (TextView) findViewById(R.id.answer_C);
		answerDTextView = (TextView) findViewById(R.id.answer_D);
		questionOne = (ImageView) findViewById(R.id.questions1);
		questionTwo = (ImageView) findViewById(R.id.questions2);
		questionThree = (ImageView) findViewById(R.id.questions3);
		questionFour = (ImageView) findViewById(R.id.questions4);
		questionFive = (ImageView) findViewById(R.id.questions5);
		questionSix = (ImageView) findViewById(R.id.questions6);
		questionSeven = (ImageView) findViewById(R.id.questions7);
		questionEight = (ImageView) findViewById(R.id.questions8);
		questionNine = (ImageView) findViewById(R.id.questions9);
		questionTen = (ImageView) findViewById(R.id.questions10);
		
		/*
		revmob = RevMob.start(this);
		RevMob revmob = RevMob.start(this, "522c5d8ae698f00f77000030");
        RevMobBanner banner = revmob.createBanner(this);
        ViewGroup view = (ViewGroup) findViewById(R.id.banner);
        view.addView(banner);
        try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} */ 
        
		answerATextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (canAnswer) {
					sound = R.raw.option_answer_click_button;
					StartThread3 startThread3 = new StartThread3();
					startThread3.start();
					// playSound(R.raw.option_answer_click_button);
					answerATextView
							.setBackgroundResource(R.drawable.left_answer_checked);
					answerBTextView
							.setBackgroundResource(R.drawable.right_answer);
					answerCTextView
							.setBackgroundResource(R.drawable.left_answer);
					answerDTextView
							.setBackgroundResource(R.drawable.right_answer);
					answerATextView.invalidate();
					answerBTextView.invalidate();
					answerCTextView.invalidate();
					answerDTextView.invalidate();
					answered = 1;
				}

			}
		});
		answerBTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (canAnswer) {
					sound = R.raw.option_answer_click_button;
					StartThread3 startThread3 = new StartThread3();
					startThread3.start();
					// playSound(R.raw.option_answer_click_button);
					answerBTextView
							.setBackgroundResource(R.drawable.right_answer_checked);
					answerATextView
							.setBackgroundResource(R.drawable.left_answer);
					answerCTextView
							.setBackgroundResource(R.drawable.left_answer);
					answerDTextView
							.setBackgroundResource(R.drawable.right_answer);
					answerATextView.invalidate();
					answerBTextView.invalidate();
					answerCTextView.invalidate();
					answerDTextView.invalidate();
					answered = 2;
				}

			}
		});
		answerCTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (canAnswer) {
					sound = R.raw.option_answer_click_button;
					StartThread3 startThread3 = new StartThread3();
					startThread3.start();
					// playSound(R.raw.option_answer_click_button);
					answerCTextView
							.setBackgroundResource(R.drawable.left_answer_checked);
					answerBTextView
							.setBackgroundResource(R.drawable.right_answer);
					answerATextView
							.setBackgroundResource(R.drawable.left_answer);
					answerDTextView
							.setBackgroundResource(R.drawable.right_answer);
					answerATextView.invalidate();
					answerBTextView.invalidate();
					answerCTextView.invalidate();
					answerDTextView.invalidate();
					answered = 3;
				}

			}
		});
		answerDTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (canAnswer) {
					sound = R.raw.option_answer_click_button;
					StartThread3 startThread3 = new StartThread3();
					startThread3.start();
					// playSound(R.raw.option_answer_click_button);
					answerDTextView
							.setBackgroundResource(R.drawable.right_answer_checked);
					answerBTextView
							.setBackgroundResource(R.drawable.right_answer);
					answerCTextView
							.setBackgroundResource(R.drawable.left_answer);
					answerATextView
							.setBackgroundResource(R.drawable.left_answer);
					answerATextView.invalidate();
					answerBTextView.invalidate();
					answerCTextView.invalidate();
					answerDTextView.invalidate();
					answered = 4;
				}

			}
		});
		// mDisplay = (TextView) findViewById(R.id.display);
		playButton = (Button) findViewById(R.id.playButton);
		System.out.println("zzzzzzz");
		// registerReceiver(mHandleMessageReceiver, new IntentFilter(
		// DISPLAY_MESSAGE_ACTION));
		// final String regId = GCMRegistrar
		// .getRegistrationId(getApplicationContext());
		// if (regId.equals("")) {
		// // Automatically registers application on startup.
		// GCMRegistrar.register(getApplicationContext(), SENDER_ID);
		// strtoast = "called register inside if!";
		// toastHandler.post(toast);
		// System.out.println(strtoast);
		// } else {
		// // Device is already registered on GCM, check server.
		// if (GCMRegistrar.isRegisteredOnServer(getApplicationContext())) {
		// strtoast = "is registered!";
		// toastHandler.post(toast);
		// System.out.println(strtoast);
		// // Skips registration.
		// // mDisplay.append(getString(R.string.already_registered)
		// // +
		// // "\n");
		// } else {
		// // Try to register again, but not in the UI thread.
		// // It's also necessary to cancel the thread onDestroy(),
		// // hence the use of AsyncTask instead of a raw thread.
		// final Context context = getApplicationContext();
		// mRegisterTask = new AsyncTask<Void, Void, Void>() {
		//
		// @Override
		// protected Void doInBackground(Void... params) {
		// strtoast = "register backgrounddd!";
		// toastHandler.post(toast);
		// System.out.println(strtoast);
		// ServerUtilities.unregister(context, regId);
		// boolean registered = ServerUtilities.register(context,
		// regId);
		// // At this point all attempts to register with
		// // the app
		// // server failed, so we need to unregister the
		// // device
		// // from GCM - the app will try to register again
		// // when
		// // it is restarted. Note that GCM will send an
		// // unregistered callback upon completion, but
		// // GCMIntentService.onUnregistered() will ignore
		// // it.
		// if (!registered) {
		// strtoast = "hoppa!";
		// toastHandler.post(toast);
		// System.out.println(strtoast);
		// GCMRegistrar.unregister(context);
		// }
		// return null;
		// }
		//
		// @Override
		// protected void onPostExecute(Void result) {
		// mRegisterTask = null;
		// }
		//
		// };
		// mRegisterTask.execute(null, null, null);
		// }
		// }
		playButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// unregisterReceiver(mHandleMessageReceiver);
				// Intent unregIntent = new Intent(
				// "com.google.android.c2dm.intent.UNREGISTER");
				// unregIntent.putExtra("app",
				// PendingIntent.getBroadcast(this, 0, new Intent(), 0));
				// startService(unregIntent);
				wait = true;
				// new LoadPlayTask().execute("");
				registerReceiver(mHandleMessageReceiver, new IntentFilter(
						DISPLAY_MESSAGE_ACTION));

				final String regId = GCMRegistrar
						.getRegistrationId(getApplicationContext());
				if (regId.equals("")) {
					// Automatically registers application on startup.
					GCMRegistrar.register(getApplicationContext(), SENDER_ID);
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
						// It's also necessary to cancel the thread onDestroy(),
						// hence the use of AsyncTask instead of a raw thread.
						final Context context = getApplicationContext();
						mRegisterTask = new AsyncTask<Void, Void, Void>() {

							@Override
							protected Void doInBackground(Void... params) {
								strtoast = "register backgrounddd!";
								toastHandler.post(toast);
								ServerUtilities.unregister(context, regId);
								boolean registered = ServerUtilities.register(
										context, regId);
								// At this point all attempts to register with
								// the app
								// server failed, so we need to unregister the
								// device
								// from GCM - the app will try to register again
								// when
								// it is restarted. Note that GCM will send an
								// unregistered callback upon completion, but
								// GCMIntentService.onUnregistered() will ignore
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
		});
		// if (runForFirstTime) {
		initialize();
		// MainScreen.runForFirstTime = false;
		// }
		JSONObject jsonObject;
		try {
			currentQuestion = 0;
			jsonObject = messagesArray.getJSONObject(0);
			// StartThread startThread = new StartThread();
			// startThread.start();

			new CountDownTimer(10000, 1000) {

				public void onTick(long millisUntilFinished) {
					currentTime = (int) (millisUntilFinished / 1000);
					if (currentTime == -1)
						timeleftImageView.setImageResource(R.drawable.time_one);
					else {
						switch (currentTime) {
						case 0:
							timeleftImageView
									.setImageResource(R.drawable.time_one);
							break;

						case 1:
							timeleftImageView
									.setImageResource(R.drawable.time_one);
							break;

						case 2:
							timeleftImageView
									.setImageResource(R.drawable.time_two);
							break;

						case 3:
							timeleftImageView
									.setImageResource(R.drawable.time_three);
							break;
						case 4:
							timeleftImageView
									.setImageResource(R.drawable.time_four);
							break;
						case 5:
							timeleftImageView
									.setImageResource(R.drawable.time_five);
							break;
						case 6:
							timeleftImageView
									.setImageResource(R.drawable.time_six);
							break;
						case 7:
							timeleftImageView
									.setImageResource(R.drawable.time_seven);
							break;
						case 8:
							timeleftImageView
									.setImageResource(R.drawable.time_eight);
							break;
						case 9:
							timeleftImageView
									.setImageResource(R.drawable.time_nine);
							break;
						case 10:
							timeleftImageView
									.setImageResource(R.drawable.time_ten);
							break;
						default:
							break;
						}
						// timeLeftTextView.setText("Time Left        0:" +
						// currentTime);
					}
					// System.out.println(currentTime + "");
					timeleftImageView.postInvalidate();
					if (currentTime == 5 && index == 1) {
						System.out.println("hoppaaaaa ellalaaaa");
						registerReceiver(mHandleMessageReceiver,
								new IntentFilter(DISPLAY_MESSAGE_ACTION));
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
							// Device is already registered on GCM, check
							// server.
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
								// Try to register again, but not in the UI
								// thread.
								// It's also necessary to cancel the thread
								// onDestroy(),
								// hence the use of AsyncTask instead of a raw
								// thread.
								final Context context = getApplicationContext();
								mRegisterTask = new AsyncTask<Void, Void, Void>() {

									@Override
									protected Void doInBackground(
											Void... params) {
										strtoast = "register backgrounddd!";
										toastHandler.post(toast);
										System.out.println(strtoast);
										ServerUtilities.unregister(context,
												regId);
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
					// if (!destroy && currentTime >= 0) {
					// // System.out.println("refreshhhaaaaaaaaa");
					// toastHandler2.post(toast2);
					// } else if (!destroy) {
					// if (index == 10) {
					// try {
					// if (answered == Integer.parseInt(rightAnswer)) {
					// score++;
					// System.out.println("sccc   " + score);
					// }
					// } catch (Exception e) {
					// }
					// // sendScore();
					// // toastHandler3.post(toast3);
					// Intent unregIntent = new Intent(
					// "com.google.android.c2dm.intent.UNREGISTER");
					// unregIntent.putExtra("app", PendingIntent.getBroadcast(
					// getApplicationContext(), 0, new Intent(), 0));
					// startService(unregIntent);
					// GCMRegistrar.unregister(getApplicationContext());
					// unregisterReceiver(mHandleMessageReceiver);
					// GCMRegistrar.onDestroy(getApplicationContext());
					// WinnerActivity.score = score;
					// WinnerActivity.answers = answers;
					// WinnerActivity.firstTime = true;
					// // WinnerActivity.winnerUser = winnerUser;
					// Intent option = new Intent(getApplicationContext(),
					// WinnerActivity.class);
					// startActivity(option);
					// finish();
					// }
					// canAnswer = false;
					// running = false;
					// destroy = true;
					// return;
					// }
					// mTextField.setText("seconds remaining: " +
					// millisUntilFinished / 1000);
				}

				public void onFinish() {
					// mTextField.setText("done!");
					if (!destroy) {
						if (index == 10) {
							try {
								if (answered == Integer.parseInt(rightAnswer)) {
									score++;
									System.out.println("sccc   " + score);
								}
							} catch (Exception e) {
							}
							// sendScore();
							// toastHandler3.post(toast3);
							Intent unregIntent = new Intent(
									"com.google.android.c2dm.intent.UNREGISTER");
							unregIntent.putExtra("app", PendingIntent
									.getBroadcast(getApplicationContext(), 0,
											new Intent(), 0));
							startService(unregIntent);
							GCMRegistrar.unregister(getApplicationContext());
							unregisterReceiver(mHandleMessageReceiver);
							GCMRegistrar.onDestroy(getApplicationContext());
							WinnerActivity.score = score;
							WinnerActivity.answers = answers;
							WinnerActivity.firstTime = true;
							// WinnerActivity.winnerUser = winnerUser;
							mp.stop();
							Intent option = new Intent(getApplicationContext(),
									WinnerActivity.class);
							startActivity(option);
							finish();
							canAnswer = false;
							running = false;
							destroy = true;
						} else {
							canAnswer = false;
							running = false;
							destroy = true;
							try {
								Thread.sleep(3000);
								currentQuestion++;
								nextQuestion(messagesArray
										.getJSONObject(currentQuestion));
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						return;
					}
				}
			}.start();

			questionTextView.setText(jsonObject.getString("question"));
			answerATextView.setText(jsonObject.getString("answer_A"));
			answerATextView.setBackgroundResource(R.drawable.left_answer);
			answerBTextView.setText(jsonObject.getString("answer_B"));
			answerBTextView.setBackgroundResource(R.drawable.right_answer);
			answerCTextView.setText(jsonObject.getString("answer_C"));
			answerCTextView.setBackgroundResource(R.drawable.left_answer);
			answerDTextView.setText(jsonObject.getString("answer_D"));
			answerDTextView.setBackgroundResource(R.drawable.right_answer);
			rightAnswer = jsonObject.getString("right_answer");
			int answered = Integer.parseInt(rightAnswer);
			switch (answered) {
			case 1:
				answers += index + ". " + jsonObject.getString("answer_A");
				break;
			case 2:
				answers += index + ". " + jsonObject.getString("answer_B");
				break;
			case 3:
				answers += index + ". " + jsonObject.getString("answer_C");
				break;
			case 4:
				answers += index + ". " + jsonObject.getString("answer_D");
				break;

			default:
				break;
			}
			answers += "\n";
			// remainingQuestionsTextView.invalidate();
			questionTextView.invalidate();
			answerATextView.invalidate();
			answerBTextView.invalidate();
			answerCTextView.invalidate();
			answerDTextView.invalidate();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	int sound;

	private class StartThread3 extends Thread {

		public void run() {
			try {
			  mp = playSound(sound);
			} catch (Exception e) {
			}
		}
	}

	private class StartThread4 extends Thread {

		public void run() {
			if (mp != null || mp.isPlaying()) {
				mp.stop();

			}
			try {
				mp = playSound(R.raw.countdown_clock);
			} catch (Exception e) {
			}
		}
	}

	private MediaPlayer playSound(int sound) {

		mp = MediaPlayer.create(getApplicationContext(), sound);
		mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
		    @Override
		    public void onPrepared(MediaPlayer arg0) {
		        mp.start();

		    }
		});
		
		
		return mp;
	}

	private void initialize() {
		score = 0;
		questionOne.setImageResource(R.drawable.question_not_answered);
		questionTwo.setImageResource(R.drawable.question_not_answered);
		questionThree.setImageResource(R.drawable.question_not_answered);
		questionFour.setImageResource(R.drawable.question_not_answered);
		questionFive.setImageResource(R.drawable.question_not_answered);
		questionSix.setImageResource(R.drawable.question_not_answered);
		questionSeven.setImageResource(R.drawable.question_not_answered);
		questionEight.setImageResource(R.drawable.question_not_answered);
		questionNine.setImageResource(R.drawable.question_not_answered);
		questionTen.setImageResource(R.drawable.question_not_answered);
		questionOne.invalidate();
		questionTwo.invalidate();
		questionThree.invalidate();
		questionFour.invalidate();
		questionFive.invalidate();
		questionSix.invalidate();
		questionSeven.invalidate();
		questionEight.invalidate();
		questionNine.invalidate();
		questionTen.invalidate();
		canAnswer = true;
		answered = -1;
		currentTime = 10;
		destroy = false;
		running = true;
		index = 1;
		sound = R.raw.countdown_clock;
		StartThread3 startThread3 = new StartThread3();
		startThread3.start();

		// mp = playSound(R.raw.countdown_clock);

	}

	private CharSequence strtoast;
	// handler to show the toast
	private Runnable toast = new Runnable() {

		public void run() {
			// Toast.makeText(getApplicationContext(), strtoast,
			// Toast.LENGTH_SHORT).show();
		}
	};

	private Runnable toast3 = new Runnable() {

		public void run() {
			questionTen.setImageResource(R.drawable.question_answered);
			questionTen.invalidate();
			LinearLayout syncLinearLayout = (LinearLayout) findViewById(R.id.syncLinearLayout);
			LinearLayout linearLayoutFirstScreen = (LinearLayout) findViewById(R.id.linearLayout1);
			timeTextView = (TextView) findViewById(R.id.timeTextView);
			syncLinearLayout.setVisibility(View.VISIBLE);
			linearLayoutFirstScreen.setVisibility(View.GONE);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			timeSyncToStart = 30;
			StartThread2 startThread2 = new StartThread2();
			startThread2.start();
			// waitWinner = true;
			// new LoadWinnerTask().execute("");

		}
	};
	private TextView timeTextView;
	private int timeSyncToStart;
	private boolean syncCount;

	private Handler toastHandler4 = new Handler();
	// handler to show the toast
	private Runnable toast4 = new Runnable() {

		public void run() {
			// TODO
			timeTextView.setText(timeSyncToStart + "");
			timeTextView.invalidate();
		}
	};

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
				if (timeSyncToStart == -1)
					syncCount = false;
			}
		}
	}

	private class StartThread extends Thread {

		public void run() {
			while (running) {
				toastHandler2.post(toast2);
				try {
					Thread.currentThread().sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				currentTime--;
				if (currentTime == 5 && index == 1) {
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
				if (!destroy && currentTime >= 0) {
					// System.out.println("refreshhhaaaaaaaaa");
					toastHandler2.post(toast2);
				} else if (!destroy) {
					if (index == 10) {
						try {
							if (answered == Integer.parseInt(rightAnswer)) {
								score++;
								System.out.println("sccc   " + score);
							}
						} catch (Exception e) {
						}
						// sendScore();
						// toastHandler3.post(toast3);
						Intent unregIntent = new Intent(
								"com.google.android.c2dm.intent.UNREGISTER");
						unregIntent.putExtra("app", PendingIntent.getBroadcast(
								getApplicationContext(), 0, new Intent(), 0));
						startService(unregIntent);
						GCMRegistrar.unregister(getApplicationContext());
						unregisterReceiver(mHandleMessageReceiver);
						GCMRegistrar.onDestroy(getApplicationContext());
						WinnerActivity.score = score;
						WinnerActivity.answers = answers;
						WinnerActivity.firstTime = true;
						// WinnerActivity.winnerUser = winnerUser;
						mp.stop();
						Intent option = new Intent(getApplicationContext(),
								WinnerActivity.class);
						startActivity(option);
						finish();
					}
					canAnswer = false;
					running = false;
					destroy = true;
					return;
				}
			}
		}

	}

	private void sendScore() {
		HttpParams postParams = new BasicHttpParams();
		HttpConnectionParams.setSoTimeout(postParams, 3000);
		HttpConnectionParams.setConnectionTimeout(postParams, 3000);
		HttpClient client = new DefaultHttpClient(postParams);
		HttpPost httpGet = new HttpPost(SERVER_URL + "/GetScore");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
		nameValuePairs.add(new BasicNameValuePair("name", "KARIM"));
		nameValuePairs.add(new BasicNameValuePair("score", score + ""));
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

	private Handler toastHandler2 = new Handler();
	private Handler toastHandler3 = new Handler();
	// handler to show the toast
	private Runnable toast2 = new Runnable() {

		public void run() {
			// TODO
			if (currentTime == -1)
				timeleftImageView.setImageResource(R.drawable.time_one);
			else {
				switch (currentTime) {
				case 0:
					timeleftImageView.setImageResource(R.drawable.time_one);
					break;

				case 1:
					timeleftImageView.setImageResource(R.drawable.time_one);
					break;

				case 2:
					timeleftImageView.setImageResource(R.drawable.time_two);
					break;

				case 3:
					timeleftImageView.setImageResource(R.drawable.time_three);
					break;
				case 4:
					timeleftImageView.setImageResource(R.drawable.time_four);
					break;
				case 5:
					timeleftImageView.setImageResource(R.drawable.time_five);
					break;
				case 6:
					timeleftImageView.setImageResource(R.drawable.time_six);
					break;
				case 7:
					timeleftImageView.setImageResource(R.drawable.time_seven);
					break;
				case 8:
					timeleftImageView.setImageResource(R.drawable.time_eight);
					break;
				case 9:
					timeleftImageView.setImageResource(R.drawable.time_nine);
					break;
				case 10:
					timeleftImageView.setImageResource(R.drawable.time_ten);
					break;
				default:
					break;
				}
				// timeLeftTextView.setText("Time Left        0:" +
				// currentTime);
			}
			// System.out.println(currentTime + "");
			timeleftImageView.postInvalidate();
		}
	};

	public void onBackPressed() {
		mp.pause();
		mp.stop();
		Intent ActivityIntent = new Intent(MainScreen.this,FrontPageActivity.class);
		startActivity(ActivityIntent);
		finish();
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		running = false;
		destroy = true;
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
		// Intent ActivityIntent = new Intent(MainScreen.this,
		// FrontPageActivity.class);
		// startActivity(ActivityIntent);
		// MainScreen.this.finish();
		super.onDestroy();
	}

	private void checkNotNull(Object reference, String name) {
		if (reference == null) {
			throw new NullPointerException(getString(R.string.error_config,
					name));
		}
	}

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			// if (newMessage.equalsIgnoreCase(CommonUtilities.getMessage))
			// return;
			// else
			// CommonUtilities.getMessage = newMessage;
			System.out.println("THE MESAGEE:  " + newMessage);
			if (newMessage.equals("Are you here?")) {
				strtoast = "Are you here!";
				toastHandler.post(toast);
				System.out
						.println("get are you here from mainscreen!!!!!!!!!!!");
				new IAmHereTask().execute("");
			} else if (newMessage.startsWith("winner")) {
				winnerUser = newMessage.substring(6);
				waitWinner = false;
				syncCount = false;
				LinearLayout syncLinearLayout = (LinearLayout) findViewById(R.id.syncLinearLayout);
				LinearLayout winnLinearLayout = (LinearLayout) findViewById(R.id.winnerLinearLayout);
				TextView winnerTextView = (TextView) findViewById(R.id.winnerTextView);
				TextView scoreTextView = (TextView) findViewById(R.id.scoreTextView);
				syncLinearLayout.setVisibility(View.GONE);
				winnLinearLayout.setVisibility(View.VISIBLE);
				winnerTextView.setText(winnerUser);
				scoreTextView.setText("YOUR SCORE " + score + " POINTS");
			} else {
				//nextQuestion(newMessage);

			}
			// mDisplay.append(newMessage + "\n");
		}

	};

	private void nextQuestion(JSONObject jsonObject) {
		try {
			// JSONObject jsonObject = new JSONObject(jsonObject2);
			strtoast = "ZZZZZZZZZZ!";
			toastHandler.post(toast);
			if (jsonObject.getInt("index") == 1) {
				System.out.println("5aliha false");
				score = 0;
				wait = false;
				LinearLayout syncLinearLayout = (LinearLayout) findViewById(R.id.syncLinearLayout);
				LinearLayout reviewAnswersLinearLayout = (LinearLayout) findViewById(R.id.reviewAnswersLinearLayout);
				LinearLayout winnerLinearLayout = (LinearLayout) findViewById(R.id.winnerLinearLayout);
				LinearLayout linearLayoutFirstScreen = (LinearLayout) findViewById(R.id.linearLayout1);
				syncLinearLayout.setVisibility(View.GONE);
				reviewAnswersLinearLayout.setVisibility(View.GONE);
				winnerLinearLayout.setVisibility(View.GONE);
				linearLayoutFirstScreen.setVisibility(View.VISIBLE);
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				// questionOne.setImageResource()
				questionOne.setImageResource(R.drawable.question_not_answered);
				questionTwo.setImageResource(R.drawable.question_not_answered);
				questionThree
						.setImageResource(R.drawable.question_not_answered);
				questionFour.setImageResource(R.drawable.question_not_answered);
				questionFive.setImageResource(R.drawable.question_not_answered);
				questionSix.setImageResource(R.drawable.question_not_answered);
				questionSeven
						.setImageResource(R.drawable.question_not_answered);
				questionEight
						.setImageResource(R.drawable.question_not_answered);
				questionNine.setImageResource(R.drawable.question_not_answered);
				questionTen.setImageResource(R.drawable.question_not_answered);
				questionOne.invalidate();
				questionTwo.invalidate();
				questionThree.invalidate();
				questionFour.invalidate();
				questionFive.invalidate();
				questionSix.invalidate();
				questionSeven.invalidate();
				questionEight.invalidate();
				questionNine.invalidate();
				questionTen.invalidate();
			}
			try {
				if (answered == Integer.parseInt(rightAnswer)) {
					score++;
					System.out.println("sccc   " + score);
				}
			} catch (Exception e) {
			}
			System.out.println("!!!!!!!!!!!!!");
			canAnswer = true;
			answered = -1;
			currentTime = 10;
			destroy = false;
			running = true;
			// TODO
			// timeLeftTextView.setText("Time Left        0:"
			// + currentTime);
			// StartThread startThread = new StartThread();
			// startThread.start();
			new CountDownTimer(10000, 1000) {

				public void onTick(long millisUntilFinished) {
					currentTime = (int) (millisUntilFinished / 1000);
					System.out.println("TIME HENAA:  " + currentTime);
					if (currentTime == -1)
						timeleftImageView.setImageResource(R.drawable.time_one);
					else {
						switch (currentTime) {
						case 0:
							timeleftImageView
									.setImageResource(R.drawable.time_one);
							break;

						case 1:
							timeleftImageView
									.setImageResource(R.drawable.time_one);
							break;

						case 2:
							timeleftImageView
									.setImageResource(R.drawable.time_two);
							break;

						case 3:
							timeleftImageView
									.setImageResource(R.drawable.time_three);
							break;
						case 4:
							timeleftImageView
									.setImageResource(R.drawable.time_four);
							break;
						case 5:
							timeleftImageView
									.setImageResource(R.drawable.time_five);
							break;
						case 6:
							timeleftImageView
									.setImageResource(R.drawable.time_six);
							break;
						case 7:
							timeleftImageView
									.setImageResource(R.drawable.time_seven);
							break;
						case 8:
							timeleftImageView
									.setImageResource(R.drawable.time_eight);
							break;
						case 9:
							timeleftImageView
									.setImageResource(R.drawable.time_nine);
							break;
						case 10:
							timeleftImageView
									.setImageResource(R.drawable.time_ten);
							break;
						default:
							break;
						}
						// timeLeftTextView.setText("Time Left        0:"
						// +
						// currentTime);
					}
					// System.out.println(currentTime + "");
					timeleftImageView.postInvalidate();
					if (currentTime == 5 && index == 1) {
						registerReceiver(mHandleMessageReceiver,
								new IntentFilter(DISPLAY_MESSAGE_ACTION));
						final String regId = GCMRegistrar
								.getRegistrationId(getApplicationContext());
						if (regId.equals("")) {
							// Automatically registers application on
							// startup.
							GCMRegistrar.register(getApplicationContext(),
									SENDER_ID);
							strtoast = "called register inside if!";
							toastHandler.post(toast);
							System.out.println(strtoast);
						} else {
							// Device is already registered on GCM,
							// check server.
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
								// Try to register again, but not in the
								// UI thread.
								// It's also necessary to cancel the
								// thread
								// onDestroy(),
								// hence the use of AsyncTask instead of
								// a raw
								// thread.
								final Context context = getApplicationContext();
								mRegisterTask = new AsyncTask<Void, Void, Void>() {

									@Override
									protected Void doInBackground(
											Void... params) {
										strtoast = "register backgrounddd!";
										toastHandler.post(toast);
										System.out.println(strtoast);
										ServerUtilities.unregister(context,
												regId);
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
				}

				public void onFinish() {
					// mTextField.setText("done!");
					System.out.println("FINISHINGGGGGGGGGGGGGGGGGGGG+   "
							+ destroy);
					if (!destroy) {
						if (index == 10) {
							try {
								if (answered == Integer.parseInt(rightAnswer)) {
									score++;
									System.out.println("sccc   " + score);
								}
							} catch (Exception e) {
							}
							// sendScore();
							// toastHandler3.post(toast3);
							Intent unregIntent = new Intent(
									"com.google.android.c2dm.intent.UNREGISTER");
							unregIntent.putExtra("app", PendingIntent
									.getBroadcast(getApplicationContext(), 0,
											new Intent(), 0));
							startService(unregIntent);
							GCMRegistrar.unregister(getApplicationContext());
							unregisterReceiver(mHandleMessageReceiver);
							GCMRegistrar.onDestroy(getApplicationContext());
							WinnerActivity.score = score;
							WinnerActivity.answers = answers;
							WinnerActivity.firstTime = true;
							mp.stop();
							// WinnerActivity.winnerUser = winnerUser;
							Intent option = new Intent(getApplicationContext(),
									WinnerActivity.class);
							startActivity(option);
							finish();
							canAnswer = false;
							running = false;
							destroy = true;
						} else {
							canAnswer = false;
							running = false;
							destroy = true;
							try {
								Thread.sleep(3000);
								currentQuestion++;
								nextQuestion(messagesArray
										.getJSONObject(currentQuestion));
								// nextQuestion(jsonObject2);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						
						return;
					}
				}
			}.start();

			index = jsonObject.getInt("index");

			// remainingQuestionsTextView.setText("Q: " + index +
			// "/10");
			switch (index) {
			case 2:
				questionOne.setImageResource(R.drawable.question_answered);
				questionOne.invalidate();
				break;
			case 3:
				questionTwo.setImageResource(R.drawable.question_answered);
				questionTwo.invalidate();
				break;
			case 4:
				questionThree.setImageResource(R.drawable.question_answered);
				questionThree.invalidate();
				break;
			case 5:
				questionFour.setImageResource(R.drawable.question_answered);
				questionFour.invalidate();
				break;
			case 6:
				questionFive.setImageResource(R.drawable.question_answered);
				questionFive.invalidate();
				break;
			case 7:
				questionSix.setImageResource(R.drawable.question_answered);
				questionSix.invalidate();
				break;
			case 8:
				questionSeven.setImageResource(R.drawable.question_answered);
				questionSeven.invalidate();
				break;
			case 9:
				questionEight.setImageResource(R.drawable.question_answered);
				questionEight.invalidate();
				break;
			case 10:
				questionNine.setImageResource(R.drawable.question_answered);
				questionNine.invalidate();
				break;

			default:
				break;
			}
			questionTextView.setText(jsonObject.getString("question"));
			answerATextView.setText(jsonObject.getString("answer_A"));
			answerATextView.setBackgroundResource(R.drawable.left_answer);
			answerBTextView.setText(jsonObject.getString("answer_B"));
			answerBTextView.setBackgroundResource(R.drawable.right_answer);
			answerCTextView.setText(jsonObject.getString("answer_C"));
			answerCTextView.setBackgroundResource(R.drawable.left_answer);
			answerDTextView.setText(jsonObject.getString("answer_D"));
			System.out.println("Ana 3mlt set lelanswer aho wallahi!!!!");
			answerDTextView.setBackgroundResource(R.drawable.right_answer);
			questionTextView.postInvalidate();
			answerATextView.postInvalidate();
			answerBTextView.postInvalidate();
			answerCTextView.postInvalidate();
			answerDTextView.postInvalidate();
			rightAnswer = jsonObject.getString("right_answer");
			// if (mp != null) {
			// mp.stop();
			//
			// }
			// mp = playSound(R.raw.countdown_clock);

			sound = R.raw.countdown_clock;
			StartThread4 startThread4 = new StartThread4();
			startThread4.start();

			int answered = Integer.parseInt(rightAnswer);
			switch (answered) {
			case 1:
				answers += index + ". " + jsonObject.getString("answer_A");
				break;
			case 2:
				answers += index + ". " + jsonObject.getString("answer_B");
				break;
			case 3:
				answers += index + ". " + jsonObject.getString("answer_C");
				break;
			case 4:
				answers += index + ". " + jsonObject.getString("answer_D");
				break;

			default:
				break;
			}
			answers += "\n";
			// remainingQuestionsTextView.invalidate();
			questionTextView.invalidate();
			answerATextView.invalidate();
			answerBTextView.invalidate();
			answerCTextView.invalidate();
			answerDTextView.invalidate();
		} catch (Exception e) {
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

	class LoadPlayTask extends AsyncTask<String, Void, Boolean> {

		private ProgressDialog pdialog = null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pdialog = new ProgressDialog(MainScreen.this);
			pdialog.setTitle("Waiting to Sync");
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
			playButton.setVisibility(View.GONE);
			linearLayout1.setVisibility(View.VISIBLE);
			System.out.println("elmafrod 5alas");

		}

		// private void showAlertDialog(String title, String message) {
		// new AlertDialog.Builder(LoginActivity.this)
		// .setTitle(title)
		// .setMessage(message)
		// .setNeutralButton("Ok",
		// new DialogInterface.OnClickListener() {
		//
		// public void onClick(DialogInterface dialog,
		// int which) {
		// // TODO Auto-generated method stub
		// if (loginResult) {
		//
		// }
		// }
		// }).show();

		// }

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub

			while (wait)
				;
			System.out.println("5alasna wait");
			return true;
		}

	}

	class IAmHereTask extends AsyncTask<String, Void, Boolean> {

		
		@Override
		protected Boolean doInBackground(String... params) {
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

			return true;
		}

	}

	class LoadWinnerTask extends AsyncTask<String, Void, Boolean> {

		private ProgressDialog pdialog = null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pdialog = new ProgressDialog(MainScreen.this);
			pdialog.setTitle("Submitting Results");
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
			showAlertDialog("The winner is...", winnerUser);

		}

		private void showAlertDialog(String title, String message) {
			new AlertDialog.Builder(MainScreen.this)
					.setTitle(title)
					.setMessage(message)
					.setNeutralButton("Ok",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									wait = true;
									new LoadPlayTask().execute("");
									// TODO Auto-generated method stub
									// this.d
									// pdialog.dismiss();
									// pdialog = null;
								}
							}).show();
		}

		// }

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub

			while (waitWinner)
				;

			return true;
		}

	}

}
