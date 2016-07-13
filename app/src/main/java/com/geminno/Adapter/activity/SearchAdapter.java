package com.geminno.Adapter.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.geminno.Bean.Action;
import com.geminno.Utils.MyPropertiesUtil;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.Properties;

import geminno.com.hiweek_android.R;

/**
 * @author 李卓原 创建时间：2015年10月23日 下午10:02:48
 * 
 */
public class SearchAdapter extends BaseAdapter {
    ArrayList<Action> ac;
    Context context;
    LayoutInflater inflater;

    public SearchAdapter(Context context, ArrayList<Action> ac) {
	this.context = context;
	this.ac = ac;
	inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
	// TODO Auto-generated method stub
	return ac.size();
    }

    @Override
    public Object getItem(int position) {
	// TODO Auto-generated method stub
	return ac.get(position);
    }

    @Override
    public long getItemId(int position) {
	// TODO Auto-generated method stub
	return position;
    }

    public static class ViewHolder {
	TextView tv;
	ImageView iv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	ViewHolder holder;
	if (convertView == null) {
	    holder = new ViewHolder();
	    convertView = inflater.inflate(R.layout.search_list, null);
	    holder.tv = (TextView) convertView.findViewById(R.id.textView1);
	    holder.iv = (ImageView) convertView.findViewById(R.id.image);
	    convertView.setTag(holder);
	} else {
	    holder = (ViewHolder) convertView.getTag();
	}
	holder.tv.setText(ac.get(position).getA_itemname());
	Properties p = MyPropertiesUtil.getProperties(context);
	String ip = p.getProperty("url");
	String imgurl = ip + "/HiWeek/ActionPhotos/5/"
		+ ac.get(position).getA_photo();
	BitmapUtils bitmapUtils = new BitmapUtils(context);

	// 加载网络图片
	bitmapUtils.display(holder.iv, imgurl);
	return convertView;
    }

}
