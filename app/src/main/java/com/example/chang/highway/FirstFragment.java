package com.example.chang.highway;

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

/**
 * Created by chang on 2018/3/6.
 */


public class FirstFragment extends Fragment {
    private String context;
    private TextView mTextView;
    private Button start;
    private Button end;
    private Button time;
    private Button order;
    private Button startmap;
    private int year, month, day;
    private MyApplication myApp;

//    public  FirstFragment(String context){
//        this.context = context;
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_fragment, container, false);
        start = view.findViewById(R.id.start);
        end =  view.findViewById(R.id.end);
        time = view.findViewById(R.id.btn);
        order = view.findViewById(R.id.order);
        startmap = view.findViewById(R.id.startmap);

        myApp = (MyApplication)getApplicationContext();
        start.setText(myApp.getAppstart());
        end.setText(myApp.getAppend());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  HH-mm-ss");
        String date = dateFormat.format(new java.util.Date());


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getActivity(), StartSearch.class);
                //intent.putExtra("starttext",1);
                startActivity(intent);
                //startActivityForResult(intent,1);
            }
        });
        end.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent2 = new Intent(getActivity(), EndSearch.class);
                //intent.putExtra("starttext",1);
                startActivity(intent2);
                //startActivityForResult(intent,2);
            }
        });
        order.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String str = time.getText().toString();
                myApp.setApptime(str);
                Intent intent3 = new Intent(getActivity(), Order.class);
                startActivity(intent3);
            }
        });
        startmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent4 = new Intent(getActivity(),geo.class);
                //intent.putExtra("starttext",1);
                startActivity(intent4);
                //startActivityForResult(intent,1);
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date();
                datePickerDialog();
            }
        });

        mTextView = (TextView) view.findViewById(R.id.txt_content);
        //       mTextView = (TextView)getActivity().findViewById(R.id.txt_content);
        mTextView.setText(context);
        return view;
    }
        private void datePickerDialog() {
            new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
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
