package com.geminno.Adapter.movie;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.geminno.Bean.Movie;
import com.geminno.Resolver.MyContentResolver;
import com.geminno.Utils.ImageUtils;
import com.geminno.columns.Cloumns.MovieCloumns;

import java.util.ArrayList;

import geminno.com.hiweek_android.R;

/**
 * 
 * @ClassName MyPagerAdapter
 * @Description viewpager的适配器
 * @author XU
 * @date 2015年10月11日
 */
public class MoviePagerAdapter extends PagerAdapter {
    private ArrayList<Movie> movies;
    private Context context;
    private LayoutInflater inflater;
    private MyContentResolver resolver;
    private Movie movie;
    ImageUtils imageUtils;

    public MoviePagerAdapter(Context context) {
	this.context = context;
	this.inflater = LayoutInflater.from(context);
	resolver = new MyContentResolver(context);
	imageUtils = ImageUtils.getInstence();
	imageUtils.setQueue(context);

	getMovies();

    }

    private void getMovies() {
	Cursor cursor = resolver.selectMovies(new String[] { MovieCloumns.NAME,
		MovieCloumns.POSTER, MovieCloumns._ID }, null, null);

	movies = new ArrayList<Movie>();
	while (cursor.moveToNext()) {
	    movie = new Movie();
	    movie.setM_name(cursor.getString(cursor
		    .getColumnIndex(MovieCloumns.NAME)));
	    movie.setM_poster(cursor.getString(cursor
		    .getColumnIndex(MovieCloumns.POSTER)));
	    movie.setM_id(cursor.getInt(cursor.getColumnIndex(MovieCloumns._ID)));
	    movies.add(movie);
	}
	if (cursor != null) {
	    cursor.close();
	    cursor = null;
	}
    }

    @Override
    public int getCount() {
	// TODO Auto-generated method stub
	return movies.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
	// TODO Auto-generated method stub
	return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

	container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
	movie = movies.get(position);
	View view = inflater.inflate(R.layout.pager_image, null);
	ImageView imageView = (ImageView) view.findViewById(R.id.iv);
	TextView textView = (TextView) view.findViewById(R.id.tv);
	imageView.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		Intent intent = new Intent("com.geminno.movieselected");
		intent.putExtra("movieID", movies.get(position).getM_id());
		intent.putExtra("imageUrl", movies.get(position).getM_poster());
		sendLocalBrodcast(intent);
	    }
	});
	imageUtils.loadImageUseVolley_ImageLoad(imageView, movie.getM_poster());
	textView.setText(movie.getM_name());
	container.addView(view);
	return view;
    }

    private void sendLocalBrodcast(Intent intent) {
	LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
		.getInstance(context);
	localBroadcastManager.sendBroadcast(intent);
    }

}
