package com.geminno.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SharedPreferenceUtils {
    private static SharedPreferenceUtils sPreferenceUtils = null;
    private static String spName;
    private static Context context;

    private SharedPreferenceUtils() {
    }

    private static class GetClass {
	static SharedPreferenceUtils internetUtils = new SharedPreferenceUtils();
    }

    public static SharedPreferenceUtils getInstence(Context context,
	    String spName) {
	if (sPreferenceUtils == null) {
	    sPreferenceUtils = GetClass.internetUtils;
	}
	SharedPreferenceUtils.spName = spName;
	SharedPreferenceUtils.context = context;
	return sPreferenceUtils;
    }

    public void putData(String key, int value) {

    }

    public void putData(String key, String value) {

    }

    public void putData(String key, boolean value) {

    }

    private Editor getEditor() {
	SharedPreferences sharedPreferences = PreferenceManager
		.getDefaultSharedPreferences(context);
	return sharedPreferences.edit();
    }
}
