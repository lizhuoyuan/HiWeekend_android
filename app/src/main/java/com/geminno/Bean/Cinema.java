package com.geminno.Bean;

import java.io.Serializable;

public class Cinema implements Serializable {
    private int c_id;
    private String c_name;
    private String c_address;
    private String c_tel;
    private double c_lat;
    private double c_lon;
    private String c_city;
    private String c_traffic;
    private float c_grade;

    public float getC_grade() {
	return c_grade;
    }

    public void setC_grade(float grade) {
	this.c_grade = grade;
    }

    public Cinema() {
    }

    public Cinema(int c_id, String c_name, String c_address, String c_tel,
	    float c_lat, float c_lon, String c_city, String c_traffic) {
	super();
	this.c_id = c_id;
	this.c_name = c_name;
	this.c_address = c_address;
	this.c_tel = c_tel;
	this.c_lat = c_lat;
	this.c_lon = c_lon;
	this.c_city = c_city;
	this.c_traffic = c_traffic;
    }

    public double getC_lat() {
	return c_lat;
    }

    public void setC_lat(double c_lat) {
	this.c_lat = c_lat;
    }

    public double getC_lon() {
	return c_lon;
    }

    public void setC_lon(double c_lon) {
	this.c_lon = c_lon;
    }

    public String getC_city() {
	return c_city;
    }

    public void setC_city(String c_city) {
	this.c_city = c_city;
    }

    public String getC_traffic() {
	return c_traffic;
    }

    public void setC_traffic(String c_traffic) {
	this.c_traffic = c_traffic;
    }

    public int getC_id() {
	return c_id;
    }

    public void setC_id(int c_id) {
	this.c_id = c_id;
    }

    public String getC_name() {
	return c_name;
    }

    public void setC_name(String c_name) {
	this.c_name = c_name;
    }

    public String getC_address() {
	return c_address;
    }

    public void setC_address(String c_address) {
	this.c_address = c_address;
    }

    public String getC_tel() {
	return c_tel;
    }

    public void setC_tel(String c_tel) {
	this.c_tel = c_tel;
    }

	@Override
	public String toString() {
		return "Cinema [c_id=" + c_id + ", c_name=" + c_name + ", c_address=" + c_address + ", c_tel=" + c_tel
				+ ", c_lat=" + c_lat + ", c_lon=" + c_lon + ", c_city=" + c_city + ", c_traffic=" + c_traffic
				+ ", c_grade=" + c_grade + "]";
	}

}
