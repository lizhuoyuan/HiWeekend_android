package com.geminno.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Date;

import geminno.com.hiweek_android.R;


/**
 * 
 * @ClassName: MyFreshListView
 * @Description: 自定义ListView实现下拉刷新，上拉加载
 * @author: XU
 * @date: 2015年10月26日 下午9:00:36
 */
public class MyFreshListView extends ListView implements OnScrollListener {
    private static final int DEFAULT = 0;// 头部默认状态
    private static final int DROPING = 1;// 头布局下滑状态
    private static final int REFRESHING = 2;// 刷新状态
    private static final int RELEASE_REFRESH = 3;// 释放即可刷新状态

    private static final int FOOTER_DEFAULT = 4;// 底部默认状态
    private static final int FOOTER_SIDE = 5;// 底部上滑的状态
    private static final int FOOTER_LOADING = 6;// 底部加载状态
    private static final int RELEASE_LOADING = 7;// 底部 释放即可刷新状态

    private LayoutInflater inflater;
    private View headView;
    private View footerView;
    private int temp_y;
    private Animation animation, reverseAnimation;
    private RelativeLayout head_content_layout;
    private int head_view_height;
    private boolean Refreshable = false;// 是否可刷新
    private boolean Loadable = false;// 可加载
    private int head_limit;// 可滑动的界限
    private int head_status;// 状态
    private int foot_status;
    private ImageView head_arrow;
    private ProgressBar head_ProgressBar;
    private TextView head_tips, head_last_update_time;
    private boolean isFromRelease;
    private boolean isRecord;// 记录
    private ProgressBar foot_Bar;
    private TextView foot_tips;
    private int foot_view_height;
    private int foot_limit;
    private boolean flag = false;// 上拉 下滑 区分

    public MyFreshListView(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
	// setScrollingCacheEnabled(false);
	setCacheColorHint(0);
	// 解析头布局与底部布局
	inflater = LayoutInflater.from(context);
	initHeadView();
	initFootView();
	initAnimation();
	Refreshable = false;
	Loadable = false;
	// 滑动事件
	setOnScrollListener(this);
	head_status = DEFAULT;
	foot_status = FOOTER_DEFAULT;
    }

    private void initAnimation() {
	animation = createRotateAnimation(-180, 0,
		RotateAnimation.RELATIVE_TO_SELF, 0.5f,
		RotateAnimation.RELATIVE_TO_SELF, 0.5f);
	reverseAnimation = createRotateAnimation(0, -180,
		RotateAnimation.RELATIVE_TO_SELF, 0.5f,
		RotateAnimation.RELATIVE_TO_SELF, 0.5f);
    }

    private Animation createRotateAnimation(float fromDegrees, float toDegrees,
	    int pivotXType, float pivotXValue, int pivotYType, float pivotYValue) {
	Animation ani = new RotateAnimation(fromDegrees, toDegrees, pivotXType,
		pivotXValue, pivotYType, pivotYValue);

	// 加速度曲线
	ani.setInterpolator(new LinearInterpolator());
	// How long this animation should last.
	ani.setDuration(250);
	// 存留
	ani.setFillAfter(true);
	return ani;
    }

    public MyFreshListView(Context context, AttributeSet attrs) {
	this(context, attrs, 0);

    }

    public MyFreshListView(Context context) {
	this(context, null);
    }

    /**
     * 
     * @Title: initFootView
     * @Description: 初始化底部布局
     * @Author XU
     */
    private void initFootView() {
	footerView = inflater.inflate(R.layout.footer_lv, null);
	// 取控件
	foot_Bar = (ProgressBar) footerView.findViewById(R.id.foot_progressbar);
	foot_tips = (TextView) footerView.findViewById(R.id.foot_tips);
	// 测量大小
	MeasureView(footerView);
	foot_view_height = footerView.getMeasuredHeight();
	foot_limit = foot_view_height;
	footerView.setPadding(0, 0, 0, -foot_view_height);
	invalidate();
	addFooterView(footerView, null, false);

    }

    /**
     * 
     * @Title: initHeadView
     * @Description: 初始化头布局
     * @Author XU
     */
    private void initHeadView() {
	headView = inflater.inflate(R.layout.header_lv, null);
	// 取控件
	head_arrow = (ImageView) headView.findViewById(R.id.header_lv_Arrow);
	head_ProgressBar = (ProgressBar) headView
		.findViewById(R.id.Header_lv_ProgressBar);
	head_tips = (TextView) headView.findViewById(R.id.Header_lv_Tips);
	head_last_update_time = (TextView) headView
		.findViewById(R.id.Header_Last_Update);

	// 一定要测量,否则获取不到高度
	MeasureView(headView);
	head_view_height = headView.getMeasuredHeight();
	head_limit = head_view_height;
	// 设置TOP的padding目的是隐藏
	headView.setPadding(0, -head_view_height, 0, 0);
	invalidate();
	addHeaderView(headView, null, false);
    }

    /**
     * 
     * @Title: MeasureView
     * @Description: 代码来自ListView 的源码，稍作修改，实现对HeadView 的测量
     * @param child
     * @Author XU
     */
    private void MeasureView(View child) {
	LayoutParams p = (LayoutParams) child.getLayoutParams();
	if (p == null) {
	    p = (AbsListView.LayoutParams) generateDefaultLayoutParams();
	    child.setLayoutParams(p);
	}

	int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
	int lpHeight = p.height;
	int childHeightSpec;
	if (lpHeight > 0) {
	    childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
		    MeasureSpec.EXACTLY);
	} else {
	    childHeightSpec = MeasureSpec.makeMeasureSpec(0,
		    MeasureSpec.UNSPECIFIED);
	}
	child.measure(childWidthSpec, childHeightSpec);
    }

    /**
     * 
     * @Title: onTouchEvent
     * @Description: 触摸事件，对下拉距离进行判断并实现各种状态的转换
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
	// 可加载或可刷新状态才需要进行判断
	if (Refreshable || Loadable) {
	    int event_y = (int) ev.getY();
	    switch (ev.getAction()) {
	    case MotionEvent.ACTION_DOWN:
		if (!isRecord) {
		    temp_y = event_y;
		    isRecord = true;
		}
		break;
	    case MotionEvent.ACTION_MOVE:
		if (!isRecord) {
		    temp_y = event_y;
		    isRecord = true;
		}
		int dis_y = event_y - temp_y;
		if (Refreshable) {

		    refreshListView(dis_y);
		} else {
		    dis_y = Math.abs(dis_y);
		    LoadingData(dis_y);
		}

		break;
	    case MotionEvent.ACTION_UP:
		if (Refreshable) {
		    // 如果下拉过程中，放开
		    if (head_status == DROPING) {
			head_status = DEFAULT;
			changeUIByStatus(head_status);
		    }
		    // 如果释放可刷新
		    if (head_status == RELEASE_REFRESH) {
			head_status = REFRESHING;
			if (refreshListener != null) {
			    refreshListener.onRefreshing();
			}
			changeUIByStatus(head_status);
		    }
		} else {
		    if (foot_status == FOOTER_SIDE) {
			foot_status = FOOTER_DEFAULT;
			changeUIByStatus(foot_status);
		    }
		    if (foot_status == RELEASE_LOADING) {
			foot_status = FOOTER_LOADING;
			changeUIByStatus(foot_status);
			if (loadingListener != null) {
			    loadingListener.onLoading();
			}
		    }
		}
		isFromRelease = false;
		isRecord = false;
		break;
	    default:
		break;
	    }
	}
	return super.onTouchEvent(ev);
    }

    private void LoadingData(int dis_y) {
	// 如果在释放即可加载状态
	if (foot_status == RELEASE_LOADING) {
	    if (dis_y < foot_limit && dis_y > 0) {
		foot_status = FOOTER_SIDE;
		changeUIByStatus(foot_status);
	    }
	}
	// 在上滑状态中
	if (foot_status == FOOTER_SIDE) {
	    // 继续滑动
	    footerView.setPadding(0, 0, 0, -foot_view_height + dis_y);
	    if (dis_y > foot_limit) {
		foot_status = RELEASE_LOADING;
		changeUIByStatus(foot_status);
	    }
	    if (dis_y < 0) {
		foot_status = FOOTER_DEFAULT;
		changeUIByStatus(foot_status);

	    }
	}
	// 开始上滑
	if (foot_status == FOOTER_DEFAULT) {
	    if (dis_y > 0) {
		foot_status = FOOTER_SIDE;
		changeUIByStatus(foot_status);
	    }
	}
    }

    /**
     * 
     * @Title: refreshListView
     * @Description: TODO
     * @param dis_y
     * @Author XU
     */
    private void refreshListView(int dis_y) {
	// 如果在释放刷新状态
	if (head_status == RELEASE_REFRESH) {
	    setSelection(0);
	    // 拖动继续下滑
	    // 如果距离在小于刷新范围，便改变状态
	    if (dis_y > 0 && dis_y < head_limit) {
		head_status = DROPING;
		changeUIByStatus(head_status);
	    }
	}
	// 正在下拉过程
	if (head_status == DROPING) {
	    setSelection(0);

	    headView.setPadding(0, dis_y - head_view_height, 0, 0);
	    if (dis_y >= head_limit) {
		head_status = RELEASE_REFRESH;
		isFromRelease = true;
		changeUIByStatus(head_status);
	    }
	    if (dis_y <= 0) {
		head_status = DEFAULT;
		changeUIByStatus(head_status);
	    }
	}
	// 开始下拉
	if (head_status == DEFAULT && dis_y > 0) {
	    head_status = DROPING;
	    changeUIByStatus(head_status);
	}

    }

    private void changeUIByStatus(int s) {
	switch (s) {
	// 下拉过程中
	case DROPING:
	    head_ProgressBar.setVisibility(View.GONE);
	    head_tips.setVisibility(View.VISIBLE);
	    head_last_update_time.setVisibility(View.VISIBLE);
	    head_arrow.clearAnimation();
	    head_arrow.setVisibility(View.VISIBLE);
	    // 是由RELEASE_To_REFRESH状态转变来的
	    if (isFromRelease) {
		isFromRelease = false;
		head_arrow.clearAnimation();
		head_arrow.startAnimation(animation);

		head_tips.setText("下拉刷新");
	    } else {
		head_tips.setText("下拉刷新");
	    }
	    break;
	case RELEASE_REFRESH:
	    head_arrow.setVisibility(View.VISIBLE);
	    head_ProgressBar.setVisibility(View.GONE);
	    head_tips.setVisibility(View.VISIBLE);
	    head_last_update_time.setVisibility(View.VISIBLE);

	    head_arrow.clearAnimation();// 清除动画
	    head_arrow.startAnimation(reverseAnimation);// 开始动画效果

	    head_tips.setText("松开刷新");
	    break;
	case DEFAULT:
	    headView.setPadding(0, -1 * head_view_height, 0, 0);

	    head_ProgressBar.setVisibility(View.GONE);
	    head_arrow.clearAnimation();
	    head_arrow.setImageResource(R.drawable.arrow);
	    head_tips.setText("下拉刷新");
	    head_last_update_time.setVisibility(View.VISIBLE);
	    break;
	case REFRESHING:
	    headView.setPadding(0, 0, 0, 0);

	    head_ProgressBar.setVisibility(View.VISIBLE);
	    head_arrow.clearAnimation();
	    head_arrow.setVisibility(View.GONE);
	    head_tips.setText("正在刷新...");
	    head_last_update_time.setVisibility(View.VISIBLE);
	    break;
	case FOOTER_DEFAULT:
	    footerView.setPadding(0, 0, 0, -foot_view_height);

	    break;
	case FOOTER_LOADING:
	    footerView.setPadding(0, 0, 0, 0);
	    foot_Bar.setVisibility(VISIBLE);
	    foot_tips.setText("正在加载。。。");
	    foot_tips.setVisibility(VISIBLE);
	    break;
	case FOOTER_SIDE:
	    foot_Bar.setVisibility(GONE);
	    foot_tips.setText("松开可加载数据");
	    foot_tips.setVisibility(VISIBLE);
	    break;
	default:
	    break;
	}
    }

    private void resetHeadView() {
	headView.setPadding(0, -head_view_height, 0, 0);

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    /**
     * firstVisibleItem：当前能看见的第一个列表项ID（从0开始）<br>
     * visibleItemCount：当前能看见的列表项个数（小半个也算）<br>
     * totalItemCount：列表项共数<br>
     * 
     * @Title: onScroll
     * @Description: 对Scroll重写，实现自己的功能，当看见的第一项为List的第0项时，是可刷新状态
     * @param view
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
	    int visibleItemCount, int totalItemCount) {
	if (firstVisibleItem == 0) {
	    Refreshable = true;
	    Loadable = false;
	    flag = true;
	} else {
	    Refreshable = false;
	}
	if (firstVisibleItem + visibleItemCount >= totalItemCount) {
	    Loadable = true;
	    Refreshable = false;
	    flag = false;
	    System.out.println("。。。。。。。。。。。。。可加载");
	} else {
	    Loadable = false;
	}
    }

    // 完成
    public void onRefreshSuccess() {
	head_status = DEFAULT;
	changeUIByStatus(head_status);

	Refreshable = false;
	head_last_update_time.setText(new java.sql.Date(new Date().getTime())
		.toString());
    }

    public void onLoadingSuccess() {
	foot_status = FOOTER_DEFAULT;
	changeUIByStatus(foot_status);
	Loadable = false;

    }

    // 接口与绑定方法
    public interface RefreshListener {
	void onRefreshing();
    }

    public interface LoadingListener {
	void onLoading();
    }

    private RefreshListener refreshListener;
    private LoadingListener loadingListener;

    public void setOnRefreshListener(RefreshListener refreshListener) {
	this.refreshListener = refreshListener;
    }

    public void setOnLoadingListener(LoadingListener loadingListener) {
	this.loadingListener = loadingListener;
    }
}
