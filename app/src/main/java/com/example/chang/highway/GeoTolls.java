package com.example.chang.highway;

/**
 * Created by chang on 2019/3/5.
 */
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeResult;

import java.util.ArrayList;
import java.util.List;

import static cn.bmob.v3.Bmob.getApplicationContext;
//import com.amap.map3d.demo.R;
//import com.amap.map3d.demo.util.AMapUtil;
//import com.amap.map3d.demo.util.ToastUtil;

/**
 * 地理编码与逆地理编码功能介绍
 */
public class GeoTolls extends Activity implements
        OnGeocodeSearchListener, OnClickListener {
    private ProgressDialog progDialog = null;
    private GeocodeSearch geocoderSearch;
    private String addressName;
    private AMap aMap;
    private MapView mapView;
    private Marker geoMarker;
    //private EditText getLocation;
    private EditText getLocationon;
    private EditText getLocationout;
    private EditText gettype;

    //private EditText startsearch;
    //private Button ensure;
    //private EditText start;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private List<String> list = new ArrayList<String>();
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    private Button geoButton;
    int i = 0;
    private MyApplication myApp;
    private void permission(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED)  {
            //进行授权
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, 3);
        }else{
            //已经授权
            //call();
            init();
        }
    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 3:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                    init();
                }
                break;
            default:
                break;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geo_tolls);
        /*
         * 设置离线地图存储目录，在下载离线地图或初始化地图设置;
         * 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
         * 则需要在离线地图下载和使用地图页面都进行路径设置
         * */
        //Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
//        MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
        //int i = 0;
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        geoButton = findViewById(R.id.geoButton);
        //getLocation = findViewById(R.id.getLocation);
        getLocationon = findViewById(R.id.getLocationon);
        getLocationout = findViewById(R.id.getLocationout);
        gettype = findViewById(R.id.gettype);

        manager = getFragmentManager();

        //init();
        permission();
        getLocationon.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    //如果actionId是搜索的id，则进行下一步的操作
                    ShowDialog();
                    //           list = initData();
                }
                return false;
            }

            public void ShowDialog() {
                Context context = GeoTolls.this;
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.search_list, null);
                ListView myListView = (ListView) layout.findViewById(R.id.formcustomspinner_list);

                String[] listA = {"沈阳西","红旗台","陵园街","朱尔屯","王家沟","沈阳东","石庙子","古城子","长青街",
                        "下深沟","白塔铺","雪莲街","金宝台","宁官","北李官"};
                String[] listB = {"二十里铺","大连港","关家店","大连","庄河","",""};
                String str = getLocationon.getText().toString();
                switch(str) {
                    case "沈阳":
                        ArrayAdapter<String> adapterA = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, listA);
                        myListView.setAdapter(adapterA);
                        break;

                    case "大连":
                        ArrayAdapter<String> adapterB = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, listB);
                        myListView.setAdapter(adapterB);
                        break;
                }

                // MyAdapter adapter = new MyAdapter(context, list);

                myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                        //在这里面就是执行点击后要进行的操作,这里只是做一个显示
                        String mlist = arg0.getItemAtPosition(position).toString();
                        getLocationon.setText(mlist);
//                        Toast.makeText(StartSearch.this, "您点击的是"+mlist, Toast.LENGTH_SHORT).show();
                        if (alertDialog != null) {
                            alertDialog.dismiss();
                        }
                    }
                });
                builder = new AlertDialog.Builder(context);
                builder.setView(layout);
                alertDialog = builder.create();
                alertDialog.show();
            }
        });
        /* 出口*/
        getLocationout.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    //如果actionId是搜索的id，则进行下一步的操作
                    ShowDialog1();
                    //           list = initData();
                }
                return false;
            }

            public void ShowDialog1() {
                Context context = GeoTolls.this;
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.search_list, null);
                ListView myListView = (ListView) layout.findViewById(R.id.formcustomspinner_list);

                String[] listA = {"沈阳西","红旗台","陵园街","朱尔屯","王家沟","沈阳东","石庙子","古城子","长青街",
                        "下深沟","白塔铺","雪莲街","金宝台","宁官","北李官"};
                String[] listB = {"二十里铺","大连港","关家店","大连","庄河","",""};
                String str1 = getLocationout.getText().toString();
                switch(str1) {
                    case "沈阳":
                        ArrayAdapter<String> adapterA = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, listA);
                        myListView.setAdapter(adapterA);
                        break;

                    case "大连":
                        ArrayAdapter<String> adapterB = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, listB);
                        myListView.setAdapter(adapterB);
                        break;
                }

                // MyAdapter adapter = new MyAdapter(context, list);

                myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                        //在这里面就是执行点击后要进行的操作,这里只是做一个显示
                        String mlist = arg0.getItemAtPosition(position).toString();
                        getLocationout.setText(mlist);
//                        Toast.makeText(StartSearch.this, "您点击的是"+mlist, Toast.LENGTH_SHORT).show();
                        if (alertDialog != null) {
                            alertDialog.dismiss();
                        }
                    }
                });
                builder = new AlertDialog.Builder(context);
                builder.setView(layout);
                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        /*车型*/
        gettype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ShowDialog3();
            }



    public void ShowDialog3() {
                Context context = GeoTolls.this;
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.search_list, null);
                ListView myListView = (ListView) layout.findViewById(R.id.formcustomspinner_list);

                String[] listA = {"一类车","二类车","三类车","四类车","五类车" };
                //String[] listB = {"二十里铺","大连港","关家店","大连","庄河","",""};
                //String str3 = getLocationout.getText().toString();

                ArrayAdapter<String> adapterA = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, listA);
                myListView.setAdapter(adapterA);

                // MyAdapter adapter = new MyAdapter(context, list);

                myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                        //在这里面就是执行点击后要进行的操作,这里只是做一个显示
                        String mlist = arg0.getItemAtPosition(position).toString();
                        gettype.setText(mlist);
//                        Toast.makeText(StartSearch.this, "您点击的是"+mlist, Toast.LENGTH_SHORT).show();
                        if (alertDialog != null) {
                            alertDialog.dismiss();
                        }
                    }
                });
                builder = new AlertDialog.Builder(context);
                builder.setView(layout);
                alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(R.mipmap.map_poi_marker_pressed)));
        }
//        geoButton = findViewById(R.id.geoButton);
//        //getLocation = findViewById(R.id.getLocation);
//        getLocationon = findViewById(R.id.getLocationon);
//        getLocationout = findViewById(R.id.getLocationout);
//        gettype = findViewById(R.id.gettype);
//        manager = getFragmentManager();
        //geoButton.setText("GeoCoding(北京市朝阳区方恒国际中心A座)");
        geoButton.setOnClickListener(this);
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        progDialog = new ProgressDialog(this);
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

    /**
     * 显示进度条对话框
     */
    public void showDialog() {
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在获取地址");
        progDialog.show();
    }

    /**
     * 隐藏进度条对话框
     */
    public void dismissDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    /**
     * 响应地理编码
     */
    public void getLatlon(final String name) {
        //showDialog();

        GeocodeQuery query = new GeocodeQuery(name, "");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
        geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
    }

    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
        dismissDialog();
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getGeocodeAddressList() != null
                    && result.getGeocodeAddressList().size() > 0) {
                GeocodeAddress address = result.getGeocodeAddressList().get(0);

                if(address != null) {
                    aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            AMapUtil.convertToLatLng(address.getLatLonPoint()), 15));
                    geoMarker.setPosition(AMapUtil.convertToLatLng(address
                            .getLatLonPoint()));
      //              addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
       //                     + address.getFormatAddress();

//                    //ToastUtil.show(GeocoderActivity.this, addressName);
        //            Toast.makeText(GeoTolls.this,
         //                   addressName,Toast.LENGTH_LONG).show();

                    LatLonPoint latLongPoint = address.getLatLonPoint();

                    if(i == 0){
                        double Lat1 = latLongPoint.getLatitude();
                        double Lon1 = latLongPoint.getLongitude();
                        myApp = (MyApplication) getApplicationContext();
                        myApp.setAppLat1(Lat1);
                        myApp.setAppLon1(Lon1);

                    }else{
                        double Lat2 = latLongPoint.getLatitude();
                        double Lon2 = latLongPoint.getLongitude();
                        String site = gettype.getText().toString();
                        String geostart = getLocationon.getText().toString();
                        String geoend = getLocationout.getText().toString();
                        myApp = (MyApplication) getApplicationContext();
                        myApp.setAppLat2(Lat2);
                        myApp.setAppLon2(Lon2);
                        myApp.setAppsite(site);
                        myApp.setAppstart(geostart);
                        myApp.setAppend(geoend);
                        Intent intent=new Intent();

                        intent.setClass(this,MapRoute.class);
                        //启动Activity并返回结果
                        startActivity(intent);
                        //startActivityForResult(intent, 0x002);
                    }
                    i++;
                }

            }
            else {
                //ToastUtil.show(GeocoderActivity.this, R.string.no_result);
                Toast.makeText(GeoTolls.this,
                        "对不起，没有搜索到相关数据！",Toast.LENGTH_LONG).show();
            }
        }
        else {
            //ToastUtil.showerror(this, rCode);
            Toast.makeText(GeoTolls.this,
                    "对不起，没有搜索到相关数据！",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 逆地理编码回调
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {

    }

//    getLocation.setOnEditorActionListener(new OnEditorActionListener() {
//        @Override
//        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//            if (actionId == EditorInfo.IME_ACTION_DONE) {   // 按下完成按钮，这里和上面imeOptions对应
//                getLatlon(getLocation.getText().toString());
//                return false;   //返回true，保留软键盘。false，隐藏软键盘
//            }
//        }
//    });

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 响应地理编码按钮
             */
            case R.id.geoButton:
                getLatlon(getLocationout.getText().toString());
                getLatlon(getLocationon.getText().toString());
                //getLatlon(getLocationout.getText().toString());
                break;
            default:
                break;
        }
    }
}