package com.geminno.Service;

import java.util.Properties;

import com.geminno.Application.MyApplication;
import com.geminno.Utils.MyPropertiesUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class HomeService extends Service {
	String a_id;
	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {

	return super.onStartCommand(intent, flags, startId);
    }
	@Override
    public void onCreate() {
	System.out.println("service started");
	Intent i = new Intent();
	a_id = i.getStringExtra("id");
	findAction();
    }

    public void findAction() {
	HttpUtils http = new HttpUtils();
	Properties p = MyPropertiesUtil.getProperties(this);
	String ip = p.getProperty("url");
	RequestParams params = new RequestParams();
	params.addBodyParameter("a_id", a_id);
	String url = ip + "/HiWeek/servlet/SelectActionById";
	http.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {

	    @Override
	    public void onFailure(HttpException error, String msg) {
		System.out.println("error:" + error + ",msg:" + msg);
	    }

	    @Override
	    public void onSuccess(ResponseInfo<String> responseInfo) {
		System.out
			.println("responseInfo.result:" + responseInfo.result);
	    }
	});
    }
}
