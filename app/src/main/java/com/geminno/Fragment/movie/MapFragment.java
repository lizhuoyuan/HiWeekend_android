package com.geminno.Fragment.movie;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import geminno.com.hiweek_android.R;

public class MapFragment extends Fragment implements
	OnGetRoutePlanResultListener, OnMapClickListener,
	OnGetGeoCoderResultListener, OnClickListener {
    private GeoCoder nSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private String city;
    private String address;
    // String myaddress;
    private double lat, lon;
    // 浏览路线节点相关
    private Button mBtnPre = null;// 上一个节点
    private Button mBtnNext = null;// 下一个节点
    private int nodeIndex = -1;// 节点索引,供浏览节点时使用
    private RouteLine route = null;
    private OverlayManager routeOverlay = null;
    private boolean useDefaultIcon = false;
    private TextView popupText = null;// 泡泡view
    // 地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
    // 如果不处理touch事件，则无需继承，直接使用MapView即可
    private MapView mMapView = null; // 地图View
    private BaiduMap mBaidumap = null;
    // 搜索相关
    private RoutePlanSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private Button walk, transit, driver;
    private EditText editSt;
    private PlanNode stNode;
    private PlanNode enNode;

    public MapFragment(String target, double lat, double lon, String city) {
	address = target;
	this.lat = lat;
	this.lon = lon;
	this.city = city;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	// ((TextView) getActivity().getActionBar().getCustomView()
	// .findViewById(R.id.actionbar_title)).setText("地理位置");
	getActivity().getActionBar().setTitle("地理位置");

	View view = inflater.inflate(R.layout.map_fragment_view, null);
	// 初始化地图
	mMapView = (MapView) view.findViewById(R.id.map);
	mBaidumap = mMapView.getMap();
	mBtnPre = (Button) view.findViewById(R.id.pre);
	mBtnNext = (Button) view.findViewById(R.id.next);
	driver = (Button) view.findViewById(R.id.drive);
	walk = (Button) view.findViewById(R.id.walk);
	transit = (Button) view.findViewById(R.id.transit);
	editSt = (EditText) view.findViewById(R.id.editText1);

	driver.setOnClickListener(this);
	walk.setOnClickListener(this);
	transit.setOnClickListener(this);
	mBtnNext.setOnClickListener(this);
	mBtnPre.setOnClickListener(this);
	mBtnPre.setVisibility(View.INVISIBLE);
	mBtnNext.setVisibility(View.INVISIBLE);
	// 地图点击事件处理
	mBaidumap.setOnMapClickListener(this);
	// 初始化搜索模块，注册事件监听
	mSearch = RoutePlanSearch.newInstance();
	mSearch.setOnGetRoutePlanResultListener(this);
	nSearch = GeoCoder.newInstance();
	nSearch.setOnGetGeoCodeResultListener(this);
	nSearch.geocode(new GeoCodeOption().city(city).address(address));
	MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
	// 最大缩放值
	mBaidumap.setMapStatus(msu);
	mBaidumap.setMaxAndMinZoomLevel(3, 19);// 设置最小、最大缩放级别
	return view;
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
	if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
	    Toast.makeText(getActivity(), "抱歉，您的位置无法识别，请手动输入", Toast.LENGTH_SHORT).show();
	}
	if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
	    // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
	    // result.getSuggestAddrInfo();
	    return;
	}
	if (result.error == SearchResult.ERRORNO.NO_ERROR) {
	    nodeIndex = -1;
	    mBtnPre.setVisibility(View.VISIBLE);
	    mBtnNext.setVisibility(View.VISIBLE);
	    route = result.getRouteLines().get(0);
	    WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaidumap);
	    mBaidumap.setOnMarkerClickListener(overlay);
	    routeOverlay = overlay;
	    overlay.setData(result.getRouteLines().get(0));
	    overlay.addToMap();
	    overlay.zoomToSpan();
	}

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult result) {

	if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
	    Toast.makeText(getActivity(), "抱歉，您的位置无法识别，请手动输入", Toast.LENGTH_SHORT).show();
	}
	if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
	    // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
	    // result.getSuggestAddrInfo()
	    return;
	}
	if (result.error == SearchResult.ERRORNO.NO_ERROR) {
	    nodeIndex = -1;
	    mBtnPre.setVisibility(View.VISIBLE);
	    mBtnNext.setVisibility(View.VISIBLE);
	    route = result.getRouteLines().get(0);
	    TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaidumap);
	    mBaidumap.setOnMarkerClickListener(overlay);
	    routeOverlay = overlay;
	    overlay.setData(result.getRouteLines().get(0));
	    overlay.addToMap();
	    overlay.zoomToSpan();
	}
    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
	if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
	    Toast.makeText(getActivity(), "抱歉，您的位置无法识别，请手动输入", Toast.LENGTH_SHORT).show();
	}
	if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
	    // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
	    // result.getSuggestAddrInfo()
	    return;
	}
	if (result.error == SearchResult.ERRORNO.NO_ERROR) {
	    nodeIndex = -1;
	    mBtnPre.setVisibility(View.VISIBLE);
	    mBtnNext.setVisibility(View.VISIBLE);
	    route = result.getRouteLines().get(0);
	    DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
	    routeOverlay = overlay;
	    mBaidumap.setOnMarkerClickListener(overlay);
	    overlay.setData(result.getRouteLines().get(0));
	    overlay.addToMap();
	    overlay.zoomToSpan();
	}
    }

    // 定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

	public MyDrivingRouteOverlay(BaiduMap baiduMap) {
	    super(baiduMap);
	}

	@Override
	public BitmapDescriptor getStartMarker() {
	    if (useDefaultIcon) {
		return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
	    }
	    return null;
	}

	@Override
	public BitmapDescriptor getTerminalMarker() {
	    if (useDefaultIcon) {
		return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
	    }
	    return null;
	}
    }

    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

	public MyWalkingRouteOverlay(BaiduMap baiduMap) {
	    super(baiduMap);
	}

	@Override
	public BitmapDescriptor getStartMarker() {
	    if (useDefaultIcon) {
		return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
	    }
	    return null;
	}

	@Override
	public BitmapDescriptor getTerminalMarker() {
	    if (useDefaultIcon) {
		return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
	    }
	    return null;
	}
    }

    private class MyTransitRouteOverlay extends TransitRouteOverlay {

	public MyTransitRouteOverlay(BaiduMap baiduMap) {
	    super(baiduMap);
	}

	@Override
	public BitmapDescriptor getStartMarker() {
	    if (useDefaultIcon) {
		return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
	    }
	    return null;
	}

	@Override
	public BitmapDescriptor getTerminalMarker() {
	    if (useDefaultIcon) {
		return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
	    }
	    return null;
	}
    }

    @Override
    public void onMapClick(LatLng point) {
	mBaidumap.hideInfoWindow();
    }

    @Override
    public boolean onMapPoiClick(MapPoi poi) {
	return false;
    }

    @Override
    public void onPause() {
	mMapView.onPause();
	super.onPause();
    }

    @Override
    public void onResume() {
	mMapView.onResume();
	super.onResume();
    }

    @Override
    public void onDestroy() {
	mSearch.destroy();
	mMapView.onDestroy();
	super.onDestroy();
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
	if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
	    Toast.makeText(getActivity(), "抱歉，网络状态不好，请稍后重试", Toast.LENGTH_LONG)
		    .show();
	    return;
	}
	mBaidumap.clear();
	mBaidumap.addOverlay(new MarkerOptions().position(result.getLocation())
		.icon(BitmapDescriptorFactory
			.fromResource(R.drawable.icon_marka)));
	mBaidumap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
		.getLocation()));
	String strInfo = String.format("纬度：%f 经度：%f",
		result.getLocation().latitude, result.getLocation().longitude);
	// Toast.makeText(RoutePlanActivity.this, strInfo,
	// Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
	if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
	    Toast.makeText(getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
	}
	if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
	    // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
	    // result.getSuggestAddrInfo()
	    return;
	}
	if (result.error == SearchResult.ERRORNO.NO_ERROR) {
	    nodeIndex = -1;
	    mBtnPre.setVisibility(View.VISIBLE);
	    mBtnNext.setVisibility(View.VISIBLE);
	    // route = result.getRouteLines().get(0);
	    DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
	    routeOverlay = overlay;
	    mBaidumap.setOnMarkerClickListener(overlay);
	    // overlay.setData(result.getRouteLines().get(0));
	    overlay.addToMap();
	    overlay.zoomToSpan();
	}
    }

    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.walk:
	    prepareforSearch();
	    mSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode)
		    .to(enNode));
	    break;
	case R.id.drive:
	    prepareforSearch();
	    mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode)
		    .to(enNode));
	    break;
	case R.id.transit:
	    prepareforSearch();
	    mSearch.transitSearch((new TransitRoutePlanOption()).from(stNode)
		    .city(city).to(enNode));
	    break;
	case R.id.pre:
	    if (route == null || route.getAllStep() == null) {
		return;
	    }
	    if (nodeIndex == -1 && v.getId() == R.id.pre) {
		return;
	    }
	    if (nodeIndex > 0) {
		nodeIndex--;
	    } else {
		break;
	    }
	    afterNodeClick();
	    break;
	case R.id.next:
	    if (route == null || route.getAllStep() == null) {
		return;
	    }
	    if (nodeIndex == -1 && v.getId() == R.id.pre) {
		return;
	    }
	    if (nodeIndex < route.getAllStep().size() - 1) {
		nodeIndex++;
	    } else {
		break;
	    }
	    break;
	default:
	    break;
	}
    }

    private void afterNodeClick() {
	// 获取节结果信息
	LatLng nodeLocation = null;
	String nodeTitle = null;
	Object step = route.getAllStep().get(nodeIndex);
	if (step instanceof DrivingRouteLine.DrivingStep) {
	    nodeLocation = ((DrivingRouteLine.DrivingStep) step).getEntrance()
		    .getLocation();
	    nodeTitle = ((DrivingRouteLine.DrivingStep) step).getInstructions();
	} else if (step instanceof WalkingRouteLine.WalkingStep) {
	    nodeLocation = ((WalkingRouteLine.WalkingStep) step).getEntrance()
		    .getLocation();
	    nodeTitle = ((WalkingRouteLine.WalkingStep) step).getInstructions();
	} else if (step instanceof TransitRouteLine.TransitStep) {
	    nodeLocation = ((TransitRouteLine.TransitStep) step).getEntrance()
		    .getLocation();
	    nodeTitle = ((TransitRouteLine.TransitStep) step).getInstructions();
	}

	if (nodeLocation == null || nodeTitle == null) {
	    return;
	}
	// 移动节点至中心
	mBaidumap.setMapStatus(MapStatusUpdateFactory.newLatLng(nodeLocation));
	// show popup
	popupText = new TextView(getActivity());
	popupText.setBackgroundResource(R.drawable.popup);
	popupText.setTextColor(0xFF000000);
	popupText.setText(nodeTitle);
	mBaidumap.showInfoWindow(new InfoWindow(popupText, nodeLocation, 0));
    }

    private void prepareforSearch() {
	// 重置浏览节点的路线数据
	route = null;
	mBtnPre.setVisibility(View.INVISIBLE);
	mBtnNext.setVisibility(View.INVISIBLE);
	mBaidumap.clear();
	// 处理搜索按钮响应
	// EditText editEn = (EditText) findViewById(R.id.end);
	// 设置起终点信息，对于tranist search 来说，城市名无意义
	if (TextUtils.isEmpty(editSt.getText())) {
	    stNode = PlanNode.withLocation(new LatLng(lat, lon));
	} else {
	    stNode = PlanNode.withCityNameAndPlaceName(city, editSt.getText()
		    .toString());
	}
	enNode = PlanNode.withCityNameAndPlaceName(city, address);
    }

}
