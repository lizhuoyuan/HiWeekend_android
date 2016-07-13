package com.geminno.Dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.geminno.Application.MyApplication;
import com.geminno.Bean.Movie_order;
import com.geminno.Service.InternetService;
import com.geminno.Utils.GetPostUtil;
import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;

import geminno.com.hiweek_android.R;

public class MovieOrderDialog implements Callback {
    private Context context;
    int u_id;
    int credit;
    double yue;
    EditText movieorderpayet;
    LayoutInflater inflater;
    public static boolean flag = false;
    Jiclicklistener listener;
    Dianclicklistener listener1;
    Movie_order mo1;
    String json;
    private PaySuccessListener paySuccessListener;
    private Handler handler;

    public MovieOrderDialog() {

    }

    /**
     * 改变flag的值
     */
    flagbian fl;

    public interface flagbian {
        public void flagbianclick();
    }

    public void setflagclick(flagbian fl) {
        this.fl = fl;
    }


    public MovieOrderDialog(Context context, Movie_order mo1) {
        this.context = context;
        this.mo1 = mo1;
        inflater = LayoutInflater.from(context);
    }

    // 电话的构造的方法
    public MovieOrderDialog(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    /**
     * 积分的接口
     */
    public interface Jiclicklistener {
        public void jiquelistener(boolean flag);
    }

    public void setonjijiclicklistener(Jiclicklistener listener) {
        this.listener = listener;
    }

    /**
     * 电话的接口
     */
    public interface Dianclicklistener {
        public void dianlistener(EditText et);
    }

    public void setdianclicklistener(Dianclicklistener listener1) {
        this.listener1 = listener1;
    }

    // 跳转
    public interface PaySuccessListener {
        void onPaySuccess();
    }

    public void setPaySuccessListener(PaySuccessListener listener) {
        this.paySuccessListener = listener;

    }

    /**
     * 电影订单的付款按钮
     */
    public void payformoviedialog() {
        handler = new Handler(this);

        RelativeLayout rl = (RelativeLayout) inflater.inflate(
                R.layout.pay_for_ok, null);
        movieorderpayet = (EditText) rl
                .findViewById(R.id.movie_order_pay_editText1);
        new AlertDialog.Builder(context).setTitle("请输入支付密码").setView(rl)
                .setPositiveButton("确定", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String Number;
                        Number = movieorderpayet.getText().toString();
                        if (TextUtils.isEmpty(Number)) {
                            Toast.makeText(context, "密码不能为空", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            return;
                        }
                        if (mo1.getUser().getU_yue() >= mo1.getMo_price()) {
                            if (Integer.parseInt(Number) == mo1.getUser()
                                    .getU_paynum()) {
                                System.out.println("输入成功");
                                System.out.println(mo1.getMo_id());
                                changejson();

                                GetPostUtil.sendPost(InternetService.ServletURL
                                        + "UpdateState", json, handler);

                            } else {
                                Toast.makeText(context, "输入密码不对", Toast.LENGTH_SHORT).show();
                            }

                            dialog.dismiss();
                        } else {
                            Toast.makeText(context, "余额不足", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("取消", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        }).create().show();
    }

    /**
     * @param
     * @return
     * @Description:上传的数据变为json
     */
    private void changejson() {
        Gson gson = new Gson();
        json = gson.toJson(mo1);
    }

    /**
     * 积分的dialog
     *
     * @param context
     * @param creadt  总共积分
     * @param price   总共价格
     */
    public void gradedialog(Context context, final int creadt,
                            final double price) {
        inflater = LayoutInflater.from(context);
        LinearLayout liear = (LinearLayout) inflater.inflate(
                R.layout.grade_dialog, null);
        final CheckBox ckb = (CheckBox) liear
                .findViewById(R.id.grade_dialog_checkBox1);
        // 设置checkbox的值
        if (creadt > price * 10) {
            ckb.setText(price * 10 + "");
        } else {
            ckb.setText(creadt + "");
        }

        ckb.setChecked(flag);

        ckb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                flag = isChecked;
            }
        });
        new AlertDialog.Builder(context).setTitle("是否使用积分").setView(liear)
                .setPositiveButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.jiquelistener(flag);
                        }
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) {
                    listener.jiquelistener(flag);
                }
                dialog.dismiss();
            }
        }).create().show();
    }

    /**
     * 电话dialog
     */
    public void teldialog() {

        RelativeLayout rl = (RelativeLayout) inflater.inflate(
                R.layout.tel_dialog, null);
        final EditText et = (EditText) rl
                .findViewById(R.id.tel_dialog_editText1);

        new AlertDialog.Builder(context).setTitle("是否确认更换电话号码").setView(rl)
                .setPositiveButton("确定", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (listener1 != null) {
                            listener1.dianlistener(et);
                        }
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        }).create().show();
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == 1) {
            if (fl != null) {
                fl.flagbianclick();
            }
            Toast toast = Toast.makeText(context, "支付成功!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            if (paySuccessListener != null) {
                MyApplication
                        .setYue(MyApplication.getYue() - mo1.getMo_price());
                MyApplication.setCredit(MyApplication.getCredit()
                        + (int) mo1.getMo_price());
                changeInfoToSP();

                Timer timer = new Timer();
                TimerTask tast = new TimerTask() {
                    @Override
                    public void run() {
                        paySuccessListener.onPaySuccess();
                    }
                };
                timer.schedule(tast, 1500);

            }
        }
        return true;
    }

    //改变SharedPreferences的值
    private void changeInfoToSP() {

        Editor editor = PreferenceManager.getDefaultSharedPreferences(context)
                .edit();

        editor.putFloat("user_YuE", (float) MyApplication.getYue());
        editor.putInt("user_credit", MyApplication.getCredit());

        editor.apply();
    }

}
