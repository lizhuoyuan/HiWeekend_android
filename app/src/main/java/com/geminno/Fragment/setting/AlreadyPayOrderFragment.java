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
 * myorder对应已付款订单的fragment 适配器是allorderlistviewadapter 在这控制数据源
 * 
 * @author 郑雅倩 1 未支付 2 已支付没评 3 已支付已评
 */
public class AlreadyPayOrderFragment extends ListFragment implements
	MyclickedListener, OnScrollListener {
    xiangclicklistener xianglistener;
    pinclicklistener pinlistener;
    ArrayList<AllOrder> list;
    private MyOrderDialog orderdialog;
    private AllOrderListViewAdapter adapter;
    ArrayList<Action_order> actionorderlist;
    ArrayList<Movie_order> movieorderlist;
    View footerView;
    int footerHeight;
    private OrderReciver reciver;
    ArrayList<AllOrder> list1;
    ListView lv;
    int pageNo = 1;
    OrderShuJu ods;
    private boolean isLoadingMore = false;
    public static final int AlreadyPayOrderFragment = 11;
    Handler shuhandler = new Handler() {

	@Override
	public void handleMessage(Message msg) {
	    super.handleMessage(msg);
	    switch (msg.what) {
	    case 0x11:
		list1 = (ArrayList<AllOrder>) msg.obj;
		list = new ArrayList();
		for (int i = 0; i < list1.size(); i++) {
		    if (list1.get(i).getstate() == 2
			    || list1.get(i).getstate() == 3) {
			list.add(list1.get(i));
		    }
		}
		adapter = new AllOrderListViewAdapter(list, getActivity());
		adapter.setOnMyClickedListener(AlreadyPayOrderFragment.this);
		setListAdapter(adapter);
		reciver = new OrderReciver(AlreadyPayOrderFragment, adapter,
			list);
		IntentFilter intentfilter = new IntentFilter();
		intentfilter.addAction("com.geminno.Dialog.frags.delete.order");
		intentfilter.addAction("com.geminno.Dialog.frags.payfor.order");
			getActivity().registerReceiver(reciver, intentfilter);
		break;
	    case 20:
		Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
	    default:
		break;
	    }

	}

    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

	super.onActivityCreated(savedInstanceState);
	lv = getListView();
	footerView = LayoutInflater.from(getActivity()).inflate(
		R.layout.layout_footer, null);
	footerView.measure(0, 0);
	footerHeight = footerView.getMeasuredHeight();
	footerView.setPadding(0, -footerHeight, 0, 0);
	lv.addFooterView(footerView);
	lv.setOnScrollListener(this);
	System.out.println("AlreadyPayOrderFragment");
	ods = new OrderShuJu(getActivity(), pageNo, shuhandler);
	ods.ordershuju();
    }

    @Override
    public void onDestroy() {
	// TODO Auto-generated method stub
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

    public interface pinclicklistener {
	public void onpinclicklistener(Bundle bundle);
    }

    public void setpinclick(pinclicklistener pinlistener) {
	this.pinlistener = pinlistener;
    }

    @Override
    public void clicked(View v, int position) {
	orderdialog = new MyOrderDialog(getActivity(), list, position);
	switch (v.getId()) {
	case R.id.all_order_listview_adapter_delete_order:
	    orderdialog.dialog(1);
	    break;
	case R.id.all_order_listview_adapter_discuss_order:
	    Bundle allbundle3 = new Bundle();
	    allbundle3.putInt("position", position);
	    allbundle3.putSerializable("list", list);
	    if (pinlistener != null) {
		pinlistener.onpinclicklistener(allbundle3);
	    }
	    break;
	case R.id.all_order_listview_adapter_cinema_xiang:
	    /*
	     * Intent intent=new Intent(getActivity(),TestActivity.class);
	     * startActivity(intent);
	     */
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
	    setSelection(lv.getCount());// 让listView最后一条显示出来
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
		    if (addlist.get(i).getstate() == 2
			    || addlist.get(i).getstate() == 3) {
			list.add(addlist.get(i));
		    }
		}
		adapter.notifyDataSetChanged();
		completeRefresh();
		break;
	    case 0xA1:
		Toast.makeText(getActivity(), msg.obj.toString(), 0).show();
		adapter.notifyDataSetChanged();
		completeRefresh();
		break;
	    case 0xB2:
		Toast.makeText(getActivity(), msg.obj.toString(), 0).show();
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
