package com.geminno.Fragment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geminno.Bean.Action_order;
import com.geminno.Dialog.OrderDialog;
import com.geminno.Dialog.OrderDialog.OnsuccessClickedListener;
import com.geminno.Dialog.OrderDialog.flagbian;
import com.geminno.Service.UpMymoneyService;
import com.geminno.Utils.MyPropertiesUtil;
import com.geminno.hiweek1_0.MainActivity;
import com.lidroid.xutils.BitmapUtils;

import java.util.Properties;

import geminno.com.hiweek_android.R;

/**
 * @author 李卓原 创建时间：2015年10月26日 下午6:14:30
 * 
 */
public class PayFragment extends Fragment implements OnsuccessClickedListener,
	flagbian {
    TextView tvtime, tvcount, tvprice, tvtel, tvcredit, tvid, tvname, tvuname;
    View root;
    OrderDialog orderdialog;
    ImageView iv;
    int u_id;
    double u_yue;
    int u_credit;
    Action_order ao1;
    public boolean flag = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	getActivity().getActionBar().setTitle("支付");
	root = inflater.inflate(R.layout.activity_payforaction, null);
	init();
	root.findViewById(R.id.btnpay).setOnClickListener(
		new OnClickListener() {

		    @Override
		    public void onClick(View v) {
			if (flag == true) {
			    orderdialog = new OrderDialog(getActivity(), ao1);
			    orderdialog.payforactiondialog();
			} else {
					Toast.makeText(getActivity(), "您已支付成功", Toast.LENGTH_SHORT).show();
				}
			orderdialog
				.setOnSuccessClickedListener(PayFragment.this);
			orderdialog.setflagclick(PayFragment.this);
		    }
		});
	return root;
    }

    private void init() {
	Bundle b = getArguments();
	ao1 = (Action_order) b.getSerializable("ao1");
	tvname = (TextView) root.findViewById(R.id.tvname);
	tvtime = (TextView) root.findViewById(R.id.tvtime);
	tvcount = (TextView) root.findViewById(R.id.count);
	tvprice = (TextView) root.findViewById(R.id.tvpay);
	tvtel = (TextView) root.findViewById(R.id.tvtel);
	tvcredit = (TextView) root.findViewById(R.id.tvcre);
	tvid = (TextView) root.findViewById(R.id.ao_id);
	tvuname = (TextView) root.findViewById(R.id.tvuname);
	iv = (ImageView) root.findViewById(R.id.aimage1);
	// 设置值
	tvtime.setText(ao1.getAction().getA_stime() + "——"
		+ ao1.getAction().getA_etime());
	tvcount.setText(ao1.getAo_count() + "");
	tvname.setText(ao1.getAction().getA_itemname());
	tvprice.setText(ao1.getAo_price() + "");
	tvtel.setText(b.getString("tel"));
	tvuname.setText(b.getString("name"));
	tvcredit.setText(ao1.getAo_credit() + "");
	tvid.setText(ao1.getAo_id() + "");
	Properties p = MyPropertiesUtil.getProperties(getActivity());
	String ip = p.getProperty("url");
	String imgurl = ip + "/HiWeek/ActionPhotos/5/"
		+ ao1.getAction().getA_photo();
	BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
	// 加载网络图片
	bitmapUtils.display(iv, imgurl);
    }

    @Override
    public void onSuccessClicked() {
	Intent intent = new Intent(getActivity(), MainActivity.class);
	getActivity().startService(
		new Intent(getActivity(), UpMymoneyService.class));
	startActivity(intent);
    }

    @Override
    public void flagbianclick() {
	flag = false;
    }
}
