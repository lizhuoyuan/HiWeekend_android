package com.geminno.Cache;

import android.graphics.Bitmap;
import android.graphics.LightingColorFilter;
import android.support.v4.util.*;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader.ImageCache;

/**
 * 
 * @ClassName MyImageCache
 * @Description 实现缓存 lrucahe
 * @author XU
 * @date 2015年10月3日
 */
public class MyImageCache implements ImageCache {
    private static final String TAG = "MyImageCache";
    // 声明lrucache
    private LruCache<String, Bitmap> bCache;

    public MyImageCache() {
	// 实例化bcache
	long maxSize = Runtime.getRuntime().maxMemory() / 2;
	Log.v(TAG, "maxSize:" + maxSize);
	bCache = new LruCache<String, Bitmap>((int) maxSize) {

	    @Override
	    protected int sizeOf(String key, Bitmap value) {
		// TODO Auto-generated method stub
		return value.getRowBytes() * value.getHeight();
	    }

	};
    }

    @Override
    public Bitmap getBitmap(String url) {
	Log.v(TAG, "取图片：" + url);
	return bCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
	Log.v(TAG, "缓存图片：" + url);
	bCache.put(url, bitmap);
    }
}
