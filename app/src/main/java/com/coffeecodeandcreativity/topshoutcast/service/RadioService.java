package com.coffeecodeandcreativity.topshoutcast.service;

import java.io.IOException;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.coffeecodeandcreativity.topshoutcast.ActivityMain;
import com.coffeecodeandcreativity.topshoutcast.R;

public class RadioService extends Service implements OnPreparedListener,
		OnErrorListener {

	MediaPlayer player;
	String url, chnlName;
	int image;
	Bundle bundle;
	PhoneStateListener psl;
	TelephonyManager tManager;

	public static final String ACTION_RESUME = "com.coffeecodeandcreativity.topshoutcast.service.action.resume";
	public static final String ACTION_PAUSE = "com.coffeecodeandcreativity.topshoutcast.service.action.pause";
	public static final String ACTION_SETUP_AND_PLAY = "com.coffeecodeandcreativity.topshoutcast.service.action.setupandplay";

	public static final String RECIEVER_ACTION_PLAYING = "com.coffeecodeandcreativity.topshoutcast.playing";
	public static final String RECIEVER_ACTION_STOPPED = "com.coffeecodeandcreativity.topshoutcast.paused";
	public static final String RECIEVER_ACTION_PREPARING = "com.coffeecodeandcreativity.topshoutcast.preparing";
	public static final String RECIEVER_ACTION_PREPARE_ERROR = "com.coffeecodeandcreativity.topshoutcast.prepareerror";

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		player = new MediaPlayer();
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		handlePhoneCall();
		player.setOnPreparedListener(this);
		player.setOnErrorListener(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		bundle = intent.getBundleExtra("BUNDLE");
		image = bundle.getInt("IMG", android.R.drawable.presence_audio_busy);
		url = bundle.getString("URL");
		chnlName = bundle.getString("NAME");
		if (intent.getAction() == ACTION_SETUP_AND_PLAY) {

			playerSetUpAndPlay();

		} else if (intent.getAction() == ACTION_RESUME) {

			playerResume();

		} else if (intent.getAction() == ACTION_PAUSE) {

			playerPause();

		}

		return START_NOT_STICKY;
	}

	private void handlePhoneCall() {
		psl = new PhoneStateListener() {

			@Override
			public void onCallStateChanged(int state, String incomingNumber) {

				Log.e("PhonState", "Changed");
				if (state == TelephonyManager.CALL_STATE_RINGING) {
					// Incoming call: Pause music
					Log.e("PhonState", "Ringing");
					if (player != null) {
						if (player.isPlaying()) {

							player.pause();

						}
					}
				} else if (state == TelephonyManager.CALL_STATE_IDLE) {
					// Not in call: Play music
					Log.e("PhonState", "Idle");
					if (player != null) {
						if (!player.isPlaying()) {
							player.start();

						}
					}

				} else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
					// A call is dialing, active or on hold
					Log.e("PhonState", "Dialing");
					if (player != null) {
						if (player.isPlaying()) {

							player.pause();

						}
					}
				}

				// TODO Auto-generated method stub
				super.onCallStateChanged(state, incomingNumber);
			}

		};
		tManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		if (tManager != null) {
			tManager.listen(psl, PhoneStateListener.LISTEN_CALL_STATE);
		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		playerStopANdRealease();

	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		player.start();
		if (player.isPlaying()) {

			pushServicetoForeground();

			broadCast(RECIEVER_ACTION_PLAYING);

		}

	}

	private void pushServicetoForeground() {

		final int notiId = 1234;
		Notification notice;
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.icon)

				.setContentTitle(getResources().getString(R.string.app_name))
				.setContentText("Playing: " + chnlName).setAutoCancel(true);
        int myColor =
                getResources().getColor(R.color.app_color);
        builder.setColor(myColor);

		Intent notificationIntent = new Intent(this, ActivityMain.class);

		notificationIntent.putExtra("BUNDLE", bundle);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(contentIntent);
		notice = builder.build();
		startForeground(notiId, notice);

	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {

		broadCast(RECIEVER_ACTION_PREPARE_ERROR);
		return true;
	}

	private void playerPause() {
		if (player != null) {

			if (player.isPlaying()) {

				player.pause();

				broadCast(RECIEVER_ACTION_STOPPED);

			}
		}

	}

	private void playerResume() {
		if (player != null) {

			if (!player.isPlaying()) {

				player.start();
				broadCast(RECIEVER_ACTION_PLAYING);

			}

		}

	}

	private void broadCast(String recieverAction) {
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(recieverAction);
		sendBroadcast(broadcastIntent);

	}

	private void playerSetUpAndPlay() {
		try {
			if (player != null) {
				if (!player.isPlaying()) {
					player.setDataSource(url);
					player.prepareAsync();

					broadCast(RECIEVER_ACTION_PREPARING);

				}

			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void playerStopANdRealease() {
		if (player != null) {

			if (player.isPlaying()) {

				player.stop();

			}
			player.release();
			player = null;

			broadCast(RECIEVER_ACTION_STOPPED);

		}

	}

}
