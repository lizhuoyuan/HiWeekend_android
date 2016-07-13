package com.geminno.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 
 * @ClassName: GetLocation
 * @Description: 定位服务的实现，定位完成，修改位置数据后，则停止服务
 * @author: XU
 * @date: 2015年10月29日 上午11:39:10
 */
public class LocationSrvice extends Service {

    @Override
    public IBinder onBind(Intent intent) {

	return null;
    }

}
