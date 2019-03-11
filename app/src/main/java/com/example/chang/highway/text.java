package com.example.chang.highway;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

/**
 * Created by chang on 2019/3/7.
 */

public class text extends AppCompatActivity {
    private Button a;
    private Button b;
    private Button c;
    private Button d;
    private MyApplication myApp;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text);
        a = findViewById(R.id.a);
        b = findViewById(R.id.b);
        c = findViewById(R.id.c);
        d = findViewById(R.id.d);
//        Intent intent = getIntent();

        myApp = (MyApplication)getApplicationContext();

        String aa = String.valueOf(myApp.getAppLat1());
        String bb = String.valueOf(myApp.getAppLon1());
        String cc = String.valueOf(myApp.getAppLat2());
        String dd = String.valueOf(myApp.getAppLon2());
        a.setText(aa);
        b.setText(bb);
        c.setText(cc);
        d.setText(dd);
//        String aa = intent.getStringExtra("lat1");
//        String bb = intent.getStringExtra("lon1");
//        String cc = intent.getStringExtra("lat2");
//        String dd = intent.getStringExtra("lon2");
//        //a = findViewById(R.id.input_edittext);
//        a.setText(aa);
//        b.setText(bb);
//        c.setText(cc);
//        d.setText(dd);
    }
}
