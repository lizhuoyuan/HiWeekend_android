package com.geminno.Reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.geminno.Service.InternetService;

/**
 * @ClassName: InternetReciver
 * @Description: 网络状态变化，开启服务判断网络
 * @author: XU
 * @date: 2015年10月31日 下午12:00:33
 */
public class InternetReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
	Intent newintent = new Intent(context, InternetService.class);
	newintent.setAction("com.geminno.service.CHECK_NET");
	context.startService(newintent);
    }
}
