package com.geminno.hiweek1_0;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.geminno.Activities.activity.ZhangZiSshiActivity;
import com.geminno.Activities.movie.MovieActivity;
import com.geminno.Adapter.activity.AllAdapter;
import com.geminno.Application.MyApplication;
import com.geminno.Bean.Action;
import com.geminno.Bean.AllBean;
import com.geminno.Bean.Cinema;
import com.geminno.Bean.Movie;
import com.geminno.Bean.NearType;
import com.geminno.Utils.ActionBarUtils;
import com.geminno.Utils.MyPropertiesUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import geminno.com.hiweek_android.R;

/**
 * 热门，本周末，附近
 * 
 * @author 李卓原 2015/11/13
 *
 */
public class ThreeActivity extends FragmentActivity implements
	OnItemClickListener {
    public static final int THE_WEEK = 0;
    public static final int THE_HOT = 1;
    public static final int THE_NEAR = 2;
    private ListView lv;
    private ArrayList<Cinema> ci;
    private ArrayList<Action> ac;
    private HashMap<String, Object> hm;
    private ArrayList<AllBean> ab;
    private HttpUtils http = new HttpUtils();
    private Gson gson;
    private AllAdapter adapter;
    private ArrayList<NearType> nears;
    private NearType nearType;
    private LatLng start, end;
    private DecimalFormat df;
    private int flag;
    private ArrayList<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	ActionBarUtils.getInstence(this).initStatusbar();
	initActionBar(getActionBar());
	setContentView(R.layout.activity_three);

	Intent i = getIntent();
	flag = i.getIntExtra("FLAG", -1);
	gson = new Gson();
	nears = new ArrayList<NearType>();
	lv = (ListView) findViewById(R.id.listView1);
	lv.setOnItemClickListener(this);
	df = new DecimalFormat("#.00");
	adapter = new AllAdapter(this, nears, flag);
	lv.setAdapter(adapter);

	init();
    }

    private void init() {
	if (flag == THE_NEAR) {
	    // 附近
	    initFujin();
	    getActionBar().setTitle("附近");
	}
	if (flag == THE_HOT) {
	    // 热门
	    initHot();
	    getActionBar().setTitle("热门");
	}
	if (flag == THE_WEEK) {
	    // 本週末
	    initTheWeek();
	    getActionBar().setTitle("本周末");
	}
    }

    @SuppressLint("NewApi")
    private void initActionBar(ActionBar actionBar) {
	// 返回按钮
	actionBar.setDisplayHomeAsUpEnabled(true);
	// 自定义View
	// actionBar.setDisplayShowCustomEnabled(true);
	// actionBar.setHomeAsUpIndicator(R.drawable.back2);
	// 返回主页
	actionBar.setDisplayShowHomeEnabled(false);
	// 显示title
	// actionBar.setDisplayShowTitleEnabled(false);

	actionBar.setBackgroundDrawable(getResources().getDrawable(
		R.drawable.actionbar));
	// actionBar.setCustomView(R.layout.actionbar_view);

    }

    public void initFujin() {

	String url = MyPropertiesUtil.getProperties(ThreeActivity.this)
		.getProperty("url") + "/HiWeek/servlet/FujinServlet";
	GetData(url);

    }

    private void initTheWeek() {
	String url = MyPropertiesUtil.getProperties(ThreeActivity.this)
		.getProperty("url") + "/HiWeek/servlet/TheWeekServlet";
	GetData(url);
    }

    public void initHot() {
	String url = MyPropertiesUtil.getProperties(ThreeActivity.this)
		.getProperty("url") + "/HiWeek/servlet/HotServlet";
	GetData(url);
    }

    private void SortList() {
	Collections.sort(nears, ThreeActivity.nearSort);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

	getMenuInflater().inflate(R.menu.menu_three, menu);

	return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case android.R.id.home:
	    int count = getSupportFragmentManager().getBackStackEntryCount();
	    if (count != 0) {
		getSupportFragmentManager().popBackStack();
	    } else {
		this.finish();
	    }
	    break;
	case R.id.thisweek:
	    if (flag == THE_WEEK) {
		break;
	    } else {
		flag = THE_WEEK;
		init();
	    }
	    break;
	case R.id.near:
	    if (flag == THE_NEAR) {
		break;
	    } else {
		flag = THE_NEAR;
		init();
	    }
	    break;
	case R.id.hot:
	    if (flag == THE_HOT) {
		break;
	    } else {
		flag = THE_HOT;
		init();
	    }
	    break;
	default:
	    break;
	}
	return true;
    }

    private void GetData(String url) {
	http.send(HttpMethod.GET, url, new RequestCallBack<String>() {

	    @Override
	    public void onFailure(HttpException arg0, String arg1) {

	    }

	    @Override
	    public void onSuccess(ResponseInfo<String> arg0) {
		nears.clear();
		start = new LatLng(MyApplication.getLat(), MyApplication
			.getLon());
		com.alibaba.fastjson.JSONObject responseObject = JSON.parseObject(arg0.result);
		String cinemaJsonString = responseObject.get("1").toString();

		if (flag == THE_NEAR) {
		    Type cinemalistType = new TypeToken<ArrayList<Cinema>>() {
		    }.getType();
		    ci = gson.fromJson(cinemaJsonString, cinemalistType);
		} else {
		    Type movieListType = new TypeToken<ArrayList<Movie>>() {

		    }.getType();
		    movies = gson.fromJson(cinemaJsonString, movieListType);
		}

		String actionJsonString = responseObject.get("2").toString();
		Type actionlistType = new TypeToken<ArrayList<Action>>() {
		}.getType();
		ac = gson.fromJson(actionJsonString, actionlistType);
		if (flag == THE_NEAR) {

		    for (Cinema cinema : ci) {
			nearType = new NearType();
			nearType.setId(cinema.getC_id());
			end = new LatLng(cinema.getC_lat(), cinema.getC_lon());
			nearType.setDis(DistanceUtil.getDistance(start, end) / 1000);
			nearType.setLocation(cinema.getC_address());
			nearType.setType(NearType.CINEMA);
			nearType.setTitle(cinema.getC_name());
			nears.add(nearType);
		    }
		} else {
		    for (Movie movie : movies) {
			nearType = new NearType();
			nearType.setId(movie.getM_id());
			nearType.setTitle(movie.getM_name());
			nearType.setUrl(movie.getM_poster());
			nearType.setType(NearType.MOVIE);
			nears.add(nearType);
		    }
		}
		for (Action action : ac) {
		    Action action2 = action;
		    System.out.println(action2);
		    nearType = new NearType();
		    nearType.setId(action.getA_id());
		    if (flag == THE_NEAR) {
			end = new LatLng(action.getA_lat(), action.getA_lon());
			nearType.setDis(DistanceUtil.getDistance(start, end) / 1000);
			nearType.setLocation(action.getA_address());
		    }
		    nearType.setUrl(action.getA_photo());
		    nearType.setType(NearType.ACTION);
		    nearType.setTitle(action.getA_itemname());
		    nears.add(nearType);
		}
		if (flag == THE_NEAR) {
		    SortList();
		}
		adapter.notifyDataSetChanged();
	    }
	});
    }

    // 创建比较器
    private static Comparator<NearType> nearSort = new Comparator<NearType>() {

	@Override
	public int compare(NearType lhs, NearType rhs) {
	    if (lhs.getDis() > rhs.getDis()) {
		return 1;
	    } else {
		return -1;
	    }
	}

    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
	    long id) {
	NearType nearType = nears.get(position);
	if (nearType.getType() == NearType.ACTION) {

	    startActivity(new Intent(this, ZhangZiSshiActivity.class).putExtra(
		    "home", nearType.getId()));
	} else if (nears.get(position).getType() == NearType.MOVIE) {
	    Intent intent = new Intent(this, MovieActivity.class);
	    intent.putExtra(MovieActivity.FLAG_FROM_WHERE,
		    MovieActivity.FLAG_FROM_HOME);
	    intent.putExtra(MovieActivity.FLAG_TO_WHERE,
		    MovieActivity.FLAG_TO_MOVIE);
	    intent.putExtra("M_id", nearType.getId());
	    // intent.putExtra("M_id", 57903);

	    intent.putExtra("Url", nearType.getUrl());
	    startActivity(intent);

	} else {

	    startActivity(new Intent(this, MovieActivity.class)
		    .putExtra(MovieActivity.FLAG_FROM_WHERE,
			    MovieActivity.FLAG_FROM_HOME)
		    .putExtra("C_id", 1207)
		    .putExtra(MovieActivity.FLAG_TO_WHERE,
			    MovieActivity.FLAG_TO_CINEMA));
	}
	this.finish();
    }
}
