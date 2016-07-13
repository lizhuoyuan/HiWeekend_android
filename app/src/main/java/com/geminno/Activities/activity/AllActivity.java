package com.geminno.Activities.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import geminno.com.hiweek_android.R;

/**
 * .
 * @author 李卓原 
 * 创建时间：2015年11月20日 下午6:08:37
 */
public class AllActivity extends Activity {
    WebView webView;
    String url;
    private static final String TAG = AllActivity.class.getSimpleName();
    private static final String APP_CACAHE_DIRNAME = "/webcache";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	// 隐藏标题栏
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.activity_all);
	init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
	webView = (WebView) findViewById(R.id.webView1);
	initWebView();
	Intent i = getIntent();
	url = i.getStringExtra("url");
	webView.loadUrl(url);
	// WebView加载web资源
	// 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
	webView.setWebViewClient(new WebViewClient() {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// TODO Auto-generated method stub
		// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
		view.loadUrl(url);
		return true;
	    }
	});
	// 启用支持javascript
	WebSettings settings = webView.getSettings();
	settings.setJavaScriptEnabled(true);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {

	webView.getSettings().setJavaScriptEnabled(true);
	webView.getSettings().setRenderPriority(RenderPriority.HIGH);
	webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // 设置
								      // 缓存模式
	// 开启 DOM storage API 功能
	webView.getSettings().setDomStorageEnabled(true);
	// 开启 database storage API 功能
	webView.getSettings().setDatabaseEnabled(true);
	String cacheDirPath = getFilesDir().getAbsolutePath()
		+ APP_CACAHE_DIRNAME;
	// String cacheDirPath =
	// getCacheDir().getAbsolutePath()+Constant.APP_DB_DIRNAME;
	Log.i(TAG, "cacheDirPath=" + cacheDirPath);
	// 设置数据库缓存路径
	webView.getSettings().setDatabasePath(cacheDirPath);
	// 设置 Application Caches 缓存目录
	webView.getSettings().setAppCachePath(cacheDirPath);
	// 开启 Application Caches 功能
	webView.getSettings().setAppCacheEnabled(true);
    }
}
