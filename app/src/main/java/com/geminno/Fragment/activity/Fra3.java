package com.geminno.Fragment.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Properties;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.geminno.Adapter.activity.QuesAdapter;
import com.geminno.Bean.Quiz;
import com.geminno.Utils.MyPropertiesUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 更多提问 最后修改： 2015年11月9日 08:56:44
 * 
 * @author 李卓原 创建时间：2015年10月21日 下午9:26:34 提问
 */
public class Fra3 extends ListFragment {
    int a_id;
    ArrayList<Quiz> qz;
    ImageView imgheart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	postload();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	getActivity().getActionBar().setTitle("更多提问");
	return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void postload() {
	a_id = getArguments().getInt("a_id");
	System.out.println("a_id:" + a_id);
	RequestParams params = new RequestParams();
	// params.addBodyParameter("a_id", a_id + "");
	HttpUtils http = new HttpUtils();
	Properties p = MyPropertiesUtil.getProperties(getActivity());
	String ip = p.getProperty("url");
	String url = ip + "/HiWeek/servlet/DownLoadQuiz?a_id=" + a_id;
	http.send(HttpMethod.GET, url, new RequestCallBack<String>() {

	    @Override
	    public void onFailure(HttpException error, String msg) {
		System.out.println(error.getExceptionCode() + ":" + msg);

	    }

	    @Override
	    public void onSuccess(ResponseInfo<String> responseInfo) {
		System.out.println("reply: " + responseInfo.result);
		Gson gson = new Gson();
		Type type = new TypeToken<ArrayList<Quiz>>() {
		}.getType();
		qz = gson.fromJson(responseInfo.result, type);
		setListAdapter(new QuesAdapter(qz, getActivity()));
	    }
	});

    }

}
