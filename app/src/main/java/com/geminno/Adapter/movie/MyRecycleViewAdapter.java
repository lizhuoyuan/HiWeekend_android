package com.geminno.Adapter.movie;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.geminno.Bean.Movie;
import com.geminno.Utils.ImageUtils;

import java.util.ArrayList;

import geminno.com.hiweek_android.R;

public class MyRecycleViewAdapter extends Adapter<MyViewHolder> implements
	Filterable {
    private LayoutInflater inflater;
    private ArrayList<Movie> movies;
    private ImageUtils imageUtils;
    private Point point;
    private Context context;
    private int length;
    private int width;
    private ArrayList<Movie> originaList;

    public MyRecycleViewAdapter(Point point, Context context,
	    ArrayList<Movie> movies) {
	this.point = point;
	this.context = context;
	inflater = LayoutInflater.from(context);
	this.movies = movies;
	imageUtils = ImageUtils.getInstence();
	imageUtils.setQueue(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
	View view = inflater.inflate(R.layout.item, parent, false);
	MyViewHolder viewHolder = new MyViewHolder(view);
	return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
	imageUtils.loadImageUseVolley_ImageLoad(holder.getImageView(), movies
		.get(position).getM_poster());
	String name = movies.get(position).getM_name();
	length = name.length();
	width = point.x / 4;
	TextView textView = holder.getTextView();
	textView.setText(name);
	textView.setWidth(width);
	float defaultsize = textView.getTextSize();
	if (defaultsize * length > width) {
	    defaultsize = width / length;
	}
	textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultsize);
	holder.itemView.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		Intent intent = new Intent("com.geminno.movieselected");
		intent.putExtra("movieID", movies.get(position).getM_id());
		intent.putExtra("imageUrl", movies.get(position).getM_poster());
		sendLocalBrodcast(intent);
	    }
	});
    }

    @Override
    public int getItemCount() {
	return movies.size();
    }

    // // 接口
    // public interface onImageClickedListener {
    // void onImageClicked(int id);
    // }
    //
    // private onImageClickedListener listener;
    //
    // public void setOnImageClickedListener(onImageClickedListener listener) {
    // this.listener = listener;
    // }

    private void sendLocalBrodcast(Intent intent) {
	LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
		.getInstance(context);
	localBroadcastManager.sendBroadcast(intent);
    }

    @Override
    public Filter getFilter() {
	Filter filter = new Filter() {

	    @Override
	    protected void publishResults(CharSequence constraint,
		    FilterResults results) {
		if (listener != null) {
		    listener.onMovieResult((ArrayList<Movie>) results.values);
		}

	    }

	    @Override
	    protected FilterResults performFiltering(CharSequence constraint) {
		FilterResults filterResults = new FilterResults();
		ArrayList<Movie> filteredlArrayList = new ArrayList<Movie>();
		originaList = new ArrayList<Movie>(movies);

		if (TextUtils.isEmpty(constraint.toString().trim())) {
		    filterResults.count = originaList.size();
		    filterResults.values = originaList;
		} else {
		    for (Movie movie : originaList) {
			if (movie.getM_name().contains(constraint)) {
			    filteredlArrayList.add(movie);
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

    public interface onSearchMovieListener {
	void onMovieResult(ArrayList<Movie> moviess);
    }

    public void setOnSearchMovieListener(onSearchMovieListener listener) {
	this.listener = listener;
    }

    private onSearchMovieListener listener;
}

class MyViewHolder extends ViewHolder {
    private ImageView imageView;
    private TextView textView;

    public MyViewHolder(View itemView) {
	super(itemView);
	imageView = (ImageView) itemView.findViewById(R.id.movie_image);
	textView = (TextView) itemView.findViewById(R.id.movie_name);
    }

    public ImageView getImageView() {
	return imageView;
    }

    public void setImageView(ImageView imageView) {
	this.imageView = imageView;
    }

    public TextView getTextView() {
	return textView;
    }

    public void setTextView(TextView textView) {
	this.textView = textView;
    }

}