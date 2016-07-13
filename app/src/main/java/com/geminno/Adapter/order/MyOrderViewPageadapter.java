package com.geminno.Adapter.order;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
/**
 * 是我的订单里面viewpage的适配器
 * @author 郑雅倩
 *
 */
public class MyOrderViewPageadapter extends FragmentPagerAdapter {
	ArrayList<Fragment> frags;
	public MyOrderViewPageadapter(FragmentManager fragmentManager,ArrayList<Fragment> frags) {
		super(fragmentManager);
		this.frags=frags;
	}


	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
	}


	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		return super.instantiateItem(container, position);
	}


	@Override
	public Fragment getItem(int pos) {
		return frags.get(pos);
	}

	@Override
	public int getCount() {
		return frags.size();
	}
	
}
