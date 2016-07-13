package com.geminno.Fragment.home;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geminno.Activities.activity.ZhangZiSshiActivity;
import com.geminno.Activities.movie.MovieActivity;
import com.geminno.Adapter.home.MyPagerAdapter;
import com.geminno.Bean.Action;
import com.geminno.Bean.Lunbo;

import java.util.ArrayList;

import geminno.com.hiweek_android.R;

/**
 * 
 * @author 李卓原 创建时间：2015年10月12日 下午2:08:00
 */
public class HomepageFragment extends Fragment implements OnClickListener {
    ImageView img1, iv1;
    RelativeLayout imgXwc, imgxlt;
    Action actions;
    View root;
    private ViewPager viewPager;
    ArrayList<Lunbo> list = new ArrayList<Lunbo>();
    TextView tv;
    int a_id, u_id; // uid;
    Double lat, lon;
    /*
     * private String a_itemname, a_introduce, a_consult, a_FAQ, a_joinex,
     * a_charge, a_stime, a_etime, a_address, a_photo, u_name; // uname private
     * double a_price;
     */
    LinearLayout pointLayout;
    private Handler handler = new Handler() {
	public void handleMessage(android.os.Message msg) {
	    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
	    handler.sendEmptyMessageDelayed(0, 2000);
	};
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	root = inflater.inflate(R.layout.fragment_a, null);
	init();
	initListener();
	return root;

    }

    public void init() {
	viewPager = (ViewPager) root.findViewById(R.id.viewpager);
	imgXwc =  (RelativeLayout) root.findViewById(R.id.xiawucha);
	imgxlt = (RelativeLayout) root.findViewById(R.id.xialuote);
	imgxlt.setOnClickListener(this);
	imgXwc.setOnClickListener(this);
	tv = (TextView) root.findViewById(R.id.tvintro);
	pointLayout = (LinearLayout) root.findViewById(R.id.pointLayout);
	list.add(new Lunbo(R.drawable.one, "敲打质朴的银镯"));
	list.add(new Lunbo(R.drawable.two, "远离工业时代，体验手工的温度"));
	list.add(new Lunbo(R.drawable.three, "为健康和美丽加码"));
	viewPager.setAdapter(new MyPagerAdapter(getActivity(), list));
	viewPager.setCurrentItem(Integer.MAX_VALUE / 2
		- (Integer.MAX_VALUE / 2) % list.size());
	initpoint();
	updateIntroAndDot();
	handler.sendEmptyMessageDelayed(0, 2000);
    }

    private void initpoint() {
	for (int i = 0; i < list.size(); i++) {
	    View view = new View(getActivity());
	    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(8,
		    8);
	    params.leftMargin = 5;
	    view.setLayoutParams(params);
	    view.setBackgroundResource(R.drawable.point_selector);
	    // 初始化point
	    pointLayout.addView(view);
	}
    }

    private void updateIntroAndDot() {
	int currentPage = viewPager.getCurrentItem() % list.size();
	tv.setText(list.get(currentPage).getIntro());
	for (int i = 0; i < pointLayout.getChildCount(); i++) {
	    pointLayout.getChildAt(i).setEnabled(i == currentPage);
	}
    }

    private void initListener() {
	viewPager.setOnPageChangeListener(new OnPageChangeListener() {

	    @Override
	    public void onPageSelected(int position) {
		// 当page选中时调用
		updateIntroAndDot();
	    }

	    @Override
	    public void onPageScrolled(int arg0, float arg1, int arg2) {
	    }

	    @Override
	    public void onPageScrollStateChanged(int arg0) {
	    }
	});
    }

    @Override
    public void onClick(View v) {
	Intent intentActivity = new Intent(getActivity(),
		ZhangZiSshiActivity.class);
	Intent intentMovie = new Intent(getActivity(), MovieActivity.class);
	switch (v.getId()) {
	case R.id.xiawucha:
	    intentActivity.putExtra("home", 1);
	    startActivity(intentActivity);
	    break;
	case R.id.xialuote:
	    intentMovie.putExtra("FROM", 1);
	    intentMovie.putExtra("M_id", 57903);
	    intentMovie.putExtra("Url", "http://v.juhe.cn/movie/img?192189");
	    //startActivity(intentMovie);
	    break;
	default:
	    break;
	}

    }
}
/*
 * @Override public void onClick(View v) { Intent intentActivity = new
 * Intent(getActivity(), ZhangZiSshiActivity.class); switch (v.getId()) { case
 * R.id.view: intentActivity.putExtra("home", 2); startActivity(intentActivity);
 * break;
 * 
 * default: break; }
 */

/*
 * public void findAction(int id) { HttpUtils http = new HttpUtils(); Properties
 * p = MyPropertiesUtil.getProperties(getActivity()); String ip =
 * p.getProperty("url"); RequestParams params = new RequestParams();
 * params.addBodyParameter("a_id", id+""); String url = ip +
 * "/HiWeek/servlet/SelectActionById"; http.send(HttpMethod.GET, url, params,
 * new RequestCallBack<String>() {
 * 
 * @Override public void onFailure(HttpException error, String msg) {
 * System.out.println("error:" + error + ",msg:" + msg); }
 * 
 * @Override public void onSuccess(ResponseInfo<String> responseInfo) {
 * System.out .println("responseInfo.result:" + responseInfo.result); Gson gson
 * = new Gson(); Type type = new TypeToken<ArrayList<Action>>() { }.getType();
 * actions = gson.fromJson(responseInfo.result, type); a_id = actions.getA_id();
 * a_photo = actions.getA_photo(); a_itemname = actions.getA_itemname();
 * a_introduce = actions.getA_introduce(); a_consult = actions.getA_consult();
 * a_FAQ = actions.getA_FAQ(); a_joinex = actions.getA_joinex(); a_charge =
 * actions.getA_charge(); a_stime = actions.getA_stime(); a_etime =
 * actions.getA_etime(); a_address = actions.getA_address(); a_price =
 * actions.getA_price(); } }); }
 */
