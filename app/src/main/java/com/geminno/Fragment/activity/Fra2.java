package com.geminno.Fragment.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.geminno.Activities.activity.RoutePlanActivity;
import com.geminno.Activities.setting.PersonActivity;
import com.geminno.Adapter.activity.QuesAdapter;
import com.geminno.Application.MyApplication;
import com.geminno.Bean.Action;
import com.geminno.Bean.Action_order;
import com.geminno.Bean.Constants;
import com.geminno.Bean.Quiz;
import com.geminno.Bean.User;
import com.geminno.Utils.MyPropertiesUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import geminno.com.hiweek_android.R;

/**
 * 活动详情
 *
 * @author 李卓原 创建时间：2015年10月12日 下午2:07:12
 */
public class Fra2 extends Fragment implements OnGetGeoCoderResultListener, OnClickListener {
    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    BaiduMap mBaiduMap = null;
    MapView mMapView = null;
    private UiSettings mUiSettings;
    View root;
    int a_id, u_id; // uid;
    Double lat, lon;
    private String a_itemname, a_introduce, a_consult, a_FAQ, a_joinex, a_charge, a_stime, a_etime, a_address, a_photo,
            u_name; // uname
    Quiz q_cont;
    private double a_price;
    TextView tVitemname, tVaddressmap, tVstarttime, tVendtime, tVprice, tVintroduce, tVstarttime2, tVendtime2, tVFAQ,
            tVcharge, tVaddress, tVconsult, tVjoinex, tVmoney, tvtiwen, tvmore, tvjoin;
    ImageView imageView1, imgheart;
    Button btnquestion;
    EditText edtiwen;
    private boolean flag = true, scroll = true;
    ListView mylist;
    ArrayList<Quiz> qz;
    private String MyQuestion;
    private ScrollView sv;
    private Menu menu;
    private boolean login = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        login = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(Constants.USER_STATUS, false);
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getActionBar().setTitle("活动详情");
        qz = new ArrayList<Quiz>();
        root = inflater.inflate(R.layout.activity_f2, null);
        sv = (ScrollView) root.findViewById(R.id.scrollview);
        btnquestion = (Button) root.findViewById(R.id.question);
        tVitemname = (TextView) root.findViewById(R.id.itemname);
        tVaddressmap = (TextView) root.findViewById(R.id.addressmap);
        tVstarttime = (TextView) root.findViewById(R.id.starttime);
        tVendtime = (TextView) root.findViewById(R.id.endtime);
        tVprice = (TextView) root.findViewById(R.id.price);
        tVintroduce = (TextView) root.findViewById(R.id.introduce);
        tVstarttime2 = (TextView) root.findViewById(R.id.starttime2);
        tVendtime2 = (TextView) root.findViewById(R.id.endtime2);
        tVFAQ = (TextView) root.findViewById(R.id.FAQ);
        tVcharge = (TextView) root.findViewById(R.id.charge);
        tVaddress = (TextView) root.findViewById(R.id.address);
        tVconsult = (TextView) root.findViewById(R.id.conult);
        tVjoinex = (TextView) root.findViewById(R.id.joinex);
        tVaddressmap = (TextView) root.findViewById(R.id.addressmap);
        tVmoney = (TextView) root.findViewById(R.id.money);
        imageView1 = (ImageView) root.findViewById(R.id.imageView1);
        tvtiwen = (TextView) root.findViewById(R.id.texttiwen);
        tvmore = (TextView) root.findViewById(R.id.more);
        tvjoin = (TextView) root.findViewById(R.id.join);
        mylist = (ListView) root.findViewById(R.id.mylist);
        tvjoin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!login) {
                    toLogin();
                    return;
                } else {

                    ActionOrderFragment1 aof = new ActionOrderFragment1();
                    Bundle b = new Bundle();
                    Action a1 = new Action();
                    User u1 = new User();
                    Action_order ao1 = new Action_order();
                    a1.setA_id(a_id);
                    a1.setA_stime(a_stime);
                    a1.setA_etime(a_etime);
                    a1.setA_itemname(a_itemname);
                    a1.setA_price(a_price);
                    a1.setA_photo(a_photo);
                    u1.setU_id(MyApplication.getU_id());
                    u1.setU_credit(MyApplication.getCredit());
                    u1.setU_yue(MyApplication.getYue());
                    ao1.setUser(u1);
                    ao1.setAction(a1);
                    b.putSerializable("ao1", ao1);
                    aof.setArguments(b);
                    getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.one_fragment, aof)
                            .commit();

                }
            }
        });
        tVaddressmap.getBackground().setAlpha(200);// 0~255透明度值
        init();
        // 地图初始化
        mMapView = (MapView) root.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        // 最大缩放值
        mBaiduMap.setMapStatus(msu);
        mBaiduMap.setMaxAndMinZoomLevel(3, 19);// 设置最小、最大缩放级别

        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        mUiSettings = mBaiduMap.getUiSettings();
        mUiSettings.setAllGesturesEnabled(false);
        mMapView.showZoomControls(false);
        mMapView.showScaleControl(false);
        // Geo搜索
        mSearch.geocode(new GeoCodeOption().city(MyApplication.getCity()).address(a_address));
        View v = mMapView.getChildAt(0);// 这个view实际上就是我们看见的绘制在表面的地图图层
        v.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RoutePlanActivity.class);
                intent.putExtra("a_address", a_address);
                startActivity(intent);
            }
        });
        btnquestion.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!login) {
                    toLogin();
                    return;
                } else {
                    setView();
                }

            }
        });
        tvmore.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("a_id", a_id);
                Fra3 fra3 = new Fra3();
                fra3.setArguments(bundle);
                getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.one_fragment, fra3).commit();

            }
        });
        loadmyquestion(); // 加载我的提问

        return root;
    }

    public void init() {
        u_id = MyApplication.getU_id(); // u_id
        u_name = MyApplication.getU_name(); // uname
        Bundle bb = getArguments();

        Action b = (Action) bb.get("action");
        a_id = b.getA_id();
        a_photo = b.getA_photo();
        a_itemname = b.getA_itemname();
        a_introduce = b.getA_introduce();
        a_consult = b.getA_consult();
        a_FAQ = b.getA_FAQ();
        a_joinex = b.getA_joinex();
        a_charge = b.getA_charge();
        a_stime = b.getA_stime();
        a_etime = b.getA_etime();
        a_address = b.getA_address();
        a_price = b.getA_price();
        System.out.println("a_address:" + a_address);
        /*
		 * a_id = b.getInt("a_id"); a_photo = b.getString("a_photo"); a_itemname
		 * = b.getString("a_itemname"); a_introduce =
		 * b.getString("a_introduce"); a_consult = b.getString("a_consult");
		 * a_FAQ = b.getString("a_FAQ"); a_joinex = b.getString("a_joinex");
		 * a_charge = b.getString("a_charge"); a_stime = b.getString("a_stime");
		 * a_etime = b.getString("a_etime"); a_address =
		 * b.getString("a_address"); a_price = b.getDouble("a_price");
		 */
        tVitemname.setText(a_itemname);
        tVaddressmap.setText(a_address);
        tVstarttime.setText(a_stime);
        tVendtime.setText(a_etime);
        tVprice.setText(a_price + "");
        tVintroduce.setText(a_introduce);
        tVstarttime2.setText(a_stime);
        tVendtime2.setText(a_etime);
        tVFAQ.setText(a_FAQ);
        tVcharge.setText(a_charge);
        tVaddress.setText(a_address);
        tVconsult.setText(a_consult);
        tVjoinex.setText(a_joinex);
        tVmoney.setText("￥" + (int) (a_price));
        Properties p = MyPropertiesUtil.getProperties(getActivity());
        String ip = p.getProperty("url");
        String imgurl = ip + "/HiWeek/ActionPhotos/5/" + a_photo;
        BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
        // 加载网络图片
        bitmapUtils.display(imageView1, imgurl);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mSearch.destroy();
        super.onDestroy();
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(getActivity(), "抱歉，未能找到结果", Toast.LENGTH_SHORT).show();
            return;
        }
        mBaiduMap.clear();
        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marka)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result.getLocation()));
        String strInfo = String.format("纬度：%f 经度：%f", result.getLocation().latitude, result.getLocation().longitude);
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(getActivity(), "抱歉，未能找到结果", Toast.LENGTH_SHORT).show();
            return;
        }
        mBaiduMap.clear();
        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marka)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result.getLocation()));
    }

    public void setView() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.layout_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        layout.findViewById(R.id.cuo).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("点了x");
                dialog.dismiss();
            }
        });
        layout.findViewById(R.id.dui).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("点了√");
                // 提交问题
                edtiwen = (EditText) layout.findViewById(R.id.edittiwen);
                if (!TextUtils.isEmpty(edtiwen.getText().toString().trim())) {
                    tvtiwen.setVisibility(View.GONE);
                    MyQuestion = edtiwen.getText().toString();
                    // tvtiwen.setText("我的提问:" + edtiwen.getText());
                    // btnquestion.setText("继续提问");
                    uploadQuiz();

                }

                dialog.dismiss();
            }
        });
        dialog.setView(new EditText(getActivity()));
        dialog.show();
        Window win = dialog.getWindow();
        win.setGravity(Gravity.BOTTOM);
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        dialog.setContentView(layout);
    }

    // 上传提问
    public void uploadQuiz() {
        RequestParams params = new RequestParams();
        DateFormat sf = new SimpleDateFormat("yyyy-MM-dd H:m:s");
        String q_time = sf.format(new Date());
        params.addBodyParameter("a_id", a_id + "");
        params.addBodyParameter("u_id", u_id + "");
        params.addBodyParameter("a_itemname", a_itemname);
        params.addBodyParameter("q_time", q_time);
        params.addBodyParameter("u_name", u_name);
        params.addBodyParameter("q_cont", MyQuestion);
        HttpUtils http = new HttpUtils();
        Properties p = MyPropertiesUtil.getProperties(getActivity());
        String ip = p.getProperty("url");
        String url = ip + "/HiWeek/servlet/UploadQuiz";
        http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException error, String msg) {
                // TODO Auto-generated method stub
                System.out.println(error.getExceptionCode() + ":" + msg);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                // TODO Auto-generated method stub
                System.out.println("reply: " + responseInfo.result);
                loadmyquestion();
            }
        });
    }

    public void loadmyquestion() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("a_id", a_id + ""); // 活动id //以后要获取
        HttpUtils http = new HttpUtils();
        Properties p = MyPropertiesUtil.getProperties(getActivity());
        String ip = p.getProperty("url");
        String url = ip + "/HiWeek/servlet/SelectMyQuestion";
        /**
         * 下载用get(根据HTTP规范，GET用于信息获取) 上传用post(根据HTTP规范，POST表示可能修改变服务器上的资源的请求)
         * (安全性高) 参数封装在表单
         */
        http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException error, String msg) {
                System.out.println(error.getExceptionCode() + ":" + msg);

            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                System.out.println("reply: " + responseInfo.result);
                // Gson gson = new Gson();
                // q_cont = gson.fromJson(responseInfo.result, Quiz.class);
                // System.out.println("------"+q_cont+"------");
                if (!TextUtils.isEmpty(responseInfo.result)) {
                    // tvtiwen.setText("我的提问:" + responseInfo.result);
                    tvtiwen.setVisibility(View.GONE);
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<Quiz>>() {
                    }.getType();
                    qz = gson.fromJson(responseInfo.result, type);
                    mylist.setAdapter(new QuesAdapter(qz, getActivity()));
                    if (scroll) {
                        sv.smoothScrollTo(0, 0);
                        scroll = false;
                    }
                }
            }
        });
    }

    public void isexist() {
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1000); // 设置缓存1秒，1秒内直接返回上次成功请求的结果。
        String url = MyPropertiesUtil.getProperties(getActivity()).getProperty("url")
                + "/HiWeek/servlet/CollectFind?u_id=" + u_id + "&&a_id=" + a_id;
        System.out.println(url);
        http.send(HttpMethod.GET, url, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                System.out.println("arg0:" + arg1);
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                System.out.println("判断是否存在这收藏:" + arg0.result.toString());
                if (arg0.result.toString().equals("0")) {
                    flag = true;
                    // menu.getItem(0).setIcon(R.drawable.heart2);
                    imgheart.setImageResource(R.drawable.heart32px);
                } else {
                    flag = false;
                    // menu.getItem(0).setIcon(R.drawable.heart32px);
                    imgheart.setImageResource(R.drawable.heart2);

                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_menu, menu);
        imgheart = (ImageView) menu.findItem(R.id.imgheart).getActionView();
        if (login) {
            isexist();
        } else {
            imgheart.setImageResource(R.drawable.heart32px);
        }
        imgheart.setOnClickListener(this);

    }

    private void toLogin() {
        Intent intent = new Intent();
        intent.putExtra("FLAG", 1);
        intent.setClass(getActivity(), PersonActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.imgheart) {
            changeHertStatus();
        }
        return true;
    }

    private void changeHertStatus() {
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1000); // 设置缓存1秒，1秒内直接返回上次成功请求的结果。
        if (flag) {
            // 收藏活动
            String url = MyPropertiesUtil.getProperties(getActivity()).getProperty("url")
                    + "/HiWeek/servlet/AddCollect";
            RequestParams params = new RequestParams();
            params.addBodyParameter("a_id", a_id + "");
            params.addBodyParameter("u_id", u_id + "");
            params.addBodyParameter("a_itemname", a_itemname);
            Date date = new Date();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = format.format(date);
            params.addBodyParameter("ce_time", time);
            http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

                @Override
                public void onFailure(HttpException arg0, String arg1) {
                    Toast.makeText(getActivity(), "网络状态不好，收藏失败，请稍后重试", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(ResponseInfo<String> arg0) {
                    Toast.makeText(getActivity(), "您已成功收藏该活动", Toast.LENGTH_SHORT).show();
                    imgheart.setImageResource(R.drawable.heart2);
                    // menu.getItem(0).setIcon(R.drawable.heart2);

                }
            });
            flag = false;
        } else {
            String url = MyPropertiesUtil.getProperties(getActivity()).getProperty("url")
                    + "/HiWeek/servlet/DeleteCollect";
            RequestParams params = new RequestParams();
            params.addBodyParameter("a_id", a_id + "");
            params.addBodyParameter("u_id", u_id + "");
            http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

                @Override
                public void onFailure(HttpException arg0, String arg1) {
                    Toast.makeText(getActivity(), "删除收藏失败，请稍候重试", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(ResponseInfo<String> arg0) {
                    Toast.makeText(getActivity(), "您已成功删除该收藏", Toast.LENGTH_SHORT).show();
                    imgheart.setImageResource(R.drawable.heart32px);
                    // menu.getItem(0).setIcon(R.drawable.heart32px);

                }
            });
            flag = true;
        }
    }

    @Override
    public void onClick(View v) {
        if (login) {
            changeHertStatus();
        } else {
            toLogin();
        }
    }

	/*
	 * public void call(View v) { //拨打电话 cal(tVconsult.getText().toString()); }
	 * 
	 * public String cal(String args) { //提取电话号 String regEx = "[^0-9]"; Pattern
	 * p = Pattern.compile(regEx); Matcher m = p.matcher(args); return
	 * m.replaceAll("").trim(); }
	 */
}
