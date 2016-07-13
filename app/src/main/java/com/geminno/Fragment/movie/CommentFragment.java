package com.geminno.Fragment.movie;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geminno.Adapter.movie.CommentAdapter;
import com.geminno.Bean.Discuss;
import com.geminno.Service.InternetService;
import com.geminno.Utils.ServletUtil;
import com.geminno.View.MyFreshListView;
import com.geminno.View.MyFreshListView.LoadingListener;
import com.geminno.View.MyFreshListView.RefreshListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import geminno.com.hiweek_android.R;

public class CommentFragment extends Fragment implements RefreshListener,
        LoadingListener {
    private MyFreshListView listview;
    private int MovieID;
    private ArrayList<Discuss> comments;
    private Gson gson;
    private CommentAdapter adapter;
    private boolean more_data;
    private int data_page;
    private boolean refresh = false;
    private boolean loading = false;
    private TextView tips;

    public CommentFragment(int movieID) {
        gson = new Gson();
        more_data = true;
        data_page = 0;
        this.MovieID = movieID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comment_fragment_view, null);
        tips = (TextView) view.findViewById(R.id.tips);
        tips.setVisibility(View.GONE);
        listview = (MyFreshListView) view.findViewById(R.id.comment_list);
        listview.setOnRefreshListener(this);
        listview.setOnLoadingListener(this);
        listview.setAdapter(adapter);
        // ((TextView) getActivity().getActionBar().getCustomView()
        // .findViewById(R.id.actionbar_title)).setText("评论");
        getActivity().getActionBar().setTitle("评论");
        new myAsyncTask().execute(0 + "", null, null);
        comments = new ArrayList<Discuss>();
        adapter = new CommentAdapter(getActivity(), comments);
        return view;
    }


    /**
     * @Title: onLoading
     * @Description:加载数据
     */
    @Override
    public void onLoading() {
        // 如果还有更多数据
        if (more_data) {
            loading = true;
            new myAsyncTask().execute((++data_page) + "", null, null);
        } else {
            tips.setVisibility(View.VISIBLE);
            tips.setText("没有更多数据了");
            listview.onLoadingSuccess();
        }
    }

    /**
     * @Title: onRefreshing
     * @Description: 刷新数据
     */
    @Override
    public void onRefreshing() {
        refresh = true;
        new myAsyncTask().execute(0 + "", null, null);
        data_page = 0;
        tips.setVisibility(View.GONE);
    }

    private class myAsyncTask extends AsyncTask<String, Void, Void> {
        private ArrayList<Discuss> discusses;

        @Override
        protected Void doInBackground(String... params) {
            // 参数长度为0表示是初次进入，则查询前10条记录
            URL url = null;
            try {
                url = new URL(InternetService.ServletURL + "MovieComment");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("Movie_ID", MovieID + "");
            paramsMap.put("page", params[0]);
            String jsonString = ServletUtil.getInstence().getString(url,
                    paramsMap);
            Type type = new TypeToken<ArrayList<Discuss>>() {
            }.getType();
            discusses = gson.fromJson(jsonString, type);
            if (discusses.size() < 10) {
                more_data = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (refresh) {
                comments.clear();
                listview.onRefreshSuccess();
                refresh = false;
            }
            if (loading) {
                listview.onLoadingSuccess();
                loading = false;
            }
            for (Discuss discuss : discusses) {
                comments.add(discuss);
            }
            adapter.notifyDataSetChanged();
        }

    }
}
