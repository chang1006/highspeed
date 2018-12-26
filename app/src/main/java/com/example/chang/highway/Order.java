package com.example.chang.highway;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Map;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.example.chang.highway.util.OrderInfoUtil2_0;
import com.example.chang.highway.AuthResult;
import com.example.chang.highway.PayResult;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Created by chang on 2018/5/21.
 */

public class Order extends AppCompatActivity {
    private TextView start;
    private TextView end;
    private TextView time;
    private TextView licence;
    private TextView type;
    public TextView money;
    public static String smoney;

    private MyApplication myApp;

    protected void onCreate(Bundle savedInstanceState) {

        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        setContentView(R.layout.pay_order);

        super.onCreate(savedInstanceState);
        start = findViewById(R.id.start);
        end = findViewById(R.id.end);
        time = findViewById(R.id.time);
        licence = findViewById(R.id.licence);
        type = findViewById(R.id.type);
        money = findViewById(R.id.money);

        myApp = (MyApplication)getApplicationContext();
        start.setText(myApp.getAppstart());
        end.setText(myApp.getAppend());
        time.setText(myApp.getApptime());
        licence.setText(myApp.getAppnumber());
        type.setText(myApp.getApptype());


        String starts = start.getText().toString();
        String ends = end.getText().toString();
        String types = type.getText().toString();


        if (starts.equals("沈阳西")) {
            if (ends.equals("红旗台")) {
                if(types.equals("≤7座")) {
                    double imoney = 0.35 * 8;
                    smoney = String.valueOf(imoney);
                    money.setText(smoney);
                }
                else{
                    double imoney = 0.8 * 8;
                    smoney = String.valueOf(imoney);
                    money.setText(smoney);
                }
            }
            else if(ends.equals("陵园街")){
                if(types.equals("≤7座")) {
                    double imoney = 0.35 * 23;
                    smoney = String.valueOf(imoney);
                    money.setText(smoney);
                }
                else{
                    double imoney = 0.8 * 23;
                    smoney = String.valueOf(imoney);
                    money.setText(smoney);
                }
            }
            else if(ends.equals("朱尔屯")){
                if(types.equals("≤7座")) {
                    double imoney = 0.35 * 72;
                    smoney = String.valueOf(imoney);
                    money.setText(smoney);
                }
                else{
                    double imoney = 0.8 * 72;
                    smoney = String.valueOf(imoney);
                    money.setText(smoney);
                }
            }
            else if(ends.equals("王家沟")){
                if(types.equals("≤7座")) {
                    double imoney = 0.35 * 77;
                    smoney = String.valueOf(imoney);
                    money.setText(smoney);
                }
                else{
                    double imoney = 0.8 * 77;
                    smoney = String.valueOf(imoney);
                    money.setText(smoney);
                }
            }

        }
        else if(starts.equals("二十里铺")){
             if (ends.equals("沈阳西")) {
                if(types.equals("≤7座")) {
                    double imoney = 0.35 * 337;
                    smoney = String.valueOf(imoney);
                    money.setText(smoney);
                }
                else{
                    double imoney = 0.8 * 337;
                    smoney = String.valueOf(imoney);
                    money.setText(smoney);
                }
            }
            else if(ends.equals("陵园街")){
                if(types.equals("≤7座")) {
                    double imoney = 0.35 * 23;
                    smoney = String.valueOf(imoney);
                    money.setText(smoney);
                }
                else{
                    double imoney = 0.8 * 23;
                    //String smoney = String.format("%.2f", imoney);
                    smoney = String.valueOf(imoney);
                    money.setText(smoney);
                }
            }
        }



    }

    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2016091400511123";

    /** 支付宝账户登录授权业务：入参pid值 */
    public static final String PID = "2088102175626965";
    /** 支付宝账户登录授权业务：入参target_id值 */
    public static final String TARGET_ID = "";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
    public static final String RSA2_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCH2Gi0AlQgVhZQj1BH1W64WMEeIDZsmbkth7nT459jcLqSFBO8+7bbGGs9EutGTQ7RYQ3mIA/il59ISl+65fY61/lwDn2Cd/nzC7/FJydifBJ1CZjgoE1YKYI3kIUBTm4iNrzfNY/e61gNhJ5yiCgx7kqIN4w0oMrvRVnOqGQyRVA581S5p7vTr65gebXH2Lu/UI3hTy7BfIPpaF1JxeADN76Hb6uSp/i38nqy/2P45HCXjc5RasmsChSn97M9VhByu2iJ41TSEyvepDqdAptzp3z93jlBPSFRXW5hoXiLmk8bLRugszcgtf8h7CIl/AQi5LL3Z0WgwQmxFsS3QXj7AgMBAAECggEAe5Fw/H05xQhJ2BHk9tLAmlOxpxOqLfNPLIlPkdx1+eBq9cHpinh1hqqZsb5p4VLq7a6ZFAGKMZW2P+3XUo0oA7K9pR6pMGquJFVFpbLN8pnluCx4satfb5iiaKS/N8b+C4U7HNb9/cqaGAHQl2IfHfwiM/JDSQQSVMACxycJrWdk+qeZWuXm9uYnNNL+HB5oIFefc+PGV+QMRHuiauo0rG8aGr9bt4pxgiU8BDeod/Lm0SlvaSMKKoyuUMYUUqRmhKAConn0JubjTol3fqb+Xvns3EmcxOSr/8O6n/gRbMzWi2ga1Hjk+LBcWcZumnz6lyWTdErfqCzwGF+nVUickQKBgQDVv9lEaE6OoqTI/v6dBLvBnkxcb3j/qhiQeebSLdA4TXuom6RsKHTnW2MrgEtcg2/Z5DTs6Wv9m6nuaivAL3j13M0bF05dnRtu0tDLZ1fzI08yA7H73H2y+p2195DDVi/OuHxHwLriJgdUEwWQWY8tnJHFaqyiTnuh+rYgY4WWJQKBgQCisnUnfNXmjAipm84Z9bTSfUsRKYqPeVMMQ00N/zX+deEpBdHVSlf+hjYU897bg0BbslvBVYCMQtjZg2fNQuLynCfDaxcSSqh00ZMmEZesHDGX6bKnvmxxur86qVoBZO9/oyL+pMC/vuiaMr7935RZdQjOS+JB4R0qm4oMehLYnwKBgQCMwBb+j7zi+cCOxjK9DIfiWC2qVJEVsVkZkdvnnNlWqdb60Pp1ajKIwknMNF/rnHVDwp0DQDWnOyCr6OfEVr5ND8dTxoYI6NJOqK1qszNb3G1i3sINukRfXd4KG+oMZq12b08PJJ8rGaWLvDbWtdKtU0M9/x+MhSeuItxY+Y/WrQKBgQCY3IEUS8cRKMrOyLOZUnDFP7Ey+8vSx5QOtoEEQJ/zZ7/Ycj5XXtVUL7dKcmMV7bifLvHksTfpELl7jGoPweNHPQ5jHg/8QKcLswjFQMw3ifnSmGEiF9j99g7Lx+fn3XTkgFtV02fCAFW84ybtko5oinxuj3+SlDZ6HM9Ur7FbUwKBgBg3yv5dBqWY5UVWnXNWNTewtmEIp5ZYd0YMUibgk5WIseiwFTOQdkYOfagbywxcchtwbWb3AkC3iZ+/1FRQWJrhkwlaNxxNKLKCuDJR58tAx2SL3xw/EvDcVG80BjgS466vziXnTmU0lUCHqRLV3GrFHs+F0CuZL0CrwJyLxM6E";
    public static final String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        myApp = (MyApplication)getApplicationContext();
                        MyOrder orders = new MyOrder();
                        orders.setUser(myApp.getAppuser());
                        orders.setNumber(myApp.getAppnumber());
                        orders.setStart(myApp.getAppstart());
                        orders.setEnd(myApp.getAppend());
                        orders.setTime(myApp.getApptime());
                        String strmoney = money.getText().toString();
                        orders.setMoney(strmoney);
                        orders.setStatus("已支付");
                        orders.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    Toast.makeText(Order.this, "支付成功" , Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Order.this, "支付失败" , Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        //Toast.makeText(Order.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(Order.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(Order.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(Order.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        };
    };



    /**
     * 支付宝支付业务
     *
     * @param v
     */
    public void payV2(View v) {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(Order.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 支付宝账户授权业务
     *
     * @param v
     */
    public void authV2(View v) {
        if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID)
                || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))
                || TextUtils.isEmpty(TARGET_ID)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER |APP_ID| RSA_PRIVATE| TARGET_ID")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * authInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
        final String authInfo = info + "&" + sign;
        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(Order.this);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);

                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }

    /**
     * get the sdk version. 获取SDK版本号
     *
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
    }


}
