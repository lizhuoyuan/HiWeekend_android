package com.geminno.Fragment.movie;

import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.geminno.Activities.setting.PersonActivity;
import com.geminno.Adapter.movie.CinemaInfo_MoviePagerAdapter;
import com.geminno.Adapter.movie.CinemaInfo_MoviePagerAdapter.onPageClickedListener;
import com.geminno.Adapter.movie.Row_of_tablets_listAdapter;
import com.geminno.Adapter.movie.Row_of_tablets_listAdapter.BookClickedListener;
import com.geminno.Bean.Broadcast;
import com.geminno.Bean.Cinema;
import com.geminno.Bean.CinemaForMovie;
import com.geminno.Bean.Cinema_info;
import com.geminno.Bean.Constants;
import com.geminno.Bean.Movie;
import com.geminno.Bean.MovieInfo;
import com.geminno.Bean.Movie_order;
import com.geminno.Bean.MovieforCinema;
import com.geminno.Bean.Seat;
import com.geminno.Bean.User;
import com.geminno.Dialog.MyDialog;
import com.geminno.Dialog.MyDialog.OnSubmitOrdersListener;
import com.geminno.JuHe.JuHeUtils;
import com.geminno.JuHe.JuHeUtils.successListener;
import com.geminno.Utils.DateUtils;
import com.geminno.animation.Transformer;
import com.google.android.support.v4.view.ViewPager;

import java.util.ArrayList;

import geminno.com.hiweek_android.R;

/**
 * @author Administrator
 * @ClassName CinemaInfoFragment
 * @Description 影院的详细信息页面
 * @date 2015年10月15日
 */
public class CinemaInfoFragment extends Fragment implements OnTouchListener,
        ViewPager.OnPageChangeListener, OnClickListener, BookClickedListener,
        OnSubmitOrdersListener, Callback, successListener,
        OnCheckedChangeListener,
        android.content.DialogInterface.OnClickListener, onPageClickedListener {
    private ViewPager viewpager;
    private ListView listview;
    private TextView cinema_info_name, cinema_info_location, movie_name;
    private ImageView cinema_call;
    private Cinema_info cinema_info;
    private ArrayList<MovieInfo> movieInfos;
    private CinemaInfo_MoviePagerAdapter pagerAdapter;
    private Row_of_tablets_listAdapter listAdapter;
    private ArrayList<Broadcast> broadcasts;
    private MyDialog myDialog;
    private MovieInfo movieInfo;
    private JuHeUtils juHeUtils;
    // private ArrayList<>
    private int cinemaID;
    private Movie_order order;
    private Handler handler;
    private ProgressDialog progressDialog;
    private RadioGroup rGroup;
    private int position;
    private AnimatorSet animationSet;
    private float positionOffset;
    private int padding;
    private int status;
    private View currentView;
    private int flashpadding;
    private DateUtils dateUtils;
    private ScrollView scrollView;
    private boolean flag = false;
    private ImageView mapImageView;

    public CinemaInfoFragment(int cinemaID) {
        this.cinemaID = cinemaID;
        juHeUtils = new JuHeUtils();
        juHeUtils.setsListener(this);
        handler = new Handler(this);
        initAnimation();
        dateUtils = DateUtils.getInstence();
    }

    private void initAnimation() {
        animationSet = new AnimatorSet();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 设置进度提示
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCancelable(false);

        View view = inflater.inflate(R.layout.cinema_info_view, null);
        // 找出控件
        scrollView = (ScrollView) view.findViewById(R.id.scroll_view);
        LinearLayout linearLayout = (LinearLayout) view
                .findViewById(R.id.viewpager_container);
        linearLayout.setOnTouchListener(this);

        viewpager = (ViewPager) view.findViewById(R.id.cinema_info_movie_pager);
        viewpager.setOffscreenPageLimit(5);
        viewpager.setPageMargin(10);
        viewpager.setOnPageChangeListener(this);
        viewpager.setPageTransformer(true, new Transformer());
        listview = (ListView) view.findViewById(R.id.cinema_info_movie_list);
        listview.setEmptyView(view.findViewById(R.id.empty_view));

        mapImageView = (ImageView) view.findViewById(R.id.map_navigation);
        mapImageView.setOnClickListener(this);
        cinema_info_name = (TextView) view.findViewById(R.id.cinema_info_name);
        cinema_info_location = (TextView) view
                .findViewById(R.id.cinema_info_location);
        cinema_call = (ImageView) view.findViewById(R.id.cinema_info_call);
        movie_name = (TextView) view.findViewById(R.id.movie_info);

        rGroup = (RadioGroup) view.findViewById(R.id.movie_time);
        rGroup.setOnCheckedChangeListener(this);
        cinema_call.setOnClickListener(this);
        movie_name.setOnClickListener(this);
        juHeUtils.getMovies(getActivity(), cinemaID, false);
        return view;

    }

    private void initView(MovieforCinema movieforCinema) {
        cinema_info = movieforCinema.getCinema_info();
        movieInfos = movieforCinema.getLists();
        movieInfo = movieInfos.get(0);
        broadcasts = (ArrayList<Broadcast>) movieInfo.getBroadcast().clone();

        pagerAdapter = new CinemaInfo_MoviePagerAdapter(movieInfos,
                getActivity());
        pagerAdapter.setOnPageClickedListener(this);
        listAdapter = new Row_of_tablets_listAdapter(broadcasts, getActivity());
        listAdapter.setOnBookClickedListener(CinemaInfoFragment.this);
        viewpager.setAdapter(pagerAdapter);
        viewpager.setCurrentItem(position);
        cinema_info_name.setText(cinema_info.getName());
        // ((TextView) getActivity().getActionBar().getCustomView()
        // .findViewById(R.id.actionbar_title)).setText(cinema_info
        // .getName());
        getActivity().getActionBar().setTitle("影院");
        cinema_info_location.setText(cinema_info.getAddress());
        movie_name.setText(movieInfos.get(position).getMovieName());
        listview.setAdapter(listAdapter);
        scrollView.smoothScrollTo(0, 0);

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return viewpager.dispatchTouchEvent(event);
    }

    /**
     * @param state arg0 ==1的时辰默示正在滑动<br>
     *              arg0==2的时辰默示滑动完毕了<br>
     *              arg0==0的时辰默示什么都没做。
     * @Title: onPageScrollStateChanged
     * @Description: TODO
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        // switch (state) {
        // case ViewPager.SCROLL_STATE_IDLE:
        // System.out.println("当前选中：" + position);
        // break;
        // case ViewPager.SCROLL_STATE_DRAGGING:
        // status = 1;
        // break;
        // case ViewPager.SCROLL_STATE_SETTLING:
        // System.out.println("当前选中：" + position);
        // break;
        // default:
        // break;
        // }
    }

    private void MyAnimation() {

    }

    /**
     * @param position             当前页面，及你点击滑动的页面
     * @param positionOffset       当前页面偏移的百分比
     * @param positionOffsetPixels 当前页面偏移的像素位置
     * @Title: onPageScrolled
     * @Description: TODO
     */
    @SuppressLint("NewApi")
    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        // if (status == 1) {
        // if (currentView == null) {
        // currentView = viewpager.getChildAt(0);
        // }
        // flashpadding = (int) (padding * positionOffset);
        // currentView.setPadding(flashpadding, flashpadding, flashpadding,
        // flashpadding);
        // }
    }

    @Override
    public void onPageSelected(int arg0) {
        // System.out.println(arg0);
        // currentView = viewpager.getChildAt(arg0);
        // padding = currentView.getPaddingBottom();
        // currentView.setPadding(0, 0, 0, 0);

        position = arg0;
        movieInfo = movieInfos.get(arg0);
        movie_name.setText(movieInfo.getMovieName());
        initBrodcasts();

    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    private void initBrodcasts() {
        broadcasts.clear();
        for (Broadcast broadcast : movieInfo.getBroadcast()) {
            if (!dateUtils.compare(broadcast.getTime(), flag)) {
                continue;
            }
            broadcasts.add(broadcast);
        }

        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cinema_info_call:
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                        .create();
                alertDialog.setTitle("需要拨打电话(" + cinema_info.getTelephone()
                        + ")，确定吗？");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "拨打",
                        CinemaInfoFragment.this);
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "不要",
                        CinemaInfoFragment.this);
                alertDialog.show();

                break;
            case R.id.map_navigation:
                if (cineaInfoClickedListener != null) {
                    cineaInfoClickedListener
                            .onCinemaLocationClickedListener(cinema_info_location
                                    .getText().toString());
                }
                break;
            case R.id.movie_info:
                if (cineaInfoClickedListener != null) {
                    cineaInfoClickedListener.onMovieNameClicked(
                            movieInfo.getMovieId(), movieInfo.getPic_url());
                }
                break;
            default:
                break;
        }
    }

    // 定义接口
    public interface CineaInfoClickedListener {
        void onMovieNameClicked(int movieID, String url);

        void onCinemaLocationClickedListener(String loca);
    }

    // 设置回调
    public void setOnMovieNameClickedListener(
            CineaInfoClickedListener cineaInfoClickedListener) {
        this.cineaInfoClickedListener = cineaInfoClickedListener;
    }

    private CineaInfoClickedListener cineaInfoClickedListener;

    @Override
    public void onBookTicket(int position) {
        if (!PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getBoolean(Constants.USER_STATUS, false)) {
            Intent intent = new Intent();
            intent.putExtra("FLAG", 1);
            intent.setClass(getActivity(), PersonActivity.class);
            startActivityForResult(intent, 0);
            return;
        }
        SeatFragment seatFragment = new SeatFragment(movieInfo, cinema_info,
                position);
        getFragmentManager().beginTransaction()
                .replace(R.id.main_movie_fl, seatFragment).addToBackStack(null)
                .commit();
        // myDialog = new MyDialog(broadcasts.get(position),
        // cinema_info.getName(), movie_name.getText().toString());
        // myDialog.setCancelable(false);
        //
        // myDialog.setOnSubmitOrderListener(this);
        // myDialog.show(getFragmentManager(), "");
        // myDialog.dismiss();
    }

    @Override
    public void onSubmitOrder(Broadcast broadcast, ArrayList<Seat> seats) {
        int count;
        if ((count = seats.size()) <= 0) {
            Toast.makeText(getActivity(), "您没有选择座位", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getBoolean(Constants.SERVER_STATUS, false)) {
            Toast.makeText(getActivity(), "网络不通", Toast.LENGTH_SHORT).show();

            return;
        }
        double price = Double.parseDouble(broadcast.getPrice());
        prepareData(broadcast);
        order.setMo_count(count);
        order.setMo_price(price * count);

        order.setMo_seat(seats.toString());
        order.setMo_session(broadcast.getTime());
        OrderFragment orderFragment = new OrderFragment(order, price);
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.main_movie_fl, orderFragment)
                .addToBackStack(null).commit();
    }

    private void prepareData(Broadcast broadcast) {
        order = new Movie_order();
        Cinema cinema = new Cinema();
        cinema.setC_id(cinemaID);
        cinema.setC_name(cinema_info.getName());
        cinema.setC_city(cinema_info.getCity());
        cinema.setC_tel(cinema_info.getTelephone());
        cinema.setC_address(cinema_info.getAddress());
        Movie movie = new Movie();
        movie.setM_name(movieInfo.getMovieName());
        movie.setM_poster(movieInfo.getPic_url());
        movie.setM_id(movieInfo.getMovieId());
        order.setCinema(cinema);
        order.setMovie(movie);
        User u1 = new User();
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        u1.setU_tel(sharedPreferences.getString("user_tel", ""));
        u1.setU_paynum(sharedPreferences.getInt("user_payNum", 0));
        u1.setU_id(sharedPreferences.getInt("user_id", 0));
        u1.setU_credit(sharedPreferences.getInt("user_credit", 0));
        u1.setU_yue(sharedPreferences.getFloat("user_YuE", 0.0f));
        order.setUser(u1);

    }

    @Override
    public boolean handleMessage(Message msg) {
        initView((MovieforCinema) msg.obj);
        if (listAdapter != null) {
            movieInfos = ((MovieforCinema) msg.obj).getLists();
            movieInfo = movieInfos.get(position);
        }
        initBrodcasts();
        progressDialog.dismiss();
        return true;
    }

    @Override
    public void success(ArrayList<CinemaForMovie> cinemas) {

    }

    @Override
    public void success(MovieforCinema movieforCinema) {
        Message msg = new Message();
        msg.obj = movieforCinema;
        handler.sendMessage(msg);
    }

    @Override
    public void success(Movie movie) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.tomorrow) {
            flag = true;
        } else {
            flag = false;
        }
        juHeUtils.getMovies(getActivity(), cinemaID, flag);

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == AlertDialog.BUTTON_POSITIVE) {

            String number = cinema_info.getTelephone();
            if (!TextUtils.isEmpty(number)) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                        + number));
                startActivity(intent);
            }
        }
    }

    @Override
    public void onPageClicked() {
        if (cineaInfoClickedListener != null) {
            cineaInfoClickedListener.onMovieNameClicked(movieInfo.getMovieId(),
                    movieInfo.getPic_url());
        }
    }
}
