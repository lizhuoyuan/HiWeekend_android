package com.geminno.Application;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import com.baidu.mapapi.SDKInitializer;
import com.geminno.Bean.Constants;
import com.geminno.Reciver.DataChangeReciver;
import com.geminno.Reciver.TipsReciver;
import com.thinkland.sdk.android.JuheSDKInitializer;

import java.text.DecimalFormat;

/**
 * @author 李卓原 创建时间：2015年10月12日 下午2:38:39
 */
public class MyApplication extends Application {
    private static String city = "连云港", u_name, pic_url;
    private static double lat, lon, yue;
    private static int u_paynum, credit, u_id;
    private SharedPreferences sharedPreferences;
    private BroadcastReceiver tipsReciver;
    private BroadcastReceiver dataChangeReciver;
    private static boolean neight = false;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        tipsReciver = new TipsReciver();
        dataChangeReciver = new DataChangeReciver();

        IntentFilter TipsIntentFilter = new IntentFilter();
        TipsIntentFilter.addAction("com.geminno.reciver.TIPS");

        IntentFilter dataChangeFilter = new IntentFilter();
        dataChangeFilter.addAction("com.geminno.reciver.DATA_CHANGE");
        dataChangeFilter.addAction("com.geminno.reciver.NET_CONNECTED");

        LocalBroadcastManager.getInstance(this).registerReceiver(tipsReciver,
                TipsIntentFilter);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                dataChangeReciver, dataChangeFilter);
        // 开启服务，开始判断网络状态
        // Intent intent = new Intent(this, InternetService.class);
        // intent.setAction("com.geminno.service.CHECK_NET");
        // startService(intent);
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(getApplicationContext());
        JuheSDKInitializer.initialize(getApplicationContext());

        if (sharedPreferences.getBoolean(Constants.USER_STATUS, false)) {
            u_id = sharedPreferences.getInt("user_id", 0);
            u_paynum = sharedPreferences.getInt("user_payNum", 0);
            credit = sharedPreferences.getInt("user_credit", 0);
            yue = (double) sharedPreferences.getFloat("user_YuE", 0.0f);

            pic_url = sharedPreferences.getString("user_picurl", "");
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(tipsReciver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                dataChangeReciver);

    }

    public static String getCity() {
        return city;
    }

    public static void setCity(String city) {
        MyApplication.city = city;
    }

    public static double getLat() {
        return lat;
    }

    public static void setLat(double lat) {
        MyApplication.lat = lat;
    }

    public static double getLon() {
        return lon;
    }

    public static void setLon(double lon) {
        MyApplication.lon = lon;
    }

    public static int getU_paynum() {
        return u_paynum;
    }

    public static void setU_paynum(int u_paynum) {
        MyApplication.u_paynum = u_paynum;
    }

    public static int getCredit() {
        return credit;
    }

    public static void setCredit(int credit) {
        MyApplication.credit = credit;
        System.out.println("credit:" + credit);
    }

    public static int getU_id() {
        return u_id;
    }

    public static void setU_id(int u_id) {
        MyApplication.u_id = u_id;
    }

    public static double getYue() {
        DecimalFormat df = new DecimalFormat("0.00");
        return Double.valueOf(df.format(yue));
    }

    public static void setYue(double yue) {
        DecimalFormat df = new DecimalFormat("0.00");
        MyApplication.yue = Double.valueOf(df.format(yue));
    }

    public static String getPic_url() {
        return pic_url;
    }

    public static void setPic_url(String pic_url) {
        MyApplication.pic_url = pic_url;
    }

    public static String getU_name() {
        return u_name;
    }

    public static void setU_name(String u_name) {
        MyApplication.u_name = u_name;
    }

    public static boolean isNeight() {
        return neight;
    }

    public static void setNeight(boolean neight) {
        MyApplication.neight = neight;
    }

}