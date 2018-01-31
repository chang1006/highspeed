package com.example.highspeed;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{
    private TextView topBar;
    private TextView tabHome;
    private TextView tabOrder;
    private TextView tabUser;
    private TextView tabService;

    private FrameLayout ly_content;

    private FirstFragment f1,f2,f3;
    //private FirstFragment f2;
    //private FirstFragment f3;
    private FourthFragment f4;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        bindView();

    }

    //UI组件初始化与事件绑定
    private void bindView() {
        topBar = (TextView)this.findViewById(R.id.txt_top);
        tabHome = (TextView)this.findViewById(R.id.txt_home);
        tabOrder = (TextView)this.findViewById(R.id.txt_order);
        tabService = (TextView)this.findViewById(R.id.txt_service);
        tabUser = (TextView)this.findViewById(R.id.txt_user);
        ly_content = (FrameLayout) this.findViewById(R.id.fragment_container);

        tabHome.setOnClickListener(this);
        tabUser.setOnClickListener(this);
        tabService.setOnClickListener(this);
        tabOrder.setOnClickListener(this);

    }

     //重置所有文本的选中状态
    public void selected(){
        tabHome.setSelected(false);
        tabUser.setSelected(false);
        tabOrder.setSelected(false);
        tabService.setSelected(false);
    }

//     隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction){
        if(f1!=null){
            transaction.hide(f1);
        }
        if(f2!=null){
            transaction.hide(f2);
        }
        if(f3!=null){
            transaction.hide(f3);
        }
        if(f4!=null){
            transaction.hide(f4);
        }
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        switch(v.getId()){
            case R.id.txt_home:
                selected();
                tabHome.setSelected(true);
                if(f1==null){
                    f1 = new FirstFragment("第一个Fragment");
                    transaction.add(R.id.fragment_container,f1);
                }else{
                    transaction.show(f1);
                }
                break;

            case R.id.txt_order:
                selected();
                tabOrder.setSelected(true);
                if(f2==null){
                    f2 = new FirstFragment("第二个Fragment");
                    transaction.add(R.id.fragment_container,f2);
                }else{
                    transaction.show(f2);
                }
                break;

            case R.id.txt_service:
                selected();
                tabService.setSelected(true);
                if(f3==null){
                    f3 = new FirstFragment("第三Fragment");
                    transaction.add(R.id.fragment_container,f3);
                }else{
                    transaction.show(f3);
                }
                break;

            case R.id.txt_user:
                selected();
                tabUser.setSelected(true);
                if(f4==null){
                    f4 = new FourthFragment();
                    transaction.add(R.id.fragment_container,f4);
                }else{
                    transaction.show(f4);
                }
                break;
        }

        transaction.commit();
    }
}