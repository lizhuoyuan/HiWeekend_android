package com.geminno.Bean;

public class NearType {
    public static final int ACTION = 0;
    public static final int CINEMA = 1;
    public static final int MOVIE = 2;
    private String title;
    private String location;
    private double dis;
    private int type;
    private int id;
    private String url;

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getLocation() {
	return location;
    }

    public void setLocation(String location) {
	this.location = location;
    }

    public double getDis() {
	return dis;
    }

    public void setDis(double dis) {
	this.dis = dis;
    }

    public int getType() {
	return type;
    }

    public void setType(int type) {
	this.type = type;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

}
