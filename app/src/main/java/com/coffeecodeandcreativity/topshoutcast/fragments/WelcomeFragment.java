package com.coffeecodeandcreativity.topshoutcast.fragments;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.coffeecodeandcreativity.topshoutcast.R;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeFragment extends Fragment {

	TextView tvAppName, tvPromoText;
	Typeface font;

	Button bStartListen;
	onWelComeButtonClickListener listener;

    AdView adView;
    AdRequest bannerRequest;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.welcome_fragment, container,
				false);
		font = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/Roboto-Light.ttf");
		tvAppName = (TextView) view.findViewById(R.id.tvAppName);
		tvPromoText = (TextView) view.findViewById(R.id.tvPromoText);

		bStartListen = (Button) view.findViewById(R.id.btnlisten);
		bStartListen.setTypeface(font);

		tvAppName.setTypeface(font);
		tvPromoText.setTypeface(font);



        adView = (AdView) view.findViewById(R.id.adView);
        bannerRequest = new AdRequest.Builder().build();
        adView.loadAd(bannerRequest);

		
		
		return view;

	}

	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		bStartListen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.onWelComeButtonClick();
			}
		});
	}

	public void setOnWelComeButtonClickListener(
			onWelComeButtonClickListener listener) {

		this.listener = listener;

	}

	public interface onWelComeButtonClickListener {

		public void onWelComeButtonClick();
	}

}
