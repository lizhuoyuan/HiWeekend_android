package com.geminno.columns;

import android.provider.BaseColumns;

public class Cloumns {
    public interface MovieCloumns extends BaseColumns {
	public static final String NAME = "m_name";
	public static final String POSTER = "m_poster";
	public static final String INTRODUCE = "m_introduce";
	public static final String GRADE = "m_grade";
	public static final String DIRECTOR = "m_director";
	public static final String DISTRICT = "m_district";
	public static final String DURATION = "m_duration";
	public static final String DATE = "m_date";
	public static final String GENRES = "m_genres";
	public static final String LANGUAGE = "m_language";
	public static final String YEAR = "m_year";
    }

    public interface CinemaCloumns extends BaseColumns {
	public static final String NAME = "c_name";
	public static final String ADDRESS = "c_address";
	public static final String TEL = "c_tel";
	public static final String LAT = "c_lat";
	public static final String LON = "c_lon";
	public static final String CITY = "c_city";
	public static final String TRAFFIC = "c_traffic";
	public static final String GRADE = "c_grade";
    }
}
