package com.geminno.Fragment.home;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.geminno.Activities.activity.AllActivity;
import com.geminno.Activities.activity.ZhangZiSshiActivity;
import com.geminno.Adapter.activity.Gridadapter;
import com.geminno.hiweek1_0.ThreeActivity;

import geminno.com.hiweek_android.R;

/**
 * @author 李卓原 创建时间：2015年10月12日 下午2:06:50
 */
public class ActivityFragment extends Fragment implements OnClickListener {

    private GridView gv;
    private Gridadapter adapter;
    String[] al = {"涨姿势", "看电影", "热门商圈", "吃货", "溜娃儿", "爱折腾", "文艺范", "凑热闹",
            "零基础"};
    private EditText mEtSearch = null;// 输入搜索内容
    private LinearLayout mLayoutClearSearchText = null;
    private ImageView btnsearch, imgall, imgben, imgfu, imghot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_b, null);
        // lv = (ListView) root.findViewById(R.id.listview);
        gv = (GridView) root.findViewById(R.id.gridView1);
        btnsearch = (ImageView) root.findViewById(R.id.btnsearch);
        imgall = (ImageView) root.findViewById(R.id.imgall);
        imgben = (ImageView) root.findViewById(R.id.imgbenzhou);
        imgfu = (ImageView) root.findViewById(R.id.imgfujin);
        imghot = (ImageView) root.findViewById(R.id.imgremen);
        imgall.setOnClickListener(this);
        imgben.setOnClickListener(this);
        imgfu.setOnClickListener(this);
        imghot.setOnClickListener(this);
        btnsearch.setOnClickListener(this);
    /*
	 * imgall.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) {
	 * 
	 * } });
	 */
	/*
	 * btnsearch.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { if
	 * (!TextUtils.isEmpty(mEtSearch.getText().toString())) { Intent i = new
	 * Intent(getActivity(), ZhangZiSshiActivity.class);
	 * i.putExtra("search", mEtSearch.getText().toString());
	 * System.out.println(mEtSearch.getText().toString()); startActivity(i);
	 * } else { mEtSearch.setHint("请输入搜索内容!!!"); } } });
	 */

        adapter = new Gridadapter(al, getActivity());
        gv.setAdapter(adapter);

        gv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(getActivity(),
                                ZhangZiSshiActivity.class));
                        break;
                    case 1:
                        if (getActivity() instanceof MovieItemOnclickedListener) {
                            ((MovieItemOnclickedListener) getActivity()).toMovie();
                        }
                        break;
                    default:
                        Intent intent = new Intent(getActivity(), AllActivity.class);
                        // intent.setData(Uri.parse("http://m.wanzhoumo.com/suzhou/huodong"));//
                        // Url
                        // intent.setAction(Intent.ACTION_VIEW);
                        intent.putExtra("url",
                                "http://m.wanzhoumo.com/lianyungang");
                        getActivity().startActivity(intent);
                        break;
                }

            }
        });

        mEtSearch = (EditText) root.findViewById(R.id.et_search);

        return root;

    }

    // 定义接口
    public interface MovieItemOnclickedListener {
        void toMovie();
    }

    // 设置绑定
    public void setOnMovieItemClickedListener(
            MovieItemOnclickedListener listener) {
        this.listener = listener;
    }

    // 拥有变量
    private MovieItemOnclickedListener listener;

    @Override
    public void onClick(View v) {
        Intent ii = new Intent(getActivity(), ThreeActivity.class);
        switch (v.getId()) {
            case R.id.imgall:
                Intent intent = new Intent(getActivity(), AllActivity.class);
                // intent.setData(Uri.parse("http://m.wanzhoumo.com/suzhou/huodong/genre/67"));//
                // Url
                // intent.setAction(Intent.ACTION_VIEW);
                intent.putExtra("url",
                        "http://m.wanzhoumo.com/suzhou/huodong/genre/67");
                getActivity().startActivity(intent); // 启动浏览器
                break;
            case R.id.btnsearch:
                if (!TextUtils.isEmpty(mEtSearch.getText().toString())) {
                    Intent i = new Intent(getActivity(), ZhangZiSshiActivity.class);
                    i.putExtra("search", mEtSearch.getText().toString());
                    System.out.println(mEtSearch.getText().toString());
                    startActivity(i);
                } else {
                    mEtSearch.setHint("请输入搜索内容!!!");
                }
                break;
            case R.id.imgbenzhou:
                startActivity(ii.putExtra("FLAG", ThreeActivity.THE_WEEK));
                break;
            case R.id.imgfujin:
                startActivity(ii.putExtra("FLAG", ThreeActivity.THE_NEAR));
                break;
            case R.id.imgremen:
                startActivity(ii.putExtra("FLAG", ThreeActivity.THE_HOT));
                break;
            default:
                break;
        }

    }
}
