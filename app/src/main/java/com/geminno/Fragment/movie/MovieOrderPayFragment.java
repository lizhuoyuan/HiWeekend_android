package com.geminno.Fragment.movie;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.geminno.Activities.movie.MovieActivity;
import com.geminno.Bean.Movie_order;
import com.geminno.Dialog.MovieOrderDialog;
import com.geminno.Dialog.MovieOrderDialog.PaySuccessListener;
import com.geminno.Dialog.MovieOrderDialog.flagbian;

import geminno.com.hiweek_android.R;

/**
 * 订单支付的一个fragment 实现了订单支付功能的界面
 *
 * @author 郑雅倩
 */
public class MovieOrderPayFragment extends Fragment implements
        PaySuccessListener, flagbian {
    public static final int NO_PAY = 1;
    public static final int PAYED_NO_COMMENT = 2;
    public static final int COMMENTED = 3;

    MovieOrderDialog orderdialog;
    View root;
    Movie_order mo1;
    TextView moviename, movieseat, moviesession, movieprice, moviecreadt,
            movieyue;
    public boolean flag = true;
    private boolean show_pay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mo1 = (Movie_order) bundle.getSerializable("Movie_order");
        if (mo1.getMo_state() == NO_PAY) {
            show_pay = true;
        }
        System.out.println(mo1.getMo_id());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getActionBar().setTitle("支付");

        root = inflater.inflate(R.layout.movie_order_pay_fragment, null);
        infind();
        indata();
        Button btn = (Button) root.findViewById(R.id.order_pay_fro_button1);
        if (show_pay) {

            btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag == true) {
                        orderdialog = new MovieOrderDialog(getActivity(), mo1);
                        orderdialog.payformoviedialog();
                    }
                    orderdialog
                            .setPaySuccessListener(MovieOrderPayFragment.this);
                    orderdialog.setflagclick(MovieOrderPayFragment.this);

                }
            });
        } else {
            btn.setVisibility(View.GONE);
        }
        return root;
    }

    private void infind() {
        moviename = (TextView) root.findViewById(R.id.movie_pay_name);
        movieseat = (TextView) root.findViewById(R.id.movie_pay_seat);
        moviesession = (TextView) root.findViewById(R.id.movie_pay_session);
        moviecreadt = (TextView) root.findViewById(R.id.movie_pay_creadt);
        movieprice = (TextView) root.findViewById(R.id.movie_pay_price);
        movieyue = (TextView) root.findViewById(R.id.movie_pay_yue);
    }

    private void indata() {
        moviename.setText(mo1.getMovie().getM_name());
        movieseat.setText(mo1.getMo_seat());
        moviesession.setText(mo1.getMo_session());
        moviecreadt.setText(mo1.getMo_credit() + "");
        movieprice.setText(mo1.getMo_price() + "");
        movieyue.setText(mo1.getUser().getU_yue() + "");
    }

    @Override
    public void onPaySuccess() {
        getActivity().finish();
    }

    @Override
    public void flagbianclick() {
        flag = false;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!MovieActivity.SHOW_BOOK) {
            getActivity().setResult(Activity.RESULT_OK);
        }
    }

}
