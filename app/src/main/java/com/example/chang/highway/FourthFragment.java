package com.example.chang.highway;

/**
 * Created by chang on 2018/3/6.
 */

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.register.Signin;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

public class FourthFragment extends Fragment {


    private TextView tex;
    private Button logintext;
    private TextView license;
    private TextView licensenumber;
    private TextView licensetype;
    private MyApplication myApp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fourth_fragment,container,false);
        tex = (TextView)view.findViewById(R.id.txt_content);
        logintext = (Button)view.findViewById(R.id.logintext);
        license = view.findViewById(R.id.license);
        licensenumber = view.findViewById(R.id.licensenumber);
        licensetype = view.findViewById(R.id.licensetype);

        myApp = (MyApplication)getApplicationContext();
        logintext.setText(myApp.getAppuser());
        myApp = (MyApplication)getApplicationContext();
        licensenumber.setText(myApp.getAppnumber());
        licensetype.setText(myApp.getApptype());

        logintext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //在当前onClick方法中监听点击button
                //显示intent
                Intent intent = new Intent("com.example.register.ACTION_START");
                startActivity(intent);
                //System.out.println("hhhh");
               // Log.i("tag","hhhh");//收集log日志


            }
        });
        license.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getActivity(), License.class);
                startActivity(intent);
            }
        });

        return view;
    }
}