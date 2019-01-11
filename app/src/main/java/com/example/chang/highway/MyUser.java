package com.example.chang.highway;

import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by chang on 2018/3/8.
 */

public class MyUser extends BmobObject {
    private String name;
    private String password;
    private String phone;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPassWord() {
        return password;
    }
    public void setPassWord(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}