package com.geminno.Adapter.home;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.geminno.Activities.activity.ZhangZiSshiActivity;
import com.geminno.Bean.Lunbo;

import java.util.ArrayList;

import geminno.com.hiweek_android.R;

public class MyPagerAdapter extends PagerAdapter {
	Context context;
	ArrayList<Lunbo> al;
	
	public MyPagerAdapter(Context context, ArrayList<Lunbo> al) {
		this.context = context;
		this.al = al;
	}
	@Override
	public int getCount() {
		// 返回多少page
		return Integer.MAX_VALUE;
	}
	/**
	 * 返回true：表示不去创建，使用缓存 false：去重新创建
	 * view:当前滑动的view
	 * object：将要进入的新创建的view，由instantiateItem方法创建
	 */
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0==arg1;
	}
	/**
	 * 类似于BaseAdapetr的getview方法 用来将数据设置给view
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = View.inflate(context, R.layout.item_lunbo, null);
		ImageView img = (ImageView) view.findViewById(R.id.imageView1);
		img.setImageResource(al.get(position%al.size()).getIconResid());
		container.addView(view); //一定不能少，将view加入viewpager当中
		img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentActivity = new Intent(context, ZhangZiSshiActivity.class);
					intentActivity.putExtra("home", 2);
					context.startActivity(intentActivity);
			}
		});
		return view;

	}

	/**
	 * 销毁page position:当前需要销毁第几个page object：当前需要销毁的page
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
}
