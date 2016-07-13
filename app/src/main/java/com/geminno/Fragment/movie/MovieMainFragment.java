package com.geminno.Fragment.movie;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.geminno.Adapter.movie.CinemaListAdapter;
import com.geminno.Adapter.movie.CinemaListAdapter.onCinemaSearchListener;
import com.geminno.Adapter.movie.MoviePagerAdapter;
import com.geminno.Adapter.movie.MyRecycleViewAdapter;
import com.geminno.Adapter.movie.MyRecycleViewAdapter.onSearchMovieListener;
import com.geminno.Adapter.movie.SearchAdapter;
import com.geminno.Application.MyApplication;
import com.geminno.Bean.Cinema;
import com.geminno.Bean.Constants;
import com.geminno.Bean.Movie;
import com.geminno.Bean.SearchResult;
import com.geminno.Resolver.MyContentResolver;
import com.geminno.Service.InternetService;
import com.geminno.Utils.ServletUtil;
import com.geminno.View.MyFreshListView;
import com.geminno.View.MyFreshListView.LoadingListener;
import com.geminno.View.MyFreshListView.RefreshListener;
import com.geminno.columns.Cloumns.CinemaCloumns;
import com.geminno.columns.Cloumns.MovieCloumns;
import com.google.android.support.v4.view.ViewPager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import geminno.com.hiweek_android.R;


/**
 * @author Administrator
 * @ClassName MovieMainFragment
 * @Description TODO
 * @date 2015年10月14日
 */
public class MovieMainFragment extends Fragment implements OnTouchListener,
        OnItemClickListener, LoadingListener, RefreshListener,
        OnQueryTextListener, onSearchMovieListener, onCinemaSearchListener {
    private static final int SORT_DIS = 0;
    private static final int SORT_GRADE = 1;

    private LinearLayout layout;
    private MyFreshListView listview;
    private ViewPager vPager;
    private View movie_main_view;
    private DisplayMetrics dm;
    private ServletUtil servletUtil;
    private ArrayList<Cinema> Mcinemas;
    private int data_page;
    private boolean refresh = false;
    private boolean loading = false;
    private MyContentResolver resolver;
    private boolean more_data;
    private CinemaListAdapter cAdapter;
    private MoviePagerAdapter mAdapter;
    private MyRecycleViewAdapter rAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Movie> movies;
    private Movie movie;
    private Point point;
    private BroadcastReceiver searchReceiver;
    private SearchView searchView;
    private boolean showMovie = true;
    private LatLng lend;
    private LatLng rend;
    private LatLng start;
    private PopupWindow pWindow;
    private ListView search_result;
    private ArrayList<SearchResult> results;
    private SearchAdapter sAdapter;
    private View content;
    private SearchResult sr;
    private InputMethodManager inputMethodManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data_page = 0;
        servletUtil = ServletUtil.getInstence();
        more_data = true;
        WindowManager wm = getActivity().getWindowManager();
        point = new Point();
        wm.getDefaultDisplay().getSize(point);
        Mcinemas = new ArrayList<Cinema>();
        setHasOptionsMenu(true);
        results = new ArrayList<SearchResult>();
        inputMethodManager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // ((TextView) getActivity().getActionBar().getCustomView()
        // .findViewById(R.id.actionbar_title)).setText("电影");
        getActivity().getActionBar().setTitle("电影");
        // 解析布局文件
        movie_main_view = inflater.inflate(R.layout.movie_main2, null);
        // 获取Layout
        // layout = (LinearLayout) movie_main_view.findViewById(R.id.container);
        // vPager = (ViewPager)
        // movie_main_view.findViewById(R.id.movie_main_vp);
        // vPager.setOffscreenPageLimit(6);
        recyclerView = (RecyclerView) movie_main_view
                .findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        content = getActivity().getLayoutInflater().inflate(
                R.layout.search_popwindow, null);
        search_result = (ListView) content.findViewById(R.id.result_list);
        search_result.setEmptyView(content.findViewById(R.id.no_result));
        // 获取ListView
        listview = (MyFreshListView) movie_main_view.findViewById(R.id.my_lv);
        listview.setTextFilterEnabled(true);
        initData();

        return movie_main_view;
    }

    private void initData() {
        getMovies();

        rAdapter = new MyRecycleViewAdapter(point, getActivity(), movies);
        rAdapter.setOnSearchMovieListener(this);
        recyclerView.setAdapter(rAdapter);

        listview.setOnRefreshListener(this);
        listview.setOnLoadingListener(this);
        // mAdapter = new MoviePagerAdapter(getContext());
        cAdapter = new CinemaListAdapter(getActivity(), Mcinemas);
        cAdapter.setOnCinemaSearchListener(this);
        new MyAsyncTask().execute(data_page + "", null, null);

        // 设置适配器
        listview.setAdapter(cAdapter);
        listview.setOnItemClickListener(this);
        // vPager.setAdapter(mAdapter);
        // vPager.setPageMargin(20);
        // vPager.addOnPageChangeListener(this);
        // layout.setOnTouchListener(this);
        // 添加监听事件
        sAdapter = new SearchAdapter(getActivity(), results);
        search_result.setAdapter(sAdapter);
        search_result.setOnItemClickListener(this);
        resolver = new MyContentResolver(getActivity());
    }

    private void getMovies() {
        Cursor cursor = resolver.selectMovies(new String[]{MovieCloumns.NAME,
                MovieCloumns.POSTER, MovieCloumns._ID}, null, null);

        movies = new ArrayList<Movie>();
        while (cursor.moveToNext()) {
            movie = new Movie();
            movie.setM_name(cursor.getString(cursor
                    .getColumnIndex(MovieCloumns.NAME)));
            movie.setM_poster(cursor.getString(cursor
                    .getColumnIndex(MovieCloumns.POSTER)));
            movie.setM_id(cursor.getInt(cursor.getColumnIndex(MovieCloumns._ID)));
            movies.add(movie);
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
    }

    /*
     * 存放ViewPager的layout的点击事件，分发给viewpager
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return vPager.dispatchTouchEvent(event);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
                .getInstance(getActivity());
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.search_adapter:
                if (results.get(position).getType() == SearchResult.MOVIE) {
                    intent = new Intent("com.geminno.movieselected");
                    intent.putExtra("movieID", results.get(position).getId());
                    intent.putExtra("imageUrl", results.get(position).getUrl());
                } else {
                    intent = new Intent("com.geminno.cinemaClicked");
                    String cId = results.get(position).getId() + "";
                    intent.putExtra("cinemaID", Integer.parseInt(cId));
                }
                break;
            case R.id.cinema_list:

                intent = new Intent("com.geminno.cinemaClicked");
                String cId = Mcinemas.get(position).getC_id() + "";
                intent.putExtra("cinemaID", Integer.parseInt(cId));

                break;
            default:
                break;

        }
        localBroadcastManager.sendBroadcast(intent);

    }

    @Override
    public void onLoading() {
        if (PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getBoolean(Constants.SERVER_STATUS, false)) {

            if (more_data) {
                data_page++;
                loading = true;
                new MyAsyncTask().execute(data_page + "", null, null);
            } else {
                Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
                listview.onLoadingSuccess();
            }
        } else {
            listview.onLoadingSuccess();
            Toast.makeText(getActivity(), "网络不通，请检查网络", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRefreshing() {
        if (PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getBoolean(Constants.SERVER_STATUS, false)) {
            more_data = true;
            refresh = true;
            data_page = 0;
            new MyAsyncTask().execute(data_page + "", null, null);
        } else {
            listview.onRefreshSuccess();
            Toast.makeText(getActivity(), "网络不通，请检查网络", Toast.LENGTH_SHORT).show();
        }
    }

    private class MyAsyncTask extends AsyncTask<String, Void, Void> {
        ArrayList<Cinema> cinemas;

        @Override
        protected Void doInBackground(String... params) {

            if (data_page == 0) {
                return null;
            }
            URL url = null;
            try {
                url = new URL(InternetService.ServletURL + "LoadingCinemas");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Gson gson = new Gson();
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("pageSize", params[0]);
            String jsonString = servletUtil.getString(url, param);
            Type type = new TypeToken<ArrayList<Cinema>>() {
            }.getType();

            cinemas = gson.fromJson(jsonString, type);
            if (cinemas.size() < 10) {
                more_data = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (data_page == 0) {
                loadDatafromSQLite();
                refresh = false;
                return;
            }
            if (refresh) {
                Mcinemas.clear();
                listview.onRefreshSuccess();
            }
            if (loading) {
                listview.onLoadingSuccess();
            }
            for (Cinema cinema : cinemas) {
                Mcinemas.add(cinema);
            }
            cAdapter.notifyDataSetChanged();
        }
    }

    private void loadDatafromSQLite() {
        Mcinemas.clear();
        Cursor cursor = resolver.selectCinemas(null, null, null);
        Cinema cinema;
        while (cursor.moveToNext()) {
            cinema = new Cinema();
            cinema.setC_name(cursor.getString(cursor
                    .getColumnIndex(CinemaCloumns.NAME)));
            cinema.setC_id(cursor.getInt(cursor
                    .getColumnIndex(CinemaCloumns._ID)));
            cinema.setC_lat(cursor.getDouble(cursor
                    .getColumnIndex(CinemaCloumns.LAT)));
            cinema.setC_lon(cursor.getDouble(cursor
                    .getColumnIndex(CinemaCloumns.LON)));
            cinema.setC_grade(cursor.getFloat(cursor
                    .getColumnIndex(CinemaCloumns.GRADE)));
            Mcinemas.add(cinema);
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        cAdapter.notifyDataSetChanged();
        listview.onRefreshSuccess();
        refresh = false;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_main, menu);
        searchView = (SearchView) menu.findItem(R.id.menu_search)
                .getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("输入电影院、电影");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        results.removeAll(results);
        rAdapter.getFilter().filter(newText);
        cAdapter.getFilter().filter(newText);
        if (!TextUtils.isEmpty(newText)) {
            if (pWindow == null) {
                showPop();
            }
        } else {
            if (pWindow != null) {
                pWindow.dismiss();
                pWindow = null;
            }
        }

        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (pWindow != null) {
            pWindow.dismiss();
            pWindow = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_show_cinema_only:
                if (showMovie) {
                    recyclerView.setVisibility(View.GONE);
                    item.setTitle("显示影片");
                    showMovie = false;
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    item.setTitle("隐藏影片");
                    showMovie = true;
                }
                break;
            case R.id.menu_movie_sort_grade:
                mySort(SORT_GRADE);
                break;
            case R.id.menu_movie_sort_dis:
                mySort(SORT_DIS);
                break;
            case R.id.menu_light:
                // getActivity().setTheme(R.style.MyThemeNight);
                // getActivity().recreate();
                break;
            default:
                break;
        }
        return true;
    }

    // 排序
    private void mySort(int mode) {
        if (mode == SORT_GRADE) {

            Collections.sort(Mcinemas, new Comparator<Cinema>() {

                @Override
                public int compare(Cinema lhs, Cinema rhs) {
                    if (lhs.getC_grade() < rhs.getC_grade()) {
                        return 1;
                    }
                    return -1;
                }
            });
        }
        if (mode == SORT_DIS) {
            start = new LatLng(MyApplication.getLat(), MyApplication.getLon());

            Collections.sort(Mcinemas, new Comparator<Cinema>() {

                @Override
                public int compare(Cinema lhs, Cinema rhs) {
                    lend = new LatLng(lhs.getC_lat(), lhs.getC_lon());
                    rend = new LatLng(rhs.getC_lat(), rhs.getC_lon());

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

    @SuppressLint("NewApi")
    public void showPop() {

        pWindow = new PopupWindow(content, searchView.getWidth(),
                LayoutParams.WRAP_CONTENT);
        pWindow.setTouchable(true);
        // popupWindow.setFocusable(true);
        pWindow.setOutsideTouchable(true);
        pWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.comment_bkg));
        pWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

        pWindow.setAnimationStyle(android.R.style.Animation_Toast);
        pWindow.showAsDropDown(searchView);
        // popupWindow.showAsDropDown(v);
    }

    @Override
    public void onMovieResult(ArrayList<Movie> moviess) {

        for (Movie movie : moviess) {
            sr = new SearchResult();
            sr.setId(movie.getM_id());
            sr.setTitle(movie.getM_name());
            sr.setUrl(movie.getM_poster());
            sr.setType(SearchResult.MOVIE);
            results.add(sr);
        }
        sAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCinemaResult(ArrayList<Cinema> cinemas) {
        for (Cinema cinema : cinemas) {
            sr = new SearchResult();
            sr.setId(cinema.getC_id());
            sr.setTitle(cinema.getC_name());
            sr.setType(SearchResult.CINEMA);
            results.add(sr);
        }
        sAdapter.notifyDataSetChanged();
    }

}
