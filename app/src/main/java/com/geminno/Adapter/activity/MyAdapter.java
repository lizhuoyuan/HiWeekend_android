package com.geminno.Adapter.activity;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.geminno.Bean.Action;
import com.geminno.Utils.MyPropertiesUtil;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.Properties;

import geminno.com.hiweek_android.R;

/**
 * 显示所有活动的图片 用于涨姿势页面
 * 
 * @author 李卓原 创建时间：2015年10月17日 上午9:17:33
 * 
 */
public class MyAdapter extends BaseAdapter {
	ArrayList<Action> ac;
	Context context;
	LayoutInflater inflater;

	public MyAdapter(Context context, ArrayList<Action> ac) {
		this.context = context;
		this.ac = ac;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ac.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public static class ViewHolder {
		ImageView iv;
		TextView tv;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_list, null);
			holder = new ViewHolder();
			holder.iv = (ImageView) convertView.findViewById(R.id.imageView1);
			holder.tv = (TextView) convertView.findViewById(R.id.tt);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 设置资源
		holder.tv.setText(ac.get(position).getA_itemname());
		Properties p = MyPropertiesUtil.getProperties(context);
		String ip = p.getProperty("url");
		String imgurl = ip + "/HiWeek/ActionPhotos/5/" + ac.get(position).getA_photo();
		BitmapUtils bitmapUtils = new BitmapUtils(context);

		// 加载网络图片
		bitmapUtils.display(holder.iv, imgurl);

		// 使用ListView等容器展示图片时可通过PauseOnScrollListener控制滑动和快速滑动过程中时候暂停加载图片
		// convertView.setOnScrollListener(new
		// PauseOnScrollListener(bitmapUtils, false, true));
		/*
		 * listView.setOnScrollListener(new PauseOnScrollListener(bitmapUtils,
		 * false, true, customListener));
		 */

		/*
		 * RequestQueue queue = Volley.newRequestQueue(context); //缓存操作 final
		 * LruCache<String, Bitmap> lruCache = new LruCache<>(50); ImageCache
		 * imageCache = new ImageCache() {
		 * 
		 * @Override public void putBitmap(String url, Bitmap bitmap) {
		 * lruCache.put(url, bitmap);
		 * 
		 * }
		 * 
		 * @Override public Bitmap getBitmap(String url) { // TODO
		 * Auto-generated method stub return lruCache.get(url); } };
		 * 
		 * //通过ImageLoader加载
		 * 
		 * ImageLoader imageLoader = new ImageLoader(queue, imageCache);
		 * //创建ImageListen监听 ImageListener imageListener =
		 * imageLoader.getImageListener(holder.iv, R.drawable.ic_launcher,
		 * R.drawable.ic_launcher); //加载 imageLoader.get(imgurl, imageListener);
		 */

		// holder.iv.setImageResource(ac.get(position).getA_photo());
		return convertView;
	}

	public Filter getFilter() {
		Filter filter = new Filter() {

			private ArrayList<Action> originaList;

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				ac = (ArrayList<Action>) results.values;
				int count = ac.size();
				System.out.println(count);
				notifyDataSetChanged();
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults filterResults = new FilterResults();
				ArrayList<Action> filteredlArrayList = new ArrayList<Action>();
				if (originaList == null) {
					originaList = new ArrayList<Action>(ac);

				}
				if (TextUtils.isEmpty(constraint.toString().trim())) {
					filterResults.count = originaList.size();
					filterResults.values = originaList;
				} else {
					for (Action cinemaForMovie : originaList) {
						if (cinemaForMovie.getA_itemname().contains(constraint)) {
							filteredlArrayList.add(cinemaForMovie);
						}
					}
					filterResults.count = filteredlArrayList.size();
					filterResults.values = filteredlArrayList;
				}
				return filterResults;
			}
		};
		return filter;
	}

}
