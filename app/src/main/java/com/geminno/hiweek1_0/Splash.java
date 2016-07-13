package com.geminno.hiweek1_0;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import geminno.com.hiweek_android.R;

/**
 * @author 李卓原
 *         创建时间：2015年10月14日 上午10:05:38
 *         验证是否是第一次启动程序
 */
public class Splash extends Activity {
    boolean isFirstIn = false;
    Intent intent;
    private final int SPLASH_DISPLAY_LENGHT = 0; //延迟0秒

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences preferences = getSharedPreferences("first_pref",
                MODE_PRIVATE);
        isFirstIn = preferences.getBoolean("isFirstIn", true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFirstIn) {
                    // start HuanYingActivity
                    intent = new Intent(Splash.this, HuanYingActivity.class);
                } else {
                    // start MainActivity
                    intent = new Intent(Splash.this, MainActivity.class);
                }
                Splash.this.startActivity(intent);
                Splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);

    }


}
