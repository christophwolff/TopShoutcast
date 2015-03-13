package com.coffeecodeandcreativity.topshoutcast.adapters;

import java.util.List;

import com.coffeecodeandcreativity.topshoutcast.R;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class SearchCursorAdapter extends CursorAdapter {

	private List<Channels> items;

	private TextView text;

	public SearchCursorAdapter(Context context, Cursor cursor,
			List<Channels> items) {

		super(context, cursor, false);

		this.items = items;

	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		// Show list item data from cursor
		text.setText(items.get(cursor.getPosition()).getChannelName());

		// Alternatively show data direct from database
		// text.setText(cursor.getString(cursor.getColumnIndex("text")));

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view = inflater.inflate(R.layout.search_item, parent, false);

		text = (TextView) view.findViewById(R.id.text);

		return view;

	}

}
