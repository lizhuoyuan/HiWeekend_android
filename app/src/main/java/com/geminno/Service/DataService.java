package com.geminno.Service;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.geminno.Bean.Cinema;
import com.geminno.Bean.Movie;
import com.geminno.Resolver.MyContentResolver;
import com.geminno.Utils.ServletUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DataService extends IntentService {

    public static final String LOADING_MOVIE_SERVLET = "com.geminno.dataservice.LOADING_MOVIE_SERVLET";
    public static final String LOADING_CINEMA_SERVLET = "com.geminno.dataservice.LOADING_CINEMA_SERVLET";
    public static final String LOADING_MOVIE_JUHE = "com.geminno.dataservice.LOADING_MOVIE_JUHE";
    public static final String LOADING_CINEMA_JUHE = "com.geminno.dataservice.LOADING_CINEMA_JUHE";
    public static String BASE_URL;
    private ApplicationInfo applicationInfo;
    private String jsonString;
    private ServletUtil servletUtil;
    private Gson gson;
    private MyContentResolver resolver;
    private static final String NAME = "DataService";

    public DataService() {
	super(NAME);
    }

    @Override
    public void onCreate() {
	super.onCreate();
	servletUtil = ServletUtil.getInstence();
	gson = new Gson();
	resolver = new MyContentResolver(this);
	ApplicationInfo applicationInfo;
	try {
	    applicationInfo = getPackageManager().getApplicationInfo(
		    getPackageName(), PackageManager.GET_META_DATA);
	    String IP = applicationInfo.metaData.getString("IP");
	    BASE_URL = "http://" + IP + ":8080/HiWeek/servlet/client/";
	} catch (NameNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @Override
    protected void onHandleIntent(Intent intent) {

	if (LOADING_CINEMA_JUHE.equals(intent.getAction())) {

	}
	if (LOADING_MOVIE_SERVLET.equals(intent.getAction())) {
	    loadMovieInfoFromServlet();

	}
	if (LOADING_CINEMA_SERVLET.equals(intent.getAction())) {
	    loadCinemaInfoFormServlet();
	}
	if (LOADING_MOVIE_JUHE.equals(intent.getAction())) {

	}

    }

    private void loadCinemaInfoFormServlet() {
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

    private boolean loadMovieInfoFromServlet() {
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
	    return true;
	} catch (MalformedURLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    return false;
	}
    }
}
