package com.example.highspeed;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class FourthFragment extends Fragment {
   

	private TextView button1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fourth_fragment,container,false);
       button1 = (TextView)view.findViewById(R.id.txt_content);
       button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//在当前onClick方法中监听点击button
				//显示intent
				Intent intent = new Intent("com.example.highspeed.ACTION_START");
				startActivity(intent);
			System.out.println("hhhh");	
			Log.i("tag","hhhh");//收集log日志
			}
		});
 //       mTextView = (TextView)getActivity().findViewById(R.id.txt_content);     
        return view;
    }
}