package com.geminno.Fragment.movie;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.geminno.Adapter.movie.CinemaforMovieListAdapter;
import com.geminno.Application.MyApplication;
import com.geminno.Bean.CinemaForMovie;
import com.geminno.Bean.Movie;
import com.geminno.Bean.MovieforCinema;
import com.geminno.JuHe.JuHeUtils;
import com.geminno.JuHe.JuHeUtils.successListener;
import com.geminno.Resolver.MyContentResolver;
import com.geminno.Utils.ImageUtils;
import com.geminno.View.MyFreshListView;
import com.geminno.View.MyFreshListView.LoadingListener;
import com.geminno.View.MyFreshListView.RefreshListener;
import com.geminno.columns.Cloumns.MovieCloumns;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import geminno.com.hiweek_android.R;

/**
 * @author Administrator
 * @ClassName Movie_Simple_Fragment
 * @Description 点击电影之后，进入的界面
 * @date 2015年10月15日
 */
public class Movie_Simple_Fragment extends Fragment implements OnClickListener,
        OnItemClickListener, RefreshListener, LoadingListener,
        Handler.Callback, successListener, OnQueryTextListener {

    private static final int SORT_DIS = 0;

    private ArrayList<CinemaForMovie> cinemaInfos;
    private Movie movieInfo;
    private Bitmap bitmap;
    private ImageUtils imageUtils;
    private MyContentResolver resolver;
    private Movie movie;
    View view;
    LinearLayout layout;
    ImageView tomovie_info;
    ImageView movie_image;
    MyFreshListView listView;
    TextView movie_simpleinfo_name, movie_simpleinfo_kind_movie,
            movie_simpleinfo_countryAnddur, movie_simpleinfo_movie_startDate;
    RatingBar ratingBar;
    private String url;
    private int id;
    private CinemaforMovieListAdapter cAdapter;
    private Handler handler;
    private JuHeUtils juHeUtils;
    ProgressDialog progressDialog;
    private Bitmap bitmap2;

    private LatLng lend;
    private LatLng rend;

    public Movie_Simple_Fragment(int id, String url) {
        System.out.println("id:" + id + "  url:" + url);
        imageUtils = ImageUtils.getInstence();
        this.url = url;
        this.id = id;
        cinemaInfos = new ArrayList<CinemaForMovie>();
        handler = new Handler(this);
        juHeUtils = new JuHeUtils();
        juHeUtils.setsListener(this);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resolver = new MyContentResolver(getActivity());
        loadImage(url);
        getMovieInfo(id);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        setHasOptionsMenu(true);

    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.movie_cinema_view, null);
        // ((TextView) getActivity().getActionBar().getCustomView()
        // .findViewById(R.id.actionbar_title)).setText("电影信息");
        getActivity().getActionBar().setTitle("电影信息");

        layout = (LinearLayout) view.findViewById(R.id.movie_simpleinfo_top);
        tomovie_info = (ImageView) view.findViewById(R.id.to_movieinfo);
        movie_image = (ImageView) view
                .findViewById(R.id.movie_simpleinfo_image);
        movie_simpleinfo_countryAnddur = (TextView) view
                .findViewById(R.id.movie_simpleinfo_countryAnddur);
        movie_simpleinfo_kind_movie = (TextView) view
                .findViewById(R.id.movie_simpleinfo_kind_movie);
        movie_simpleinfo_movie_startDate = (TextView) view
                .findViewById(R.id.movie_simpleinfo_movie_startDate);
        movie_simpleinfo_name = (TextView) view
                .findViewById(R.id.movie_simpleinfo_name);
        ratingBar = (RatingBar) view.findViewById(R.id.movie_simpleinfo_rat);

        tomovie_info.setOnClickListener(this);

        listView = (MyFreshListView) view
                .findViewById(R.id.movie_simpleinfo_cinemalist);
        listView.setOnRefreshListener(this);
        cAdapter = new CinemaforMovieListAdapter(cinemaInfos, getActivity());
        listView.setAdapter(cAdapter);
        listView.setOnItemClickListener(this);
        listView.setOnLoadingListener(this);
        // Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
        // R.drawable.image1);
        // Bitmap newbitmap = imageUtils.blurBitmap(getContext(), bitmap);
        // Bitmap newbitmap = BitmapFactory.decodeResource(getResources(),
        // R.drawable.test);
        initView();
        // blur(bitmap, layout, 1.0f);
        return view;

    }

    @SuppressLint("NewApi")
    public void initView() {
        movie_simpleinfo_name.setText(movie.getM_name());
        movie_simpleinfo_countryAnddur.setText(movie.getM_district() + "/"
                + movie.getM_duration());
        movie_simpleinfo_kind_movie.setText(movie.getM_genres());
        movie_simpleinfo_movie_startDate.setText(movie.getM_date());
        ratingBar.setRating((float) movie.getM_grade() / 2);
        juHeUtils.getCinemas(getActivity(), id);

    }

    @Override
    public void onClick(View v) {
        if (imageClickedListener != null) {
            imageClickedListener.toMovieInfo(movie.getM_id(),
                    movie.getM_poster());
        }
    }

    // 接口
    public interface ImageClickedListener {
        void toMovieInfo(int id, String url);
    }

    private ImageClickedListener imageClickedListener;

    // 设置监听
    public void setToMovieInfoImageClicked(
            ImageClickedListener imageClickedListener) {
        this.imageClickedListener = imageClickedListener;
    }

    // 定义接口
    public interface cinemaClickedListener {
        void onCinemaClicked(int cinemaID);
    }

    // 设置回调接口
    public void setOnCinemaClickedListener(cinemaClickedListener listener) {
        this.clickedListener = listener;
    }

    // 定义接口变量
    private cinemaClickedListener clickedListener;
    private SearchView searchView;
    private boolean showMovie = true;

    private LatLng start;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        if (clickedListener != null) {
            clickedListener.onCinemaClicked(Integer.parseInt(cinemaInfos.get(
                    position).getCinemaId()));
        }
    }

    @Override
    public void onRefreshing() {
        listView.onRefreshSuccess();
    }

    private void loadImage(String url) {
        bitmap = imageUtils.getBitmap(url);
    }

    private void getMovieInfo(int id) {
        Cursor cursor = resolver.selectMovieByID(id);
        movie = new Movie();
        while (cursor.moveToNext()) {
            movie.setM_poster(cursor.getString(cursor
                    .getColumnIndex(MovieCloumns.POSTER)));
            movie.setM_name(cursor.getString(cursor
                    .getColumnIndex(MovieCloumns.NAME)));
            movie.setM_genres(cursor.getString(cursor
                    .getColumnIndex(MovieCloumns.GENRES)));
            movie.setM_grade(cursor.getFloat(cursor
                    .getColumnIndex(MovieCloumns.GRADE)));
            movie.setM_district(cursor.getString(cursor
                    .getColumnIndex(MovieCloumns.DISTRICT)));
            movie.setM_date(cursor.getString(cursor
                    .getColumnIndex(MovieCloumns.DATE)));
            movie.setM_duration(cursor.getString(cursor
                    .getColumnIndex(MovieCloumns.DURATION)));
            movie.setM_id(cursor.getInt(cursor.getColumnIndex(MovieCloumns._ID)));
        }
    }

    @Override
    public void onLoading() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        listView.onLoadingSuccess();
        Toast.makeText(getActivity(), "没有更多数据", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("NewApi")
    @Override
    public boolean handleMessage(Message msg) {
        if (bitmap == null) {
            ImageUtils.getInstence().setQueue(getActivity());
            ImageUtils.getInstence().loadImageUseVolley_ImageLoad(movie_image,
                    url, layout);
        } else {
            movie_image.setImageBitmap(bitmap);
            imageUtils.blur(getActivity(), bitmap, layout);
        }
        cinemaInfos.addAll((ArrayList<CinemaForMovie>) msg.obj);
        cAdapter.notifyDataSetChanged();
        progressDialog.dismiss();
        return true;
    }

    @Override
    public void success(ArrayList<CinemaForMovie> cinemas) {
        Message msg = new Message();
        msg.obj = cinemas;
        handler.sendMessage(msg);
    }

    @Override
    public void success(MovieforCinema movieforCinema) {
        // TODO Auto-generated method stub

    }

    @Override
    public void success(Movie movie) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_main, menu);
        menu.removeItem(R.id.menu_movie_sort_grade);
        searchView = (SearchView) menu.findItem(R.id.menu_search)
                .getActionView();
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (cinemaInfos.size() <= 0) {
            return false;
        }
        cAdapter.getFilter().filter(newText);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_show_cinema_only:
                if (showMovie) {
                    layout.setVisibility(View.GONE);
                    item.setTitle("显示影片");
                    showMovie = false;
                } else {
                    layout.setVisibility(View.VISIBLE);
                    item.setTitle("隐藏影片");
                    showMovie = true;
                }
                break;

            case R.id.menu_movie_sort_dis:
                mySort(SORT_DIS);
                break;
            default:
                break;
        }
        return true;
    }

    private void mySort(int mode) {

        if (mode == SORT_DIS) {
            start = new LatLng(MyApplication.getLat(), MyApplication.getLon());

            Collections.sort(cinemaInfos, new Comparator<CinemaForMovie>() {

                @Override
                public int compare(CinemaForMovie lhs, CinemaForMovie rhs) {
                    lend = new LatLng(lhs.getLatitude(), lhs.getLongitude());
                    rend = new LatLng(rhs.getLatitude(), rhs.getLongitude());

                    if (DistanceUtil.getDistance(start, lend) > DistanceUtil
                            .getDistance(start, rend)) {
                        return 1;
                    }
                    return -1;
                }
            });
        }
        cAdapter.notifyDataSetChanged();
    }
}
