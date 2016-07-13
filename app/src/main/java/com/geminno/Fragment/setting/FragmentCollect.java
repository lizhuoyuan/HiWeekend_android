package com.geminno.Fragment.setting;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.geminno.Activities.activity.ZhangZiSshiActivity;
import com.geminno.Adapter.activity.CollectAdapter;
import com.geminno.Application.MyApplication;
import com.geminno.Bean.Collect;
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
 * @author 李卓原 创建时间：2015年10月27日 下午4:02:36
 * 
 */
public class FragmentCollect extends Fragment {
    private TextView tv;
    private ListView lv;
    ArrayList<Collect> collects;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	getActivity().getActionBar().setTitle("我的收藏");
	View root = inflater.inflate(R.layout.activity_search, null);
	lv = (ListView) root.findViewById(R.id.searchlist);
	tv = (TextView) root.findViewById(R.id.tvsearch);
	return root;

    }

    @Override
    public void onStart() {
	super.onStart();
	getCollect();
    }

    public void getCollect() {
	HttpUtils http = new HttpUtils();
	http.configCurrentHttpCacheExpiry(1000); // 设置缓存1秒，1秒内直接返回上次成功请求的结果。
	Properties p = MyPropertiesUtil.getProperties(getActivity());
	String ip = p.getProperty("url");
	String url = ip + "/HiWeek/servlet/SelectCollect?u_id="
		+ MyApplication.getU_id();
	System.out.println("url:" + url);
	http.send(HttpMethod.GET, url, new RequestCallBack<String>() {

	    @Override
	    public void onFailure(HttpException arg0, String arg1) {
		System.out.println(arg0 + "," + arg1);
		tv.setText("还没有活动，快去收藏吧!");
	    }

	    @Override
	    public void onSuccess(ResponseInfo<String> response) {
		String rs;
		System.out.println("ResponseInfo:" + response.result.toString());
		Gson gson = new Gson();
		Type type = new TypeToken<ArrayList<Collect>>() {
		}.getType();
		collects = gson.fromJson(response.result, type);
		if (collects.size() == 0) {
		    rs = "还没有活动，快去收藏吧!";
		} else {
		    rs = "您的收藏";
		}
		tv.setText(rs);
		CollectAdapter adapter = new CollectAdapter(collects,
			getActivity());
		lv.setAdapter(adapter);
	    }
	});
	lv.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> parent, View view,
		    int position, long id) {
		System.out.println(collects.get(position).getA_id());
		Intent i = new Intent(getActivity(), ZhangZiSshiActivity.class);
		i.putExtra("home", collects.get(position).getA_id());
		getActivity().startActivity(i);
	    }

	});
    }

}
