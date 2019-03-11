package com.example.chang.highway;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by chang on 2019/2/21.
 */

public class weather_citysearch extends AppCompatActivity {

    private EditText weathercity;
    private Button weatherensure;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wearher_citysearch);
        weathercity = findViewById(R.id.weather_city);
        weatherensure = findViewById(R.id.weather_ensure);

        weatherensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String scity = weathercity.getText().toString();
                Intent intent=new Intent();
                //将参数放入intent
                intent.putExtra("scity", scity);
                setResult(RESULT_OK,intent);
                finish();
                //跳转到指定的Activity
                //intent.setClass(weather_citysearch.this,StartWeather.class);
                //启动Activity并返回结果
                //startActivityForResult(intent, 0x002);
//                Intent intent = new Intent(weather_citysearch.this,weather_citysearch.class);
//                startActivity(intent);
            }
        });
    }
}
