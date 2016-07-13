package com.geminno.Bean;

import java.util.ArrayList;

public class MovieInfo {
    private int movieId;
    private String movieName;
    private String pic_url;
    ArrayList<Broadcast> broadcast;

    public int getMovieId() {
	return movieId;
    }

    public void setMovieId(int movieId) {
	this.movieId = movieId;
    }

    public String getMovieName() {
	return movieName;
    }

    public void setMovieName(String movieName) {
	this.movieName = movieName;
    }

    public String getPic_url() {
	return pic_url;
    }

    public void setPic_url(String pic_url) {
	this.pic_url = pic_url;
    }

    public ArrayList<Broadcast> getBroadcast() {
	return broadcast;
    }

    public void setBroadcast(ArrayList<Broadcast> broadcast) {
	this.broadcast = broadcast;
    }

}
