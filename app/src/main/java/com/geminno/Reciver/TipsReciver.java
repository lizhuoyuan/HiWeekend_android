package com.geminno.Reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * 
 * @ClassName: TipsReciver
 * @Description: 系统各种提示的广播接受着
 * @author: XU
 * @date: 2015年10月30日 下午4:50:26
 */
public class TipsReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
	Toast.makeText(context, intent.getStringExtra("TIPS"), 0).show();
    }

}
