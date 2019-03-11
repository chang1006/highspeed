package com.example.chang.highway;

/**
 * Created by chang on 2019/2/20.
 */

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static cn.bmob.v3.Bmob.getApplicationContext;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Fragment;
//import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.maps.MapView;
import com.example.chang.highway.util.mapshow;
import com.example.register.Login;
import com.example.register.Signin;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static cn.bmob.v3.Bmob.getApplicationContext;



public class StartOrder extends AppCompatActivity {
    private String context;
    private TextView mTextView;
    private Button start;
    private Button end;
    private Button time;
    private Button order;
    private Button startmap;
    private String starts;
    private String ends;
    private int year, month, day;
    private MyApplication myApp;

//    public  FirstFragment(String context){
//        this.context = context;
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_order);
        //View view = inflater.inflate(R.layout.first_fragment, container, false);
        start = findViewById(R.id.start);
        end =  findViewById(R.id.end);
        time = findViewById(R.id.btn);
        order = findViewById(R.id.order);
        //startmap = findViewById(R.id.startmap);

        myApp = (MyApplication)getApplicationContext();
        start.setText(myApp.getAppstart());
        end.setText(myApp.getAppend());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  HH-mm-ss");
        String date = dateFormat.format(new java.util.Date());


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(StartOrder.this, StartSearch.class);
                //intent.putExtra("starttext",1);
                startActivityForResult(intent,1);
                //startActivityForResult(intent,1);
            }
        });
        end.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent2 = new Intent(StartOrder.this, EndSearch.class);
                //intent.putExtra("starttext",1);
                startActivityForResult(intent2,2);
                //startActivityForResult(intent,2);
            }
        });
        order.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String str = time.getText().toString();
                myApp.setApptime(str);
                Intent intent3 = new Intent(StartOrder.this, OrderTolls.class);
                startActivity(intent3);
            }
        });
//        startmap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                Intent intent4 = new Intent(StartOrder.this,geo.class);
//                //intent.putExtra("starttext",1);
//                startActivity(intent4);
//                //startActivityForResult(intent,1);
//            }
//        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date();
                datePickerDialog();
            }
        });

        //mTextView = (TextView) findViewById(R.id.txt_content);
        //       mTextView = (TextView)getActivity().findViewById(R.id.txt_content);
        //mTextView.setText(context);
        //return view;
    }

    protected void onActivityResult (int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    starts = data.getStringExtra("str");
                    Log.e("11111",starts);
                        start.setText(starts);
                }
                break;
            case 2:
                if(resultCode == RESULT_OK){
                    ends = data.getStringExtra("str");
                    Log.e("11112",ends);
                    end.setText(ends);
                }
                break;
            default:
        }
    }


    private void datePickerDialog() {
        new DatePickerDialog(StartOrder.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                String date = String.format("%d-%d-%d", year, monthOfYear, dayOfMonth);
                time.setText(date);
            }
        }, year, month, day).show();
    }

    //获取当前系统时间
    private void date() {
        Calendar c = Calendar.getInstance();
        //年
        year = c.get(Calendar.YEAR);
        //月
        month = c.get(Calendar.MONTH);
        //日
        day = c.get(Calendar.DAY_OF_MONTH);

    }




}
