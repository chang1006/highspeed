package com.example.chang.highway;

import android.app.Application;

/**
 * Created by chang on 2018/5/15.
 */

public class MyApplication extends Application {
    public String appstart;
    public String append;
    public String appuser;
    public String apptime;
    public String appnumber;
    public String apptype;
    public String appmoney;
    public Double applat;
    public Double applon;
    public String getAppstart() {
        return appstart;
    }
    public void setAppstart(String appstart) {
        this.appstart = appstart;
    }

    public String getAppend() {
        return append;
    }
    public void setAppend(String append) {
        this.append = append;
    }

    public String getAppuser() {
        return appuser;
    }
    public void setAppuser(String appuser) {
        this.appuser = appuser;
    }

    public String getApptime() {
        return apptime;
    }
    public void setApptime(String apptime) {
        this.apptime = apptime;
    }

    public String getAppnumber() {
        return appnumber;
    }
    public void setAppnumber(String appnumber) {
        this.appnumber = appnumber;
    }

    public String getApptype() {
        return apptype;
    }
    public void setApptype(String apptype) {
        this.apptype = apptype;
    }

    public String getAppmoney() {
        return appmoney;
    }
    public void setAppmoney(String appmoney) {
        this.appmoney = appmoney;
    }

    public Double getAppLat() {
        return applat;
    }
    public void setAppLat(Double applat) {
        this.applat = applat;
    }
    public Double getAppLon() {
        return applon;
    }
    public void setAppLon(Double applon) {
        this.applon = applon;
    }
}
