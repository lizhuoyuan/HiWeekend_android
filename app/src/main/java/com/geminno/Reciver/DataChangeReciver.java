package com.geminno.Reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.geminno.Bean.Constants;
import com.geminno.Service.DataService;

public class DataChangeReciver extends BroadcastReceiver {
    public static final int LAOD_MOVIE_SERVLET = 0;
    public static final int LAOD_MOVIE_JUHE = 1;
    public static final int LAOD_CINEMA_SERVLET = 2;
    public static final int LAOD_CINEMA_JUHE = 3;
    public static final String LOAD_FLAG = "flag";
    private Intent newintent;

    public DataChangeReciver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

	newintent = new Intent();
	switch (intent.getIntExtra(LOAD_FLAG, 4)) {
	case LAOD_CINEMA_JUHE:

	    break;
	case LAOD_CINEMA_SERVLET:
	    newintent.setClass(context, DataService.class);
	    newintent.setAction(DataService.LOADING_CINEMA_SERVLET);
	    context.startService(newintent);

	    break;
	case LAOD_MOVIE_JUHE:

	    break;
	case LAOD_MOVIE_SERVLET:
	    newintent.setClass(context, DataService.class);
	    newintent.setAction(DataService.LOADING_MOVIE_SERVLET);
	    context.startService(newintent);
	    break;
	default:
	    // 默只有初始化时才执行
	    if (!PreferenceManager.getDefaultSharedPreferences(context)
		    .getBoolean(Constants.DATA_LOADING_STATUS, false)) {
		newintent.setClass(context, DataService.class);
		newintent.setAction(DataService.LOADING_MOVIE_SERVLET);
		context.startService(newintent);
		newintent.setAction(DataService.LOADING_CINEMA_SERVLET);
		context.startService(newintent);
	    }
	    break;
	}
    }
}
