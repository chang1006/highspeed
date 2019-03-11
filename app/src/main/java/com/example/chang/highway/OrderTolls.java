package com.example.chang.highway;

/**
 * Created by chang on 2019/3/11.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.WalkRouteResult;
//import com.amap.map3d.demo.R;
//import com.amap.map3d.demo.util.AMapUtil;
//import com.amap.map3d.demo.util.ToastUtil;

//import overlay.DrivingRouteOverlay;

/**
 * 驾车出行路线规划 实现
 */
public class OrderTolls extends Activity implements OnMapClickListener,
        OnMarkerClickListener, OnInfoWindowClickListener, InfoWindowAdapter, OnRouteSearchListener {
    private AMap aMap;
    private MapView mapView;
    private Context mContext;
    private RouteSearch mRouteSearch;
    private DriveRouteResult mDriveRouteResult;
    private LatLonPoint mStartPoint;  // = new LatLonPoint(39.942295,116.335891);//起点，39.942295,116.335891
    private LatLonPoint mEndPoint;   // = new LatLonPoint(39.995576,116.481288);//终点，39.995576,116.481288
    private MyApplication myApp;

    private final int ROUTE_TYPE_DRIVE = 2;

    private RelativeLayout mBottomLayout, mHeadLayout;
    private TextView mresult,mRotueTimeDes, mRouteDetailDes,ensure;
    private ProgressDialog progDialog = null;// 搜索时进度条
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.order_tolls);

        mContext = this.getApplicationContext();
        mapView = (MapView) findViewById(R.id.route_map);

        mapView.onCreate(bundle);// 此方法必须重写
        myApp = (MyApplication)getApplicationContext();
        double lat1 = myApp.getAppLat1();
        double lon1 = myApp.getAppLon1();
        double lat2 = myApp.getAppLat2();
        double lon2 = myApp.getAppLon2();
        String site = myApp.getAppsite();
        Log.e("1111", Double.toString(lat1));
        mStartPoint = new LatLonPoint(lat1,lon1);
        mEndPoint = new LatLonPoint(lat2,lon2);
        init();
        setfromandtoMarker();
        searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);
    }

    private void setfromandtoMarker() {
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mStartPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.start)));
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mEndPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.end)));
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        registerListener();
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        mHeadLayout = (RelativeLayout)findViewById(R.id.routemap_header);
        mresult = (TextView) findViewById(R.id.result);
        ensure = findViewById(R.id.ensure);
        mRotueTimeDes = (TextView) findViewById(R.id.firstline);
        mRouteDetailDes = (TextView) findViewById(R.id.secondline);
        mHeadLayout.setVisibility(View.GONE);
    }

    /**
     * 注册监听
     */
    private void registerListener() {
        aMap.setOnMapClickListener(OrderTolls.this);
        aMap.setOnMarkerClickListener(OrderTolls.this);
        aMap.setOnInfoWindowClickListener(OrderTolls.this);
        aMap.setInfoWindowAdapter(OrderTolls.this);

    }

    @Override
    public View getInfoContents(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onMapClick(LatLng arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
        if (mStartPoint == null) {
            //ToastUtil.show(mContext, "定位中，稍后再试...");
            return;
        }
        if (mEndPoint == null) {
            //ToastUtil.show(mContext, "终点未设置");
        }
        showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
            DriveRouteQuery query = new DriveRouteQuery(fromAndTo, mode, null,
                    null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult result, int errorCode) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        dissmissProgressDialog();
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);
                    if(drivePath == null) {
                        return;
                    }
                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                            //DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                            mContext, aMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
                    mBottomLayout.setVisibility(View.VISIBLE);
                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration();
                    mresult.setVisibility(View.VISIBLE);
                    String des = "全程"+AMapUtil.getFriendlyLength(dis);
                    String des1 = AMapUtil.getFriendlyLength(dis);
                    des1 = des1.replace("公里","");
                    mRotueTimeDes.setText(des);
                    mRouteDetailDes.setVisibility(View.VISIBLE);
                    String start = myApp.getAppstart();
                    String end = myApp.getAppend();
                    mresult.setText(start+"-"+end);
                    //int taxiCost = (int) mDriveRouteResult.getTaxiCost();
                    //mRouteDetailDes.setText("打车约"+taxiCost+"元");
                    //mRouteDetailDes.setText(taxiCost+"元");
                    double rate = 0;
                    double mileage = Double.valueOf(des1.trim());
                    String site = myApp.getAppsite();
                    switch (site) {
                        case "一类车":
                            rate = 0.45;
                            break;
                        case "二类车":
                            rate = 0.8;
                            break;
                        case "三类车":
                            rate = 1.15;
                            break;
                        case "四类车":
                            rate = 1.45;
                            break;
                        case "五类车":
                            rate = 1.7;
                            break;
                        default:
                            break;
                    }
                    //Log.e("33333", Double.valueOf(des1));
                    double money = mileage*rate;
                    mRouteDetailDes.setText("过路费："+money+"元");
                    //myApp = (MyApplication) getApplicationContext();
                    String smoney = String.valueOf(money);
                    myApp.setAppmoney(smoney);
                    myApp.setAppstart(start);
                    myApp.setAppend(end);
                    ensure.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myApp = (MyApplication) getApplicationContext();
//                            myApp.setAppsite(money);
//                            myApp.setAppstart(start);
//                            myApp.setAppend(end);
                            Intent intent=new Intent();
                            intent.setClass(OrderTolls.this,Order.class);
                            //启动Activity并返回结果
                            startActivity(intent);
                            //startActivityForResult(intent, 0x002);
                        }
                    });

                } else if (result != null && result.getPaths() == null) {
                    //ToastUtil.show(mContext, R.string.no_result);
                }

            } else {
                //ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
            //ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }


    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {

    }


    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null) {
            progDialog = new ProgressDialog(this);
        }
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在搜索");
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onRideRouteSearched(RideRouteResult arg0, int arg1) {
        // TODO Auto-generated method stub

    }

}
