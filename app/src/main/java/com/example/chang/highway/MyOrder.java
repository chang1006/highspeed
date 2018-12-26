package com.example.chang.highway;

import cn.bmob.v3.BmobObject;

/**
 * Created by chang on 2018/7/20.
 */

public class MyOrder extends BmobObject {
    private String user;
    private String number;
    private String start;
    private String end;
    private String time;
    private String money;
    private String status;

    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }

    public String getNumber(){
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }

    public String getStart() {
        return start;
    }
    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd(){
        return end;
    }
    public void setEnd(String end) {
        this.end = end;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getMoney() {
        return money;
    }
    public void setMoney(String money) {
        this.money = money;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

}
