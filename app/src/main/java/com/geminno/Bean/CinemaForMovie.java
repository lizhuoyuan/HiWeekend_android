package com.geminno.Bean;

public class CinemaForMovie {
    String cinemaName;
    String cinemaId;
    String address;
    float latitude;
    float longitude;

    public String getCinemaName() {
	return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
	this.cinemaName = cinemaName;
    }

    public String getCinemaId() {
	return cinemaId;
    }

    public void setCinemaId(String cinemaId) {
	this.cinemaId = cinemaId;
    }

    public String getAddress() {
	return address;
    }

    public void setAddress(String address) {
	this.address = address;
    }

    public float getLatitude() {
	return latitude;
    }

    public void setLatitude(float latitude) {
	this.latitude = latitude;
    }

    public float getLongitude() {
	return longitude;
    }

    public void setLongitude(float longitude) {
	this.longitude = longitude;
    }
}
