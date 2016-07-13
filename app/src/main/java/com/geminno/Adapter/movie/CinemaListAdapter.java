package com.geminno.Adapter.movie;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RatingBar;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.geminno.Application.MyApplication;
import com.geminno.Bean.Cinema;

import java.text.DecimalFormat;
import java.util.ArrayList;

import geminno.com.hiweek_android.R;

/**
 * 
 * @ClassName MyListAdapter
 * @Description ListView的适配器
 * @author XU
 * @date 2015年10月11日
 */
public class CinemaListAdapter extends BaseAdapter implements Filterable {
    private ArrayList<Cinema> cinemas;
    private LayoutInflater inflater;
    private Cinema cinema;
    private Animation animation;
    private LatLng start;
    private DistanceUtil util;
    private LatLng end;
    private DecimalFormat df;
    private ArrayList<Cinema> originaList;
    private ArrayList<Cinema> tempArrayList;

    public CinemaListAdapter(Context context, ArrayList<Cinema> mcinemas) {
	inflater = LayoutInflater.from(context);
	animation = AnimationUtils.loadAnimation(context, R.anim.item_anim);
	this.cinemas = mcinemas;
	start = new LatLng(MyApplication.getLat(), MyApplication.getLon());
	df = new DecimalFormat("#.00");
	tempArrayList = mcinemas;
    }

    @Override
    public int getCount() {
	// TODO Auto-generated method stub
	return cinemas.size();
    }

    @Override
    public Object getItem(int position) {
	// TODO Auto-generated method stub
	return cinemas.get(position);
    }

    @Override
    public long getItemId(int position) {
	// TODO Auto-generated method stub
	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	viewHolder vHolder = null;
	if (convertView == null) {
	    convertView = inflater.inflate(R.layout.list_item, null);

	    vHolder = new viewHolder();
	    vHolder.cinema_name = ((TextView) convertView
		    .findViewById(R.id.cinema_name));
	    vHolder.cinema_dist = ((TextView) convertView
		    .findViewById(R.id.cinema_dist));

	    vHolder.cinema_avg_price = ((TextView) convertView
		    .findViewById(R.id.cinema_avg_price));
	    vHolder.rBar = ((RatingBar) convertView
		    .findViewById(R.id.cinema_rat));
	    convertView.setTag(vHolder);
	} else {

	    vHolder = (viewHolder) convertView.getTag();
	}
	// 设置数据
	cinema = cinemas.get(position);

	vHolder.cinema_name.setText(cinema.getC_name());
	end = new LatLng(cinema.getC_lat(), cinema.getC_lon());
	vHolder.cinema_dist.setText(df.format(DistanceUtil.getDistance(start,
		end) / 1000) + " km");
	vHolder.cinema_avg_price.setText("40.0元起");
	float grade = cinema.getC_grade();
	vHolder.rBar.setRating(grade);
	convertView.startAnimation(animation);

	return convertView;
    }

    private class viewHolder {
	private TextView cinema_name;
	private TextView cinema_dist;
	private TextView cinema_avg_price;
	private RatingBar rBar;

    }

    // 接口
    public interface onCinemaSearchListener {
	void onCinemaResult(ArrayList<Cinema> cinemas);
    }

    public void setOnCinemaSearchListener(onCinemaSearchListener listener) {
	this.listener = listener;
    }

    private onCinemaSearchListener listener;

    @Override
    public Filter getFilter() {
	Filter filter = new Filter() {

	    @Override
	    protected void publishResults(CharSequence constraint,
		    FilterResults results) {
		if (listener != null) {
		    listener.onCinemaResult((ArrayList<Cinema>) results.values);
		}
	    }

	    @Override
	    protected FilterResults performFiltering(CharSequence constraint) {
		FilterResults filterResults = new FilterResults();
		ArrayList<Cinema> filteredlArrayList = new ArrayList<Cinema>();

		originaList = new ArrayList<Cinema>(cinemas);

		if (TextUtils.isEmpty(constraint.toString().trim())) {
		    filterResults.count = originaList.size();
		    filterResults.values = originaList;
		} else {
		    for (Cinema cinema : originaList) {
			if (cinema.getC_name().contains(constraint)) {
			    filteredlArrayList.add(cinema);
			}
		    }
		    filterResults.count = filteredlArrayList.size();
		    filterResults.values = filteredlArrayList;
		}
		return filterResults;
	    }
	};
	return filter;
    }
}
