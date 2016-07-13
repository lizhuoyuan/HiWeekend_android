package com.geminno.Adapter.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geminno.Bean.Quiz;
import com.geminno.Utils.MyPropertiesUtil;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.Properties;

import geminno.com.hiweek_android.R;

public class QuesAdapter extends BaseAdapter {
	ArrayList<Quiz> qz;
	Context context;
	LayoutInflater inflater;

	public QuesAdapter(ArrayList<Quiz> qz, Context context) {
		this.qz = qz;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return qz.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return qz.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public class ViewHolder {
		TextView tvname;
		TextView tvtime;
		TextView tvcont;
		ImageView imgHead;
		TextView tvanswer;
		LinearLayout layout;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_question, null);
			holder.tvname = (TextView) convertView.findViewById(R.id.uname);
			holder.tvtime = (TextView) convertView.findViewById(R.id.time);
			holder.tvcont = (TextView) convertView.findViewById(R.id.cont);
			holder.tvanswer = (TextView) convertView.findViewById(R.id.tvanswer);
			holder.imgHead = (ImageView) convertView.findViewById(R.id.touxiang);
			holder.layout = (LinearLayout) convertView.findViewById(R.id.mer);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvname.setText(qz.get(position).getUser().getU_name());
		holder.tvtime.setText(qz.get(position).getQ_time());
		holder.tvcont.setText(qz.get(position).getQ_cont());
		holder.tvanswer.setText(qz.get(position).getQ_answer());
		if (holder.tvanswer.getText().toString().isEmpty()) {
			holder.layout.setVisibility(View.GONE);
		} else {
			holder.layout.setVisibility(View.VISIBLE);
		}
		Properties p = MyPropertiesUtil.getProperties(context);
		// String imgurl = MyApplication.getPic_url();
		String imgurl = p.getProperty("url") + "/HiWeek/Image/" + qz.get(position).getUser().getU_pic();
		System.out.println("-------" + qz.get(position).getUser().getU_pic() + "---------");
		BitmapUtils bitmapUtils = new BitmapUtils(context);
		// 加载网络图片
		bitmapUtils.display(holder.imgHead, imgurl);
		return convertView;
	}

}
