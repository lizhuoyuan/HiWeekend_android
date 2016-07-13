package com.geminno.Dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geminno.Bean.Broadcast;
import com.geminno.Bean.Seat;
import com.geminno.View.SeatView;
import com.geminno.View.SeatView.SeatClickedListener;

import java.util.ArrayList;

import geminno.com.hiweek_android.R;

public class MyDialog extends DialogFragment implements OnClickListener,
	SeatClickedListener {

    private View view;
    private SeatView seatView;
    private Button ok_bt, cancle_bt;
    private TextView book_seat;
    private StringBuilder sb;
    private ImageView iv;
    private Broadcast broadcast;
    private TextView movie_name_tv;
    private TextView movie_info;
    private TextView movie_price;
    private String cinema_name;
    private String movie_name;
    private TextView cinema_name_tv;
    private HorizontalScrollView scrollView;
    private String price;
    private ArrayList<Seat> seats;

    public MyDialog(Broadcast broadcast, String cinema_name, String movie_name) {
	this.broadcast = broadcast;
	this.cinema_name = cinema_name;
	this.movie_name = movie_name;
	price = broadcast.getPrice();
	sb = new StringBuilder();
	seats = new ArrayList<Seat>();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
	return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
	view = inflater.inflate(R.layout.dialog_view, null);

	seatView = (SeatView) view.findViewById(R.id.seatView);
	seatView.setOnSeatClickedListener(this);
	seatView.setSeat_X_Y(8, 6);
	ok_bt = (Button) view.findViewById(R.id.ok);
	iv = (ImageView) view.findViewById(R.id.back_image);
	iv.setAlpha(0.5f);
	book_seat = (TextView) view.findViewById(R.id.booked_seat);
	cinema_name_tv = (TextView) view.findViewById(R.id.cinema_name);
	movie_name_tv = (TextView) view.findViewById(R.id.movie_name);
	movie_info = (TextView) view.findViewById(R.id.movie_info);
	movie_price = (TextView) view.findViewById(R.id.movie_price);
	// cancle_bt = (Button) view.findViewById(R.id.cancle);

	ok_bt.setOnClickListener(this);
	// cancle_bt.setOnClickListener(this);
	iv.setOnClickListener(this);

	return view;
    }

    @Override
    public void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	movie_name_tv.setText(movie_name);
	cinema_name_tv.setText(cinema_name);
	movie_info.setText(broadcast.getHall() + "    " + broadcast.getTime());
    }

    // 定义接口
    public interface OnSubmitOrdersListener {
	void onSubmitOrder(Broadcast broadcast, ArrayList<Seat> seats);
    }

    private OnSubmitOrdersListener listener;

    public void setOnSubmitOrderListener(OnSubmitOrdersListener listener) {
	this.listener = listener;
    }

    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.ok:
	    if (listener != null) {
		listener.onSubmitOrder(broadcast, seats);
		this.dismiss();
	    }
	    break;
	case R.id.back_image:
	    this.dismiss();
	    break;
	default:
	    break;
	}
    }

    @Override
    public void onSeatClicked(String toast, ArrayList<Seat> seats) {
	// TODO Auto-generated method stub
	Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
	if (seats.size() > 0) {
	    String m_price = "￥" + Integer.parseInt(price) * seats.size() + "";
	    sb.replace(0, sb.length(), "");
	    sb.append("您选中了：");
	    for (Seat seat : seats) {
		sb.append(seat);
	    }
	    book_seat.setText(sb.toString());
	    movie_price.setVisibility(View.VISIBLE);
	    movie_price.setText(m_price);
	    this.seats = seats;
	} else {
	    book_seat.setText("");
	    movie_price.setVisibility(View.GONE);
	}
    }
}
