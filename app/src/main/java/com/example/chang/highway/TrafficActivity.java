package com.example.chang.highway;

/**
 * Created by chang on 2019/3/1.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.TextOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.traffic.CircleTrafficQuery;
import com.amap.api.services.traffic.RoadTrafficQuery;
import com.amap.api.services.traffic.TrafficSearch;
import com.amap.api.services.traffic.TrafficStatusInfo;
import com.amap.api.services.traffic.TrafficStatusResult;
//import com.amap.map3d.demo.R;

import java.util.ArrayList;
import java.util.List;


public class TrafficActivity extends Activity implements View.OnClickListener, TrafficSearch.OnTrafficSearchListener {

    private AMap aMap;
    private MapView mapView;
    private EditText highway;
    private EditText city;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private List<String> list = new ArrayList<String>();
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    ProgressDialog progDialog;

    TrafficSearch trafficSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_trafficsearch);

        highway = findViewById(R.id.highway);
        city = findViewById(R.id.city);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        manager = getFragmentManager();

        aMap = mapView.getMap();

        findViewById(R.id.traffic_road).setOnClickListener(this);
        //findViewById(R.id.traffic_circle).setOnClickListener(this);

        trafficSearch = new TrafficSearch(this);
        trafficSearch.setTrafficSearchListener(this);

        highway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ShowDialog();
            }
        });

        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ShowDialog2();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.traffic_road :
                loadRoadTraffic();
                break;
            case R.id.highway :
                ShowDialog();
                break;
            case R.id.city :
                loadRoadTraffic();
                break;
//            case R.id.traffic_circle :
//                loadCircleTraffic();
//                break;
        }
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
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

    private void loadRoadTraffic() {

        showProgressDialog();
        String shighway = highway.getText().toString();
        String scity = city.getText().toString();
        String scitycode = " ";
        switch (scity) {
            case "鞍山市" :
                scitycode = "210300";
                break;
            case "本溪市" :
                scitycode = "210500";
                break;
            case "朝阳市" :
                scitycode = "211300";
                break;
            case "大连市" :
                scitycode = "210200";
                break;
            case "丹东市" :
                scitycode = "210600";
                break;
            case "抚顺市" :
                scitycode = "210400";
                break;
            case "阜新市" :
                scitycode = "210900";
                break;
            case "葫芦岛市" :
                scitycode = "211400";
                break;
            case "锦州市" :
                scitycode = "210700";
                break;
            case "辽阳市" :
                scitycode = "211000";
                break;
            case "盘锦市" :
                scitycode = "211100";
                break;
            case "沈阳市" :
                scitycode = "210100";
                break;
            case "铁岭市" :
                scitycode = "211200";
                break;
            case "营口市" :
                scitycode = "210800";
                break;
        }
        RoadTrafficQuery roadTrafficQuery = new RoadTrafficQuery(shighway, scitycode, TrafficSearch.ROAD_LEVEL_NORMAL_WAY);
        trafficSearch.loadTrafficByRoadAsyn(roadTrafficQuery);
    }

    public void ShowDialog() {
        Context context = TrafficActivity.this;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.search_list, null);
        ListView myListView = (ListView) layout.findViewById(R.id.formcustomspinner_list);

        String[] list = {"G1京哈高速","G11鹤大高速","G1113丹阜高速","G1212沈吉高速","G15沈海高速","G1501沈阳绕城高速","G16丹锡高速","G25长深高速","G2511新鲁高速",
                "G2512阜锦高速","G91辽宁中部环线高速"};
        String str = highway.getText().toString();
        ArrayAdapter<String> adapterA = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list);
        myListView.setAdapter(adapterA);

        // MyAdapter adapter = new MyAdapter(context, list);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                //在这里面就是执行点击后要进行的操作,这里只是做一个显示
                String mlist = arg0.getItemAtPosition(position).toString();
                highway.setText(mlist);
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

    public void ShowDialog2() {
        Context context = TrafficActivity.this;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.search_list, null);
        ListView myListView = (ListView) layout.findViewById(R.id.formcustomspinner_list);

        String[] list = {"鞍山市","本溪市","朝阳市","大连市","丹东市","抚顺市","阜新市","葫芦岛市","锦州市",
                "辽阳市","盘锦市","沈阳市","铁岭市","营口市"};
        String str = city.getText().toString();
        ArrayAdapter<String> adapterA = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list);
        myListView.setAdapter(adapterA);

        // MyAdapter adapter = new MyAdapter(context, list);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                //在这里面就是执行点击后要进行的操作,这里只是做一个显示
                String mlist = arg0.getItemAtPosition(position).toString();
                city.setText(mlist);
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


    private void loadCircleTraffic() {

        showProgressDialog();
        LatLonPoint latLonPoint = new LatLonPoint(39.98641364, 116.3057764);
        CircleTrafficQuery trafficQuery = new CircleTrafficQuery(latLonPoint, 500, TrafficSearch.ROAD_LEVEL_NONAME_WAY);
        trafficSearch.loadTrafficByCircleAsyn(trafficQuery);
    }

    @Override
    public void onRoadTrafficSearched(TrafficStatusResult roadTrafficResult, int errorCode) {
        Log.e("amap", "===>>>> result code " + errorCode);
        dissmissProgressDialog();// 隐藏对话框
        aMap.clear();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(TrafficStatusInfo trafficStatusInfo : roadTrafficResult.getRoads()) {
            List<LatLonPoint> list = trafficStatusInfo.getCoordinates();
            if(list != null){
                LatLonPoint latLonPoint = list.get(0);

                aMap.addText(new TextOptions().position(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude())).text(trafficStatusInfo.getName() + " " + trafficStatusInfo.getDirection() + " " + trafficStatusInfo.getStatus()));

                List<LatLng> latLngs = new ArrayList<LatLng>();
                for(LatLonPoint latLonPoint2 : list)
                {
                    LatLng latLng = new LatLng(latLonPoint2.getLatitude(), latLonPoint2.getLongitude());
                    builder.include(latLng);
                    latLngs.add(latLng);
                }
                int color = Color.GREEN;
                switch (Integer.parseInt(trafficStatusInfo.getStatus())) {
                    case 0: {
                        color = Color.GRAY;
                    }

                    break;
                    case 1: {
                        color = Color.GREEN;
                    }

                    break;
                    case 2: {
                        color = Color.YELLOW;
                    }

                    break;
                    case 3: {
                        color = Color.RED;
                    }

                    break;
                }
                aMap.addPolyline((new PolylineOptions()).addAll(latLngs).color(color));
            }
        }

        LatLngBounds bounds  = builder.build();
        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,10));

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}