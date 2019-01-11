package com.example.chang.highway;

        import android.Manifest;
        import android.app.Activity;
        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.os.Bundle;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.ContextCompat;
        import android.util.Log;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.inputmethod.EditorInfo;
        import android.widget.Button;
        import android.widget.EditText;
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

        import static cn.bmob.v3.Bmob.getApplicationContext;
//import com.amap.map3d.demo.R;
        //import com.amap.map3d.demo.util.AMapUtil;
        //import com.amap.map3d.demo.util.ToastUtil;

/**
 * 地理编码与逆地理编码功能介绍
 */
public class geo extends Activity implements
        OnGeocodeSearchListener, OnClickListener {
    private ProgressDialog progDialog = null;
    private GeocodeSearch geocoderSearch;
    private String addressName;
    private AMap aMap;
    private MapView mapView;
    private Marker geoMarker;
    private EditText getLocation;
    private Button geoButton;
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
        setContentView(R.layout.geocoder_avtivity);
        /*
         * 设置离线地图存储目录，在下载离线地图或初始化地图设置;
         * 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
         * 则需要在离线地图下载和使用地图页面都进行路径设置
         * */
        //Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
//        MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写

        //init();
        permission();

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
        geoButton = findViewById(R.id.geoButton);
        getLocation = findViewById(R.id.getLocation);
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
//                    addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
//                            + address.getFormatAddress();
//                    //ToastUtil.show(GeocoderActivity.this, addressName);
//                    Toast.makeText(geo.this,
//                            "对不起，没有搜索到相关数据！"+addressName,Toast.LENGTH_LONG).show();
                }

                LatLonPoint latLongPoint = address.getLatLonPoint();
                double Lat = latLongPoint.getLatitude();
                double Lon = latLongPoint.getLongitude();
                //String map_lat = Float.toString(Lat);
                //String map_lon = Float.toString(Lon);
                //Log.e("111",Double.toString(Lat));
//                myApp = (MyApplication) getApplicationContext();
//                myApp.setAppLat(Lat);
//                myApp.setAppLon(Lon);
//                Intent intent = new Intent(this, Mapshow.class);
//                startActivity(intent);
                String site = getLocation.getText().toString();
                Intent intent=new Intent();
                //将参数放入intent
                intent.putExtra("lat", Lat);
                intent.putExtra("lon", Lon);
                intent.putExtra("site", site);
                //跳转到指定的Activity
                intent.setClass(this, Mapshow.class);
                //启动Activity并返回结果
                startActivityForResult(intent, 0x002);
            }
            else {
                //ToastUtil.show(GeocoderActivity.this, R.string.no_result);
                Toast.makeText(geo.this,
                        "对不起，没有搜索到相关数据！",Toast.LENGTH_LONG).show();
            }
        }
        else {
            //ToastUtil.showerror(this, rCode);
            Toast.makeText(geo.this,
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
                getLatlon(getLocation.getText().toString());
                //getLatlon("北京市朝阳区方恒国际中心A座");
                break;
            default:
                break;
        }
    }
}