package com.geminno.Activities.setting;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.geminno.Activities.activity.ZhangZiSshiActivity;
import com.geminno.Activities.movie.MovieActivity;
import com.geminno.Adapter.order.MyOrderViewPageadapter;
import com.geminno.Bean.Action_order;
import com.geminno.Bean.AllOrder;
import com.geminno.Bean.Movie_order;
import com.geminno.Fragment.setting.AllOrderFragment;
import com.geminno.Fragment.setting.AllOrderFragment.pinclicklistener;
import com.geminno.Fragment.setting.AllOrderFragment.xiangclicklistener;
import com.geminno.Fragment.setting.AlreadyPayOrderFragment;
import com.geminno.Fragment.setting.NoDiscussFragment;
import com.geminno.Fragment.setting.NoPayOrderFragment;
import com.geminno.Service.InternetService;
import com.geminno.Utils.ActionBarUtils;
import com.geminno.Utils.ServletUtil;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import geminno.com.hiweek_android.R;

/**
 * 是一个activity 包含四个fragment:AllOrderFragment(全部),NoPayOrderFragment(未支付订单)
 * AlreadyPayOrderFragment(已支付订单),NoDiscussFragment(未评价) indata()
 * 设置adapter数据源是fragment myorderactionbar() 设置导航条的内容添加监听事件 myorderviewpage()
 * 设置viewpage适配器和监听事件
 * 
 * @author 郑雅倩
 * 
 */

public class MyOrderActivity extends FragmentActivity
	implements
	TabListener,
	OnPageChangeListener,
	xiangclicklistener,
	com.geminno.Fragment.setting.NoPayOrderFragment.xiangclicklistener,
	com.geminno.Fragment.setting.AlreadyPayOrderFragment.xiangclicklistener,
	com.geminno.Fragment.setting.NoDiscussFragment.xiangclicklistener,
	pinclicklistener,
	com.geminno.Fragment.setting.AlreadyPayOrderFragment.pinclicklistener,
	com.geminno.Fragment.setting.NoDiscussFragment.pinclicklistener {
    protected static final String TAG = "TEST";
    public static final int MOVIESIGN = 5;
    public static final int ACTIONSIGN = 4;
    private ViewPager vp;
    private ActionBar ab;
    private ArrayList<Fragment> frags;
    private AllOrderFragment allorder;
    private NoPayOrderFragment nopay;
    private AlreadyPayOrderFragment alread;
    private NoDiscussFragment nodis;
    public static final int ORDERSIGN = 9;
    private ServletUtil servletUtil;
    private String url = "http://" + InternetService.IP + ":8080/HiWeek";
    private int CurrentMo_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	// requestWindowFeature(Window.FEATURE_NO_TITLE);
	ActionBarUtils.getInstence(this).initStatusbar();
	setContentView(R.layout.activity_my_order);
	vp = (ViewPager) this.findViewById(R.id.my_order_viewpager);
	ab = getActionBar();
	indata();
	myorderactionbar();
	myorderviewpage();
	servletUtil = ServletUtil.getInstence();

    }

    // 获取adapter数据源是fragment
    private void indata() {
	frags = new ArrayList<Fragment>();
	allorder = new AllOrderFragment();
	// allorder.setBtnonclickListener(MyOrderActivity.this);
	allorder.setxiangclick(MyOrderActivity.this);
	allorder.setpinclick(MyOrderActivity.this);

	nopay = new NoPayOrderFragment();
	// nopay.setBtnonclickListener(MyOrderActivity.this);
	nopay.setxiangclick(MyOrderActivity.this);

	alread = new AlreadyPayOrderFragment();
	alread.setxiangclick(MyOrderActivity.this);
	alread.setpinclick(MyOrderActivity.this);

	nodis = new NoDiscussFragment();
	nodis.setxiangclick(MyOrderActivity.this);
	nodis.setpinclick(MyOrderActivity.this);

	frags.add(allorder);
	frags.add(alread);
	frags.add(nopay);
	frags.add(nodis);

    }

    // 设置导航条的内容添加监听事件

    private void myorderactionbar() {
	// 返回按钮
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
	    ab.setDisplayHomeAsUpEnabled(true);
	    ab.setDisplayShowCustomEnabled(false);
	    ab.setDisplayShowHomeEnabled(false);
	    ab.setDisplayShowTitleEnabled(true);
	    ab.setBackgroundDrawable(getResources().getDrawable(
		    R.drawable.actionbar));
	} else {
	    ab.setDisplayHomeAsUpEnabled(true);
	}
	ab.setBackgroundDrawable(getResources().getDrawable(
		R.drawable.actionbar));
	ab.setTitle("我的订单");
	// 设置导航模式
	ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	ab.addTab(ab.newTab().setText("全部").setTabListener(this));
	ab.addTab(ab.newTab().setText("已付款").setTabListener(this));
	ab.addTab(ab.newTab().setText("未付款").setTabListener(this));
	ab.addTab(ab.newTab().setText("未评论").setTabListener(this));
    }

    // 设置viewpage适配器和监听事件
    private void myorderviewpage() {
	MyOrderViewPageadapter adapter = new MyOrderViewPageadapter(
		getSupportFragmentManager(), frags);
	vp.setAdapter(adapter);
	vp.setOnPageChangeListener(this);
	vp.setOffscreenPageLimit(3);
    }

    // 实现tablistener重写的方法
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
	System.out.println("tab.getPosition()=========" + tab.getPosition());
	vp.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {

    }

    // 实现OnPageChangeListener重写得方法
    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int pos) {
	System.out.println("pos" + pos);
	Tab tab = ab.getTabAt(pos);
	ab.selectTab(tab);

    }

    // 设置overflow里面的图标显示
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
	if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
	    if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
		try {
		    Method m = menu.getClass().getDeclaredMethod(
			    "setOptionalIconsVisible", Boolean.TYPE);
		    m.setAccessible(true);
		    m.invoke(menu, true);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	}
	return super.onMenuOpened(featureId, menu);
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

	default:
	    break;
	}
	return super.onOptionsItemSelected(item);
    }

    @Override
    public void onxiangclicklistener(Bundle allbundle) {
	int position = allbundle.getInt("position");
	ArrayList<AllOrder> list = (ArrayList<AllOrder>) allbundle
		.getSerializable("list");

	switch (list.get(position).getSign()) {
	case AllOrderFragment.MOVIESIGN:

	    // 封装传给MovieActivity支付订单的值
	    Movie_order mo1 = new Movie_order();
	    mo1.setMo_id(list.get(position).getId());
	    mo1.setMovie(list.get(position).getMovie());
	    mo1.setUser(list.get(position).getUser());
	    mo1.setMo_credit(list.get(position).getCredit());
	    mo1.setMo_session(list.get(position).getSession());
	    mo1.setMo_seat(list.get(position).getSeat());
	    mo1.setMo_price(list.get(position).getprice());
	    mo1.setMo_count(list.get(position).getCount());
	    mo1.setMo_state(list.get(position).getstate());

	    Bundle bundle = new Bundle();
	    bundle.putSerializable("Movie_order", mo1);

	    Intent intent = new Intent(MyOrderActivity.this,
		    MovieActivity.class);
	    intent.putExtra(MovieActivity.FLAG_FROM_WHERE,
		    MovieActivity.FLAG_FROM_ORDER);
	    intent.putExtra(MovieActivity.FLAG_TO_WHERE,
		    MovieActivity.FLAG_TO_ORDER);
	    intent.putExtras(bundle);
	    startActivity(intent);
	    break;

	case AllOrderFragment.ACTIONSIGN:

	    // 封装给ZhangZiSshiActivity的支付订单的值
	    Action_order ao1 = new Action_order();
	    ao1.setAo_id(list.get(position).getId());
	    ao1.setAction(list.get(position).getAction());
	    ao1.setUser(list.get(position).getUser());
	    ao1.setAo_credit(list.get(position).getCredit());
	    ao1.setAo_count(list.get(position).getCount());
	    ao1.setAo_price(list.get(position).getprice());
	    ao1.setAo_count(list.get(position).getCount());
	    ao1.setAo_state(list.get(position).getstate());

	    Bundle b = new Bundle();
	    b.putString("name", list.get(position).getUser().getU_name());
	    b.putSerializable("ao1", ao1);
	    b.putString("tel", list.get(position).getTel());
	    b.putInt("ORDERSIGN", ORDERSIGN);
	    Intent intent2 = new Intent(MyOrderActivity.this,
		    ZhangZiSshiActivity.class);
	    intent2.putExtras(b);

	    startActivity(intent2);
	    break;

	default:
	    break;
	}

    }

    @Override
    public void onpinclicklistener(Bundle bundle) {
	int position = bundle.getInt("position");
	ArrayList<AllOrder> list = (ArrayList<AllOrder>) bundle
		.getSerializable("list");

	switch (list.get(position).getSign()) {
	case AllOrderFragment.ACTIONSIGN:

	    break;

	case AllOrderFragment.MOVIESIGN:

	    int m_id = 57903;
	    String url = list.get(position).getposter();
	    int mo_id = list.get(position).getId();
	    CurrentMo_id = mo_id;
	    Intent intent2 = new Intent(MyOrderActivity.this,
		    MovieActivity.class);
	    intent2.putExtra("url", url);
	    intent2.putExtra("m_id", m_id);
	    intent2.putExtra(MovieActivity.FLAG_FROM_WHERE,
		    MovieActivity.FLAG_FROM_ORDER);
	    intent2.putExtra(MovieActivity.FLAG_TO_WHERE,
		    MovieActivity.FLAG_TO_MOVIE);
	    // startActivity(intent2);
	    startActivityForResult(intent2, 0);
	    break;
	default:
	    break;
	}

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	if (resultCode == RESULT_OK) {
	    try {
		final URL urll = new URL(url
			+ "/servlet/UpdataCommentState?mo_id=" + CurrentMo_id);
		new Thread() {
		    public void run() {
			servletUtil.getString(urll);

		    };
		}.start();
		Intent intent = new Intent();
		intent.setAction("com.geminno.Dialog.frags.payfor.order");
		this.sendOrderedBroadcast(intent, null);
	    } catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	if (resultCode == RESULT_CANCELED) {
	    Toast.makeText(this, "您还没有评论", 0).show();
	}
    }
}
