package com.geminno.hiweek1_0;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.geminno.Activities.movie.MovieActivity;
import com.geminno.Activities.setting.MyOrderActivity;
import com.geminno.Activities.setting.MyselfActivity;
import com.geminno.Activities.setting.PersonActivity;
import com.geminno.Application.MyApplication;
import com.geminno.Bean.Constants;
import com.geminno.Fragment.home.ActivityFragment;
import com.geminno.Fragment.home.ActivityFragment.MovieItemOnclickedListener;
import com.geminno.Fragment.home.HomepageFragment;
import com.geminno.Fragment.home.PersonCenterFragment;
import com.geminno.Fragment.home.PersonCenterFragment.onLoginButtonClickedListener;
import com.geminno.Fragment.setting.FragmentCollect;
import com.geminno.Utils.ActionBarUtils;
import com.geminno.Utils.ImageUtils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;
import com.lidroid.xutils.BitmapUtils;

import geminno.com.hiweek_android.R;

/**
 * 启动Activity
 *
 * @author 李卓原 创建时间：2015年10月12日 下午2:02:11
 */
public class MainActivity extends Activity implements OnClickListener,
        MovieItemOnclickedListener, onLoginButtonClickedListener,
        android.content.DialogInterface.OnClickListener, OnOpenedListener {
    private Button home_bt, activities_bt, Person_center;
    private Fragment fa, fb, fc, currentFragment;
    private SlidingMenu slidingmenu;

    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver bReceiver;
    private TextView actionbar_title;
    private LocationClient locationClient;
    private static final int UPDATE_TIME = 5000;
    private boolean isFirst = true;
    private TextView tvcity;
    private SharedPreferences sharedPreferences;
    private ImageView user_img;
    private static int fragmentindex;
    public static boolean login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // if (MyApplication.isNeight()) {
        //
        // setTheme(R.style.neightTheme);
        // } else {
        // setTheme(R.style.defaultTheme);
        // }
        ActionBarUtils.getInstence(this).initStatusbar();
        initActionBar(getActionBar());
        setContentView(R.layout.activity_main);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //sharedPreferences = getSharedPreferences("hiweek", Activity.MODE_PRIVATE);
        login = sharedPreferences.getBoolean(Constants.USER_STATUS, false);
        Log.e("TAG", "onCreate: " + login);
        // 初始化侧滑栏
        initSlidingMenu();
        // 定位当前位置
        initLocation();

        home_bt = (Button) findViewById(R.id.home_page);
        activities_bt = (Button) findViewById(R.id.activities);
        Person_center = (Button) findViewById(R.id.Personal_Center);
        home_bt.setOnClickListener(this);
        activities_bt.setOnClickListener(this);
        Person_center.setOnClickListener(this);
        actionbar_title.setText("首页");
        /**
         * 如果存储状态为空。则重新创建
         */
        if (savedInstanceState == null) {

            fa = new HomepageFragment();

            fb = new ActivityFragment();
            ((ActivityFragment) fb).setOnMovieItemClickedListener(this);

            fc = new PersonCenterFragment();
            ((PersonCenterFragment) fc).setOnLoginButtonClickedListener(this);
        }
        // 设置默认的Fragment页面
        getFragmentManager().beginTransaction().replace(R.id.main_fragment, fa).commit();
        // }
        // Toast.makeText(this, fragmentindex + "", 0).show();
    }

    /**
     * @Title: initSlidingMenu
     * @Description:初始化侧滑栏
     * @Author 李
     */
    private void initSlidingMenu() {
        // 对SlideMenu进行配置
        slidingmenu = new SlidingMenu(this);
        slidingmenu.setMode(SlidingMenu.LEFT);
        slidingmenu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
        slidingmenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingmenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingmenu.setMenu(R.layout.leftmenu);
        tvcity = (TextView) slidingmenu.findViewById(R.id.tvcity);
        user_img = (ImageView) slidingmenu.findViewById(R.id.user_img);
        slidingmenu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                // 如果用户已经登陆
                if (login) {
                    String Url = sharedPreferences.getString("user_picurl", "");
                    if (!TextUtils.isEmpty(Url)) {
                        // ImageUtils.getInstence().setQueue(getActivity());
                        //ImageUtils.genstence().loadImageUseVolley_ImageLoad(img, Url);
                        BitmapUtils bitmapUtils = new BitmapUtils(MainActivity.this);
                        // 加载网络图片
                        // Log.e("TAG", "init: " + Url);
                        bitmapUtils.display(user_img, Url);
                    } else {
                        user_img.setImageResource(R.drawable.defaultportrait);
                    }

                } else {
                    user_img.setImageResource(R.drawable.defaultportrait);
                }
            }
        });

        /*// 如果用户已经登陆
        if (login) {
            String Url = sharedPreferences.getString("user_picurl", "");
            if (!TextUtils.isEmpty(Url)) {
                // ImageUtils.getInstence().setQueue(getActivity());
                //ImageUtils.genstence().loadImageUseVolley_ImageLoad(img, Url);
                BitmapUtils bitmapUtils = new BitmapUtils(this);
                // 加载网络图片
                // Log.e("TAG", "init: " + Url);
                bitmapUtils.display(user_img, Url);
            } else {
                user_img.setImageResource(R.drawable.defaultportrait);
            }

        } else {
            user_img.setImageResource(R.drawable.defaultportrait);

        }*/
/*
     * if (PreferenceManager.getDefaultSharedPreferences(getBaseContext()).
	 * getBoolean(Constants.USER_STATUS, false)) {
	 *
	 * String Url =
	 * PreferenceManager.getDefaultSharedPreferences(getBaseContext
	 * ()).getString("user_picurl", ""); if (!TextUtils.isEmpty(Url)) {
	 * ImageUtils.getInstence().loadImageUseVolley_ImageLoad(user_img, Url);
	 *
	 * ImageUtils.getInstence().loadImageUseVolley_ImageLoad((
	 * ImageView)(new
	 * SlidingMenu).setMenu(R.layout.leftmenu).findViewById(R.id. user_img),
	 * Url);
	 *
	 * } else { user_img.setImageResource(R.drawable.defaultportrait);
	 *
	 * } } else { user_img.setImageResource(R.drawable.defaultportrait);
	 *
	 * }
*/
        slidingmenu.findViewById(R.id.tcce).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (login) {

                            slidingmenu.toggle();
                            getFragmentManager()
                                    .beginTransaction()
                                    .addToBackStack(null)
                                    .replace(R.id.main_fragment,
                                            new FragmentCollect()).commit();
                        } else {
                            Toast.makeText(MainActivity.this, "请点击头像进行登陆", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
        slidingmenu.findViewById(R.id.tvexit).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        slidingmenu.findViewById(R.id.willpay).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (login) {

                            slidingmenu.toggle();
                            startActivity(new Intent(MainActivity.this,
                                    MyOrderActivity.class));
                        } else {
                            Toast.makeText(MainActivity.this, "请点击头像进行登陆", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
        slidingmenu.findViewById(R.id.tvset).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (login) {

                            slidingmenu.toggle();
                            Intent intent = new Intent(MainActivity.this,
                                    MyselfActivity.class);
                            intent.putExtra("go", 2);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "请点击头像进行登陆", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
        slidingmenu.findViewById(R.id.user_img).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        slidingmenu.toggle();

                        Intent intent = new Intent(MainActivity.this,
                                PersonActivity.class);
                        if (!PreferenceManager.getDefaultSharedPreferences(
                                MainActivity.this).getBoolean(
                                Constants.USER_STATUS, false)) {
                            intent.putExtra("FLAG", 1);
                        }
                        startActivity(intent);
                    }
                });
    }

    /**
     * @Title: initLocation
     * @Description: 初始化定位功能
     * @Author 李
     */
    private void initLocation() {
        locationClient = new LocationClient(this);
        // 设置定位条件
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(UPDATE_TIME);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);// 可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);// 可选，默认false，设置是
        locationClient.setLocOption(option);
        locationClient.start();
        // 注册位置监听器
        locationClient.registerLocationListener(new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation location) {
                // map view 销毁后不在处理新接收的位置
                if (location == null)
                    return;
                if (isFirst) {
                    isFirst = false;
                    MyApplication.setLat(location.getLatitude());
                    MyApplication.setLon(location.getLongitude());
                    MyApplication.setCity(location.getCity());
                    tvcity.setText("当前地点:" + location.getCity());
                }

            }
        });
    }

    /**
     * @param actionBar
     * @Title: initActionBar
     * @Description: 初始化ActionBAr
     * @Author XU
     */
    @SuppressLint("NewApi")
    private void initActionBar(ActionBar actionBar) {
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setCustomView(R.layout.actionbar_view);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_view_headline);
        actionBar.setDisplayShowTitleEnabled(false);
        RelativeLayout layout = (RelativeLayout) actionBar.getCustomView();
        layout.setGravity(Gravity.CENTER);
        actionbar_title = (TextView) actionBar.getCustomView().findViewById(
                R.id.actionbar_title);
        actionBar.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.actionbar));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                slidingmenu.toggle(true);
                break;

            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_page:
                actionbar_title.setText("首页");
                fragmentindex = 0;
                // if (getSupportFragmentManager().findFragmentByTag("home") ==
                // null) {
                // getSupportFragmentManager().beginTransaction()
                // .replace(R.id.main_fragment, fa, "home")
                // .addToBackStack("home").commit();
                // } else {
                // getSupportFragmentManager().popBackStack("home",
                // FragmentManager.POP_BACK_STACK_INCLUSIVE);
                // }
                if (!fa.isAdded()) {

                    getFragmentManager().beginTransaction().hide(fb)
                            .hide(fc).add(R.id.main_fragment, fa).show(fa).commit();
                } else {
                    getFragmentManager().beginTransaction().hide(fb)
                            .hide(fc).show(fa).commit();
                }
                break;
            case R.id.activities:
                actionbar_title.setText("发现");
                fragmentindex = 1;
                // if (getSupportFragmentManager().findFragmentByTag("activity") ==
                // null) {
                // getSupportFragmentManager().beginTransaction()
                // .replace(R.id.main_fragment, fb, "activity")
                // .addToBackStack("activity").commit();
                // } else {
                // getSupportFragmentManager().popBackStack("activity",
                // FragmentManager.POP_BACK_STACK_INCLUSIVE);
                // }
                if (!fb.isAdded()) {
                    getFragmentManager().beginTransaction().hide(fa)
                            .hide(fc).add(R.id.main_fragment, fb).show(fb).commit();
                } else {
                    getFragmentManager().beginTransaction().hide(fa)
                            .hide(fc).show(fb).commit();
                }
                break;
            case R.id.Personal_Center:
                actionbar_title.setText("个人中心");
                fragmentindex = 2;
                // if (getSupportFragmentManager().findFragmentByTag("person") ==
                // null) {
                // getSupportFragmentManager().beginTransaction()
                // .replace(R.id.main_fragment, fc, "person")
                // .addToBackStack("person").commit();
                // } else {
                // getSupportFragmentManager().popBackStack("person",
                // FragmentManager.POP_BACK_STACK_INCLUSIVE);
                // }
                if (!fc.isAdded()) {
                    getFragmentManager().beginTransaction().hide(fa)
                            .hide(fb).add(R.id.main_fragment, fc).show(fc).commit();
                } else {
                    getFragmentManager().beginTransaction().hide(fa)
                            .hide(fb).show(fc).commit();
                }
                break;
            default:
                break;
        }
    }

    // 暂停状态不接受广播
    @Override
    protected void onPause() {
        super.onPause();
    }

    // 与用户交互时，才显示广播
    @Override
    protected void onResume() {
        super.onResume();

    }

    // 跳至Movie
    @Override
    public void toMovie() {
        startActivity(new Intent(this, MovieActivity.class));
    }

    @Override
    public void loginButtonClicked() {
        Intent intent = new Intent(this, PersonActivity.class);
        intent.putExtra("FLAG", 1);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {
            ((Button) fc.getView().findViewById(R.id.btnlogin)).setText("注销");
            ImageView iv = (ImageView) fc.getView().findViewById(R.id.touxiang);
            ImageUtils.getInstence().setQueue(this);
            String pic_url = sharedPreferences.getString("user_picurl", "");
            ImageUtils.getInstence().loadImageUseVolley_ImageLoad(iv, pic_url);
            ImageUtils.getInstence().loadImageUseVolley_ImageLoad(
                    (ImageView) slidingmenu.findViewById(R.id.user_img),
                    pic_url);
            ((TextView) fc.getView().findViewById(R.id.textcredit))
                    .setText(sharedPreferences.getInt("user_credit", 0) + "");
            ((TextView) fc.getView().findViewById(R.id.textmymoney))
                    .setText(sharedPreferences.getFloat("user_YuE", 0.0f) + "");

            Toast.makeText(this, data.getStringExtra("result"), Toast.LENGTH_SHORT).show();
        }
        if (resultCode == RESULT_OK && requestCode == 2) {
            ImageView iv = (ImageView) fc.getView().findViewById(R.id.touxiang);
            ImageUtils.getInstence().setQueue(this);
            String pic_url = sharedPreferences.getString("user_picurl", "");
            ImageUtils.getInstence().loadImageUseVolley_ImageLoad(iv, pic_url);
        }
    }

    @Override
    public void logOff() {
        AlertDialog.Builder builder = new Builder(this);
        builder.setTitle("注销").setMessage("注销之后部分功能将无法使用，你确定要注销吗？")
                .setNegativeButton("取消", this).setPositiveButton("确定", this)
                .show();

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case Dialog.BUTTON_NEGATIVE:

                break;
            case Dialog.BUTTON_POSITIVE:
                // 登录状态改为false
                sharedPreferences.edit().putBoolean(Constants.USER_STATUS, false)
                        .apply();
                PersonCenterFragment.login = false;
                // 用户头像改为默认
                ((ImageView) fc.getView().findViewById(R.id.touxiang))
                        .setImageBitmap(BitmapFactory.decodeResource(
                                getResources(), R.drawable.defaultportrait));
                ((Button) fc.getView().findViewById(R.id.btnlogin)).setText("登录");
                ((TextView) fc.getView().findViewById(R.id.textcredit))
                        .setText("0");
                ((TextView) fc.getView().findViewById(R.id.textmymoney))
                        .setText("0");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("login", false);
                editor.commit();
                break;
            default:
                break;
        }

    }

    @Override
    public void PersonSetting() {
        Intent intent = new Intent(this, PersonActivity.class);
        intent.putExtra("FLAG", 2);
        startActivityForResult(intent, 2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_1, menu);
        menu.removeItem(R.id.menu_home);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (slidingmenu.isShown()) {
                    slidingmenu.toggle(true);
                }
                break;
            case R.id.menu_light:

                // MyApplication.setNeight(!MyApplication.isNeight());
                // System.out.println("栈内："
                // + getSupportFragmentManager().getBackStackEntryCount());
                // getSupportFragmentManager().popBackStack();
                // recreate();
                break;
            default:
                break;
        }
        return true;
    }


    @Override
    public void onOpened() {
        String url = sharedPreferences.getString("user_picurl", "");
        if (sharedPreferences.getBoolean(Constants.USER_STATUS, false)
                && url != "") {

            ImageUtils.getInstence().setQueue(this);
            ImageUtils.getInstence()
                    .loadImageUseVolley_ImageLoad(user_img, url);
        } else {
            user_img.setImageResource(R.drawable.defaultportrait);
        }
    }
}