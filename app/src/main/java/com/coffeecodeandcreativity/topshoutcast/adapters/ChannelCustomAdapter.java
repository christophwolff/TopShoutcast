package com.coffeecodeandcreativity.topshoutcast.adapters;

import java.util.List;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.coffeecodeandcreativity.topshoutcast.R;

public class ChannelCustomAdapter extends ArrayAdapter<Channels> {

	Activity context;
	List<Channels> list;
	View rowView = null;
	Typeface font, selectFonts;

	public ChannelCustomAdapter(Activity context, List<Channels> list) {

		super(context, R.layout.channel_row, list);
		this.context = context;
		this.list = list;
		font = Typeface.createFromAsset(context.getAssets(),
				"fonts/Roboto-Light.ttf");
		selectFonts = (Typeface.createFromAsset(context.getAssets(),
				"fonts/Roboto-Bold.ttf"));

	}

	public static class ViewHolder {

		TextView tv;
        TextView tv_genre;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.channel_row, parent, false);
			ViewHolder holder = new ViewHolder();

			holder.tv = (TextView) rowView.findViewById(R.id.tv_channel_name);
			holder.tv.setTag(list.get(position));

            holder.tv_genre = (TextView) rowView.findViewById(R.id.tv_channel_genre);
            holder.tv_genre.setTag(list.get(position));

			rowView.setTag(holder);

		} else {

			rowView = convertView;
			((ViewHolder) rowView.getTag()).tv.setTag(list.get(position));
            ((ViewHolder) rowView.getTag()).tv_genre.setTag(list.get(position));

		}

		ViewHolder viewHolder = (ViewHolder) rowView.getTag();

		viewHolder.tv.setTypeface(font);
        viewHolder.tv_genre.setTypeface(font);

		viewHolder.tv.setText(list.get(position).getChannelName());
        viewHolder.tv_genre.setText(list.get(position).getGenre());

		return rowView;
	}

}
