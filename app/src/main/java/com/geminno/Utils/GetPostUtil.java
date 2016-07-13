package com.geminno.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import android.os.Handler;
import android.os.Message;

/**
 * get post请求的工具类 get请求参数
 * 
 * @params params 请求参数是name=value&name1=value1
 * @author Administrator
 *
 */
public class GetPostUtil {

    public GetPostUtil() {

    }

    // post请求工具类
    public static void sendPost(final String url, final String params,
	    final Handler handler) {
	new Thread() {
	    @Override
	    public void run() {
		// TODO Auto-generated method stub

		try {
		    URL uri = new URL(url);
		    String newParams = URLEncoder.encode(params, "utf-8");
		    HttpURLConnection conn = (HttpURLConnection) uri
			    .openConnection();
		    conn.setRequestMethod("POST");
		    // 设置请求的参数
		    conn.setDoInput(true);
		    conn.setDoOutput(true);

		    conn.getOutputStream().write(newParams.getBytes());

		    if (conn.getResponseCode() == 200) {
			InputStream is = conn.getInputStream();
			String result = convertInputStreamToString(is);
			// 通过handler发送消息
			Message msg = new Message();
			msg.what = 1;
			msg.obj = result;
			handler.sendMessage(msg);
		    }
		} catch (MalformedURLException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }

	}.start();
    }

    // get请求工具类
    public static void sendGet(final String url, final String params,
	    final Handler handle) {
	new Thread() {

	    @Override
	    public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
		    // String newParams=URLEncoder.encode(params, "utf-8");
		    String newurl = url + "?" + params;

		    URL uri = new URL(newurl);
		    try {
			HttpURLConnection conn = (HttpURLConnection) uri
				.openConnection();
			conn.connect();
			// 获取返回结果
			if (conn.getResponseCode() == 200) {
			    InputStream is = conn.getInputStream();
			    String result = convertInputStreamToString(is);
			    // 通过handle发送消息
			    Message msg = new Message();
			    msg.what = 2;
			    msg.obj = result;
			    handle.sendMessage(msg);
			}
		    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		} catch (MalformedURLException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }

	}.start();
    }

    // 将inputStream转换成字符串
    public static String convertInputStreamToString(InputStream is) {

	byte[] b = new byte[1024];

	int length;
	String s = null;
	// 读取缓存中数据
	ByteArrayOutputStream baos = new ByteArrayOutputStream();

	try {
	    while ((length = is.read(b)) != -1) {
		baos.write(b, 0, length);
	    }
	    s = baos.toString();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    try {
		is.close();
		baos.close();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	}
	return s;

    }
}
