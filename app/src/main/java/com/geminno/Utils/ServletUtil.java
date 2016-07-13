package com.geminno.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.R.integer;
import android.os.Message;

import com.geminno.Bean.Movie;
import com.google.gson.Gson;

/**
 * 
 * @ClassName: ServletUtil
 * @Description: 客户端获取数据的工具类
 * @author: XU
 * @date: 2015年10月16日 下午3:01:04
 */
public class ServletUtil {
    private static ServletUtil servletUtils = null;

    private ServletUtil() {

    }

    private static class GetClass {
	static ServletUtil servletUtils = new ServletUtil();
    }

    public static ServletUtil getInstence() {
	if (servletUtils == null) {
	    servletUtils = GetClass.servletUtils;
	}
	return servletUtils;
    }

    /**
     * 
     * @Title: getString
     * @Description: 根据传入的网址，和参数集合，返回JSon字符串
     * @param url
     *            目标网址
     * @param args
     *            参数集合
     * @return 字符串
     * @Author XU
     */
    public String getString(URL url, Map<String, String> args) {

	StringBuilder stringBuilder = new StringBuilder();
	String result = null;
	URL newUrl = null;
	BufferedReader reader = null;
	String bufferString;
	try {
	    newUrl = (args == null ? url : new URL(url + "?"
		    + AnalyParameters(args)));
	    HttpURLConnection httpconn = (HttpURLConnection) newUrl
		    .openConnection();
	    httpconn.setRequestMethod("GET");
	    httpconn.setInstanceFollowRedirects(true);
	    // httpconn.setIfModifiedSince(newValue);
	    httpconn.setUseCaches(true);

	    httpconn.connect();

	    if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
		reader = new BufferedReader(new InputStreamReader(
			httpconn.getInputStream()));
		bufferString = null;
		while ((bufferString = reader.readLine()) != null) {
		    stringBuilder.append(bufferString);
		}
		result = stringBuilder.toString();

	    }
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    try {
		if (reader != null) {
		    reader.close();
		    bufferString = null;

		}
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	return result;

    }

    public String getString(URL url) {
	return getString(url, null);
    }

    /**
     * 
     * @Title: AnalyParameters
     * @Description: 解析传入的参数集合
     * @param args
     *            map封装的参数
     * @return 格式化后的参数方式
     */

    private String AnalyParameters(Map<String, String> args) {
	StringBuilder sbBuilder = new StringBuilder();
	Set<Entry<String, String>> sets = args.entrySet();
	int count = 0;
	int sum = sets.size();
	for (Entry<String, String> entry : sets) {
	    count++;
	    if (sum == 1 || count == 1) {
		sbBuilder.append(entry.getKey() + "=" + entry.getValue());
	    } else {
		sbBuilder.append("&" + entry.getKey() + "=" + entry.getValue());
	    }
	}
	return sbBuilder.toString();
    }

    /**
     * 
     * @Title: PingIP
     * @Description: 检测与服务器连接是否成功
     * @param ip
     * @return
     * @Author XU
     */
    public boolean PingURL(URL url) {
	boolean b = false;
	try {
	    HttpURLConnection httpconn = (HttpURLConnection) url
		    .openConnection();
	    httpconn.setRequestMethod("GET");
	    httpconn.setInstanceFollowRedirects(true);
	    // httpconn.setIfModifiedSince(newValue);
	    httpconn.setUseCaches(true);
	    httpconn.setConnectTimeout(2000);
	    httpconn.connect();
	    int code = httpconn.getResponseCode();
	    b = (code == HttpURLConnection.HTTP_OK);

	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return b;
    }

    // public void uploadMovie(URL url, Object object) {
    // try {
    // Gson gson = new Gson();
    // String jsonString = URLEncoder.encode(gson.toJson(object), "UTF-8");
    // HttpURLConnection connection = (HttpURLConnection) url
    // .openConnection();
    //
    // connection.setRequestMethod("POST");
    // connection.setDoInput(true);
    // connection.setDoOutput(true);
    // connection.setUseCaches(false);
    //
    // OutputStream outputStream = connection.getOutputStream();
    // outputStream.write(jsonString.getBytes());
    // outputStream.flush();
    // outputStream.close();
    // int code = connection.getResponseCode();
    // System.out.println("code:" + code);
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }

    public void upload(URL url, Object object) {
	try {
	    Gson gson = new Gson();
	    String jsonString = URLEncoder.encode(gson.toJson(object), "UTF-8");
	    HttpURLConnection connection = (HttpURLConnection) url
		    .openConnection();

	    connection.setRequestMethod("POST");
	    connection.setDoInput(true);
	    connection.setDoOutput(true);
	    connection.setUseCaches(false);

	    OutputStream outputStream = connection.getOutputStream();
	    outputStream.write(jsonString.getBytes());
	    outputStream.flush();
	    outputStream.close();
	    int code = connection.getResponseCode();
	    System.out.println("code:" + code);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void getBitmap(URL url) {

    }
}
