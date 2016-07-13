package com.geminno.Bean;

import java.util.ArrayList;

public class MovieforCinema {
    private Cinema_info cinema_info;
    private ArrayList<MovieInfo> lists;

    public Cinema_info getCinema_info() {
	return cinema_info;
    }

    public void setCinema_info(Cinema_info cinema_info) {
	this.cinema_info = cinema_info;
    }

    public ArrayList<MovieInfo> getLists() {
	return lists;
    }

    public void setLists(ArrayList<MovieInfo> lists) {
	this.lists = lists;
    }
}
