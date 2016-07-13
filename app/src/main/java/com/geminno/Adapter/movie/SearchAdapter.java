package com.geminno.Adapter.movie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.geminno.Bean.SearchResult;

import java.util.ArrayList;

import geminno.com.hiweek_android.R;

public class SearchAdapter extends BaseAdapter {
	private ArrayList<SearchResult> results;
	private Context context;
	private LayoutInflater inflater;

	public SearchAdapter(Context context, ArrayList<SearchResult> results) {
		this.results = results;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return results.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		VHolder vHolder;
		if (convertView == null) {
			vHolder = new VHolder();
			convertView = inflater.inflate(R.layout.search_list_item, null);
			vHolder.type_image = (ImageView) convertView.findViewById(R.id.type_image);
			vHolder.title = (TextView) convertView.findViewById(R.id.title_name);
			convertView.setTag(vHolder);
		} else {
			vHolder = (VHolder) convertView.getTag();
		}
		if (results.get(position).getType() == SearchResult.CINEMA) {

			vHolder.type_image.setImageDrawable(context.getResources().getDrawable(R.drawable.cinema_lable));
		} else {
			vHolder.type_image.setImageDrawable(context.getResources().getDrawable(R.drawable.movie_lable));
		}
		vHolder.title.setText(results.get(position).getTitle());
		return convertView;
	}

	private class VHolder {
		private ImageView type_image;
		private TextView title;
	}
}
