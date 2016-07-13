package com.geminno.Fragment.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.geminno.Adapter.activity.SearchAdapter;
import com.geminno.Bean.Action;
import com.geminno.Utils.MyPropertiesUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Properties;

import geminno.com.hiweek_android.R;

/**
 * @author 李卓原 创建时间：2015年10月24日 上午11:31:47
 * 
 */
public class SearchFragment extends Fragment {
    private TextView tv;
    private ListView lv;
    ArrayList<Action> actions;
    String search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	getActivity().getActionBar().setTitle("搜索结果");

	View root = inflater.inflate(R.layout.activity_search, null);
	lv = (ListView) root.findViewById(R.id.searchlist);
	tv = (TextView) root.findViewById(R.id.tvsearch);
	getsearchResult();
	return root;
    }

    private void getsearchResult() {
	HttpUtils http = new HttpUtils();
	Properties p = MyPropertiesUtil.getProperties(getActivity());
	String ip = p.getProperty("url");
	search = getArguments().getString("search");
	String url = ip + "/HiWeek/servlet/SearchServlet?search=" + search;
	http.send(HttpMethod.GET, url, new RequestCallBack<String>() {

	    @Override
	    public void onFailure(HttpException arg0, String arg1) {
		tv.setText("对不起，没有您想查找的内容");
		System.out.println(arg0 + "," + arg1);
	    }

	    @Override
	    public void onSuccess(ResponseInfo<String> response) {
		System.out.println("search" + search);
		String rs;
		System.out.println(response.result + "------");
		Gson gson = new Gson();
		Type type = new TypeToken<ArrayList<Action>>() {
		}.getType();
		actions = gson.fromJson(response.result, type);
		if (actions.size() == 0) {
		    rs = "还没有找到和\"" + search + "\"有关的活动";
		} else {
		    rs = "您搜索的内容是:" + search;
		}
		tv.setText(rs);
		SearchAdapter adapter = new SearchAdapter(getActivity(),
			actions);
		lv.setAdapter(adapter);
	    }
	});
	lv.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> parent, View view,
		    int position, long id) {
		Bundle bundle = new Bundle();
		bundle.putSerializable("action", actions.get(position));
		/*
		 * String a_itemname = actions.get(position).getA_itemname();
		 * String a_introduce = actions.get(position).getA_introduce();
		 * String a_consult = actions.get(position).getA_consult();
		 * String a_FAQ = actions.get(position).getA_FAQ(); String
		 * a_joinex = actions.get(position).getA_joinex(); String
		 * a_charge = actions.get(position).getA_charge(); String
		 * a_stime = actions.get(position).getA_stime(); String a_etime
		 * = actions.get(position).getA_etime(); String a_address =
		 * actions.get(position).getA_address(); Double a_price =
		 * actions.get(position).getA_price(); int a_id =
		 * actions.get(position).getA_id();
		 * bundle.putString("a_itemname", a_itemname);
		 * bundle.putString("a_introduce", a_introduce);
		 * bundle.putString("a_consult", a_consult);
		 * bundle.putString("a_FAQ", a_FAQ);
		 * bundle.putString("a_joinex", a_joinex);
		 * bundle.putString("a_charge", a_charge);
		 * bundle.putString("a_stime", a_stime);
		 * bundle.putString("a_etime", a_etime);
		 * bundle.putString("a_address", a_address);
		 * bundle.putString("a_itemname", a_itemname);
		 * bundle.putDouble("a_price", a_price); bundle.putInt("a_id",
		 * actions.get(position).getA_id()); bundle.putString("a_photo",
		 * actions.get(position).getA_photo());
		 */
		Fra2 f2 = new Fra2();
		f2.setArguments(bundle);
		getFragmentManager().beginTransaction().addToBackStack(null)
			.replace(R.id.one_fragment, f2).commit();
	    }
	});
    }
}
