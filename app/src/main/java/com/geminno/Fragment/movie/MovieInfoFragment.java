package com.geminno.Fragment.movie;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.navisdk.ui.widget.BNNetworkingDialog.OnBackPressedListener;
import com.geminno.Activities.movie.MovieActivity;
import com.geminno.Activities.setting.PersonActivity;
import com.geminno.Adapter.movie.CommentAdapter;
import com.geminno.Application.MyApplication;
import com.geminno.Bean.CinemaForMovie;
import com.geminno.Bean.Constants;
import com.geminno.Bean.Discuss;
import com.geminno.Bean.Movie;
import com.geminno.Bean.MovieforCinema;
import com.geminno.Bean.User;
import com.geminno.JuHe.JuHeUtils;
import com.geminno.JuHe.JuHeUtils.successListener;
import com.geminno.Resolver.MyContentResolver;
import com.geminno.Service.InternetService;
import com.geminno.Utils.ImageUtils;
import com.geminno.Utils.ServletUtil;
import com.geminno.View.MyListView;
import com.geminno.columns.Cloumns.MovieCloumns;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import geminno.com.hiweek_android.R;

/**
 * @author Administrator
 * @ClassName MovieInfoFragment
 * @Description 电影详细信息
 * @date 2015年10月15日
 */
public class MovieInfoFragment extends Fragment
        implements OnClickListener, successListener, Callback, OnRatingBarChangeListener, OnBackPressedListener {
    private static final int LOAD_FOR_LOCA = 0;
    private static final int LOAD_FOR_SREVLET = 1;
    private static final int LOAD_FOR_JUHE = 2;
    private static final int LOAD_SUCCESS = 3;

    private View view;
    private ImageView movie_image;
    private TextView Movie_name, Movie_kinds, Movie_cuntry_duration, Movie_date;
    private Button book_ticket;
    private TextView movie_director, movie_language, movie_year, Movie_introduce;
    private MyContentResolver resolver;
    private Movie movie;
    private Bitmap bitmap;
    private LinearLayout layout;
    private ProgressDialog progressDialog;
    private int movieID;
    private Context context;
    private Handler handler;
    private JuHeUtils juHeUtils;
    private Gson gson;
    private String movie_image_url;
    private CommentAdapter adapter;
    private ListView commentListView;
    private TextView more_comment;
    private MyListView myListView;
    private ScrollView scrollView;
    private CommentFragment commentFragment;
    private ServletUtil servletUtil;
    private ImageUtils imageUtils;
    private TextView comment;
    private PopupWindow pWindow;
    private RatingBar ratingBar;
    private EditText editText;
    private TextView rating, ratdis;
    private Button cancle, submit;
    private Discuss discuss;
    private SimpleDateFormat simpleDateFormat;
    private User user;
    private ArrayList<Discuss> comments;
    private boolean show;
    private Bundle bundle;
    public static int result;

    public MovieInfoFragment(int id, String url) {
        result = Activity.RESULT_CANCELED;

        movieID = id;
        this.movie_image_url = url;
        handler = new Handler(this);
        imageUtils = ImageUtils.getInstence();
        juHeUtils = new JuHeUtils();
        juHeUtils.setsListener(this);
        gson = new Gson();
        servletUtil = ServletUtil.getInstence();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        user = new User();
        comments = new ArrayList<Discuss>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    private boolean initMovieInfo(int id) {
        Cursor cursor = resolver.selectMovieByID(id);

        if (!cursor.moveToNext()) {
            return false;
        }
        cursor.moveToFirst();
        movie = new Movie();
        movie.setM_id(movieID);

        movie.setM_name(cursor.getString(cursor.getColumnIndex(MovieCloumns.NAME)));
        movie.setM_genres(cursor.getString(cursor.getColumnIndex(MovieCloumns.GENRES)));
        movie.setM_grade(cursor.getFloat(cursor.getColumnIndex(MovieCloumns.GRADE)));
        movie.setM_district(cursor.getString(cursor.getColumnIndex(MovieCloumns.DISTRICT)));
        movie.setM_date(cursor.getString(cursor.getColumnIndex(MovieCloumns.DATE)));
        movie.setM_duration(cursor.getString(cursor.getColumnIndex(MovieCloumns.DURATION)));
        movie.setM_director(cursor.getString(cursor.getColumnIndex(MovieCloumns.DIRECTOR)));
        movie.setM_language(cursor.getString(cursor.getColumnIndex(MovieCloumns.LANGUAGE)));
        movie.setM_introduce(cursor.getString(cursor.getColumnIndex(MovieCloumns.INTRODUCE)));
        movie.setM_year(cursor.getString(cursor.getColumnIndex(MovieCloumns.YEAR)));
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bundle = getArguments();
        view = inflater.inflate(R.layout.movie_info_view, null);
        // 找出所有控件
        layout = (LinearLayout) view.findViewById(R.id.top_ll);
        scrollView = (ScrollView) view.findViewById(R.id.scrollView1);
        movie_image = (ImageView) view.findViewById(R.id.movie_image);
        Movie_name = (TextView) view.findViewById(R.id.Movie_name);
        Movie_kinds = (TextView) view.findViewById(R.id.Movie_kinds);
        Movie_cuntry_duration = (TextView) view.findViewById(R.id.Movie_cuntry_duration);
        Movie_date = (TextView) view.findViewById(R.id.Movie_date);

        book_ticket = (Button) view.findViewById(R.id.book_ticket);
        if (!MovieActivity.SHOW_BOOK) {
            book_ticket.setVisibility(View.GONE);
        } else {
            book_ticket.setOnClickListener(this);
        }

        movie_director = (TextView) view.findViewById(R.id.movie_director);
        movie_language = (TextView) view.findViewById(R.id.movie_language);
        movie_year = (TextView) view.findViewById(R.id.movie_year);
        myListView = (MyListView) view.findViewById(R.id.myListView1);
        myListView.setEmptyView(view.findViewById(R.id.empty_item));
        // commentListView = (ListView)
        // view.findViewById(R.id.comment_for_movie);
        comment = (TextView) view.findViewById(R.id.comment);
        comment.setOnClickListener(this);
        more_comment = (TextView) view.findViewById(R.id.more_comment);
        more_comment.setOnClickListener(this);
        Movie_introduce = (TextView) view.findViewById(R.id.Movie_introduce);
        new myAsyncTask().execute(movieID);
        scrollView.smoothScrollTo(0, 0);
        return view;
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

	/*@Override
    public void onAttach(Context context) {
		super.onAttach(context);
		this.context = context;
		progressDialog = new ProgressDialog(context);
		resolver = new MyContentResolver(context);

	}*/

    @Override
    public void onResume() {
        super.onResume();
        System.out.println(movieID);
        progressDialog.show();
        // 本地数据库
        if (!initMovieInfo(movieID)) {
            // 查询服务器
            initMovieInfoFromServlet(movieID);

        } else {
            initView();
            initPopWindow();
            progressDialog.dismiss();
        }

    }

    private void initMovieInfoFromServlet(final int id) {
        new Thread() {
            public void run() {
                Map<String, String> paramsMap = new HashMap<String, String>();
                paramsMap.put("MovieID", id + "");
                try {
                    String jsonString = servletUtil.getString(new URL(InternetService.ServletURL + "GetMovieInfo"),
                            paramsMap);
                    if (!TextUtils.isEmpty(jsonString)) {
                        movie = gson.fromJson(jsonString, Movie.class);
                        handler.sendEmptyMessage(LOAD_SUCCESS);
                    } else {
                        handler.sendEmptyMessage(LOAD_FOR_JUHE);
                    }

                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            ;
        }.start();
    }

    @SuppressLint("NewApi")
    private void initView() {

        bitmap = ImageUtils.getInstence().getBitmap(movie_image_url);

        if (bitmap == null) {
            ImageUtils.getInstence().setQueue(getActivity());
            ImageUtils.getInstence().loadImageUseVolley_ImageLoad(movie_image, movie_image_url, layout);
        } else {
            movie_image.setImageBitmap(bitmap);
            imageUtils.blur(getActivity(), bitmap, layout);
        }

        movie_director.setText(movie.getM_director());
        movie_language.setText(movie.getM_language());
        Movie_cuntry_duration.setText(movie.getM_district() + "/" + movie.getM_duration());
        movie_year.setText(movie.getM_year());
        Movie_date.setText(movie.getM_date());
        Movie_kinds.setText(movie.getM_genres());
        Movie_introduce.setText(movie.getM_introduce());
        Movie_name.setText(movie.getM_name());
        adapter = new CommentAdapter(getActivity(), comments);
        // commentListView.setAdapter(adapter);
        myListView.setAdapter(adapter);
        // ((TextView) getActivity().getActionBar().getCustomView()
        // .findViewById(R.id.actionbar_title)).setText(movie.getM_name());
        getActivity().getActionBar().setTitle("详细信息");

    }

    // 定义接口
    public interface MovieBookedClickedListener {
        void onMovieBooked();
    }

    private MovieBookedClickedListener listener;

    public void setOnMovieBookedListener(MovieBookedClickedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.book_ticket:
                if (listener != null) {
                    listener.onMovieBooked();
                }
                break;
            case R.id.more_comment:
                commentFragment = new CommentFragment(movieID);
                getFragmentManager().beginTransaction().replace(R.id.main_movie_fl, commentFragment).addToBackStack(null)
                        .commit();
                break;
            case R.id.comment:
                if (!PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Constants.USER_STATUS, false)) {
                    Intent intent = new Intent();
                    intent.putExtra("FLAG", 1);
                    intent.setClass(context, PersonActivity.class);
                    startActivityForResult(intent, 0);
                } else {
                    showPop();
                }
                break;
            case R.id.cancle:
                if (pWindow != null) {
                    pWindow.dismiss();
                }
                break;
            case R.id.submit:
                if (checkContent()) {

                    initDisscuss();
                    System.out.println("MovieName:" + discuss.getMovie().getM_name());
                    System.out.println("UID" + discuss.getUser().getU_id());
                    new UploadComment().execute(null, null);
                }

                break;
            default:
                break;
        }

    }

    private boolean checkContent() {
        if (ratingBar.getRating() == 0) {
            Toast.makeText(context, "请给点分吧", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(editText.getText().toString().trim())) {
            Toast.makeText(context, "请输入评论内容", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void initDisscuss() {
        discuss = new Discuss();
        user.setU_id(MyApplication.getU_id());
        discuss.setD_cont(editText.getText().toString());
        discuss.setUser(user);
        discuss.setMovie(movie);
        discuss.setM_grade(ratingBar.getRating());
        discuss.setD_time(simpleDateFormat.format(new Date()));
    }

    private class myAsyncTask extends AsyncTask<Integer, Void, Void> {
        private ArrayList<Discuss> discusses;

        @Override
        protected Void doInBackground(Integer... params) {
            URL url = null;
            try {
                url = new URL(InternetService.ServletURL + "MovieComment");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("Movie_ID", params[0] + "");
            String jsonString = ServletUtil.getInstence().getString(url, paramsMap);
            Type type = new TypeToken<ArrayList<Discuss>>() {
            }.getType();
            discusses = gson.fromJson(jsonString, type);
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            comments.clear();
            comments.addAll(discusses);
            if (comments == null || comments.size() == 0) {
                more_comment.setVisibility(View.GONE);
            } else {
                if (comments.size() == 2) {
                    more_comment.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    private class UploadComment extends AsyncTask<Void, Void, Void> {
        private Discuss discuss;

        @Override
        protected void onPreExecute() {
            this.discuss = MovieInfoFragment.this.discuss;
        }

        @Override
        protected void onPostExecute(Void results) {
            new myAsyncTask().execute(movieID);
            pWindow.dismiss();
            Toast.makeText(context, "评论成功", Toast.LENGTH_SHORT).show();
            result = Activity.RESULT_OK;
        }

        @Override
        protected Void doInBackground(Void... params) {
            URL url = null;
            try {
                url = new URL(InternetService.ServletURL + "MovieComment");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            servletUtil.upload(url, discuss);
            return null;
        }

    }

    @Override
    public void success(ArrayList<CinemaForMovie> cinemas) {

    }

    @Override
    public void success(MovieforCinema movieforCinema) {

    }

    @Override
    public void success(Movie m) {
        movie = m;
        try {
            URL url = new URL(InternetService.ServletURL + "UpLoadMovieInfo");
            servletUtil.upload(url, m);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        handler.sendEmptyMessage(LOAD_SUCCESS);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case LOAD_SUCCESS:
                initView();
                initPopWindow();
                progressDialog.dismiss();
                break;
            case LOAD_FOR_JUHE:
                juHeUtils.getMovieInfo(getActivity(), movieID);
                break;
            default:
                break;
        }
        return true;

    }

    private void initPopWindow() {
        View content = getActivity().getLayoutInflater().inflate(R.layout.pop_view, null);

        ratingBar = (RatingBar) content.findViewById(R.id.movie_rating);
        ratingBar.setOnRatingBarChangeListener(this);

        editText = (EditText) content.findViewById(R.id.content);
        rating = (TextView) content.findViewById(R.id.rating);
        ratdis = (TextView) content.findViewById(R.id.rating_dis);
        cancle = (Button) content.findViewById(R.id.cancle);
        submit = (Button) content.findViewById(R.id.submit);

        cancle.setOnClickListener(this);
        submit.setOnClickListener(this);

        pWindow = new PopupWindow(content, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        pWindow.setTouchable(true);
        pWindow.setFocusable(true);
        pWindow.setOutsideTouchable(false);
        // pWindow.setBackgroundDrawable(getResources().getDrawable(
        // R.drawable.comment_bkg));
        pWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
    }

    public void showPop() {
        editText.setText("");
        ratingBar.setRating(0.0f);
        pWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        // popupWindow.showAsDropDown(v);
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float ratings, boolean fromUser) {
        if (!rating.isShown()) {
            rating.setVisibility(View.VISIBLE);

        }
        rating.setText(ratings + "分");
        switch (Math.round(ratings * 2)) {
            case 0:
                rating.setVisibility(View.GONE);
                ratdis.setText("请滑动星星评分");
                break;
            case 1:
            case 2:
                ratdis.setText("超烂");
                break;
            case 3:
            case 4:
                ratdis.setText("比较差");
                break;
            case 5:
            case 6:
                ratdis.setText("一般般");
                break;
            case 7:
            case 8:
                ratdis.setText("比较好");
                break;
            case 9:
                ratdis.setText("棒极了");

                break;
            case 10:
                ratdis.setText("完美");
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (result != Activity.RESULT_OK)
            result = Activity.RESULT_CANCELED;
        getActivity().onBackPressed();
    }

    // @Override
    // public boolean onKeyDown(int keyCode, KeyEvent event) {
    // // TODO Auto-generated method stub
    // if (keyCode == KeyEvent.KEYCODE_BACK) {
    // Intent i = new Intent();
    // i.putExtra("a", tv.getText().toString());
    // setResult(1, i);
    // }
    // return super.onKeyDown(keyCode, event);
    // }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.removeItem(R.id.menu_home);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
