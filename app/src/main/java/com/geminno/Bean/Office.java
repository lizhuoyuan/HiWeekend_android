package com.geminno.Bean;

import java.io.Serializable;

public class Office implements Serializable {
    private int o_id;
    private String o_name;
    private Cinema cinema;
    private int o_row;
    private int o_col;

    public Cinema getCinema() {
	return cinema;
    }

    public void setCinema(Cinema cinema) {
	this.cinema = cinema;
    }

    public int getO_id() {
	return o_id;
    }

    public void setO_id(int o_id) {
	this.o_id = o_id;
    }

    public String getO_name() {
	return o_name;
    }

    public void setO_name(String o_name) {
	this.o_name = o_name;
    }

    public int getO_row() {
	return o_row;
    }

    public void setO_row(int o_row) {
	this.o_row = o_row;
    }

    public int getO_col() {
	return o_col;
    }

    public void setO_col(int o_col) {
	this.o_col = o_col;
    }

}
