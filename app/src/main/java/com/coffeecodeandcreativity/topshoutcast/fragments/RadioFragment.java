package com.coffeecodeandcreativity.topshoutcast.fragments;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.coffeecodeandcreativity.topshoutcast.R;
import com.coffeecodeandcreativity.topshoutcast.service.RadioService;

public class RadioFragment extends Fragment implements OnClickListener {

	String url, chnlName;

	String buttonText = "";

	Button bPlayPause;
	SeekBar skBar;
	MediaPlayer player;
	Intent playService;
	public static String PLAYER_STATE;
	boolean isStoppedAndReleased = false;
	BroadcastReceiver playerStateReceiver;
	TextView tvBuffering, tvNowPlaying, tvChannelName;
	AudioManager audioManager;
	Typeface font, fontItalic;

	AdView adView;
	AdRequest bannerRequest;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.radio_fragment, container, false);
		initializer(view);
		setTypeFaces();
		setAttributes();

		bPlayPause.setOnClickListener(this);
		skBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
						progress, 0);

			}
		});
		playerStateReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction() == RadioService.RECIEVER_ACTION_PREPARING) {

					tvBuffering.setText("Buffering...");
					tvBuffering.setTextColor(getResources().getColor(
							R.color.grey_text_color));
					bPlayPause.setText("Stop");
					bPlayPause.setEnabled(false);
					bPlayPause.setTextColor(getResources().getColor(
							R.color.grey_text_color));

				} else if (intent.getAction() == RadioService.RECIEVER_ACTION_PLAYING) {
					Log.i("receiver called", "playing");
					tvBuffering.setText("Playing...");
					tvBuffering.setTextColor(Color.GREEN);
					bPlayPause.setText("Stop");
					bPlayPause.setEnabled(true);
					bPlayPause.setTextColor(getResources().getColor(
							R.color.app_color));

				} else if (intent.getAction() == RadioService.RECIEVER_ACTION_STOPPED) {
					Log.i("receiver called", "Stopped");
					tvBuffering.setText("Stopped");
					tvBuffering.setTextColor(getResources().getColor(
							R.color.app_color));
					bPlayPause.setText("Play");
					bPlayPause.setEnabled(true);

					bPlayPause.setTextColor(getResources().getColor(
							R.color.app_color));

				} else if (intent.getAction() == RadioService.RECIEVER_ACTION_PREPARE_ERROR) {

					tvBuffering.setText("Error occured");
					tvBuffering.setTextColor(getResources().getColor(
							R.color.grey_text_color));
					bPlayPause.setText("Stop");
					bPlayPause.setEnabled(false);
				}

			}
		};

		startRadioPlayService(RadioService.ACTION_SETUP_AND_PLAY);

		adView = (AdView) view.findViewById(R.id.adView);
		bannerRequest = new AdRequest.Builder().build();
		adView.loadAd(bannerRequest);

		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		IntentFilter filter = new IntentFilter();
		filter.addAction(RadioService.RECIEVER_ACTION_PLAYING);
		filter.addAction(RadioService.RECIEVER_ACTION_PREPARING);
		filter.addAction(RadioService.RECIEVER_ACTION_STOPPED);

		getActivity().registerReceiver(playerStateReceiver, filter);

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		getActivity().unregisterReceiver(playerStateReceiver);
	}

	@Override
	public void onClick(View v) {
		buttonText = bPlayPause.getText().toString();
		switch (v.getId()) {

		case R.id.bPlayPause:
			if (buttonText.contentEquals("Play")) {

				startRadioPlayService(RadioService.ACTION_RESUME);

			} else if (buttonText.contentEquals("Stop")) {

				startRadioPlayService(RadioService.ACTION_PAUSE);
			}

			break;

		}

	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		if (buttonText.contentEquals("Stop")
				| tvBuffering.getText().toString()
						.contentEquals("Buffering...")
				| tvBuffering.getText().toString()
						.contentEquals("Error occured")) {

			getActivity().stopService(playService);
		}
	}

	private void startRadioPlayService(String actionSetupAndPlay) {

		playService.putExtra("BUNDLE", getArguments());
		playService.setAction(actionSetupAndPlay);
		getActivity().startService(playService);

	}

	private void setAttributes() {
		skBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		skBar.setProgress(audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC));
		tvChannelName.setText(chnlName);

	}

	private void setTypeFaces() {

		bPlayPause.setTypeface(fontItalic);
		tvBuffering.setTypeface(fontItalic);
		tvNowPlaying.setTypeface(fontItalic);
		tvChannelName.setTypeface(fontItalic);

	}

	private void initializer(View view) {
		bPlayPause = (Button) view.findViewById(R.id.bPlayPause);
		tvBuffering = (TextView) view.findViewById(R.id.tvBuffering);
		tvNowPlaying = (TextView) view.findViewById(R.id.tvNowListening);
		tvChannelName = (TextView) view.findViewById(R.id.tvRadioChannelName);
		skBar = (SeekBar) view.findViewById(R.id.sbVolume);
		audioManager = (AudioManager) getActivity().getSystemService(
				Context.AUDIO_SERVICE);
		url = getArguments().getString("URL");
		chnlName = getArguments().getString("NAME");
		font = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/Roboto-Light.ttf");
		fontItalic = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/Roboto-LightItalic.ttf");
		playService = new Intent(getActivity(), RadioService.class);

	}

}
