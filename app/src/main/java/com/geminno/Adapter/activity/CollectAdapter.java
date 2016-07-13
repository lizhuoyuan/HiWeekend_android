package com.geminno.Adapter.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.geminno.Bean.Collect;

import java.util.ArrayList;

import geminno.com.hiweek_android.R;

/**
 * @author 李卓原 创建时间：2015年10月27日 下午7:48:50
 * 
 */
public class CollectAdapter extends BaseAdapter {
	ArrayList<Collect> collect;
	LayoutInflater inflater;
	Context context;

	public CollectAdapter(ArrayList<Collect> collect, Context context) {
		this.collect = collect;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return collect.size();
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

	public class ViewHolder {
		TextView tvname;
		TextView tvtime;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.collect_imte, null);
			holder.tvname = (TextView) convertView.findViewById(R.id.ttitemname);
			holder.tvtime = (TextView) convertView.findViewById(R.id.collecttime);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvname.setText(collect.get(position).getA_itemname());
		holder.tvtime.setText(collect.get(position).getCe_time());
		return convertView;
	}
}
