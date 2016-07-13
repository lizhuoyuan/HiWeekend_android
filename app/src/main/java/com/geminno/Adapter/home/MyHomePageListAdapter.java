package com.geminno.Adapter.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import geminno.com.hiweek_android.R;


/**
 * @ClassName: MyHomePageListAdapter
 * @Description: 首页的适配器
 * @author: XU
 * @date: 2015年10月16日 下午1:31:21
 */
public class MyHomePageListAdapter extends BaseAdapter {

    private static final String BUTTON_KEY = "bt_txt";
    private static final String TEXTVIEW_KEY = "tv_txt";
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, String>> itemInfos;// 每个item信息的集合
    private HashMap<String, String> map;

    public MyHomePageListAdapter(Context context) {

        inflater = LayoutInflater.from(context);
        itemInfos = new ArrayList<HashMap<String, String>>();
        initViews();
    }

    // 根据数据创建view集合的大小，并获取信息集合
    private void initViews() {
        for (int i = 0; i < 10; i++) {
            map = new HashMap<String, String>();
            map.put(BUTTON_KEY, "button" + i);
            map.put(TEXTVIEW_KEY, "text" + i);
            itemInfos.add(map);
        }
    }

    @Override
    public int getCount() {
        return itemInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    // 根据信息集合 创建相应的View 并根据网址获取图片， 返回View
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.home_page_list_item, null);
            viewHolder.bt = (Button) convertView
                    .findViewById(R.id.home_page_list_item_bt);
            viewHolder.tv = (TextView) convertView
                    .findViewById(R.id.home_page_list_item_tv);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        map = itemInfos.get(position);
        viewHolder.tv.setText((String) map.get(TEXTVIEW_KEY));
        viewHolder.bt.setText(map.get(BUTTON_KEY).toString());
        return convertView;
    }

    private class ViewHolder {
        TextView tv;
        Button bt;
    }
}
