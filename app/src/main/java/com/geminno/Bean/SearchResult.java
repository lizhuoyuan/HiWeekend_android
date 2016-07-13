package com.geminno.Bean;

public class SearchResult {
    public static final int MOVIE = 0;
    public static final int CINEMA = 1;
    private String title;
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
