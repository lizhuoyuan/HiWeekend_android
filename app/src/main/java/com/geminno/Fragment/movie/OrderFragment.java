package com.geminno.Fragment.movie;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geminno.Application.MyApplication;
import com.geminno.Bean.Movie_order;
import com.geminno.Dialog.MovieOrderDialog;
import com.geminno.Dialog.MovieOrderDialog.Dianclicklistener;
import com.geminno.Dialog.MovieOrderDialog.Jiclicklistener;
import com.geminno.Service.InternetService;
import com.geminno.Utils.GetPostUtil;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import geminno.com.hiweek_android.R;

/**
 * ClassName:OrderFragment
 *
 * @author 郑雅倩 @2015年11月2日下午7:01:26
 * @Description:是一个fragment对应 对应一个布局 实现了电影订单的布局
 */
public class OrderFragment extends Fragment implements Jiclicklistener,
        Dianclicklistener {
    private String left[];
    private String right[];
    private LinearLayout layout;
    private Button btn;
    private String json;
    private View root;
    private MovieOrderDialog orderdialog;
    private int result; // 返回的订单id
    private boolean flag = true;
    private int i;
    private double dan;
    private TextView tx1, tx2, tx3;
    private Movie_order mo1;
    private String tel;
    int usecreadt;
    private Editor editor;

    // View in4;

    public interface Onintentlistener {
        public void onbtnlistener(Movie_order mo1, int result);
    }

    public OrderFragment(Movie_order order, double price) {
        mo1 = order;
        dan = price;
    }

    public void setonintentlistener(Onintentlistener listener) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        indata();
        LayoutInflater.from(getActivity());
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // ((TextView) getActivity().getActionBar().getCustomView()
        // .findViewById(R.id.actionbar_title)).setText("订单信息");
        getActivity().getActionBar().setTitle("订单信息");

        root = inflater.inflate(R.layout.movie_order_fragment, null);
        layout = (LinearLayout) root.findViewById(R.id.moive_order_lineout);
        View v;
        for (i = 0; i < left.length; i++) {
            v = inflater.inflate(R.layout.move_order_fra_b, null);
            TextView tv1 = (TextView) v
                    .findViewById(R.id.move_order_fra_b_textview1);
            TextView tv2 = (TextView) v
                    .findViewById(R.id.move_order_fra_b_textview2);
            tv1.setText(left[i]);
            tv2.setText(right[i]);
            if (i == 0 || i == 6 || i == 9 || i == 11) {
                tv2.setVisibility(View.GONE);
                tv1.setBackgroundColor(Color.parseColor("#CDC9C9"));
            }

            layout.addView(v, i);
        }
        View in7 = layout.getChildAt(7);
        View in10 = layout.getChildAt(10);
        final View in8 = layout.getChildAt(8);
        tx3 = (TextView) in7.findViewById(R.id.move_order_fra_b_textview2);
        tx1 = (TextView) in8.findViewById(R.id.move_order_fra_b_textview2);
        tx2 = (TextView) in10.findViewById(R.id.move_order_fra_b_textview2);

        // 是否使用积分
        in7.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // Toast.makeText(getActivity(), "wewe",1).show();
                orderdialog = new MovieOrderDialog();
                orderdialog.setonjijiclicklistener(OrderFragment.this);
                orderdialog.gradedialog(getActivity(), mo1.getUser()
                        .getU_credit(), dan);
            }
        });

        // 是否更换电话
        in10.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                orderdialog = new MovieOrderDialog(getActivity());
                orderdialog.setdianclicklistener(OrderFragment.this);
                orderdialog.teldialog();
            }
        });

        // 最后做的 提交订单按钮点击事件 在之前都是总共积分
        btn = (Button) root.findViewById(R.id.moive_order_button1);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == true) {
                    System.out.println("可用点击");
                    changjson();
                    GetPostUtil.sendPost(InternetService.ServletURL
                            + "UpLoadMovie_order", json, handler);
                } else {
                    Toast.makeText(getActivity(), "不能重复提交", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return root;
    }

    /**
     * @param
     * @return
     * @Description:设置数据源
     */

    private void indata() {
        tel = mo1.getUser().getU_tel();
        left = new String[]{"选座票信息", "影院", "影片", "场次", "座位", "单价", "结算信息",
                "总共积分" + "  可使用", "总价", "联系的手机号码", "电话" + " 可更改"};
        right = new String[]{"1", mo1.getCinema().getC_name(),
                mo1.getMovie().getM_name(), mo1.getMo_session(),
                mo1.getMo_seat(), dan + "", "1",
                mo1.getUser().getU_credit() + "",
                (dan * mo1.getMo_count()) + "", "1", tel};
    }

    /**
     * @return
     * @Description:订单界面内容转换成json
     */
    private void changjson() {
        Date date = new Date();
        System.out.println(date);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        mo1.setMo_date(time);
        mo1.setMo_price(Double.parseDouble(tx1.getText().toString()));
        mo1.setMo_credit(usecreadt);
        mo1.getUser().setU_credit(Integer.parseInt(tx3.getText().toString()));
        Gson gson = new Gson();
        json = gson.toJson(mo1);
    }

    /**
     * 接收子线程的消息
     */
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.what == 1) {
                // 返回的订单id
                result = Integer.parseInt((String) msg.obj);
                System.out.println(result);
                Toast.makeText(getActivity(), "提交成功", Toast.LENGTH_SHORT).show();
                flag = false;
                // 设置改变之后的值
                MyApplication.setCredit(mo1.getUser().getU_credit());
                editor = PreferenceManager.getDefaultSharedPreferences(
                        getActivity()).edit();
                editor.putInt("user_credit", MyApplication.getCredit());
                editor.apply();
                // 把id放在对象中
                mo1.setMo_id(result);
                mo1.setMo_state(1);
                // 加载第二个支付的fragment
                MovieOrderPayFragment mof = new MovieOrderPayFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("Movie_order", mo1);
                mof.setArguments(bundle);
                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.main_movie_fl, mof).addToBackStack(null)
                        .commit();
        /*
         * if(listener!=null){ listener.onbtnlistener(mo1, result); }
		 */
		/*
		 * orderdialog=new OrderDialog(getActivity(),result);
		 * orderdialog.payformoviedialog();
		 */
            }
        }

    };

    @Override
    public void jiquelistener(boolean flag) {
        if (mo1.getUser().getU_credit() > dan * 10) {
            usecreadt = (int) (dan) * 10;

        } else {
            usecreadt = mo1.getUser().getU_credit();
        }
        if (flag == true) {
            tx1.setText((dan * mo1.getMo_count() - usecreadt / 100) + "");
            tx3.setText((mo1.getUser().getU_credit() - usecreadt) + "");
        }
        if (flag == false) {
            tx3.setText(mo1.getUser().getU_credit() + "");
            tx1.setText(dan * mo1.getMo_count() + "");
        }
    }

    @Override
    public void dianlistener(EditText et) {
        String newtel = et.getText().toString();
        if (newtel.length() == 11) {
            tx2.setText(newtel);
            tel = newtel;
        } else
            tx2.setText(tel);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
    }

}
