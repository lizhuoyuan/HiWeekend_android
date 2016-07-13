package com.geminno.Service;

import com.geminno.Application.MyApplication;
import com.geminno.Utils.MyPropertiesUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class CollectIsExist extends Service {
	int u_id, a_id;
	Handler handler;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		handler = new Handler();
		u_id = MyApplication.getU_id();
		a_id = intent.getIntExtra("a_id", 0);
		isexist();
		return super.onStartCommand(intent, flags, startId);
	}

	public void isexist() {
		HttpUtils http = new HttpUtils();
		String url = MyPropertiesUtil.getProperties(this).getProperty("url") + "/HiWeek/servlet/CollectFind?u_id=" + u_id
				+ "&&a_id=" + a_id;
		System.out.println(url);
		http.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				System.out.println("arg0:" + arg1);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				System.out.println("判断是否存在这收藏:" + arg0.result);
				handler.sendEmptyMessage(Integer.parseInt(arg0.result));
			}
		});
	}
}
