package com.geminno.Adapter.movie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.geminno.Bean.Discuss;
import com.geminno.Service.InternetService;
import com.geminno.Utils.ImageUtils;
import com.geminno.View.CircularImage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import geminno.com.hiweek_android.R;

public class CommentAdapter extends BaseAdapter {
    private ArrayList<Discuss> discusses;
    private LayoutInflater inflater;
    private SimpleDateFormat format;
    private LayoutParams layoutParams;
    private Context context;
    private ImageUtils imageUtils;
    private static String url = "http://" + InternetService.IP
	    + ":8080/HiWeek/Image/";
    private int width;

    public CommentAdapter(Context context, ArrayList<Discuss> discusses) {
	this.discusses = discusses;
	this.context = context;
	inflater = LayoutInflater.from(context);
	format = new SimpleDateFormat("yyyy-mm-dd");
	imageUtils = ImageUtils.getInstence();
	imageUtils.setQueue(context);
    }

    @Override
    public int getCount() {
	return discusses.size();
    }

    @Override
    public Object getItem(int position) {
	return position;
    }

    @Override
    public long getItemId(int position) {
	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	viewHolder vHolder = null;
	if (convertView == null) {
	    vHolder = new viewHolder();
	    convertView = inflater.inflate(R.layout.comment_ist_item, null);
	    vHolder.user_name = (TextView) convertView
		    .findViewById(R.id.U_Name);
	    vHolder.comment = (TextView) convertView
		    .findViewById(R.id.U_Comment);
	    vHolder.date = (TextView) convertView
		    .findViewById(R.id.comment_date);
	    vHolder.imageView = (CircularImage) convertView
		    .findViewById(R.id.U_image);
	    vHolder.ratingBar = (RatingBar) convertView
		    .findViewById(R.id.U_grade);
	    convertView.setTag(vHolder);
	} else {
	    vHolder = (viewHolder) convertView.getTag();
	}
	vHolder.user_name
		.setText(discusses.get(position).getUser().getU_name());
	vHolder.comment.setText(discusses.get(position).getD_cont());
	String date = null;
	try {
	    date = format.format(format.parse(discusses.get(position)
		    .getD_time()));
	} catch (ParseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	imageUtils.loadImageUseVolley_ImageLoad(vHolder.imageView, url
		+ discusses.get(position).getUser().getU_pic());
	vHolder.date.setText(date);
	vHolder.ratingBar.setRating(discusses.get(position).getM_grade());

	return convertView;
    }

    private class viewHolder {
	TextView user_name;
	TextView comment;
	TextView date;
	RatingBar ratingBar;
	CircularImage imageView;
    }

    public int Dp2Px(Context context, float dp) {
	final float scale = context.getResources().getDisplayMetrics().density;
	return (int) (dp * scale + 0.5f);
    }

    public int Px2Dp(Context context, float px) {
	final float scale = context.getResources().getDisplayMetrics().density;
	return (int) (px / scale + 0.5f);
    }

}
