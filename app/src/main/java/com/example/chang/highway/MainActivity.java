package com.example.chang.highway;

import android.app.Fragment;
//import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static com.example.chang.highway.R.id.fragment_container;
import static com.example.chang.highway.R.layout.activity_main;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
        private TextView topBar;
        private TextView tabHome;
        private TextView tabOrder;
        private TextView tabUser;
        private TextView tabService;
        private Fragment menua;

        private FrameLayout ly_content;

        private FirstFragment f1;
        private FirstFragment ff1;
        private FourthFragment f14;
        private FourthFragment f24;
        private SecondFragment f2;
        private ThirdFragment f3;
        private FourthFragment f4;
        private FragmentManager fragmentManager;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            //requestWindowFeature(Window.FEATURE_NO_TITLE);

            Bmob.initialize(this, "d82d140b8ea7e0688c75b0109054f848");

            setContentView(activity_main);

            bindView();
            int id = getIntent().getIntExtra("id", 0);
            if (id == 1||id == 2) {
                ff1 = new FirstFragment();
                getFragmentManager()
                    .beginTransaction()
                        .replace(fragment_container,ff1)
                        .addToBackStack(null)
                        .commit();
            }
            int logid = getIntent().getIntExtra("logid", 0);
            if (logid == 1) {
                f14 = new FourthFragment();
                getFragmentManager()
                        .beginTransaction()
                        .replace(fragment_container,f14)
                        .addToBackStack(null)
                        .commit();
            }
            int licenseid = getIntent().getIntExtra("licenseid", 0);
            if (licenseid == 1) {
                f24 = new FourthFragment();
                getFragmentManager()
                        .beginTransaction()
                        .replace(fragment_container,f24)
                        .addToBackStack(null)
                        .commit();
            }

            // Intent intent0 = getIntent();
            //获取传递的值
            //String str = intent.getStringExtra("data");
            //设置值
            //tv = (TextView)this.findViewById(R.id.startsearch);
           // tv.setText(str);
        }

        //UI组件初始化与事件绑定

        private void bindView() {
            //topBar = (TextView)this.findViewById(R.id.txt_top);
            tabHome = (TextView)this.findViewById(R.id.txt_home);
            tabOrder = (TextView)this.findViewById(R.id.txt_order);
            tabService = (TextView)this.findViewById(R.id.txt_service);
            tabUser = (TextView)this.findViewById(R.id.txt_user);
            ly_content = (FrameLayout) this.findViewById(fragment_container);

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

        // 隐藏所有Fragment
        public void hideAllFragment(FragmentTransaction transaction){
            if(ff1!=null){
                transaction.hide(ff1);
            }
            if(f14!=null){
                transaction.hide(f14);
            }
            if(f24!=null){
                transaction.hide(f24);
            }
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
                        f1 = new FirstFragment();
                        transaction.add(fragment_container,f1);
                    }else{
                        transaction.show(f1);
                    }
                    break;

                case R.id.txt_order:
                    selected();
                    tabOrder.setSelected(true);
                    if(f2==null){
                        f2 = new SecondFragment();
                        transaction.add(fragment_container,f2);
                    }else{
                        transaction.show(f2);
                    }
                    break;

                case R.id.txt_service:
                    selected();
                    tabService.setSelected(true);
                    if(f3==null){
                        f3 = new ThirdFragment();
                        transaction.add(fragment_container,f3);
                    }else{
                        transaction.show(f3);
                    }
                    break;

                case R.id.txt_user:
                    selected();
                    tabUser.setSelected(true);
                    f4 = new FourthFragment();
                    transaction.add(fragment_container,f4);
                    break;
//                    if(f4==null){
//                        f4 = new FourthFragment();
//                        transaction.add(fragment_container,f4);
//                    }else{
//                        transaction.show(f4);
//                    }
//                    break;
            }

            transaction.commit();
        }
    }
