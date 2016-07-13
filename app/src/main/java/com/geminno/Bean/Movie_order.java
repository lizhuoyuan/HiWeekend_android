package com.geminno.Bean;

import java.io.Serializable;

public class Movie_order implements Serializable {
    private int mo_id;
    private Cinema cinema;
    private Movie movie;
    private Office office;
    private User user;
    private String mo_seat;
    private double mo_price;
    private String mo_session;// 场次
    private int mo_state;
    private String mo_date;
    private int mo_count;
    private int mo_credit;

    public int getMo_credit() {
	return mo_credit;
    }

    public void setMo_credit(int mo_credit) {
	this.mo_credit = mo_credit;
    }

    public int getMo_count() {
	return mo_count;
    }

    public void setMo_count(int mo_count) {
	this.mo_count = mo_count;
    }

    public String getMo_date() {
	return mo_date;
    }

    public void setMo_date(String mo_date) {
	this.mo_date = mo_date;
    }

    public Cinema getCinema() {
	return cinema;
    }

    public void setCinema(Cinema cinema) {
	this.cinema = cinema;
    }

    public Movie getMovie() {
	return movie;
    }

    public void setMovie(Movie movie) {
	this.movie = movie;
    }

    public Office getOffice() {
	return office;
    }

    public void setOffice(Office office) {
	this.office = office;
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public int getMo_id() {
	return mo_id;
    }

    public void setMo_id(int mo_id) {
	this.mo_id = mo_id;
    }

    public String getMo_seat() {
	return mo_seat;
    }

    public void setMo_seat(String mo_seat) {
	this.mo_seat = mo_seat;
    }

    public double getMo_price() {
	return mo_price;
    }

    public void setMo_price(double mo_price) {
	this.mo_price = mo_price;
    }

    public String getMo_session() {
	return mo_session;
    }

    public void setMo_session(String mo_session) {
	this.mo_session = mo_session;
    }

    public int getMo_state() {
	return mo_state;
    }

    public void setMo_state(int mo_state) {
	this.mo_state = mo_state;
    }

    @Override
    public String toString() {
	return "Movie_order [mo_id=" + mo_id + ", cinema=" + cinema
		+ ", movie=" + movie + ", office=" + office + ", user=" + user
		+ ", mo_seat=" + mo_seat + ", mo_price=" + mo_price
		+ ", mo_session=" + mo_session + ", mo_state=" + mo_state
		+ ", mo_date=" + mo_date + ", mo_count=" + mo_count + "]";
    }

}
