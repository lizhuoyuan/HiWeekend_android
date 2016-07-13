package com.geminno.Bean;

import android.R.integer;

public class Seat {
    public static final int SELECTED = 0;
    public static final int SALED = 1;
    public static final int OPTIONAL = 2;

    private int count_x;
    private int count_y;
    private int status;

    public Seat(int count_x, int count_y, int status) {
	super();
	this.count_x = count_x;
	this.count_y = count_y;
	this.status = status;
    }

    public int getCount_x() {
	return count_x;
    }

    public void setCount_x(int count_x) {
	this.count_x = count_x;
    }

    public int getCount_y() {
	return count_y;
    }

    public void setCount_y(int count_y) {
	this.count_y = count_y;
    }

    public int getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    @Override
    public String toString() {
	return "(" + (char) (64 + count_y + 1) + "排," + (count_x + 1) + "列)";
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + count_x;
	result = prime * result + count_y;
	result = prime * result + status;
	return result;
    }

    @Override
    public boolean equals(Object obj) {

	if (this.count_x != ((Seat) obj).count_x) {
	    return false;
	}
	if (this.count_y != ((Seat) obj).count_y) {
	    return false;
	}
	if (this.status != ((Seat) obj).status) {
	    return false;
	}
	return true;
    }
}
