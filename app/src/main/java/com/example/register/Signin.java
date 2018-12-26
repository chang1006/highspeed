package com.example.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chang.highway.FourthFragment;
import com.example.chang.highway.MyUser;
import com.example.chang.highway.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by chang on 2018/3/6.
 */

public class Signin extends AppCompatActivity{
    private EditText username;
    private EditText password;
    private EditText password2;
    private Button button;
    private TextView textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
		/*
		 * 1.初始化控件
		 * 2.需要一个适配器
		 * 3.初始化数据源
		 * 4.将adpter与当前AutoCompleteTextView绑定
		 */
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        password2 = (EditText) findViewById(R.id.password2);
        button = (Button)findViewById(R.id.button);
        textView2 = (TextView)findViewById(R.id.textView2);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent logining = new Intent();
                logining.setClass(Signin.this, Login.class);
                startActivity(logining);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 获取用户输入的用户名和密码
                String Username = username.getText().toString();
                String Password = password.getText().toString();
                String Password2 = password2.getText().toString();
                if (!Password.equals(Password2)) {
                    Toast.makeText(Signin.this, "两次输入密码不同，请重新输入！", Toast.LENGTH_LONG).show();
                    return;
                }
                if(Username.equals("")||Password.equals("")){
                    Toast.makeText(Signin.this, "帐号或密码不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                // 使用BmobSDK提供的注册功能
                MyUser user = new MyUser();
                user.setName(Username);
                user.setPassWord(Password);
                user.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Toast.makeText(Signin.this, "注册成功，ObjectId:" + s, Toast.LENGTH_SHORT).show();
                            Intent intent =new Intent(Signin.this, Login.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Signin.this, "注册失败，错误信息：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}