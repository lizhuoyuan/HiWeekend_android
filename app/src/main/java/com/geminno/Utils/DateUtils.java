package com.geminno.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.R.integer;

public class DateUtils {
    private static DateUtils dateUtils = null;
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(
	    "HH:mm");

    private DateUtils() {
    }

    private static class GetClass {
	static DateUtils distanceUtils = new DateUtils();
    }

    public static DateUtils getInstence() {
	if (dateUtils == null) {
	    dateUtils = GetClass.distanceUtils;
	}

	return dateUtils;
    }

    /**
     * 
     * @Title: compare
     * @Description: 与当前时间作比较，若在当前时间之后则显示，返回true；
     * @param date
     * @return
     * @Author XU
     */
    public boolean compare(String date, boolean tomorrow) {
	Date sysDate = new Date();
	boolean show = true;
	// 获取当前周几
	// int day = DateFormat.getInstance().getCalendar()
	// .get(Calendar.DAY_OF_WEEK);
	// 测试期间，默认当天为周六
	int day = 7;
	// 如果不是周六周日，则不用判断
	if (day != 1 || day != 7) {
	    show = true;
	}
	// 若是周六，并且查看明天，则可显示
	if (day == 7 && tomorrow) {
	    show = true;
	}
	// 若是周日，并且查看周六，则都不显示
	if (day == 1 && !tomorrow) {
	    show = false;
	}
	if ((day == 1 && tomorrow) || (day == 7 && !tomorrow)) {

	    try {
		long time1 = SIMPLE_DATE_FORMAT.parse(
			SIMPLE_DATE_FORMAT.format(sysDate)).getTime();

		long time2 = SIMPLE_DATE_FORMAT.parse(date).getTime();

		if (time1 < time2) {
		    show = true;
		} else {
		    show = false;
		}
	    } catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	return show;
    }
}
