package com.geminno.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.geminno.Application.MyApplication;
import com.geminno.Bean.Action_order;
import com.geminno.Bean.AllOrder;
import com.geminno.Bean.Movie_order;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class OrderShuJu {
    private ArrayList<Action_order> actionorderlist;
    private ArrayList<Movie_order> movieorderlist;
    private ArrayList<AllOrder> list;
    private AllOrder a1;
    public static final int MOVIESIGN = 5;
    public static final int ACTIONSIGN = 4;
    private Context context;
    private int page;
    private Handler handler;

    public OrderShuJu(Context context, int page, Handler handler) {
	this.context = context;
	this.page = page;
	this.handler = handler;
    }

    public void ordershuju() {
	System.out.println("page__________" + page);
	RequestQueue mQueue = Volley.newRequestQueue(context);
	StringRequest stringRequest = new StringRequest(MyPropertiesUtil
		.getProperties(context).getProperty("url")
		+ "/HiWeek/servlet/GetOrderServlet?u_id="
		+ MyApplication.getU_id() + "&page=" + page,
		new Listener<String>() {

		    @Override
		    public void onResponse(String response) {
			if (response.equals("数据全部加载完成")) {
			    Message msg = new Message();
			    msg.what = 0xA1;
			    msg.obj = "数据全部加载完成";
			    handler.sendMessage(msg);
			} else {
			    actionorderlist = new ArrayList<Action_order>();
			    movieorderlist = new ArrayList<Movie_order>();
			    Gson gson = new Gson();
			    Type type;
			    if (response != null) {
				Object object;
				if ((object = JSON.parseObject(response).get(
					"1")) != null) {
				    String ActionjsonString = object.toString();
				    type = new TypeToken<ArrayList<Action_order>>() {
				    }.getType();
				    actionorderlist = gson.fromJson(
					    ActionjsonString, type);
				}
				if ((object = JSON.parseObject(response)
					.getString("2")) != null) {
				    String moviejsonString = object.toString();
				    type = new TypeToken<ArrayList<Movie_order>>() {
				    }.getType();
				    movieorderlist = gson.fromJson(
					    moviejsonString, type);
				}
			    }
			    jiexi();

			    Message msg = new Message();
			    msg.what = 0x11;
			    msg.obj = list;
			    handler.sendMessage(msg);
			}
		    }
		}, new com.android.volley.Response.ErrorListener() {

		    @Override
		    public void onErrorResponse(VolleyError error) {
			Message msg = new Message();
			msg.what = 0xB2;
			msg.obj = "网络错误";
			handler.sendMessage(msg);
		    }

		});
	mQueue.add(stringRequest);
    }

    public ArrayList<AllOrder> jiexi() {
	list = new ArrayList();
	for (int i = 0; i < movieorderlist.size() + actionorderlist.size(); i++) {
	    a1 = new AllOrder();
	    if (i < actionorderlist.size()) {
		a1.setId(actionorderlist.get(i).getAo_id());
		a1.setname(actionorderlist.get(i).getAction().getA_itemname());
		a1.setintroduce(actionorderlist.get(i).getAction()
			.getA_introduce());
		a1.setprice(actionorderlist.get(i).getAo_price());
		a1.setstate(actionorderlist.get(i).getAo_state());
		a1.setDate(actionorderlist.get(i).getAo_date());
		a1.setCount(actionorderlist.get(i).getAo_count());
		a1.setTel(actionorderlist.get(i).getUser().getU_tel());
		a1.setCredit(actionorderlist.get(i).getAo_credit());
		a1.setAction(actionorderlist.get(i).getAction());
		a1.setUser(actionorderlist.get(i).getUser());
		a1.setposter(actionorderlist.get(i).getAction().getA_photo());
		a1.setSign(ACTIONSIGN);
	    } else {
		// System.out.println(movieorderlist.get(i-actionorderlist.size()));
		a1.setId(movieorderlist.get(i - actionorderlist.size())
			.getMo_id());
		a1.setname(movieorderlist.get(i - actionorderlist.size())
			.getMovie().getM_name());
		a1.setintroduce(movieorderlist.get(i - actionorderlist.size())
			.getMovie().getM_introduce());
		a1.setprice(movieorderlist.get(i - actionorderlist.size())
			.getMo_price());
		a1.setstate(movieorderlist.get(i - actionorderlist.size())
			.getMo_state());
		a1.setDate(movieorderlist.get(i - actionorderlist.size())
			.getMo_date());
		a1.setSession(movieorderlist.get(i - actionorderlist.size())
			.getMo_session());
		a1.setCredit(movieorderlist.get(i - actionorderlist.size())
			.getMo_credit());
		a1.setSeat(movieorderlist.get(i - actionorderlist.size())
			.getMo_seat());
		a1.setCount(movieorderlist.get(i - actionorderlist.size())
			.getMo_count());
		a1.setUser(movieorderlist.get(i - actionorderlist.size())
			.getUser());
		a1.setMovie(movieorderlist.get(i - actionorderlist.size())
			.getMovie());
		a1.setC_id(movieorderlist.get(i - actionorderlist.size())
			.getCinema().getC_id());
		a1.setposter(movieorderlist.get(i - actionorderlist.size())
			.getMovie().getM_poster());
		a1.setSign(MOVIESIGN);
	    }
	    list.add(a1);
	}
	Collections.sort(list);
	return list;

    }
}