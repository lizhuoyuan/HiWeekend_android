package com.geminno.Fragment.setting;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.geminno.Utils.DataCleanManager;

import geminno.com.hiweek_android.R;

/**
 * @author 李卓原 创建时间：2015年10月28日 上午11:12:48
 */
public class FragMyseting extends Fragment {
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.what == 0x123) {
                Toast.makeText(getActivity(), "清除完成", Toast.LENGTH_SHORT).show();
            }
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getActionBar().setTitle("设置");
        View root = inflater.inflate(R.layout.fragment_myseting, null);
        TextView pinjia = (TextView) root
                .findViewById(R.id.textViewpingjiawomen);
        pinjia.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                getFragmentManager().beginTransaction()
                        .replace(R.id.myselffrg, new PingjiaFragment())
                        .addToBackStack(getTag()).commit();
            }
        });
        TextView lianxi = (TextView) root
                .findViewById(R.id.textViewlianxiwomen);
        lianxi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.myselffrg, new lianxiFragment())
                        .addToBackStack(getTag()).commit();

            }
        });
        TextView qinghuan = (TextView) root
                .findViewById(R.id.textViewqingchuhuancun);
        qinghuan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "正在清除...", Toast.LENGTH_SHORT).show();

                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        DataCleanManager acm = new DataCleanManager();
                        acm.clearAllCache(getActivity());
                        handler.sendEmptyMessage(0x123);
                    }
                }).start();
            }
        });
        return root;
    }

}
