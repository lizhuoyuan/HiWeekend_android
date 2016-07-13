package com.geminno.Reciver;

import java.util.ArrayList;

import com.geminno.Application.MyApplication;
import com.geminno.Bean.AllOrder;
import com.geminno.Utils.MyPropertiesUtil;
import com.geminno.Utils.OrderShuJu;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.widget.BaseAdapter;
import android.widget.Toast;

public class OrderReciver extends BroadcastReceiver {
	private ArrayList<AllOrder> orders;
	private Bundle bundle;
	private int id;
	private int sign;
	private BaseAdapter adapter;
	private int name;
	public OrderReciver(int name, BaseAdapter adapter,
			ArrayList<AllOrder> orders) {
		this.orders = orders;
		this.adapter = adapter;
		this.name = name;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals("com.geminno.Dialog.frags.delete.order")) {
			bundle = intent.getExtras();
			id = bundle.getInt("id");
			sign = bundle.getInt("sign");
			AllOrder allOrder = new AllOrder();
			allOrder.setId(id);
			allOrder.setSign(sign);
			orders.remove(allOrder);
			adapter.notifyDataSetChanged();
		} else {
			new OrderShuJu(context, 1, shuahandler).ordershuju();
		}
		// System.out.println(name + " " + adapter);
	}

	Handler shuahandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 0x11 :
					orders.clear();
					ArrayList<AllOrder> addlist = (ArrayList<AllOrder>) msg.obj;
					switch (name) {
						case 10 :
							orders.addAll(addlist);
							break;
						case 11 :
							for (int i = 0; i < addlist.size(); i++) {
								if (addlist.get(i).getstate() == 2
										|| addlist.get(i).getstate() == 3) {
									orders.add(addlist.get(i));
								}
							}
							break;
						case 12 :
							for (int i = 0; i < addlist.size(); i++) {
								if (addlist.get(i).getstate() == 1) {
									orders.add(addlist.get(i));
								}
							}
							break;
						case 13 :
							for (int i = 0; i < addlist.size(); i++) {
								if (addlist.get(i).getstate() == 2) {
									orders.add(addlist.get(i));
								}
							}
							break;
						default :
							break;
					}

					adapter.notifyDataSetChanged();
					break;
				case 0xA1 :
					// Toast.makeText(context, msg.obj.toString(), 0).show();
					break;
				case 0xB2 :
					// Toast.makeText(context, msg.obj.toString(), 0).show();
					break;
				default :
					break;
			}
		}

	};

}
