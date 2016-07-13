package com.geminno.Service;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import com.baidu.a.a.a.a;
import com.geminno.Bean.Cinema;
import com.geminno.Bean.Constants;
import com.geminno.Bean.Movie;
import com.geminno.Resolver.MyContentResolver;
import com.geminno.Utils.ServletUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @ClassName: LoadDataFromServer
 * @Description: 从服务器获取数据加载到本地数据库
 * @author: XU
 * @date: 2015年10月16日 下午2:49:33
 */
public class LoadDataFromServer extends IntentService {

    private static final String NAME = "LoadDataFromServer";
    private static final String LOADING_DATA = "com.geminno.service.LOADING_DATA";
    private static final String TAG = "IntentService";
    ApplicationInfo applicationInfo;
    private Gson gson;
    MyContentResolver resolver;
    public static String IP;
    private ServletUtil servletUtil;
    private String resultstr;
    private int errorCode;
    private Intent intent;
    private String jsonString;
    String BASE_URL;
    public LoadDataFromServer() {
	super(NAME);
	gson = new Gson();
	servletUtil = ServletUtil.getInstence();
    }

    @Override
    public void onCreate() {

	super.onCreate();
	resolver = new MyContentResolver(this);
	try {
	    applicationInfo = getPackageManager().getApplicationInfo(
		    getPackageName(), PackageManager.GET_META_DATA);
	    IP = applicationInfo.metaData.getString("IP");
	     BASE_URL = "http://" + IP + ":8080/HiWeek/servlet/client/";

	} catch (NameNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    @Override
    /**
     * 方法运行在自己的线程里，每次只处理一个请求；
     */
    protected void onHandleIntent(Intent intent) {
	String actionNameString = intent.getAction();
	if (LOADING_DATA.equals(actionNameString)) {
	    loadMovieInfo();
	    loadCinemaInfo();
	    PreferenceManager.getDefaultSharedPreferences(this).edit()
		    .putBoolean(Constants.DATA_LOADING_STATUS, true).apply();
	    intent = new Intent("com.geminno.STATUS_TIP");
	    intent.putExtra("STATUS", "数据加载成功");
	    sendLocalBroadcast(intent);
	}

    }

    private void loadCinemaInfo() {
	URL url;
	try {
	    url = new URL(BASE_URL + "LoadingCinemas");

	    jsonString = servletUtil.getString(url);
	    Type type = new TypeToken<ArrayList<Cinema>>() {
	    }.getType();

	    ArrayList<Cinema> cinemas = gson.fromJson(jsonString, type);
	    // 存储到数据库
	    resolver.emptyCinema();
	    resolver.addCinemas(cinemas);

	} catch (MalformedURLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    private void loadMovieInfo() {
	URL url;
	try {
	    url = new URL(BASE_URL + "LoadingMovies");
	    jsonString = servletUtil.getString(url);

	    Type type = new TypeToken<ArrayList<Movie>>() {
	    }.getType();

	    ArrayList<Movie> movies = gson.fromJson(jsonString, type);
	    // 存储到数据库
	    resolver.emptyMovie();
	    resolver.addMovies(movies);
	} catch (MalformedURLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    private void sendLocalBroadcast(Intent intent) {
	LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
		.getInstance(this);
	localBroadcastManager.sendBroadcast(intent);

    }
}
