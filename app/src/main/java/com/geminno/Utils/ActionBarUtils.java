package com.geminno.Utils;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import geminno.com.hiweek_android.R;


/**
 * 
 * @ClassName: ActionBarUtils
 * @Description: 对Actionbar进行处理
 * @author: XU
 * @date: 2015年10月16日 下午1:18:06
 */
public class ActionBarUtils {
    private static ActionBarUtils actionBarUtils = null;
    private static Activity activity;

    private ActionBarUtils() {
    }

    public static ActionBarUtils getInstence(Activity activity) {
	if (actionBarUtils == null) {
	    actionBarUtils = new ActionBarUtils();
	}
	ActionBarUtils.activity = activity;
	return actionBarUtils;
    }

    public void initActionBar(ActionBar actionBar) {
	// actionBar.setCustomView(R.layout.actionbar_view);
	// 显示标题
	// actionBar.setDisplayShowTitleEnabled(true);
	// // 返回主页
	// actionBar.setDisplayShowHomeEnabled(false);
	// // 返回
	// actionBar.setDisplayHomeAsUpEnabled(true);
	// // 返回图标
	// // actionBar.setHomeAsUpIndicator(R.drawable.ic_launcher);
	// // 显示自定义View
	// actionBar.setTitle("返回");
	actionBar.setBackgroundDrawable(activity.getResources().getDrawable(
		R.drawable.actionbar));
    }

    public void initStatusbar() {
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
	    setTranslucentStatus(true);
	}

	SystemBarTintManager tintManager = new SystemBarTintManager(activity);
	tintManager.setStatusBarTintEnabled(true);
	tintManager.setStatusBarTintResource(R.color.ActionBar_Color);// 通知栏所需颜色

    }

    private void setTranslucentStatus(boolean on) {
	Window win = activity.getWindow();
	WindowManager.LayoutParams winParams = win.getAttributes();
	final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
	if (on) {
	    winParams.flags |= bits;
	} else {
	    winParams.flags &= ~bits;
	}
	win.setAttributes(winParams);

    }
}
