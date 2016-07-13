package com.geminno.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class InternetUtils {
    private static InternetUtils internetUtils = null;

    private InternetUtils() {
    }

    private static class GetClass {
	static InternetUtils internetUtils = new InternetUtils();
    }

    public static InternetUtils getInstence() {
	if (internetUtils == null) {
	    internetUtils = GetClass.internetUtils;
	}

	return internetUtils;
    }

    public boolean isNetworkAvailable(Context context) {
	ConnectivityManager connectivity = (ConnectivityManager) context
		.getSystemService(Context.CONNECTIVITY_SERVICE);
	if (connectivity == null) {
	    Log.i("NetWorkState", "Unavailabel");

	    return false;
	} else {
	    NetworkInfo[] info = connectivity.getAllNetworkInfo();
	    if (info != null) {
		for (int i = 0; i < info.length; i++) {
		    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
			Log.i("NetWorkState", "Availabel");
			return true;
		    }
		}
	    }
	}

	return false;
    }

}
