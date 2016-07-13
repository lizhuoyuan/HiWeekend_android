package com.geminno.Adapter.movie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.geminno.Bean.Broadcast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import geminno.com.hiweek_android.R;

public class Row_of_tablets_listAdapter extends BaseAdapter {
    private ArrayList<Broadcast> broadcasts;
    private LayoutInflater inflater;
    private BookClickedListener listener;
    private SimpleDateFormat simpleDateFormat;
    private long time;

    public Row_of_tablets_listAdapter(ArrayList<Broadcast> broadcast,
	    Context context) {
	this.broadcasts = broadcast;
	inflater = LayoutInflater.from(context);
	simpleDateFormat = new SimpleDateFormat("HH:mm");
    }

    @Override
    public int getCount() {
	// TODO Auto-generated method stub
	return broadcasts.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
	Broadcast broadcast = broadcasts.get(position);

	ViewHolder vHolder = null;
	if (convertView == null) {
	    vHolder = new ViewHolder();
	    convertView = inflater.inflate(R.layout.movie_table, null);
	    vHolder.movie_start_time = (TextView) convertView
		    .findViewById(R.id.movie_start_time);
	    vHolder.movie_office = (TextView) convertView
		    .findViewById(R.id.movie_office);
	    vHolder.movie_price = (TextView) convertView
		    .findViewById(R.id.movie_price);
	    vHolder.book_ticket = (TextView) convertView
		    .findViewById(R.id.book_ticket);
	    vHolder.movie_end_time = (TextView) convertView
		    .findViewById(R.id.movie_end_time);
	    vHolder.movie_language = (TextView) convertView
		    .findViewById(R.id.movie_language);
	    convertView.setTag(vHolder);

	} else {
	    vHolder = (ViewHolder) convertView.getTag();

	}
	vHolder.movie_office.setText(broadcast.getHall());
	vHolder.movie_price.setText("￥" + broadcast.getPrice());
	vHolder.movie_start_time.setText(broadcast.getTime());
	vHolder.movie_language.setText("国语2D");
	Date oldDate = null;
	try {
	    oldDate = simpleDateFormat.parse(broadcast.getTime());
	} catch (ParseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	oldDate.setTime(oldDate.getTime() + 120 * 60 * 1000);
	vHolder.movie_end_time.setText(simpleDateFormat.format(oldDate));
	vHolder.book_ticket.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		Click(position);

	    }

	});
	return convertView;
    }

    private class ViewHolder {
	TextView movie_start_time;
	TextView movie_office;
	TextView movie_price;
	TextView book_ticket;
	TextView movie_end_time;
	TextView movie_language;
    }

    // 定义接口
    public interface BookClickedListener {
	void onBookTicket(int position);
    }

    // 绑定回调
    public void setOnBookClickedListener(BookClickedListener listener) {
	this.listener = listener;
    }

    private void Click(int position) {
	if (listener != null) {
	    listener.onBookTicket(position);
	}
    }
}
