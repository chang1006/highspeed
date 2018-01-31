package com.example.register;

import com.example.highspeed.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

public class login extends Activity {
    private EditText username;
    private EditText password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		/*
		 * 1.初始化控件
		 * 2.需要一个适配器
		 * 3.初始化数据源
		 * 4.将adpter与当前AutoCompleteTextView绑定
		 */
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		
	}
}