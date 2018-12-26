package com.example.register;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chang.highway.FourthFragment;
import com.example.chang.highway.MyUser;
import com.example.chang.highway.R;

import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
//import cn.bmob.v3.BmobSMS;
import cn.bmob.sms.BmobSMS;
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
    private EditText phonenumber;
    private TextView sendverification;
    private EditText verification;
    private Button button;
    private TextView textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cn.bmob.sms.BmobSMS.initialize(Signin.this,"62f79064bc2e4d00c92b961e3045a835");
        setContentView(R.layout.signin);
		/*
		 * 1.初始化控件
		 * 2.需要一个适配器
		 * 3.初始化数据源
		 * 4.将adpter与当前AutoCompleteTextView绑定
		 */
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        password2 = findViewById(R.id.password2);
        phonenumber = findViewById(R.id.phonenumber);
        sendverification = findViewById(R.id.sendverification);
        verification = findViewById(R.id.verification);
        button = findViewById(R.id.button);
        textView2 = findViewById(R.id.textView2);

        sendverification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String phone = phonenumber.getText().toString();
                cn.bmob.sms.BmobSMS.requestSMSCode(Signin.this, phone, "DataSDK", new RequestSMSCodeListener() {
                    @Override
                    public void done(Integer integer, cn.bmob.sms.exception.BmobException e) {
                        if(e == null){
                            new CountDownTimer(60000,1000){
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    sendverification.setText(millisUntilFinished / 1000 + "秒");
                                }
                                @Override
                                public void onFinish() {
                                    sendverification.setClickable(true);
                                    sendverification.setText("重新发送");
                                }
                            }.start();
                            Toast.makeText(Signin.this,"验证码发送成功，请尽快验证",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Signin.this,"验证码发送失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

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
                String phone = phonenumber.getText().toString();
                String number = verification.getText().toString();
                cn.bmob.sms.BmobSMS.verifySmsCode(Signin.this, phone, number, new VerifySMSCodeListener() {
                    @Override
                    public void done( cn.bmob.sms.exception.BmobException e) {
                        if(e==null){
                            Toast.makeText(Signin.this,"验证成功",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(Signin.this,"验证失败",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                MyUser user = new MyUser();
                user.setName(Username);
                user.setPassWord(Password);
                user.setPhone(phone);
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