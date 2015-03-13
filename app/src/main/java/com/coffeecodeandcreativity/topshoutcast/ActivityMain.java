package com.coffeecodeandcreativity.topshoutcast;

import java.util.ArrayList;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.coffeecodeandcreativity.topshoutcast.adapters.ChannelCustomAdapter;
import com.coffeecodeandcreativity.topshoutcast.adapters.Channels;
import com.coffeecodeandcreativity.topshoutcast.fragments.RadioFragment;
import com.coffeecodeandcreativity.topshoutcast.fragments.WelcomeFragment;
import com.coffeecodeandcreativity.topshoutcast.fragments.WelcomeFragment.onWelComeButtonClickListener;
import com.coffeecodeandcreativity.topshoutcast.service.RadioService;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.MatrixCursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SearchView.OnSuggestionListener;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListAdapter;

public class ActivityMain extends Activity implements OnItemClickListener,
		onWelComeButtonClickListener, OnClickListener {
	MatrixCursor cursor;
	ActionBar actionBar;
	DrawerLayout dLayout;
	ListView channelListView;
	String[] chName, chUrl, chGenre;
	ChannelCustomAdapter adapter;
	ArrayList<Channels> arrChannels;
    ExpandableListAdapter arrExChannels;

	ActionBarDrawerToggle toggle;
	CharSequence title;
	Bundle bundle;
	Menu menu;
	RelativeLayout rlDrawerOpen;
	Typeface selectFonts;
	TextView tFacebook, tRateus;

	AdRequest fullScreenAdRequest;
	InterstitialAd fullScreenAdd;

	WelcomeFragment welFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initializer();
		dLayout.setDrawerListener(toggle);
		channelListView.setOnItemClickListener(this);
		welFragment.setOnWelComeButtonClickListener(this);
		tFacebook.setOnClickListener(this);
		tRateus.setOnClickListener(this);

		//enableAd();
	}

	private void enableAd() {
		// adding full screen add
		fullScreenAdd = new InterstitialAd(this);
		fullScreenAdd.setAdUnitId("ca-app-pub-8065034992429095/2082842767");
		fullScreenAdRequest = new AdRequest.Builder().build();
		fullScreenAdd.loadAd(fullScreenAdRequest);

		fullScreenAdd.setAdListener(new AdListener() {

			@Override
			public void onAdLoaded() {

				Log.i("FullScreenAdd", "Loaded successfully");
				fullScreenAdd.show();

			}

			@Override
			public void onAdFailedToLoad(int errorCode) {

				Log.i("FullScreenAdd", "failed to Load");
			}
		});

	}

	private void initializer() {
		actionBar = getActionBar();
		welFragment = new WelcomeFragment();
		selectFonts = (Typeface.createFromAsset(getAssets(),
				"fonts/Roboto-Bold.ttf"));
		dLayout = (DrawerLayout) findViewById(R.id.dl_drawerLayout);
		rlDrawerOpen = (RelativeLayout) findViewById(R.id.rl_drawer_open);
		channelListView = (ListView) findViewById(R.id.lv_channel_List);
		chName = getResources().getStringArray(R.array.channelsName);
		chUrl = getResources().getStringArray(R.array.channelsUrl);
        chGenre = getResources().getStringArray(R.array.channelsGenre);

		title = getResources().getString(R.string.app_name);
		tFacebook = (TextView) findViewById(R.id.tvFacbook);
		tRateus = (TextView) findViewById(R.id.tvRateUs);
		arrChannels = new ArrayList<Channels>();

		for (int i = 0; i < chName.length; i++) {

			arrChannels.add(new Channels(chName[i], chUrl[i], chGenre[i]));
		}

		adapter = new ChannelCustomAdapter(this, arrChannels);
		channelListView.setAdapter(adapter);
		toggle = new ActionBarDrawerToggle(this, dLayout, R.drawable.ic_drawer,
				R.string.app_name, R.string.app_name) {

			@Override
			public void onDrawerOpened(View drawerView) {

				setTitle(R.string.nd_title);
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				setTitle(title);
				invalidateOptionsMenu();
			}

		};
		actionBar.setDisplayHomeAsUpEnabled(true);

		actionBar.setHomeButtonEnabled(true);
		handleIntent(getIntent());

	}

	@Override
	protected void onNewIntent(Intent intent) {
		handleIntent(intent);

	}

	private void handleIntent(Intent intent) {

		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			// use the query to search your data somehow

			Toast.makeText(getApplicationContext(), query, Toast.LENGTH_LONG)
					.show();

		} else {

			fragmentSelector();

		}

	}

	private void fragmentSelector() {
		bundle = getIntent().getBundleExtra("BUNDLE");
		if (bundle == null) {
			Fragment fr = new WelcomeFragment();
			FragmentManager manager = getFragmentManager();
			manager.beginTransaction().replace(R.id.fl_content, fr).commit();

			setTitle(title);

			((WelcomeFragment) fr).setOnWelComeButtonClickListener(this);

		} else {
			selectItem(bundle.getInt("POS"));
			setTitle(bundle.getString("NAME"));
		}

	}

	public void selectItem(int position) {

		Fragment fr = new RadioFragment();

		bundle = new Bundle();

		bundle.putString("URL", arrChannels.get(position).getUrl());
		bundle.putString("NAME", arrChannels.get(position).getChannelName());
		bundle.putInt("POS", position);
		fr.setArguments(bundle);

		FragmentManager manager = getFragmentManager();
		manager.beginTransaction().replace(R.id.fl_content, fr).commit();
		title = arrChannels.get(position).getChannelName();
		dLayout.closeDrawer(rlDrawerOpen);
	}

	@Override
	public void setTitle(CharSequence title) {
		actionBar.setTitle(title);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.main, menu);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		final SearchView searchView = (SearchView) menu.findItem(R.id.search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {

				if (getPositionfromString(query) != -1) {

					Intent intent = new Intent(ActivityMain.this,
							RadioService.class);
					stopService(intent);
					selectItem(getPositionfromString(query));
					setTitle(query);
				} else {

					Toast.makeText(getApplicationContext(), "No channel found",
							Toast.LENGTH_LONG).show();

				}
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {

				String[] columnNames = { "_id", "text" };
				cursor = new MatrixCursor(columnNames);

				String[] temp = new String[2];
				// int id = 0;
				for (int i = 0; i < arrChannels.size(); i++) {
					if (arrChannels.get(i).getChannelName().toLowerCase()
							.contains(newText.toLowerCase())) {
						temp[0] = Integer.toString(i);
						temp[1] = arrChannels.get(i).getChannelName();
						cursor.addRow(temp);
					}
				}
				String[] from = { "text" };
				int[] to = { R.id.text };
				SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
						ActivityMain.this, R.layout.search_item, cursor, from,
						to, SimpleCursorAdapter.NO_SELECTION);

				searchView.setSuggestionsAdapter(cursorAdapter);

				return true;
			}
		});

		searchView.setOnSuggestionListener(new OnSuggestionListener() {

			@Override
			public boolean onSuggestionSelect(int position) {

				return false;
			}

			@Override
			public boolean onSuggestionClick(int position) {
				cursor.moveToPosition(position);

				searchView.setQuery(cursor.getString(1), true);

				return false;
			}
		});

		return true;
	}

	private int getPositionfromString(String chn) {

		for (int i = 0; i < arrChannels.size(); i++) {

			if (chn.contentEquals(arrChannels.get(i).getChannelName())) {

				return i;

			}

		}
		return -1;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (toggle.onOptionsItemSelected(item))
			return true;
		if (item.getItemId() == R.id.search)
			return true;
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.findItem(R.id.search).setVisible(
				!dLayout.isDrawerOpen(rlDrawerOpen));
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		Intent intent = new Intent(ActivityMain.this, RadioService.class);
		stopService(intent);
		selectItem(position);

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		toggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		toggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onWelComeButtonClick() {
		dLayout.openDrawer(rlDrawerOpen);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvFacbook:
			Intent i = new Intent(ActivityMain.this, ShowFacebook.class);
			startActivity(i);
			break;

		case R.id.tvRateUs:
			String linkurl = "http://play.google.com/store/apps/details?id=com.coffeecodeandcreativity.topshoutcast";
			if (linkurl != null) {
				Intent shareIntent = new Intent(Intent.ACTION_SEND);
				shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				shareIntent.putExtra(Intent.EXTRA_TEXT, linkurl);
				shareIntent.setType("text/plain");
				startActivity(shareIntent);
			} else {
				Toast.makeText(getApplicationContext(), "Sharing failed...",
						Toast.LENGTH_LONG).show();
			}

			break;
		}

	}

}
