package com.example.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chang.highway.FourthFragment;
import com.example.chang.highway.MainActivity;
import com.example.chang.highway.MyApplication;
import com.example.chang.highway.MyUser;
import com.example.chang.highway.R;
import com.example.chang.highway.StartSearch;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by chang on 2018/3/6.
 */

public class Login extends AppCompatActivity{
    private EditText username;
    private EditText password;
    private Button button;
    private TextView textView;
    private MyApplication myApp;
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
        button = (Button)findViewById(R.id.button);
        textView = (TextView)findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent signining = new Intent();
                signining.setClass(Login.this, Signin.class);
                startActivity(signining);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 获取用户输入的用户名和密码
                String Username = username.getText().toString();
                String Password = password.getText().toString();
                if(Username.equals("")||Password.equals("")){
                    Toast.makeText(Login.this, "帐号或密码不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                // 查询与注册信息是否匹配
                BmobQuery<MyUser> query=new BmobQuery<MyUser>();
                query.addWhereEqualTo("name", Username);
                query.addWhereEqualTo("password", Password);
                query.findObjects(new FindListener<MyUser>() {

                    @Override
                    public void done(List<MyUser> arg0, BmobException e) {
                        // TODO Auto-generated method stub
                         if(e == null){
                            String gname = arg0.get(0).getName().toString();
                            String gpassword = arg0.get(0).getPassWord().toString();

                            String name = username.getText().toString();
                            String pass = password.getText().toString();
                            Toast.makeText(Login.this, gname, Toast.LENGTH_LONG).show();
                            if(gname.equals(name)&&gpassword.equals(pass))
                            {
                                //Toast.makeText(Login.this, "登陆成功", Toast.LENGTH_LONG).show();
//                                Intent seccess = new Intent();
//                                seccess.setClass(Login.this, MainActivity.class);
//                                startActivity(seccess);
                                String str = username.getText().toString();
                                myApp = (MyApplication) getApplicationContext();
                                myApp.setAppuser(str);

                                Intent log = new Intent(Login.this, MainActivity.class);
                                log.putExtra("logid",1);
                                startActivity(log);

                             }

                             }
                         else{
                        Toast.makeText(Login.this, "帐号或密码有误", Toast.LENGTH_LONG).show();
                    }

                   }
                });


            }

        });
    }
}