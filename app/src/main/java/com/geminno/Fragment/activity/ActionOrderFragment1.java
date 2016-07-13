package com.geminno.Fragment.activity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geminno.Application.MyApplication;
import com.geminno.Bean.Action_order;
import com.geminno.Utils.GetPostUtil;
import com.geminno.Utils.MyPropertiesUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import geminno.com.hiweek_android.R;


/**
 * 活动订单布局
 *
 * @author  李卓原
 */
public class ActionOrderFragment1 extends Fragment implements
        CompoundButton.OnCheckedChangeListener {
    View root;
    ImageView ivjia, ivjian, ivat, imgheart;
    TextView tvname, tvtime, tvprice, tvpay, tvyue;
    EditText etcount, ettel, etname;
    CheckBox ck;
    Double ao_price, a_price;
    Button btn;
    String json, a_photo;
    int result, jifen, u_credit;
    String time;
    Action_order ao1;
    Bundle b;
    boolean flag = true;
    Editor editor;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.what == 1) {
                result = Integer.parseInt((String) msg.obj);
                System.out.println("我真的改变了");
                // 设置MyApplication和SharedPreferences的积分
                MyApplication.setCredit(ao1.getUser().getU_credit());

                editor = PreferenceManager.getDefaultSharedPreferences(
                        getActivity()).edit();
                editor.putInt("user_credit", MyApplication.getCredit());
                editor.apply();
                System.out.println(result);// 返回的订单id
                ao1.setAo_id(result);
                PayFragment pf = new PayFragment();
                Bundle b = new Bundle();
                b.putSerializable("ao1", ao1);
                b.putString("tel", ettel.getText().toString());
                b.putString("name", etname.getText().toString());
                pf.setArguments(b);
                getFragmentManager().beginTransaction()
                        .replace(R.id.one_fragment, pf).commit();
            }
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().getActionBar().setTitle("订单详情");
        root = inflater.inflate(R.layout.action_order_fragment1, null);
        u_credit = MyApplication.getCredit();
        infind();
        ck.setOnCheckedChangeListener(this);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                double yue = MyApplication.getYue();
                if (yue < Double.valueOf(tvpay.getText().toString())) {
                    Toast.makeText(getActivity(), "对不起，余额不足", Toast.LENGTH_SHORT).show();
                } else if ((TextUtils.isEmpty(ettel.getText()))
                        || TextUtils.isEmpty(etcount.getText())
                        || ettel.getText().toString().length() != 11) {
                    Toast.makeText(getActivity(), "数量和电话不能乱写哦", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty(ettel.getText())
                        && TextUtils.isEmpty(etcount.getText())) {
                    Toast.makeText(getActivity(), "请填写正确的数量", Toast.LENGTH_SHORT).show();
                } else if (etcount.getText().toString().equals("0")) {
                    Toast.makeText(getActivity(), "请填写正确的数量", Toast.LENGTH_SHORT).show();
                } else {
                    if (flag) {
                        // System.out.println("fkjhjkgjlfkdjgg"+json);
                        changejson();
                        GetPostUtil
                                .sendPost(
                                        MyPropertiesUtil.getProperties(
                                                getActivity())
                                                .getProperty("url")
                                                + "/HiWeek/servlet/client/UpLoadAction_order",
                                        json, handler);
                        flag = false;
                    } else {
                        Toast.makeText(getActivity(), "请不要重复提交订单",
                                Toast.LENGTH_SHORT).show();
                    }

                }

            }

        });
        ivjia.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(etcount.getText().toString());
                i = ++i;
                System.out.println("i:" + i);
                etcount.setText(i + "");
            }
        });
        ivjian.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(etcount.getText().toString());
                if (i > 1) {
                    i = i - 1;
                    etcount.setText(i + "");
                }
            }
        });

        return root;

    }

    // 获取数据显示
    // 找到控件
    private void infind() {
        ivat = (ImageView) root.findViewById(R.id.action_order_image1);
        ivjia = (ImageView) root.findViewById(R.id.imgjia);
        ivjian = (ImageView) root.findViewById(R.id.imgjian);
        tvname = (TextView) root.findViewById(R.id.tvname);
        tvtime = (TextView) root.findViewById(R.id.tvtime);
        tvprice = (TextView) root.findViewById(R.id.tvprice);
        tvpay = (TextView) root.findViewById(R.id.tvpay);
        tvyue = (TextView) root.findViewById(R.id.yue);
        ck = (CheckBox) root.findViewById(R.id.jifen);
        etcount = (EditText) root.findViewById(R.id.edcount);
        ettel = (EditText) root.findViewById(R.id.edtel);
        btn = (Button) root.findViewById(R.id.btnsubmit);
        etname = (EditText) root.findViewById(R.id.edname);
        // 给控件设值
        b = getArguments();
        ao1 = (Action_order) b.getSerializable("ao1");
        tvname.setText(ao1.getAction().getA_itemname());
        tvtime.setText(ao1.getAction().getA_stime() + "——"
                + ao1.getAction().getA_etime());
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        ettel.setText(sharedPreferences.getString("user_tel", ""));
        etname.setText(sharedPreferences.getString("user_name", ""));
        a_price = ao1.getAction().getA_price();
        tvprice.setText(a_price + "");

        if (u_credit > a_price * 10) {
            ck.setText((int) (a_price * 10) + ""); // 用户积分
        } else {
            ck.setText(u_credit + ""); // 用户积分
        }
        tvpay.setText(ao1.getAction().getA_price() + "");// (b.getDouble("a_price")-Double.valueOf(ck.getText().toString())/100)+""
        tvyue.setText(MyApplication.getYue() + ""); // 用户余额
        Properties p = MyPropertiesUtil.getProperties(getActivity());
        String ip = p.getProperty("url");
        a_photo = ao1.getAction().getA_photo();
        String imgurl = ip + "/HiWeek/ActionPhotos/5/" + a_photo;
        BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
        // 加载网络图片
        bitmapUtils.display(ivat, imgurl);
        etcount.addTextChangedListener(textWatcher);
    }

    // 设置值
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(etcount.getText())) {
                System.out.println("-3-afterTextChanged-->"
                        + etcount.getText().toString() + "<--");
                int i = Integer.parseInt(etcount.getText().toString());
                if (i >= 1) {
                    if (ck.isChecked()) {
                        ao_price = (i - 1) * a_price + a_price
                                - Double.valueOf(ck.getText().toString()) / 100;
                    } else {
                        ao_price = (i - 1) * a_price + a_price;
                    }

                    tvpay.setText(ao_price + "");
                    if (i == 1) {
                        ivjian.setImageResource(R.drawable.jianh);
                    } else {
                        ivjian.setImageResource(R.drawable.jianl);
                    }
                } else {
                    Toast.makeText(getActivity(), "请输入正确的数量", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }
    };

    // 获取当前时间把值封装起来转换成json
    private void changejson() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time = format.format(date);
        ao1.setAo_price(Double.valueOf(tvpay.getText().toString()));
        ao1.setAo_date(time);
        ao1.setAo_count(Integer.parseInt(etcount.getText().toString()));
        if (ck.isChecked()) {
            ao1.setAo_credit(Integer.parseInt(ck.getText().toString()));
        } else {
            ao1.setAo_credit(0);
        }
        ao1.getUser().setU_credit(
                MyApplication.getCredit() - ao1.getAo_credit());
        Gson gson = new Gson();
        json = gson.toJson(ao1);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            ao_price = a_price * Integer.parseInt(etcount.getText().toString())
                    - Double.valueOf(ck.getText().toString()) / 100;
            tvpay.setText(ao_price + "");
        } else {
            ao_price = a_price * Integer.parseInt(etcount.getText().toString());
            tvpay.setText(ao_price + "");
        }

    }

}
