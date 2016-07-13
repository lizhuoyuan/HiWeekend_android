package com.geminno.Adapter.movie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.geminno.Bean.MovieInfo;
import com.geminno.Utils.ImageUtils;
import com.google.android.support.v4.view.PagerAdapter;

import java.util.ArrayList;

import geminno.com.hiweek_android.R;

public class CinemaInfo_MoviePagerAdapter extends PagerAdapter {
    private static final String TAG = "CinemaInfo";
    private int cinemaID;
    private ArrayList<MovieInfo> movies;
    private LayoutInflater inflater;
    private Context context;
    private int currentPosition;
    private int tempPosition;

    public CinemaInfo_MoviePagerAdapter(ArrayList<MovieInfo> movieInfos,
	    Context context) {
	this.context = context;
	inflater = LayoutInflater.from(context);
	this.movies = movieInfos;
    }

    @Override
    public int getCount() {
	// TODO Auto-generated method stub
	return movies.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
	// TODO Auto-generated method stub
	return arg0 == (View) arg1;
    }

    @Override
    public void startUpdate(ViewGroup container) {
	tempPosition = currentPosition;
	super.startUpdate(container);
    }

    @Override
    public int getItemPosition(Object object) {
	return super.getItemPosition(object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
	if (currentPosition != tempPosition)
	    super.finishUpdate(container);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

	View view = inflater.inflate(R.layout.pager_adapter_view, null);

	ImageView imageView = (ImageView) view.findViewById(R.id.movie_image);
	ImageUtils.getInstence().setQueue(context);
	ImageUtils.getInstence().loadImageUseVolley_ImageLoad(imageView,
		movies.get(position).getPic_url());
	imageView.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		if (listener != null) {
		    listener.onPageClicked();
		}
	    }
	});
	container.addView(view);

	return view;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
	// ((View)
	// object).findViewById(R.id.RelativeLayout1).setBackgroundColor(
	// Color.WHITE);

	super.setPrimaryItem(container, position, object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
	System.out.println("destroyItem" + position);
	container.removeView((View) object);
    }

    // 接口
    public interface onPageClickedListener {
	void onPageClicked();
    }

    public void setOnPageClickedListener(onPageClickedListener listener) {
	this.listener = listener;
    };

    private onPageClickedListener listener;
}
