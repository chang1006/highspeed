package com.example.chang.highway;

        import android.Manifest;
        import android.app.Activity;
        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.graphics.BitmapFactory;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.ContextCompat;
        import android.util.Log;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.amap.api.maps.AMap;
        import com.amap.api.maps.AMap.InfoWindowAdapter;
        import com.amap.api.maps.AMap.OnInfoWindowClickListener;
        import com.amap.api.maps.AMap.OnMapClickListener;
        import com.amap.api.maps.AMap.OnMarkerClickListener;
        import com.amap.api.maps.CameraUpdateFactory;
        import com.amap.api.maps.MapView;
        import com.amap.api.maps.model.BitmapDescriptor;
        import com.amap.api.maps.model.BitmapDescriptorFactory;
        import com.amap.api.maps.model.CircleOptions;
        import com.amap.api.maps.model.LatLng;
        import com.amap.api.maps.model.LatLngBounds;
        import com.amap.api.maps.model.Marker;
        import com.amap.api.maps.model.MarkerOptions;
        import com.amap.api.services.core.AMapException;
        import com.amap.api.services.core.LatLonPoint;
        import com.amap.api.services.core.PoiItem;
        import com.amap.api.services.core.SuggestionCity;
        import com.amap.api.services.geocoder.GeocodeAddress;
        import com.amap.api.services.geocoder.GeocodeQuery;
        import com.amap.api.services.geocoder.GeocodeResult;
        import com.amap.api.services.geocoder.GeocodeSearch;
        import com.amap.api.services.geocoder.RegeocodeResult;
        import com.amap.api.services.poisearch.PoiResult;
        import com.amap.api.services.poisearch.PoiSearch;
        import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
        import com.amap.api.services.poisearch.PoiSearch.SearchBound;
        //import com.amap.map3d.demo.R;
        //import com.amap.map3d.demo.util.ToastUtil;

        import java.util.ArrayList;
        import java.util.List;

/**
 * 介绍poi周边搜索功能
 */
public class Mapshow extends Activity implements OnClickListener,
        OnMapClickListener, OnInfoWindowClickListener, InfoWindowAdapter, OnMarkerClickListener,
        OnPoiSearchListener {
    private MapView mapview;
    private AMap mAMap;

    //private TextView searchButton;
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private LatLonPoint lp;
    //private LatLonPoint lp = new LatLonPoint(39.993743, 116.472995);; // 116.472995,39.993743
    //lp = new LatLonPoint(39.993743, 116.472995);
    //private LatLonPoint lp = new LatLonPoint(39.993743, 116.472995);

    private Marker locationMarker; // 选择的点
    private Marker detailMarker;
    private Marker mlastMarker;
    private PoiSearch poiSearch;
    private myPoiOverlay poiOverlay;// poi图层
    private List<PoiItem> poiItems;// poi数据

    private RelativeLayout mPoiDetail;
    private TextView mPoiName, mPoiAddress;
    private String keyWord = "";
    private String site = "";
    private EditText mSearchText;
    //private MyApplication myApp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_poiarround);
        mapview = (MapView)findViewById(R.id.mapView);
        mapview.onCreate(savedInstanceState);
//        myApp = (MyApplication)getApplicationContext();
//        double lat = myApp.getAppLat();
//        double lon = myApp.getAppLon();
//        Log.e("1111", Double.toString(lat));
//        lp = new LatLonPoint(lat,lon);
        // lp = new LatLonPoint(39.993743, 116.472995);

        //lp = new LatLonPoint(lat,lon);
        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("lat",1);
        double lon = intent.getDoubleExtra("lon",1);
        String site = intent.getStringExtra("site");
        mSearchText = findViewById(R.id.input_edittext);
        mSearchText.setText(site);
        lp = new LatLonPoint(lat,lon);
        Log.e("1111", Double.toString(lat));
        //lp = new LatLonPoint(39.9937433456, 116.472995);
        permission();
        //lp = new LatLonPoint(39.993743, 116.472995);
        //init();
    }

    /**
     * 权限获取
     */
    private void permission(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED)  {
            //进行授权
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, 2);
        }else{
            //已经授权
            //call();
            init();
        }
    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                    init();
                }
                break;
            default:
                break;
        }
    }
    /**
     * 初始化AMap对象
     */
    private void init() {
        //lp = new LatLonPoint(39.993743, 116.472995);
        if (mAMap == null) {
            mAMap = mapview.getMap();
            mAMap.setOnMapClickListener(this);
            mAMap.setOnMarkerClickListener(this);
            mAMap.setOnInfoWindowClickListener(this);
            mAMap.setInfoWindowAdapter(this);
            TextView searchButton = (TextView) findViewById(R.id.btn_search);
            searchButton.setOnClickListener(this);
            locationMarker = mAMap.addMarker(new MarkerOptions()
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory
                            .fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.map_point)))
                    .position(new LatLng(lp.getLatitude(), lp.getLongitude())));
            locationMarker.showInfoWindow();

        }

        setup();
        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lp.getLatitude(), lp.getLongitude()), 14));
    }


    private void setup() {
        mPoiDetail = (RelativeLayout) findViewById(R.id.poi_detail);
        mPoiDetail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//				Intent intent = new Intent(PoiSearchActivity.this,
//						SearchDetailActivity.class);
//				intent.putExtra("poiitem", mPoi);
//				startActivity(intent);

            }
        });
        mPoiName = (TextView) findViewById(R.id.poi_name);
        mPoiAddress = (TextView) findViewById(R.id.poi_address);

    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        //keyWord = mSearchText.getText().toString().trim();
        keyWord = "高速收费站".trim();
        Log.e("1234 ", keyWord);
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        if (lp != null) {
            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.setBound(new SearchBound(lp, 50000, true));//
            // 设置搜索区域为以lp点为圆心，其周围5000米范围
            poiSearch.searchPOIAsyn();// 异步搜索
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapview.onResume();
        whetherToShowDetailInfo(false);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapview.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapview.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapview.onDestroy();
    }

    @Override
    public void onPoiItemSearched(PoiItem arg0, int arg1) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onPoiSearched(PoiResult result, int rcode) {
        if (rcode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    if (poiItems != null && poiItems.size() > 0) {
                        //清除POI信息显示
                        whetherToShowDetailInfo(false);
                        //并还原点击marker样式
                        if (mlastMarker != null) {
                            resetlastmarker();
                        }
                        //清理之前搜索结果的marker
                        if (poiOverlay !=null) {
                            poiOverlay.removeFromMap();
                        }
                        mAMap.clear();
                        poiOverlay = new myPoiOverlay(mAMap, poiItems);
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();

                        mAMap.addMarker(new MarkerOptions()
                                .anchor(0.5f, 0.5f)
                                .icon(BitmapDescriptorFactory
                                        .fromBitmap(BitmapFactory.decodeResource(
                                                getResources(), R.mipmap.map_point)))
                                .position(new LatLng(lp.getLatitude(), lp.getLongitude())));

                        mAMap.addCircle(new CircleOptions()
                                .center(new LatLng(lp.getLatitude(),
                                        lp.getLongitude())).radius(5000)
                                .strokeColor(Color.BLUE)
                                .fillColor(Color.argb(50, 1, 1, 1))
                                .strokeWidth(2));

                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {
                        Toast.makeText(Mapshow.this,
                                "对不起，没有搜索到相关数据！", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(Mapshow.this,
                        "对不起，没有搜索到相关数据！", Toast.LENGTH_LONG).show();
            }
        } else  {
            Toast.makeText(Mapshow.this,
                    "对不起，没有搜索到相关数据！", Toast.LENGTH_LONG).show();
            //ToastUtil.showerror(this.getApplicationContext(), rcode);
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        if (marker.getObject() != null) {
            whetherToShowDetailInfo(true);
            try {
                PoiItem mCurrentPoi = (PoiItem) marker.getObject();
                if (mlastMarker == null) {
                    mlastMarker = marker;
                } else {
                    // 将之前被点击的marker置为原来的状态
                    resetlastmarker();
                    mlastMarker = marker;
                }
                detailMarker = marker;
                detailMarker.setIcon(BitmapDescriptorFactory
                        .fromBitmap(BitmapFactory.decodeResource(
                                getResources(),
                                R.mipmap.map_poi_marker_pressed)));

                setPoiItemDisplayContent(mCurrentPoi);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }else {
            whetherToShowDetailInfo(false);
            resetlastmarker();
        }


        return true;
    }

    // 将之前被点击的marker置为原来的状态
    private void resetlastmarker() {
        int index = poiOverlay.getPoiIndex(mlastMarker);
        if (index < 10) {
            mlastMarker.setIcon(BitmapDescriptorFactory
                    .fromBitmap(BitmapFactory.decodeResource(
                            getResources(),
                            markers[index])));
        }else {
            mlastMarker.setIcon(BitmapDescriptorFactory.fromBitmap(
                    BitmapFactory.decodeResource(getResources(), R.mipmap.map_marker_other_highlight)));
        }
        mlastMarker = null;

    }


    private void setPoiItemDisplayContent(final PoiItem mCurrentPoi) {
        mPoiName.setText(mCurrentPoi.getTitle());
        mPoiAddress.setText(mCurrentPoi.getSnippet()+mCurrentPoi.getDistance());
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                //site = mSearchText.getText().toString().trim();
                //getLatlon(s);
                doSearchQuery();
                break;

            default:
                break;
        }

    }

    private int[] markers = {R.mipmap.poi_marker_1,
            R.mipmap.poi_marker_2,
            R.mipmap.poi_marker_3,
            R.mipmap.poi_marker_4,
            R.mipmap.poi_marker_5,
            R.mipmap.poi_marker_6,
            R.mipmap.poi_marker_7,
            R.mipmap.poi_marker_8,
            R.mipmap.poi_marker_9,
            R.mipmap.poi_marker_10
    };

    private void whetherToShowDetailInfo(boolean isToShow) {
        if (isToShow) {
            mPoiDetail.setVisibility(View.VISIBLE);

        } else {
            mPoiDetail.setVisibility(View.GONE);

        }
    }


    @Override
    public void onMapClick(LatLng arg0) {
        whetherToShowDetailInfo(false);
        if (mlastMarker != null) {
            resetlastmarker();
        }
    }

    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
        //ToastUtil.show(this, infomation);
        Toast.makeText(Mapshow.this, infomation, Toast.LENGTH_LONG).show();

    }


    /**
     * 自定义PoiOverlay
     *
     */

    private class myPoiOverlay {
        private AMap mamap;
        private List<PoiItem> mPois;
        private ArrayList<Marker> mPoiMarks = new ArrayList<Marker>();
        public myPoiOverlay(AMap amap ,List<PoiItem> pois) {
            mamap = amap;
            mPois = pois;
        }

        /**
         * 添加Marker到地图中。
         * @since V2.1.0
         */
        public void addToMap() {
            if(mPois != null) {
                int size = mPois.size();
                for (int i = 0; i < size; i++) {
                    Marker marker = mamap.addMarker(getMarkerOptions(i));
                    PoiItem item = mPois.get(i);
                    marker.setObject(item);
                    mPoiMarks.add(marker);
                }
            }
        }

        /**
         * 去掉PoiOverlay上所有的Marker。
         *
         * @since V2.1.0
         */
        public void removeFromMap() {
            for (Marker mark : mPoiMarks) {
                mark.remove();
            }
        }

        /**
         * 移动镜头到当前的视角。
         * @since V2.1.0
         */
        public void zoomToSpan() {
            if (mPois != null && mPois.size() > 0) {
                if (mamap == null)
                    return;
                LatLngBounds bounds = getLatLngBounds();
                mamap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            }
        }

        private LatLngBounds getLatLngBounds() {
            LatLngBounds.Builder b = LatLngBounds.builder();
            if(mPois != null) {
                int size = mPois.size();
                for (int i = 0; i < size; i++) {
                    b.include(new LatLng(mPois.get(i).getLatLonPoint().getLatitude(),
                            mPois.get(i).getLatLonPoint().getLongitude()));
                }
            }
            return b.build();
        }

        private MarkerOptions getMarkerOptions(int index) {
            return new MarkerOptions()
                    .position(
                            new LatLng(mPois.get(index).getLatLonPoint()
                                    .getLatitude(), mPois.get(index)
                                    .getLatLonPoint().getLongitude()))
                    .title(getTitle(index)).snippet(getSnippet(index))
                    .icon(getBitmapDescriptor(index));
        }

        protected String getTitle(int index) {
            return mPois.get(index).getTitle();
        }

        protected String getSnippet(int index) {
            return mPois.get(index).getSnippet();
        }

        /**
         * 从marker中得到poi在list的位置。
         *
         * @param marker 一个标记的对象。
         * @return 返回该marker对应的poi在list的位置。
         * @since V2.1.0
         */
        public int getPoiIndex(Marker marker) {
            for (int i = 0; i < mPoiMarks.size(); i++) {
                if (mPoiMarks.get(i).equals(marker)) {
                    return i;
                }
            }
            return -1;
        }

        /**
         * 返回第index的poi的信息。
         * @param index 第几个poi。
         * @return poi的信息。poi对象详见搜索服务模块的基础核心包（com.amap.api.services.core）中的类 <strong><a href="../../../../../../Search/com/amap/api/services/core/PoiItem.html" title="com.amap.api.services.core中的类">PoiItem</a></strong>。
         * @since V2.1.0
         */
        public PoiItem getPoiItem(int index) {
            if (index < 0 || index >= mPois.size()) {
                return null;
            }
            return mPois.get(index);
        }

        protected BitmapDescriptor getBitmapDescriptor(int arg0) {
            if (arg0 < 10) {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
                        BitmapFactory.decodeResource(getResources(), markers[arg0]));
                return icon;
            }else {
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
                        BitmapFactory.decodeResource(getResources(), R.mipmap.map_marker_other_highlight));
                return icon;
            }
        }
    }

}



//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.amap.api.fence.PoiItem;
//import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationClient;
//import com.amap.api.location.AMapLocationClientOption;
//import com.amap.api.location.AMapLocationListener;
//import com.amap.api.maps.AMap;
//import com.amap.api.maps.CameraUpdateFactory;
//import com.amap.api.maps.LocationSource;
//import com.amap.api.maps.MapView;
//import com.amap.api.maps.model.BitmapDescriptorFactory;
//import com.amap.api.maps.model.CameraPosition;
//import com.amap.api.maps.model.LatLng;
//import com.amap.api.maps.model.Marker;
//import com.amap.api.maps.model.MarkerOptions;
//import com.amap.api.maps.model.MyLocationStyle;
//
//import com.amap.api.services.core.LatLonPoint;
//import com.amap.api.services.core.SuggestionCity;
//import com.amap.api.services.poisearch.PoiResult;
//import com.amap.api.services.poisearch.PoiSearch;
//
//import java.util.List;
//import java.util.logging.Logger;
//
//import static android.app.Activity.RESULT_OK;
//import static com.example.chang.highway.Mapshow.KEY_DES;
//import static com.example.chang.highway.Mapshow.KEY_LAT;
//import static com.example.chang.highway.Mapshow.KEY_LNG;
//
///**
// * Created by chang on 2018/12/28.
// */
//
//public class Mapshow extends AppCompatActivity  implements LocationSource,
//        AMapLocationListener, AMap.OnCameraChangeListener, PoiSearch.OnPoiSearchListener{
//    ListView mapList;
//
//    public static final String KEY_LAT = "lat";
//    public static final String KEY_LNG = "lng";
//    public static final String KEY_DES = "des";
//
//
//    private AMapLocationClient mLocationClient;
//    private LocationSource.OnLocationChangedListener mListener;
//    private LatLng latlng;
//    private String city;
//    private String deepType = "";// poi搜索类型
//    private PoiSearch.Query query;// Poi查询条件类
//    private PoiSearch poiSearch;
//    private PoiResult poiResult; // poi返回的结果
//    private PoiOverlay poiOverlay;// poi图层
//    private List<PoiItem> poiItems;// poi数据
//
//    private PoiSearch_adapter adapter;
//
//    private com.amap.api.maps.MapView map;
//    private Button button;
//    AMap amap;
//    private void permission(){
//        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
//                != PackageManager.PERMISSION_GRANTED)  {
//            //进行授权
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, 2);
//        }else{
//            //已经授权
//            //call();
//        }
//    }
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case 2:
//                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    //TODO
//                    showLocate();
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
//    private void showLocate(){
//        if (mLocationClient == null) {
//            mLocationClient = new AMapLocationClient(getApplicationContext());
//            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
//            //设置定位监听
//            mLocationClient.setLocationListener(this);
//            //设置为高精度定位模式
//            mLocationOption.setOnceLocation(true);
//            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//            //设置定位参数
//            mLocationClient.setLocationOption(mLocationOption);
//            mLocationClient.startLocation();
//        }
//
//        //设置地图的放缩级别
//        amap.moveCamera(CameraUpdateFactory.zoomTo(13));
//        MyLocationStyle myLocationStyle;
//        //初始化定位蓝点样式类
//        myLocationStyle = new MyLocationStyle();
//        //连续定位、且不将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
//        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
//        myLocationStyle.interval(2000);
//        //设置定位蓝点的Style
//        amap.setMyLocationStyle(myLocationStyle);
//        //设置默认定位按钮是否显示，非必需设置。
//        amap.getUiSettings().setMyLocationButtonEnabled(true);
//        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
//        amap.setMyLocationEnabled(true);
//    }
//
//
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.f1_mapshow);
//        permission();
//        map = findViewById(R.id.map);
//        button = findViewById(R.id.button);
//        map.onCreate(savedInstanceState);
//        if(amap == null){
//            amap = map.getMap();//初始化地图控制器对象那个
//            amap.setOnCameraChangeListener(this);
//        }
//        deepType = "餐饮";//这里以餐饮为例
//        amap.setTrafficEnabled(true);
//
////        LatLng latLng = new LatLng(121.525433,38.871007);
////        LatLng latLng1 = new LatLng(121.637479,39.033328);
////        //final Marker marker = amap.addMarker(new MarkerOptions().position(latLng).title("沈阳西收费站").snippet("DefaultMarker").visible(true));
////        MarkerOptions markerOption = new MarkerOptions();
////        markerOption.position(latLng);
////        markerOption.title("沈阳西收费站").snippet("西安市：34.341568, 108.940174");
////        markerOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.my_car));
////        markerOption.visible(true);
////        markerOption.draggable(true);//设置Marker可拖动
////        markerOption.setFlat(true);//设置marker平贴地图效果
////        amap.addMarker(markerOption);
//    }
//
//    /**
//     * 开始进行poi搜索
//     */
//    protected void doSearchQuery() {
//        amap.setOnMapClickListener(null);// 进行poi搜索时清除掉地图点击事件
//        int currentPage = 0;
//        query = new PoiSearch.Query("", deepType, city);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
//        query.setPageSize(20);// 设置每页最多返回多少条poiitem
//        query.setPageNum(currentPage);// 设置查第一页
//        LatLonPoint lp = new LatLonPoint(latlng.latitude, latlng.longitude);
//
//        poiSearch = new PoiSearch(this, query);
//        poiSearch.setOnPoiSearchListener(this);
//        poiSearch.setBound(new PoiSearch.SearchBound(lp, 5000, true));
//        // 设置搜索区域为以lp点为圆心，其周围2000米范围
//        poiSearch.searchPOIAsyn();// 异步搜索
//
//    }
//
//    @Override
//    public void onLocationChanged(AMapLocation aMapLocation) {
//        if (mListener != null && aMapLocation != null) {
//            if (aMapLocation.getErrorCode() == 0) {
//                // 显示我的位置
//                mListener.onLocationChanged(aMapLocation);
//                //设置第一次焦点中心
//                latlng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
//                amap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 14), 1000, null);
//                city = aMapLocation.getProvince();
//                doSearchQuery();
//            } else {
//                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
//                Log.e("AmapErr", errText);
//            }
//        }
//    }
//
//    @Override
//    public void activate(LocationSource.OnLocationChangedListener listener) {
//        mListener = listener;
//        mLocationClient.startLocation();
//    }
//
//    @Override
//    public void deactivate() {
//        mListener = null;
//        if (mLocationClient != null) {
//            mLocationClient.stopLocation();
//            mLocationClient.onDestroy();
//        }
//        mLocationClient = null;
//    }
//
//    @Override
//    public void onCameraChange(CameraPosition cameraPosition) {
//    }
//
//    @Override
//    public void onCameraChangeFinish(CameraPosition cameraPosition) {
//        latlng = cameraPosition.target;
//        amap.clear();
//        amap.addMarker(new MarkerOptions().position(latlng));
//        doSearchQuery();
//    }
//
//    @Override
//    public void onPoiSearched(PoiResult result, int rCode) {
//        if (rCode == 1000) {
//            if (result != null && result.getQuery() != null) {// 搜索poi的结果
//                if (result.getQuery().equals(query)) {// 是否是同一条
//                    poiResult = result;
//                    poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
//                    List<SuggestionCity> suggestionCities = poiResult
//                            .getSearchSuggestionCitys();
//                    if (poiItems != null && poiItems.size() > 0) {
//                        adapter = new PoiSearch_adapter(this, poiItems);
//                        mapList.setAdapter(adapter);
//                        mapList.setOnItemClickListener(new mOnItemClickListener());
//
//                    }
//                }
//                else {
//                    //Log.e("AmapErr");
//                }
//            }
//        } else {
//            //Logger.e("无结果");
//        }
//    } else if (rCode == 27) {
//        Logger.e("error_network");
//    } else if (rCode == 32) {
//        Logger.e("error_key");
//    } else {
//        Logger.e("error_other：" + rCode);
//    }
//}
//    /**
//     * POI信息查询回调方法
//     */
//    @Override
//    public void onPoiSearched(PoiResult result, int rCode) {
//
//        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
//            if (result != null && result.getQuery() != null) {  // 搜索poi的结果
//                if (result.getQuery().equals(query)) {  // 是否是同一条
//                    poiResult = result;
//                    final ArrayList<PoiAddressBean> data = new ArrayList<PoiAddressBean>();//自己创建的数据集合
//                    // 取得搜索到的poiitems有多少页
//                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
//                    List<SuggestionCity> suggestionCities = poiResult
//                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
//                    for(PoiItem item : poiItems){
//                        //获取经纬度对象
//                        LatLonPoint llp = item.getLatLonPoint();
//                        double lon = llp.getLongitude();
//                        double lat = llp.getLatitude();
//
//                        String title = item.getTitle();
//                        String text = item.getSnippet();
//                        String provinceName = item.getProvinceName();
//                        String cityName = item.getCityName();
//                        String adName = item.getAdName();
//                        data.add(new PoiAddressBean(String.valueOf(lon), String.valueOf(lat), title, text,provinceName,
//                                cityName,adName));
//                    }
//
//                    PoiKeywordSearchAdapter poiadapter = new PoiKeywordSearchAdapter(getActivity(),data);
//                    recyclerView.setAdapter(poiadapter);
//                    poiadapter.setItemClickListener(new PoiKeywordSearchAdapter.PoiItemClickListener() {
//                        @Override
//                        public void onItemClick(View view, int pos) {
//                            Double poiLatitude = Double.valueOf(data.get(pos).getLatitude()).doubleValue();
//                            Double poiLongtude = Double.valueOf(data.get(pos).getLongitude()).doubleValue();
//
//                            //通过经纬度重新再地图获取位置
//                            CameraPosition  cp = aMap.getCameraPosition();
//                            CameraPosition cpNew = CameraPosition.fromLatLngZoom(new LatLng(poiLatitude,poiLongtude),cp.zoom);
//                            CameraUpdate cu = CameraUpdateFactory.newCameraPosition(cpNew);
//                            aMap.moveCamera(cu);
//                            behavior.setState(4); //设置底部bottom sheet为半隐藏
//                            searchead.setText(null);//清空Edittext内容
//                            mHint.setText("附近热点");
//                            mHint.setTextColor(getResources().getColor(R.color.tab_Indicator_color));
//                            Log.d("ceshi","postion"+pos+"lat"+data.get(pos).getLatitude()+"long:"+data.get(pos).getLongitude());
//
//                        }
//                    });
//                }
//            } else {
//                Toast.makeText(getActivity(),"no_result",Toast.LENGTH_SHORT).show();
//
//            }
//        } else {
//            Toast.makeText(getActivity(),""+rCode,Toast.LENGTH_SHORT).show();
//
//        }
//
//    }
//
//    @Override
//    public void onPoiItemSearched(PoiItem poiItem, int i) {
//
//    }
//
////-------- 定位 End ------
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        map.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        map.onPause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        map.onDestroy();
//    }
//
//private class mOnItemClickListener implements AdapterView.OnItemClickListener {
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent();
//        intent.putExtra(KEY_LAT, poiItems.get(position).getLatLonPoint().getLatitude());
//        intent.putExtra(KEY_LNG, poiItems.get(position).getLatLonPoint().getLongitude());
//        intent.putExtra(KEY_DES, poiItems.get(position).getTitle());
//        setResult(RESULT_OK, intent);
//        finish();
//    }
//}
//
//
//}


