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
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.geminno.Application.MyApplication;
import com.geminno.Bean.CinemaForMovie;

import java.text.DecimalFormat;
import java.util.ArrayList;

import geminno.com.hiweek_android.R;

/**
 * 
 * @ClassName: CinemaforMovieListAdapter
 * @Description: TODO
 * @author: XU
 * @date: 2015年10月19日 下午6:33:41
 */
public class CinemaforMovieListAdapter extends BaseAdapter implements
	Filterable {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<CinemaForMovie> cForMovies;
    Animation animation;
    private LatLng start;
    private LatLng end;
    private DecimalFormat df;

    public CinemaforMovieListAdapter(ArrayList<CinemaForMovie> cinemaInfos,
	    Context context) {
	this.context = context;
	this.cForMovies = cinemaInfos;
	inflater = LayoutInflater.from(context);
	animation = AnimationUtils.loadAnimation(context, R.anim.item_anim);
	start = new LatLng(MyApplication.getLat(), MyApplication.getLon());
	df = new DecimalFormat("#.00");

    }

    @Override
    public int getCount() {
	// TODO Auto-generated method stub
	return cForMovies.size();
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
	ViewHolder vHolder = null;
	if (convertView == null) {
	    vHolder = new ViewHolder();
	    convertView = inflater.inflate(R.layout.cinema_for_movie_listi_tem,
		    null);
	    vHolder.cinema_name = (TextView) convertView
		    .findViewById(R.id.cinema_Name);
	    vHolder.cinema_dist = (TextView) convertView
		    .findViewById(R.id.cinema_Dist);
	    convertView.setTag(vHolder);

	} else {
	    vHolder = (ViewHolder) convertView.getTag();
	}
	end = new LatLng(cForMovies.get(position).getLatitude(), cForMovies
		.get(position).getLongitude());
	vHolder.cinema_name.setText(cForMovies.get(position).getCinemaName());
	// vHolder.cinema_dist.setText(cForMovies.get(position).get);
	vHolder.cinema_dist.setText(df.format(DistanceUtil.getDistance(start,
		end) / 1000) + " km");
	convertView.startAnimation(animation);
	return convertView;
    }

    private static class ViewHolder {
	TextView cinema_name;
	TextView cinema_dist;
    }

    @Override
    public Filter getFilter() {
	Filter filter = new Filter() {

	    private ArrayList<CinemaForMovie> originaList;

	    @Override
	    protected void publishResults(CharSequence constraint,
		    FilterResults results) {
		cForMovies = (ArrayList<CinemaForMovie>) results.values;
		int count = cForMovies.size();
		System.out.println(count);
		notifyDataSetChanged();
	    }

	    @Override
	    protected FilterResults performFiltering(CharSequence constraint) {
		FilterResults filterResults = new FilterResults();
		ArrayList<CinemaForMovie> filteredlArrayList = new ArrayList<CinemaForMovie>();
		if (originaList == null) {
		    originaList = new ArrayList<CinemaForMovie>(cForMovies);

		}
		if (TextUtils.isEmpty(constraint.toString().trim())) {
		    filterResults.count = originaList.size();
		    filterResults.values = originaList;
		} else {
		    for (CinemaForMovie cinemaForMovie : originaList) {
			if (cinemaForMovie.getCinemaName().contains(constraint)) {
			    filteredlArrayList.add(cinemaForMovie);
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
