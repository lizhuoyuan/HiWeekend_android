package com.geminno.Activities.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.geminno.Activities.setting.MyOrderActivity;
import com.geminno.Bean.Action;
import com.geminno.Fragment.activity.ActionOrderFragment1;
import com.geminno.Fragment.activity.Fra1;
import com.geminno.Fragment.activity.Fra2;
import com.geminno.Fragment.activity.PayFragment;
import com.geminno.Fragment.activity.SearchFragment;
import com.geminno.Utils.ActionBarUtils;
import com.geminno.Utils.MyPropertiesUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.Properties;

import geminno.com.hiweek_android.R;

/**
 * @author 李卓原 创建时间：2015年10月12日 下午2:07:51
 */
public class ZhangZiSshiActivity extends FragmentActivity {
    Fragment sf, aof;
    String search, collect;
    Bundle bundle;
    private String a_itemname, a_introduce, a_consult, a_FAQ, a_joinex,
	    a_charge, a_stime, a_etime, a_address, a_photo, u_name;
    private double a_price;
    int a_id;
    Action actions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	ActionBarUtils.getInstence(this).initStatusbar();
	initActionBar(getActionBar());
	setContentView(R.layout.activity_zhang_zi_sshi);

	aof = new ActionOrderFragment1();
	sf = new SearchFragment();
	Intent i = getIntent();
	search = i.getStringExtra("search");
	collect = i.getStringExtra("collect");
	if (i.getIntExtra("home", -1) != -1) {
	    a_id = i.getIntExtra("home", -1);
	    System.out.println("--------------------" + a_id);
	    findAction();
	} else {
	    Bundle b = new Bundle();
	    b.putString("search", search);
	    sf.setArguments(b);
	    Bundle newb = new Bundle();
	    newb = getIntent().getExtras();
	    int flag = newb == null ? 0 : newb.getInt("ORDERSIGN");

	    if (flag == MyOrderActivity.ORDERSIGN) {
		PayFragment payFragment = new PayFragment();
		payFragment.setArguments(newb);
		getSupportFragmentManager().beginTransaction()
			.replace(R.id.one_fragment, payFragment).commit();
	    } else if (!TextUtils.isEmpty(search)) {
		getSupportFragmentManager().beginTransaction()
			.add(R.id.one_fragment, sf).commit();
	    } else {
		getSupportFragmentManager().beginTransaction()
			.add(R.id.one_fragment, new Fra1()).commit();
	    }
	}
    }

    @SuppressLint("NewApi")
    private void initActionBar(ActionBar actionBar) {
	// 返回按钮
	actionBar.setDisplayHomeAsUpEnabled(true);
	// 自定义View
	actionBar.setDisplayShowCustomEnabled(true);
	// actionBar.setHomeAsUpIndicator(R.drawable.back2);
	// 返回主页
	actionBar.setDisplayShowHomeEnabled(false);
	// 显示title
	actionBar.setDisplayShowTitleEnabled(true);
	actionBar.setBackgroundDrawable(getResources().getDrawable(
		R.drawable.actionbar));
	// actionBar.setCustomView(R.layout.actionbar_view);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case android.R.id.home:
	    if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
		this.finish();
	    } else {
		getSupportFragmentManager().popBackStack();
	    }
	    break;
	case R.id.menu_home:
	    this.finish();
	    break;
	default:
	    break;
	}
	return super.onOptionsItemSelected(item);
    }

    public void findAction() {
	HttpUtils http = new HttpUtils();
	Properties p = MyPropertiesUtil.getProperties(this);
	String ip = p.getProperty("url");
	RequestParams params = new RequestParams();
	// params.addBodyParameter("a_id", id + "");
	String url = ip + "/HiWeek/servlet/SelectActionById?a_id=" + a_id;
	System.out.println("---------" + url + "-----------");
	http.send(HttpMethod.GET, url, new RequestCallBack<String>() {

	    @Override
	    public void onFailure(HttpException error, String msg) {
		System.out.println("error:" + error + ",msg:" + msg);
	    }

	    @Override
	    public void onSuccess(ResponseInfo<String> responseInfo) {
		System.out
			.println("responseInfo.result:" + responseInfo.result);
		Gson gson = new Gson();
		// Type type = new TypeToken<ArrayList<Action>>() {
		// }.getType();
		actions = gson.fromJson(responseInfo.result, Action.class);
		bundle = new Bundle();
		bundle.putSerializable("action", actions);
		Fragment f2 = new Fra2();
		f2.setArguments(bundle);
		getSupportFragmentManager().beginTransaction()
			.replace(R.id.one_fragment, f2).commit();

	    }
	});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.movie_1, menu);

	return true;
    }

}
