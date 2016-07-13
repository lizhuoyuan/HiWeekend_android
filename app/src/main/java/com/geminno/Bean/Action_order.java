package com.geminno.Bean;

import java.io.Serializable;

public class Action_order implements Serializable {

    private static final long serialVersionUID = 1L;
    private int ao_id;
    private User user;
    private Action action;
    private String ao_time;
    private double ao_price;
    private int ao_state;
    private int ao_count;
    private int ao_credit;
    private String ao_date;

    public int getAo_count() {
	return ao_count;
    }

    public int getAo_credit() {
	return ao_credit;
    }

    public void setAo_credit(int ao_credit) {
	this.ao_credit = ao_credit;
    }

    public void setAo_count(int ao_count) {
	this.ao_count = ao_count;
    }

    public String getAo_date() {
	return ao_date;
    }

    public void setAo_date(String ao_date) {
	this.ao_date = ao_date;
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public Action getAction() {
	return action;
    }

    public void setAction(Action action) {
	this.action = action;
    }

    public int getAo_id() {
	return ao_id;
    }

    public void setAo_id(int ao_id) {
	this.ao_id = ao_id;
    }

    public String getAo_time() {
	return ao_time;
    }

    public void setAo_time(String ao_time) {
	this.ao_time = ao_time;
    }

    public double getAo_price() {
	return ao_price;
    }

    public void setAo_price(double ao_price) {
	this.ao_price = ao_price;
    }

    public int getAo_state() {
	return ao_state;
    }

    public void setAo_state(int ao_state) {
	this.ao_state = ao_state;
    }

}
