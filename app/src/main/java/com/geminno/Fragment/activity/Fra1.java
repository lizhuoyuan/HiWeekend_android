package com.geminno.Fragment.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.geminno.Adapter.activity.MyAdapter;
import com.geminno.Bean.Action;
import com.geminno.Utils.GetPostUtil;
import com.geminno.Utils.MyPropertiesUtil;
import com.geminno.View.ZhangZiShiListView;
import com.geminno.View.ZhangZiShiListView.OnRefreshListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import geminno.com.hiweek_android.R;

/**
 * 涨姿势
 * 
 * @author 李卓原 创建时间：2015年10月12日 下午2:07:05 郑雅倩 修改时间 2015年11月14日
 */

public class Fra1 extends Fragment implements OnRefreshListener, OnItemClickListener, OnQueryTextListener {
	ArrayList<Action> actions;
	int a_id;
	MyAdapter adapter;
	ZhangZiShiListView lv;
	int pageNO = 1;
	boolean falg = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getActivity().getActionBar().setTitle("涨姿势");
		lv = new ZhangZiShiListView(getActivity());
		GetAction();
		lv.setOnItemClickListener(this);
		return lv;
	}

	Handler strarthandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 2) {
				Gson gson = new Gson();
				Type type = new TypeToken<ArrayList<Action>>() {
				}.getType();
				actions = gson.fromJson(msg.obj.toString(), type);
				adapter = new MyAdapter(getActivity(), actions);
				lv.setAdapter(adapter);
				lv.setOnRefreshListener(Fra1.this);
			}
		}

	};

	public void GetAction() {
		GetPostUtil.sendGet(
				MyPropertiesUtil.getProperties(getActivity()).getProperty("url") + "/HiWeek/servlet/LiActionAll",
				"page=" + 1, strarthandler);
	}

	@Override
	public void onPullRefresh() {
		falg = false;
		pageNO = 1;
		GetPostUtil.sendGet(
				MyPropertiesUtil.getProperties(getActivity()).getProperty("url") + "/HiWeek/servlet/LiActionAll",
				"page=" + 1, pullhandler);
	}

	@Override
	public void onLoadingMore() {
		GetPostUtil.sendGet(
				MyPropertiesUtil.getProperties(getActivity()).getProperty("url") + "/HiWeek/servlet/LiActionAll",
				"page=" + (++pageNO), loadhandler);
		System.out.println("page+" + pageNO);
	}

	Handler pullhandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 2) {
				falg = true;
				Gson gson = new Gson();
				Type type = new TypeToken<ArrayList<Action>>() {
				}.getType();
				actions = gson.fromJson((String) msg.obj, type);
				adapter = new MyAdapter(getActivity(), actions);
				lv.setAdapter(adapter);
				// adapter.notifyDataSetChanged();
				lv.completeRefresh();
			}
		}

	};

	Handler loadhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 2) {
				if (msg.obj.equals("数据加载完成")) {
					Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
					// adapter.notifyDataSetChanged();
					lv.completeRefresh();
				} else {
					Gson gson = new Gson();
					Type type = new TypeToken<ArrayList<Action>>() {
					}.getType();
					ArrayList<Action> newactions = gson.fromJson((String) msg.obj, type);
					actions.addAll(newactions);
					adapter = new MyAdapter(getActivity(), actions);
					adapter.notifyDataSetChanged();
					lv.completeRefresh();
				}
			}
		};
	};
	private SearchView searchView;

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (falg) {

			// System.out.println(actions.get(position));
			Bundle bundle = new Bundle();
			bundle.putSerializable("action", actions.get(position - 1));
			Fra2 f2 = new Fra2();
			f2.setArguments(bundle);
			getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.one_fragment, f2).commit();

		}
	}

	/*
	 * @Override public void onListItemClick(ListView l, View v, int position,
	 * long id) { System.out.println(actions.get(position)); Bundle bundle = new
	 * Bundle(); bundle.putSerializable("action", actions.get(position)); Fra2
	 * f2 = new Fra2(); f2.setArguments(bundle);
	 * getFragmentManager().beginTransaction().addToBackStack(null)
	 * .replace(R.id.one_fragment, f2).commit(); }
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.activity_main_menu, menu);
		searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
		searchView.setOnQueryTextListener(this);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		if (actions.size() <= 0) {
			return false;
		}
		if (newText.length() != 0) {
			adapter.getFilter().filter(newText);
		} else {
			lv.clearTextFilter();
		}
		return false;
	}
}
