package com.geminno.Activities.movie;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.geminno.Activities.setting.PersonActivity;
import com.geminno.Application.MyApplication;
import com.geminno.Bean.Constants;
import com.geminno.Fragment.movie.CinemaInfoFragment;
import com.geminno.Fragment.movie.CinemaInfoFragment.CineaInfoClickedListener;
import com.geminno.Fragment.movie.MapFragment;
import com.geminno.Fragment.movie.MovieInfoFragment;
import com.geminno.Fragment.movie.MovieInfoFragment.MovieBookedClickedListener;
import com.geminno.Fragment.movie.MovieMainFragment;
import com.geminno.Fragment.movie.MovieOrderPayFragment;
import com.geminno.Fragment.movie.Movie_Simple_Fragment;
import com.geminno.Fragment.movie.Movie_Simple_Fragment.ImageClickedListener;
import com.geminno.Fragment.movie.Movie_Simple_Fragment.cinemaClickedListener;
import com.geminno.Utils.ActionBarUtils;

import geminno.com.hiweek_android.R;

/**
 * 
 * @ClassName: MovieActivity
 * @Description: TODO
 * @author: XU
 * @date: 2015年11月16日 下午7:20:24
 */
public class MovieActivity extends Activity implements
	ImageClickedListener, MovieBookedClickedListener,
	cinemaClickedListener, CineaInfoClickedListener {
    public static final String FLAG_FROM_WHERE = "FROM";
    public static final String FLAG_TO_WHERE = "TO";

    // 判断使用哪个页面，通过FLAG标志传参
    public static final int FLAG_FROM_HOME = 1;
    public static final int FLAG_FROM_ORDER = 2;
    public static final int FLAG_FROM_NORMAL = 3;
    // 去哪个页面
    public static final int FLAG_TO_MOVIE = 4;
    public static final int FLAG_TO_CINEMA = 5;
    public static final int FLAG_TO_ORDER = 6;
    // 对子页面控件显示的标志
    public static boolean SHOW_BOOK = true;
    public static boolean SHOW_PAY = true;
    private MovieMainFragment movieMainFragment;
    private BroadcastReceiver mReceiver;
    private Movie_Simple_Fragment movie_Simple_Fragment;
    private CinemaInfoFragment cinemaInfoFragment;
    private MovieInfoFragment movieInfoFragment;
    private MovieActivity mActivity;
    public static Context context;
    private Bundle bundle;
    private int flag;
    private SearchView searchView;
    private int m_id;
    private MovieOrderPayFragment payFragment;
    private int from;
    private boolean Book = false;
    private int Book_ID;
    private String Book_url;
    private Fragment currentFragment;
    public static int FLAG_FROM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	ActionBarUtils.getInstence(this).initStatusbar();
	initActionBar(getActionBar());
	setContentView(R.layout.activity_movie_main);
	mActivity = this;
	context = this;
	FLAG_FROM = getIntent().getIntExtra(FLAG_FROM_WHERE, 3);
	choiceDefaultFragment();
	getFragmentManager().beginTransaction()
		.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
		.replace(R.id.main_movie_fl, currentFragment).commit();
    }

    private void choiceDefaultFragment() {
	switch (FLAG_FROM) {
	// 从首页进入
	case 1:
	    SHOW_BOOK = true;
	    SHOW_PAY = true;
	    formHome();
	    break;
	// 从订单进入
	case 2:
	    fromOrder();
	    break;
	// 正常进入
	case 3:
	    fromNormal();
	    SHOW_BOOK = true;
	    SHOW_PAY = true;
	    break;
	default:
	    break;
	}
    }

    // 从订单转入
    /**
     * 
     * @Title: fromOrder
     * @Description: 标志为FROM_ORDER,
     * @Author XU
     */
    private void fromOrder() {
	int to;
	to = getIntent().getIntExtra(FLAG_TO_WHERE, -1);
	if (to == FLAG_TO_ORDER) {
	    Bundle bundle = getIntent().getExtras();
	    SHOW_PAY = false;
	    payFragment = new MovieOrderPayFragment();
	    payFragment.setArguments(bundle);
	    currentFragment = payFragment;
	} else {
	    movieInfoFragment = new MovieInfoFragment(getIntent().getIntExtra(
		    "m_id", 57903), getIntent().getStringExtra("url"));
	    SHOW_BOOK = false;
	    movieInfoFragment.setOnMovieBookedListener(this);
	    currentFragment = movieInfoFragment;
	}
    }

    /**
     * 
     * @Title: formHome
     * @Description: 从首页进入，根据标志判断进入电影，还是影院
     * @Author XU
     */

    private void formHome() {
	int to = getIntent().getIntExtra(FLAG_TO_WHERE, -1);
	if (to == FLAG_TO_CINEMA) {
	    cinemaInfoFragment = new CinemaInfoFragment(getIntent()
		    .getIntExtra("C_id", -1));
	    currentFragment = cinemaInfoFragment;
	} else {
	    Book = true;
	    Book_ID = getIntent().getIntExtra("M_id", 57903);
	    Book_url = getIntent().getStringExtra("Url");
	    movieInfoFragment = new MovieInfoFragment(Book_ID, Book_url);
	    movieInfoFragment.setOnMovieBookedListener(this);
	    currentFragment = movieInfoFragment;
	}
    }

    private void fromNormal() {
	movieMainFragment = new MovieMainFragment();
	currentFragment = movieMainFragment;
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

    @Override
    protected void onPause() {
	super.onPause();
	LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
		.getInstance(this);
	if (localBroadcastManager == null) {
	    localBroadcastManager = LocalBroadcastManager.getInstance(this);
	}
	localBroadcastManager.unregisterReceiver(mReceiver);

    }

    @Override
    protected void onResume() {
	super.onResume();
	LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
		.getInstance(this);
	localBroadcastManager = LocalBroadcastManager.getInstance(this);
	IntentFilter intentFilter = new IntentFilter();
	intentFilter.addAction("com.geminno.movieselected");
	intentFilter.addAction("com.geminno.cinemaClicked");
	mReceiver = new BroadcastReceiver() {
	    /**
	     * 
	     * @TODO 选中电影时的广播接收
	     * @author Administrator
	     */
	    @Override
	    public void onReceive(Context context, Intent intent) {
		FragmentTransaction fTransaction = getFragmentManager()
			.beginTransaction();
		if (intent.getAction().equals("com.geminno.movieselected")) {
		    int movieID = intent.getIntExtra("movieID", 0);
		    String url = intent.getStringExtra("imageUrl");
		    // 电影基本信息页面
		    movie_Simple_Fragment = new Movie_Simple_Fragment(movieID,
			    url);
		    movie_Simple_Fragment
			    .setToMovieInfoImageClicked(MovieActivity.this);
		    movie_Simple_Fragment
			    .setOnCinemaClickedListener(MovieActivity.this);
		    fTransaction
			    .setTransition(
				    FragmentTransaction.TRANSIT_FRAGMENT_FADE)
			    .replace(R.id.main_movie_fl, movie_Simple_Fragment)
			    .addToBackStack(null).commit();
		}
		if (intent.getAction().equals("com.geminno.cinemaClicked")) {
		    int cinemaID = intent.getIntExtra("cinemaID", 0);
		    // 影院信息页面
		    cinemaInfoFragment = new CinemaInfoFragment(cinemaID);
		    cinemaInfoFragment
			    .setOnMovieNameClickedListener(MovieActivity.this);
		    fTransaction
			    .setTransition(
				    FragmentTransaction.TRANSIT_FRAGMENT_FADE)
			    .replace(R.id.main_movie_fl, cinemaInfoFragment)
			    .addToBackStack(
				    cinemaInfoFragment.getClass().getName())
			    .commit();
		}
	    }
	};
	localBroadcastManager.registerReceiver(mReceiver, intentFilter);

    }

    @Override
    public void toMovieInfo(int id, String url) {
	// 进入电影详细信息页面
	movieInfoFragment = new MovieInfoFragment(id, url);
	movieInfoFragment.setOnMovieBookedListener(this);
	getFragmentManager().beginTransaction()
		.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
		.replace(R.id.main_movie_fl, movieInfoFragment)
		.addToBackStack(null).commit();
    }

    /**
     * 
     * @Title: onMovieBooked
     * @Description: 电影详细信息页面，立即购票的回调
     */
    @Override
    public void onMovieBooked() {
	if (!PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
		Constants.USER_STATUS, false)) {
	    Intent intent = new Intent(this, PersonActivity.class);
	    intent.putExtra("FLAG", 1);
	    startActivityForResult(intent, 0);
	    return;
	}
	if (getFragmentManager().getBackStackEntryCount() == 0) {
	    movie_Simple_Fragment = new Movie_Simple_Fragment(Book_ID, Book_url);
	    movie_Simple_Fragment
		    .setToMovieInfoImageClicked(MovieActivity.this);
	    movie_Simple_Fragment
		    .setOnCinemaClickedListener(MovieActivity.this);
		getFragmentManager().beginTransaction()
		    .replace(R.id.main_movie_fl, movie_Simple_Fragment)
		    .commit();
	    Book = !Book;
	} else {
		getFragmentManager().popBackStack();
	}
    }

    @Override
    public void onCinemaClicked(int cinemaID) {
	FragmentTransaction fTransaction = getFragmentManager()
		.beginTransaction();
	cinemaInfoFragment = new CinemaInfoFragment(cinemaID);
	cinemaInfoFragment.setOnMovieNameClickedListener(this);
	fTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
		.replace(R.id.main_movie_fl, cinemaInfoFragment)
		.addToBackStack(null).commit();

    }

    @Override
    public void onMovieNameClicked(int movieID, String url) {
	movieID = 57903;
	toMovieInfo(movieID, url);

    }

    @Override
    public void onCinemaLocationClickedListener(String loca) {
	// Intent intent = new Intent(this, RoutePlanActivity.class);
	// intent.putExtra("a_address", "苏州市平江区人民路1755号");
	// startActivity(intent);
	MapFragment mapFragment = new MapFragment("苏州市平江区人民路1755号",
		MyApplication.getLat(), MyApplication.getLon(),
		MyApplication.getCity());
		getFragmentManager().beginTransaction()
		.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
		.replace(R.id.main_movie_fl, mapFragment).addToBackStack(null)
		.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.movie_1, menu);
	// searchView = (SearchView) menu.findItem(R.id.menu_search)
	// .getActionView();
	// searchView.setOnQueryTextListener(this);
	// searchView.setOnCloseListener(this);
	// searchView.setFocusable(false);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case android.R.id.home:
	    if (getFragmentManager().getBackStackEntryCount() == 0) {
		this.finish();
	    } else {
			getFragmentManager().popBackStack();
	    }
	    break;
	case R.id.menu_home:
	    // setResult(MovieInfoFragment.result);
	    this.finish();

	    break;

	default:
	    break;
	}
	return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
	setResult(MovieInfoFragment.result);
	super.onBackPressed();
    }

}
