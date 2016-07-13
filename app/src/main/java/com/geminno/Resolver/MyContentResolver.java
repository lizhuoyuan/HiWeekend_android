package com.geminno.Resolver;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.geminno.Bean.Cinema;
import com.geminno.Bean.Movie;
import com.geminno.columns.Cloumns.CinemaCloumns;
import com.geminno.columns.Cloumns.MovieCloumns;

import java.util.ArrayList;

/**
 * 
 * @ClassName: MovieProviderHelper
 * @Description: 电影内容提供者的帮助类，为上层提供直接的调用方法
 * @author: XU
 * @date: 2015年10月17日 下午10:13:46
 */
public class MyContentResolver {
    private static final String AUTHORITY = "com.geminno.provider.DATA_BASE_PROVIDER";

    private static final String MOVIE_TABLE_NAME = "movie";
    private static final String CINEMA_TABLE_NAME = "cinema";

    private ContentResolver resolver;
    private Uri uri;

    public MyContentResolver(Context context) {
	this.resolver = context.getContentResolver();
    }

    /**
     * 
     * param <T>
     * @Title: addMovies
     * @Description: 添加电影信息
     * @param movies
     * @Author XU
     */
    public void addMovies(ArrayList<Movie> movies) {
	ContentValues contentValues = new ContentValues();
	uri = Uri.parse("content://" + AUTHORITY + "/" + MOVIE_TABLE_NAME);

	for (Movie movie : movies) {

	    contentValues.put(MovieCloumns._ID, movie.getM_id());
	    contentValues.put(MovieCloumns.NAME, movie.getM_name());
	    contentValues.put(MovieCloumns.POSTER, movie.getM_poster());
	    contentValues.put(MovieCloumns.INTRODUCE, movie.getM_introduce());
	    contentValues.put(MovieCloumns.GRADE, movie.getM_grade());
	    contentValues.put(MovieCloumns.DIRECTOR, movie.getM_director());
	    contentValues.put(MovieCloumns.DISTRICT, movie.getM_district());
	    contentValues.put(MovieCloumns.DURATION, movie.getM_duration());
	    contentValues.put(MovieCloumns.DATE, movie.getM_date());
	    contentValues.put(MovieCloumns.GENRES, movie.getM_genres());
	    contentValues.put(MovieCloumns.LANGUAGE, movie.getM_language());
	    contentValues.put(MovieCloumns.YEAR, movie.getM_year());
	    resolver.insert(uri, contentValues);
	}
    }

    public void addMovie(Movie moive) {
	ArrayList<Movie> movies = new ArrayList<Movie>();
	movies.add(moive);
	addMovies(movies);

    }

    public void addCinemas(ArrayList<Cinema> cinemas) {
	ContentValues contentValues = new ContentValues();
	uri = Uri.parse("content://" + AUTHORITY + "/" + CINEMA_TABLE_NAME);

	for (Cinema cinema : cinemas) {

	    contentValues.put(CinemaCloumns._ID, cinema.getC_id());
	    contentValues.put(CinemaCloumns.NAME, cinema.getC_name());
	    contentValues.put(CinemaCloumns.ADDRESS, cinema.getC_address());
	    contentValues.put(CinemaCloumns.TEL, cinema.getC_tel());
	    contentValues.put(CinemaCloumns.CITY, cinema.getC_city());
	    contentValues.put(CinemaCloumns.LAT, cinema.getC_lat());
	    contentValues.put(CinemaCloumns.LON, cinema.getC_lon());
	    contentValues.put(CinemaCloumns.TRAFFIC, cinema.getC_traffic());
	    contentValues.put(CinemaCloumns.GRADE, cinema.getC_grade());
	    resolver.insert(uri, contentValues);

	}
    }

    public Cursor selectCinemas(String[] columns, String terms, String[] args) {
	uri = Uri.parse("content://" + AUTHORITY + "/" + CINEMA_TABLE_NAME);
	return resolver.query(uri, columns, terms, args, CinemaCloumns._ID
		+ " desc limit 10 offset 0");
    }

    public int emptyCinema() {
	uri = Uri.parse("content://" + AUTHORITY + "/" + CINEMA_TABLE_NAME);
	return resolver.delete(uri, null, null);
    }

    public Cursor selectMovies(String[] columns, String terms, String[] args) {
	uri = Uri.parse("content://" + AUTHORITY + "/" + MOVIE_TABLE_NAME);
	return resolver.query(uri, columns, terms, args, MovieCloumns._ID
		+ " desc limit 10 offset 0");
    }

    public int emptyMovie() {
	uri = Uri.parse("content://" + AUTHORITY + "/" + MOVIE_TABLE_NAME);
	return resolver.delete(uri, null, null);
    }

    public Cursor selectMovieByID(int id) {
	uri = Uri.parse("content://" + AUTHORITY + "/" + MOVIE_TABLE_NAME + "/"
		+ id);
	return resolver.query(uri, null, null, null, null);
    }
}
