package com.geminno.Adapter.order;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geminno.Bean.AllOrder;
import com.geminno.Fragment.setting.AllOrderFragment;
import com.geminno.Utils.MyPropertiesUtil;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.Properties;

import geminno.com.hiweek_android.R;

/**
 * AllOrderListViewAdapter是四个fragment所共用的listview适配器
 * 设置每个item样式，4个fragment共用一个xml文件 接口用于回调在fragment实现接口
 * 
 * @author 郑雅倩
 *
 */
public class AllOrderListViewAdapter extends BaseAdapter {
	ArrayList<AllOrder> list;
	Context context;
	LayoutInflater inflater;
	MyclickedListener listener;
	String imgurl;
	String newimgurl;
	holder holder;
	
    Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==0x123){
				
			}
		}
    	
    };
	public AllOrderListViewAdapter() {
		super();
	}

	// 定义接口
	public interface MyclickedListener {
		public void clicked(View v, int position);
	}

	// 設置回調第二种方法
	public void setOnMyClickedListener(MyclickedListener listener) {
		this.listener = listener;
	}
	public void setchuanzhi() {

	}
	public AllOrderListViewAdapter(ArrayList<AllOrder> list, Context context
			) {
		
		this.list = list;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		if(list!=null){
		return list.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new holder();
			convertView = inflater.inflate(R.layout.all_order_listview_adapter,
					null);
			holder.tv1 = (TextView) convertView
					.findViewById(R.id.all_order_listview_adapter_c_name);
			holder.tv2 = (TextView) convertView
					.findViewById(R.id.all_order_listview_adapter_mo_state);
			holder.iv1 = (ImageView) convertView
					.findViewById(R.id.all_order_listview_adapter_mo_poster);
			holder.tv3 = (TextView) convertView
					.findViewById(R.id.all_order_listview_adapter_m_introduce);
			holder.tv4 = (TextView) convertView
					.findViewById(R.id.all_order_listview_adapter_mo_price);
			// holder.tv5=(TextView)
			// convertView.findViewById(R.id.all_order_listview_adapter_mo_session);
			holder.tv6 = (TextView) convertView.findViewById(
					R.id.all_order_listview_adapter_shiji_mo_price);
			holder.tv7 = (TextView) convertView
					.findViewById(R.id.all_order_listview_adapter_count);
			holder.iv1 = (ImageView) convertView
					.findViewById(R.id.all_order_listview_adapter_mo_poster);
			holder.btn1 = (Button) convertView
					.findViewById(R.id.all_order_listview_adapter_cancel_order);
			holder.btn2 = (Button) convertView
					.findViewById(R.id.all_order_listview_adapter_delete_order);
			holder.btn3 = (Button) convertView
					.findViewById(R.id.all_order_listview_adapter_payer_order);
			holder.btn4 = (Button) convertView.findViewById(
					R.id.all_order_listview_adapter_discuss_order);

			// holder.lay1=(LinearLayout)
			// convertView.findViewById(R.id.all_order_listview_adapter_cinema_xiang);
			 holder.lay2=(LinearLayout)
			 convertView.findViewById(R.id.all_order_listview_adapter_move_order_xiang);
			convertView.setTag(holder);
		} else {
			holder = (holder) convertView.getTag();
		}

		holder.tv1.setText(list.get(position).getname());

		switch (list.get(position).getstate()) {
			case 1 :
				holder.tv2.setText("未支付");
				holder.btn2.setVisibility(View.GONE);
				holder.btn4.setVisibility(View.GONE);
				holder.btn3.setVisibility(View.VISIBLE);
				holder.btn1.setVisibility(View.VISIBLE);
				break;
			case 2 :
				holder.tv2.setText("已支付未评价");
				holder.btn3.setVisibility(View.GONE);
				holder.btn1.setVisibility(View.GONE);
				holder.btn2.setVisibility(View.VISIBLE);
				holder.btn4.setVisibility(View.VISIBLE);
				break;
			case 3 :
				holder.tv2.setText("已支付已评价");
				holder.btn4.setVisibility(View.GONE);
				holder.btn1.setVisibility(View.GONE);
				holder.btn3.setVisibility(View.GONE);
				holder.btn2.setVisibility(View.VISIBLE);
				break;
			default :
				break;
		}
		// holder.tv2.setText(list.get(position).getstate()+"");
		// System.out.println(list.get(position).getstate());
		holder.tv3.setText(list.get(position).getintroduce());
		System.out.println(list.get(position).getprice() + "");
		holder.tv4.setText(list.get(position).getprice() + "");
		// holder.tv5.setText(list.get(position).getMo_session());
		holder.tv6.setText((list.get(position).getDate()) + "");
		System.out.println("Count:" + list.get(position).getCount() + "");
		holder.tv7.setText(list.get(position).getCount() + "");
		Properties p = MyPropertiesUtil.getProperties(context);
		String ip = p.getProperty("url");
		imgurl = ip + "/HiWeek/ActionPhotos/5/";
		if (list.get(position).getSign() == AllOrderFragment.ACTIONSIGN) {
			newimgurl = imgurl + list.get(position).getposter();
		}
		if (list.get(position).getSign() == AllOrderFragment.MOVIESIGN) {
			newimgurl = list.get(position).getposter();
		}
				BitmapUtils bitmapUtils = new BitmapUtils(context);
				// 加载网络图片
				bitmapUtils.display(holder.iv1, newimgurl);
				handler.sendEmptyMessage(0x123);

		// 设置点击事件
		holder.btn1.setOnClickListener(new MyClicklistener(position));
		holder.btn2.setOnClickListener(new MyClicklistener(position));
		holder.btn3.setOnClickListener(new MyClicklistener(position));
		holder.btn4.setOnClickListener(new MyClicklistener(position));
		// holder.lay1.setOnClickListener(new MyClicklistener(position));
	   holder.lay2.setOnClickListener(new MyClicklistener(position));
		return convertView;
	}

	private class MyClicklistener implements View.OnClickListener {
		private int position;
		public MyClicklistener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if (listener != null) {
				listener.clicked(v, position);

			}
		}

	}

	public class holder {
		TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7;
		Button btn1, btn2, btn3, btn4;
		ImageView iv1;
		LinearLayout lay1, lay2;
	}

}