package com.geminno.Fragment.setting;

import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.Toast;

import com.geminno.Adapter.order.AllOrderListViewAdapter;
import com.geminno.Adapter.order.AllOrderListViewAdapter.MyclickedListener;
import com.geminno.Bean.Action_order;
import com.geminno.Bean.AllOrder;
import com.geminno.Bean.Movie_order;
import com.geminno.Dialog.MyOrderDialog;
import com.geminno.Reciver.OrderReciver;
import com.geminno.Utils.OrderShuJu;

import java.util.ArrayList;

import geminno.com.hiweek_android.R;

/**
 * myorder对应未支付订单的fragment 适配器是allorderlistviewadapter 在这控制数据源
 *
 * @author 郑雅倩 1 未支付 2 已支付没评 3 已支付已评
 */
public class NoPayOrderFragment extends ListFragment implements
        MyclickedListener, OnScrollListener {
    private ArrayList<AllOrder> list;
    private AllOrderListViewAdapter adapter;
    private MyOrderDialog orderdialog;
    ArrayList<Action_order> actionorderlist;
    ArrayList<Movie_order> movieorderlist;

    private OrderReciver reciver;
    xiangclicklistener xianglistener;
    ArrayList<AllOrder> list1;
    OrderShuJu ods;
    int pageNo = 1;
    ListView lv;
    View footerView;
    int footerHeight;
    private boolean isLoadingMore = false;
    public static final int NoPayOrderFragment = 12;
    Handler shuhandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x11:
                    list1 = (ArrayList<AllOrder>) msg.obj;
                    for (int i = 0; i < list1.size(); i++) {
                        if (list1.get(i).getstate() == 1) {
                            list.add(list1.get(i));
                        }

                    }
                    if (list != null) {
                        adapter.notifyDataSetChanged();
                        reciver = new OrderReciver(NoPayOrderFragment, adapter,
                                list);
                        IntentFilter intentFilter = new IntentFilter();
                        intentFilter
                                .addAction("com.geminno.Dialog.frags.delete.order");
                        intentFilter
                                .addAction("com.geminno.Dialog.frags.payfor.order");
                        getActivity().registerReceiver(reciver, intentFilter);
                    }
                    break;
                case 20:
                    Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

        }

    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lv = getListView();
        list = new ArrayList();
        adapter = new AllOrderListViewAdapter(list, getActivity());
        setListAdapter(adapter);
        adapter.setOnMyClickedListener(NoPayOrderFragment.this);

        footerView = LayoutInflater.from(getActivity()).inflate(
                R.layout.layout_footer, null);
        footerView.measure(0, 0);
        footerHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0, -footerHeight, 0, 0);
        lv.addFooterView(footerView);
        lv.setOnScrollListener(this);
        System.out.println("NoPayOrderFragment");
        ods = new OrderShuJu(getActivity(), pageNo, shuhandler);
        ods.ordershuju();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (reciver != null) {
            getActivity().unregisterReceiver(reciver);
            reciver = null;

        }
    }

    public interface xiangclicklistener {
        public void onxiangclicklistener(Bundle bundle);
    }

    public void setxiangclick(xiangclicklistener xianglistener) {
        this.xianglistener = xianglistener;
    }

    @Override
    public void clicked(View v, int position) {
        orderdialog = new MyOrderDialog(getActivity(), list, position);
        switch (v.getId()) {
            case R.id.all_order_listview_adapter_cancel_order:
                orderdialog.dialog(0);
                break;
            case R.id.all_order_listview_adapter_payer_order:
                new MyOrderDialog(getActivity(), list, position, adapter, lv)
                        .paydialog();
        /*
	     * Bundle allbundle = new Bundle(); allbundle.putInt("position",
	     * position); allbundle.putSerializable("list", list); if (listener
	     * != null) { listener.onbtnclicklistener(allbundle); }
	     */
                break;
            case R.id.all_order_listview_adapter_cinema_xiang:
	    /*
	     * Intent intent1=new Intent(getActivity(),TestActivity.class);
	     * startActivity(intent1);
	     */
                break;
            case R.id.all_order_listview_adapter_move_order_xiang:
                Bundle allbundle2 = new Bundle();
                allbundle2.putInt("position", position);
                allbundle2.putSerializable("list", list);
                if (xianglistener != null) {
                    xianglistener.onxiangclicklistener(allbundle2);
                }
                break;
            default:
                break;
        }

    }

    /**
     * 滚动状态监听 SCROLL_STATE_IDLE:闲置状态，松开状态
     * SCROLL_STATE_TOUCH_SCROLL：手指触摸滑动，手指按下，滑动状态 SCROLL_STATE_FLING：快速滑动后松开
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                && lv.getLastVisiblePosition() == (lv.getCount() - 1)
                && !isLoadingMore) {
            isLoadingMore = true;
            footerView.setPadding(0, 0, 0, 0);// 显示出加载更多
            // setSelection(lv.getCount());// 让listView最后一条显示出来
            ods = new OrderShuJu(getActivity(), ++pageNo, endhandler);
            ods.ordershuju();
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
    }

    Handler endhandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x11:
                    ArrayList<AllOrder> addlist = (ArrayList<AllOrder>) msg.obj;
                    for (int i = 0; i < addlist.size(); i++) {
                        if (addlist.get(i).getstate() == 1) {
                            list.add(addlist.get(i));
                        }
                    }
                    adapter.notifyDataSetChanged();
                    completeRefresh();
                    break;
                case 0xA1:
                    Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    completeRefresh();
                    break;
                case 0xB2:
                    Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    completeRefresh();
                    break;
                default:
                    break;
            }

        }

    };

    // 完成刷新操作，重置状态,在获取完数据并更新完adapter之后，去在UI线程中调用

    public void completeRefresh() {
        if (isLoadingMore) {
            // 重置footerView状态
            footerView.setPadding(0, -footerHeight, 0, 0);
            isLoadingMore = false;
        }
    }

}
