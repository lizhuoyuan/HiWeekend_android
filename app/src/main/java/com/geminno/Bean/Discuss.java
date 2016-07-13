package com.geminno.Bean;

public class Discuss {
    private int d_id;
    private Movie movie;
    private User user;
    private String d_cont;
    private String d_time;
    private float m_grade;

    public float getM_grade() {
        return m_grade;
    }

    public void setM_grade(float m_grade) {
        this.m_grade = m_grade;
    }

    public Discuss(int d_id, Movie movie, User user, String d_cont,
	    String d_time) {
	super();
	this.d_id = d_id;
	this.movie = movie;
	this.user = user;
	this.d_cont = d_cont;
	this.d_time = d_time;
    }

    public Discuss() {

    }

    public int getD_id() {
	return d_id;
    }

    public void setD_id(int d_id) {
	this.d_id = d_id;
    }

    public Movie getMovie() {
	return movie;
    }

    public void setMovie(Movie movie) {
	this.movie = movie;
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public String getD_cont() {
	return d_cont;
    }

    public void setD_cont(String d_cont) {
	this.d_cont = d_cont;
    }

    public String getD_time() {
	return d_time;
    }

    public void setD_time(String d_time) {
	this.d_time = d_time;
    }

}
