package com.geminno.Fragment.movie;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.geminno.Bean.Broadcast;
import com.geminno.Bean.Cinema;
import com.geminno.Bean.Cinema_info;
import com.geminno.Bean.Constants;
import com.geminno.Bean.Movie;
import com.geminno.Bean.MovieInfo;
import com.geminno.Bean.Movie_order;
import com.geminno.Bean.Seat;
import com.geminno.Bean.User;
import com.geminno.View.SeatView;
import com.geminno.View.SeatView.SeatClickedListener;

import java.util.ArrayList;
import java.util.Date;

import geminno.com.hiweek_android.R;

public class SeatFragment extends Fragment implements SeatClickedListener,
	OnClickListener {
    private ArrayList<Seat> saledsSeats;
    private Button button;
    private SeatView seatView;
    private ArrayList<Seat> selectedSeats;
    private Broadcast broadcast;
    private String cinema_name;
    private String movie_name;
    private TextView movie_name_tv, date_tv, time_tv, ticket_tv, price_tv,
	    language_tv;
    private View view;
    private StringBuilder sb;
    private boolean show;
    private Date date;
    private Date time;
    private MovieInfo movieInfo;
    private Cinema_info cinema_info;
    private Movie_order order;

    public SeatFragment(MovieInfo movieInfo, Cinema_info cinema_info,
	    int position) {
	this();
	this.movieInfo = movieInfo;
	this.cinema_info = cinema_info;
	this.broadcast = movieInfo.getBroadcast().get(position);
	this.cinema_name = cinema_info.getName();
	this.movie_name = movieInfo.getMovieName();
	initDate();
    }

    private void initDate() {

	date = new Date();

    }

    public SeatFragment() {
	selectedSeats = new ArrayList<Seat>();
	saledsSeats = new ArrayList<Seat>();
	sb = new StringBuilder();
	saledsSeats.add(new Seat(1, 2, Seat.SALED));
	saledsSeats.add(new Seat(3, 6, Seat.SALED));
	saledsSeats.add(new Seat(5, 3, Seat.SALED));
	saledsSeats.add(new Seat(4, 1, Seat.SALED));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	getActivity().getActionBar().setTitle(cinema_name);
	view = inflater.inflate(R.layout.seat_fragment_view, null);
	findView();
	addListener();
	initView();

	return view;
    }

    private void initView() {
	seatView.setSeat_X_Y(8, 6);
	seatView.setSaledSeats(saledsSeats);
	seatView.setSelectedSeats(selectedSeats);

	movie_name_tv.setText(movie_name);
	date_tv.setText("XX-XX 周六");
	time_tv.setText(broadcast.getTime());
	language_tv.setText("国语");
    }

    private void addListener() {
	seatView.setOnSeatClickedListener(this);
	button.setOnClickListener(this);
    }

    private void findView() {
	seatView = (SeatView) view.findViewById(R.id.seatView1);
	button = (Button) view.findViewById(R.id.submit);
	movie_name_tv = (TextView) view.findViewById(R.id.movie_name);
	date_tv = (TextView) view.findViewById(R.id.play_Date);
	time_tv = (TextView) view.findViewById(R.id.play_time);
	ticket_tv = (TextView) view.findViewById(R.id.tacket_info);
	price_tv = (TextView) view.findViewById(R.id.price);
	language_tv = (TextView) view.findViewById(R.id.movie_language);
    }

    @Override
    public void onSeatClicked(String toast, ArrayList<Seat> seats) {
	if (seats.size() == 4) {
	    if (show) {
		Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
	    } else {
		show = true;
	    }

	} else {
	    show = false;
	}
	ticket_tv.setText(getTicketInfo());
	if (seats.size() > 0) {
	    price_tv.setVisibility(View.VISIBLE);
	    price_tv.setText("￥" + Double.parseDouble(broadcast.getPrice())
		    * seats.size());
	} else {
	    price_tv.setVisibility(View.GONE);
	}
    }

    private String getTicketInfo() {
	sb.replace(0, sb.length(), "");

	for (Seat seat : selectedSeats) {
	    sb.append(seat);
	}
	return sb.toString().trim() + "";
    }

    @Override
    public void onClick(View v) {
	int count;
	prepareData();
	if ((count = selectedSeats.size()) <= 0) {
	    Toast.makeText(getActivity(), "您没有选择座位", Toast.LENGTH_SHORT).show();
	    return;
	}
	if (!PreferenceManager.getDefaultSharedPreferences(getActivity())
		.getBoolean(Constants.SERVER_STATUS, false)) {
	    Toast.makeText(getActivity(), "网络不通", Toast.LENGTH_SHORT).show();

	    return;
	}
	double price = Double.parseDouble(broadcast.getPrice());
	order.setMo_count(count);
	order.setMo_price(price * count);

	order.setMo_seat(selectedSeats.toString());
	order.setMo_session("XX-XX" + broadcast.getTime());
	OrderFragment orderFragment = new OrderFragment(order, price);
	getFragmentManager().beginTransaction()
		.replace(R.id.main_movie_fl, orderFragment)
		.addToBackStack(null).commit();
    }

    private void prepareData() {
	order = new Movie_order();
	Cinema cinema = new Cinema();
	cinema.setC_id(cinema_info.getId());
	cinema.setC_name(cinema_info.getName());
	cinema.setC_city(cinema_info.getCity());
	cinema.setC_tel(cinema_info.getTelephone());
	cinema.setC_address(cinema_info.getAddress());
	Movie movie = new Movie();
	movie.setM_name(movieInfo.getMovieName());
	movie.setM_poster(movieInfo.getPic_url());
	movie.setM_id(movieInfo.getMovieId());
	User u1 = new User();
	SharedPreferences sharedPreferences = PreferenceManager
		.getDefaultSharedPreferences(getActivity());
	u1.setU_tel(sharedPreferences.getString("user_tel", ""));
	u1.setU_paynum(sharedPreferences.getInt("user_payNum", 0));
	u1.setU_id(sharedPreferences.getInt("user_id", 0));
	u1.setU_credit(sharedPreferences.getInt("user_credit", 0));
	u1.setU_yue(sharedPreferences.getFloat("user_YuE", 0.0f));

	order.setCinema(cinema);
	order.setMovie(movie);
	order.setUser(u1);
    }

}
