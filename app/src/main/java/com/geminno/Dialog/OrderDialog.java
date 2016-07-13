package com.geminno.Dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.geminno.Application.MyApplication;
import com.geminno.Bean.Action_order;
import com.geminno.Utils.MyPropertiesUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;

import geminno.com.hiweek_android.R;

/*
 * 
 * 李卓原，郑雅倩
 */
public class OrderDialog {
    private Context context;
    LayoutInflater inflater;
    Action_order ao1;
    String json;

    public OrderDialog() {
        super();
    }

    public OrderDialog(Context context, Action_order ao1) {
        this.context = context;
        this.ao1 = ao1;
        inflater = LayoutInflater.from(context);
    }

    public void payforactiondialog() {

        final LinearLayout rl = (LinearLayout) inflater
                .inflate(R.layout.pay_for_ok1, null);
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

    flagbian fl;

    public interface flagbian {
        public void flagbianclick();
    }

    public void setflagclick(flagbian fl) {
        this.fl = fl;
    }

    private void updatexutil() {
        RequestParams params = new RequestParams("utf-8");
        try {
            Gson gson = new Gson();
            json = gson.toJson(ao1);
            json = URLEncoder.encode(json, "utf-8");
            params.addBodyParameter("ao1", json);
            int credit = ao1.getUser().getU_credit();
            System.out.println("ao1.getUser().getU_credit()=" + credit);
            HttpUtils httpUtils = new HttpUtils();
            httpUtils.send(HttpRequest.HttpMethod.POST,
                    MyPropertiesUtil.getProperties(context).getProperty("url")
                            + "/HiWeek/servlet/client/UpdataActionState",
                    params, new RequestCallBack<String>() {

                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> arg0) {

                            if (fl != null) {
                                fl.flagbianclick();
                            }
                            Toast toast = Toast.makeText(context, "支付成功!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            // 改变MyApplication和SharedPreferences的积分和余额
                            MyApplication.setYue(
                                    MyApplication.getYue() - ao1.getAo_price());
                            MyApplication.setCredit(MyApplication.getCredit()
                                    + (int) ao1.getAo_price());
                            int credit = MyApplication.getCredit();
                            System.out.println(
                                    "MyApplication.setCredit=" + credit);
                            changeInfoToSP();

                            Timer timer = new Timer();
                            TimerTask tast = new TimerTask() {
                                @Override
                                public void run() {
                                    if (listener != null) {
                                        listener.onSuccessClicked();
                                    }
                                }
                            };
                            timer.schedule(tast, 1500);

                        }

                    });
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public interface OnsuccessClickedListener {
        void onSuccessClicked();
    }

    private OnsuccessClickedListener listener;

    public void setOnSuccessClickedListener(OnsuccessClickedListener listener) {
        this.listener = listener;
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
