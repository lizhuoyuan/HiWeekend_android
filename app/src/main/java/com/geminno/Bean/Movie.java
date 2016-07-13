package com.geminno.Bean;

import java.io.Serializable;

public class Movie implements Serializable {

    private int m_id;
    private String m_name;
    private String m_poster;
    private String m_introduce;
    private double m_grade;
    private String m_director;
    private String m_district;
    private String m_duration;
    private String m_date;
    private String m_genres;
    private String m_language;
    private String m_year;

    public Movie() {

    }

    public Movie(int m_id, String m_name, String m_poster, String m_introduce,
	    double m_grade, String m_director, String m_district,
	    String m_duration, String m_date, String m_genres,
	    String m_language, String m_year) {
	super();
	this.m_id = m_id;
	this.m_name = m_name;
	this.m_poster = m_poster;
	this.m_introduce = m_introduce;
	this.m_grade = m_grade;
	this.m_director = m_director;
	this.m_district = m_district;
	this.m_duration = m_duration;
	this.m_date = m_date;
	this.m_genres = m_genres;
	this.m_language = m_language;
	this.m_year = m_year;
    }

    public String getM_genres() {
	return m_genres;
    }

    public void setM_genres(String m_genres) {
	this.m_genres = m_genres;
    }

    public String getM_language() {
	return m_language;
    }

    public void setM_language(String m_language) {
	this.m_language = m_language;
    }

    public String getM_year() {
	return m_year;
    }

    public void setM_year(String m_year) {
	this.m_year = m_year;
    }

    public int getM_id() {
	return m_id;
    }

    public void setM_id(int m_id) {
	this.m_id = m_id;
    }

    public String getM_name() {
	return m_name;
    }

    public void setM_name(String m_name) {
	this.m_name = m_name;
    }

    public String getM_poster() {
	return m_poster;
    }

    public void setM_poster(String m_poster) {
	this.m_poster = m_poster;
    }

    public String getM_introduce() {
	return m_introduce;
    }

    public void setM_introduce(String m_introduce) {
	this.m_introduce = m_introduce;
    }

    public double getM_grade() {
	return m_grade;
    }

    public void setM_grade(double m_grade) {
	this.m_grade = m_grade;
    }

    public String getM_director() {
	return m_director;
    }

    public void setM_director(String m_director) {
	this.m_director = m_director;
    }

    public String getM_district() {
	return m_district;
    }

    public void setM_district(String m_district) {
	this.m_district = m_district;
    }

    public String getM_duration() {
	return m_duration;
    }

    public void setM_duration(String m_duration) {
	this.m_duration = m_duration;
    }

    public String getM_date() {
	return m_date;
    }

    public void setM_date(String m_date) {
	this.m_date = m_date;
    }

    @Override
    public String toString() {
	return "Movie [m_id=" + m_id + ", m_name=" + m_name + ", m_poster="
		+ m_poster + ", m_introduce=" + m_introduce + ", m_grade="
		+ m_grade + ", m_director=" + m_director + ", m_district="
		+ m_district + ", m_duration=" + m_duration + ", m_date="
		+ m_date + ", m_genres=" + m_genres + ", m_language="
		+ m_language + ", m_year=" + m_year + "]";
    }

}
