package com.geminno.Adapter.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import geminno.com.hiweek_android.R;


/**
 * @author 李卓原 创建时间：2015年10月23日 上午9:47:51
 * 
 */
public class Gridadapter extends BaseAdapter {
    String[] al;
    private Context context;
    LayoutInflater inflater;

    public Gridadapter(String[] al, Context context) {
	// TODO Auto-generated constructor stub
	this.al = al;
	this.context = context;
	inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
	// TODO Auto-generated method stub
	return al.length;
    }

    @Override
    public Object getItem(int position) {
	// TODO Auto-generated method stub
	return al[position];
    }

    @Override
    public long getItemId(int position) {
	// TODO Auto-generated method stub
	return position;
    }

    @SuppressLint({ "ViewHolder", "InflateParams" })
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	if (position >= 0 && position <= 5) {
	    convertView = inflater.inflate(R.layout.grid147, null);
	} else {
	    convertView = inflater.inflate(R.layout.gridview_layout, null);
	}

	TextView tv = (TextView) convertView.findViewById(R.id.textView1);
	tv.setText(al[position]);
	return convertView;
    }

}
