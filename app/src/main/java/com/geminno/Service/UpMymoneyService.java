package com.geminno.Service;

import java.util.Properties;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.geminno.Application.MyApplication;
import com.geminno.Utils.MyPropertiesUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @author 李卓原 创建时间：2015年10月26日 下午10:40:59
 * 
 */
public class UpMymoneyService extends Service {
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
	changemoney();
    }

    public void changemoney() {
	HttpUtils http = new HttpUtils();
	Properties p = MyPropertiesUtil.getProperties(this);
	String ip = p.getProperty("url");
	String u_id = String.valueOf(MyApplication.getU_id());
	String u_credit = String.valueOf(MyApplication.getCredit());
	String u_yue = String.valueOf(MyApplication.getYue());
	RequestParams params = new RequestParams();
	params.addBodyParameter("u_id", u_id);
	params.addBodyParameter("u_credit", u_credit);
	params.addBodyParameter("u_yue", u_yue);
	String url = ip + "/HiWeek/servlet/ChangeMoney";
	http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

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
