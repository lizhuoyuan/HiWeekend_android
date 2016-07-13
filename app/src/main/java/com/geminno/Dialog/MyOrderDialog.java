package com.geminno.Dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.geminno.Adapter.order.AllOrderListViewAdapter;
import com.geminno.Application.MyApplication;
import com.geminno.Bean.AllOrder;
import com.geminno.Utils.GetPostUtil;
import com.geminno.Utils.MyPropertiesUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

import geminno.com.hiweek_android.R;

public class MyOrderDialog {
    private Context context;
    private ArrayList<AllOrder> list;
    private int position;
    double useprice;
    AllOrderListViewAdapter adapter;
    LayoutInflater inflater;
    EditText et;
    String json;
    double c_grade;
    int c_id;
    int i;
    int u_credit;
    Editor editor;
    reqingqiu relistenerl;
    ListView lv;

    public MyOrderDialog() {
        super();
    }

    public MyOrderDialog(Context context, ArrayList<AllOrder> list, int position) {
        this.context = context;
        this.list = list;
        this.position = position;
        inflater = LayoutInflater.from(context);
    }

    public MyOrderDialog(Context context, ArrayList<AllOrder> list,
                         int position, AllOrderListViewAdapter adapter, ListView lv) {
        this.context = context;
        this.list = list;
        this.position = position;
        this.adapter = adapter;
        this.lv = lv;
        inflater = LayoutInflater.from(context);
    }

    public interface reqingqiu {
        public void shujuclick();
    }

    public void setreqingqiuckick(reqingqiu relistener) {
        this.relistenerl = relistener;
    }

    /**
     * 接收删除操作成功的消息
     */
    Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {

                if (i == 0) {
                    // 给application和SharedPreferences
                    MyApplication.setCredit(u_credit);
                    editor = PreferenceManager.getDefaultSharedPreferences(
                            context).edit();
                    editor.putInt("user_credit", MyApplication.getCredit());
                }
                // 发送广播
                Intent intent = new Intent();
                intent.setAction("com.geminno.Dialog.frags.delete.order");
                Bundle bundle = new Bundle();
                bundle.putInt("id", list.get(position).getId());
                bundle.putInt("sign", list.get(position).getSign());
                intent.putExtras(bundle);
                context.sendOrderedBroadcast(intent, null);
                Toast.makeText(context, "操作成功", Toast.LENGTH_SHORT).show();
            }
        }

    };
    String title[] = {"是否确认取消订单", "是否确认删除订单"};

    /**
     * 生成取消和删除对话框 i==0 取消 i==1 删除
     *
     * @param i
     */
    public void dialog(final int i) {
        this.i = i;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage(title[i]);
        builder.setPositiveButton("确认", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                int id = list.get(position).getId();
                int sign = list.get(position).getSign();
                int u_id = list.get(position).getUser().getU_id();
                u_credit = MyApplication.getCredit()
                        + list.get(position).getCredit();
                GetPostUtil.sendGet(
                        new MyPropertiesUtil().getProperties(context)
                                .getProperty("url")
                                + "/HiWeek/servlet/client/DeleteOrder", "id="
                                + id + "&sign=" + sign + "&i=" + i + "&u_id="
                                + u_id + "&u_credit=" + u_credit, handle);

            }
        });
        builder.setNegativeButton("取消", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void paydialog() {
        final LinearLayout rl = (LinearLayout) inflater.inflate(
                R.layout.pay_for_ok1, null);
        new AlertDialog.Builder(context).setTitle("请输入支付密码").setView(rl)
                .setPositiveButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditText et = (EditText) rl.findViewById(R.id.etpaynum);
                        System.out.println(et.getText().toString());
                        if (et.getText().toString()
                                .equals(MyApplication.getU_paynum() + "")) {
                            updatexutil();
                            dialog.dismiss();
                        } else {
                            // dialog.dismiss();
                            Toast.makeText(context, "密码错误请重新输入", Toast.LENGTH_SHORT).show();
                            et.setText(null);
                        }
                    }
                }).setNegativeButton("取消", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        }).create().show();
    }

    private void updatexutil() {
        RequestParams params = new RequestParams("utf-8");
        int id = list.get(position).getId();
        int sign = list.get(position).getSign();
        useprice = list.get(position).getprice();
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(
                HttpRequest.HttpMethod.GET,
                MyPropertiesUtil.getProperties(context).getProperty("url")
                        + "/HiWeek/servlet/PaydioagServlet?id=" + id + "&sign="
                        + sign + "&useprice=" + useprice + "&newcredit="
                        + MyApplication.getCredit() + "&newyue="
                        + MyApplication.getYue() + "&u_id="
                        + MyApplication.getU_id(), params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // 改变MyApplication和SharedPreferences的积分和余额
                        MyApplication.setYue(MyApplication.getYue() - useprice);
                        MyApplication.setCredit(MyApplication.getCredit()
                                + (int) useprice);
                        int credit = MyApplication.getCredit();
                        System.out.println("MyApplication.setCredit=" + credit);
                        changeInfoToSP();
                        // 发送广播
                        Intent intent = new Intent();
                        intent.setAction("com.geminno.Dialog.frags.payfor.order");
                        context.sendOrderedBroadcast(intent, null);
                        Toast toast = Toast.makeText(context, "支付成功!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        // new OrderShuJu(context,1,shuahandler).ordershuju();
                    }

                });
    }

    // 改变SharedPreferences的值
    private void changeInfoToSP() {

        Editor editor = PreferenceManager.getDefaultSharedPreferences(context)
                .edit();

        editor.putFloat("user_YuE", (float) MyApplication.getYue());
        editor.putInt("user_credit", MyApplication.getCredit());
        editor.apply();
    }
}
