package com.geminno.JuHe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.geminno.Bean.CinemaForMovie;
import com.geminno.Bean.CinemaInfoRoot;
import com.geminno.Bean.Movie;
import com.geminno.Bean.MovieforCinema;
import com.geminno.JuHe.JuHeUtils.successListener;
import com.geminno.Service.InternetService;
import com.google.gson.Gson;
import com.thinkland.sdk.android.DataCallBack;

public class JuHeUtils {
    private static Movie movie;
    private DataCallBack callBack;
    private successListener sListener;
    Gson gson;
    private GetInfo getInfo;
    private String jsonString;
    protected ArrayList<CinemaForMovie> ccinemas;

    public JuHeUtils() {
	getInfo = new GetInfo();
    }

    public void getCinemas(Context context, int movieid) {
	gson = new Gson();
	ccinemas = new ArrayList<CinemaForMovie>();
	// Parameters parameters = new Parameters();
	//
	// parameters.add("cityid", 21);
	// parameters.add("movieid", movieid);
	// parameters.add("dtype", "json");
	// parameters.add("key", "dca1d94fecb6675766c3a756328a7ff6");
	// JuheData.executeWithAPI(context, 42,
	// "http://v.juhe.cn/movie/movies.cinemas", JuheData.GET,
	// parameters, callBack);
	// ------------------------测试--------
	try {
	    URL url = new URL("http://" + InternetService.IP
		    + ":8080/MyJuHe/movie/cinemas");
	    getInfo.getCinemas(url, sListener);
	} catch (IOException e) {
	    e.printStackTrace();

	}
    }

    public void getMovies(Context context, int cinemaID, boolean flag) {
	String jsonString = null;
	gson = new Gson();
	// Parameters parameters = new Parameters();
	// parameters.add("key", "dca1d94fecb6675766c3a756328a7ff6");
	// parameters.add("cinemaid", cinemaID);
	// JuheData.executeWithAPI(null, 42,
	// "http://v.juhe.cn/movie/cinemas.movies", JuheData.GET,
	// parameters, new DataCallBack() {
	//
	// @Override
	// public void onSuccess(int arg0, String arg1) {
	// // TODO Auto-generated method stub
	// System.out.println(arg1);
	// }
	//
	// @Override
	// public void onFinish() {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onFailure(int arg0, String arg1, Throwable arg2) {
	// // TODO Auto-generated method stub
	//
	// }
	// });
	try {
	    URL url = new URL("http://" + InternetService.IP
		    + ":8080/MyJuHe/cinema/movies?cinemaid=1188&flag="
		    + String.valueOf(flag));
	    getInfo.getCinemaInfo(url, sListener);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * 
     * @Title: getMovieInfo
     * @Description: 从网络获取电影信息，解析整理并放入数据库
     * @param movieID
     * @Author XU
     */
    public void getMovieInfo(Context context, int movieID) {
	// Parameters parameters = new Parameters();
	// parameters.add("key", "dca1d94fecb6675766c3a756328a7ff6");
	// parameters.add("movieid", movieID);
	// JuheData.executeWithAPI(null, 42, "http://v.juhe.cn/movie/query",
	// JuheData.GET, parameters, callBack);
	// ----------------------测试--------------------
	try {
	    URL url = new URL("http://" + InternetService.IP
		    + ":8080/MyJuHe/movie/query");
	    getInfo.getMovie(url, sListener);
	} catch (IOException e) {
	    e.printStackTrace();
	}

    }

    public void setOnDataCallBack(DataCallBack callBack) {
	this.callBack = callBack;
    }

    public void setsListener(successListener sListener) {
	this.sListener = sListener;
    }

    public interface successListener {
	void success(ArrayList<CinemaForMovie> cinemas);

	void success(MovieforCinema movieforCinema);

	void success(Movie movie);
    }

} // 测试所用方法

class GetInfo {

    private Gson gson;

    public GetInfo() {
	gson = new Gson();
    }

    public void getCinemas(final URL url, final successListener listener) {
	new Thread() {
	    public void run() {
		try {
		    HttpURLConnection connection = (HttpURLConnection) url
			    .openConnection();
		    connection.setRequestMethod("GET");

		    connection.connect();
		    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			BufferedReader reader = new BufferedReader(
				new InputStreamReader(
					connection.getInputStream()));
			char[] buffer = new char[100];
			StringBuilder stringBuilder = new StringBuilder();
			int length;
			while ((length = reader.read(buffer)) > 0) {
			    stringBuilder.append(String.copyValueOf(buffer, 0,
				    length));
			}
			CinemaInfoRoot cRoot = gson.fromJson(
				stringBuilder.toString(), CinemaInfoRoot.class);
			listener.success(cRoot.getResult());
		    }

		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    };
	}.start();

    }

    public void getCinemaInfo(final URL url, final successListener listener) {
	new Thread() {
	    public void run() {
		try {
		    HttpURLConnection connection = (HttpURLConnection) url
			    .openConnection();
		    connection.setRequestMethod("GET");

		    connection.connect();
		    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			BufferedReader reader = new BufferedReader(
				new InputStreamReader(
					connection.getInputStream()));
			char[] buffer = new char[100];
			StringBuilder stringBuilder = new StringBuilder();
			int length;
			while ((length = reader.read(buffer)) > 0) {
			    stringBuilder.append(String.copyValueOf(buffer, 0,
				    length));
			}
			System.out.println(stringBuilder.toString());
			String fastjsonstring = JSON
				.parseObject(stringBuilder.toString())
				.get("result").toString();

			listener.success(gson.fromJson(fastjsonstring,
				MovieforCinema.class));
		    }

		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    };
	}.start();
    }

    public void getMovie(final URL url, final successListener listener) {
	new Thread() {
	    public void run() {
		try {
		    HttpURLConnection connection = (HttpURLConnection) url
			    .openConnection();
		    connection.setRequestMethod("GET");

		    connection.connect();
		    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			BufferedReader reader = new BufferedReader(
				new InputStreamReader(
					connection.getInputStream()));
			char[] buffer = new char[100];
			StringBuilder stringBuilder = new StringBuilder();
			int length;
			while ((length = reader.read(buffer)) > 0) {
			    stringBuilder.append(String.copyValueOf(buffer, 0,
				    length));
			}
			MovieInfo movieInfo = gson.fromJson(
				JSON.parseObject(stringBuilder.toString())
					.get("result").toString(),
				MovieInfo.class);
			// System.out.println(movieInfo.getMovieid());
			Movie movie = new Movie(Integer.parseInt(movieInfo
				.getMovieid()), movieInfo.getTitle(),
				movieInfo.getPoster(),
				movieInfo.getPlot_simple(),
				Double.parseDouble(movieInfo.getRating()),
				movieInfo.getDirectors(),
				movieInfo.getCountry(), movieInfo.getRuntime(),
				movieInfo.getRelease_date() + "",
				movieInfo.getGenres(), movieInfo.getLanguage(),
				movieInfo.getYear() + "");

			listener.success(movie);
		    }

		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    };
	}.start();
    }
}
