package com.example.chang.highway;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by chang on 2018/10/26.
 */

public class ThirdFragment extends Fragment {
    //private ImageView imageView;
    //String imageUrl = "http://content.52pk.com/files/100623/2230_102437_1_lit.jpg";
    //Bitmap bmImg;
    //TextView goodsname , goodsdesc ,goodsprice;
    //private String[] data = {"1","2","3","4","5","6"};
     ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.third_fragment, container, false);
        //findView();
        //imageView = (ImageView) view.findViewById(R.id.iv);

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                getActivity(),android.R.layout.simple_list_item_1,data);
        listView = (ListView)view.findViewById(R.id.list);
//        ListView list = (ListView)view.findViewById(R.id.list);
//        list.setAdapter(adapter);
        loadDate();
        return view;
    }


    private void loadDate() {
        //final Goods[] goods ; //= (List<Goods>) new Goods();
        final ArrayList<MyOrder> goods = null;
        //BmobFile image = new BmobFile();
        BmobQuery<MyOrder> bmobQuery = new BmobQuery<MyOrder>();
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(new FindListener<MyOrder>() {
            @Override
            public void done(List<MyOrder> list, BmobException e) {
                System.out.println("查询成功"+list.get(0).getUser()+list.get(0).getNumber()+list.get(0).getStart()+list.get(0).getEnd());
                final String[] user  =  new String[list.size()];
                final String[] number  =  new String[list.size()];
                final String[] start  =  new String[list.size()];
                final String[] end  =  new String[list.size()];
                final String[] time  =  new String[list.size()];
                final String[] money  =  new String[list.size()];
                final String[] status  =  new String[list.size()];

                final BmobFile[] image = new BmobFile[list.size()];
                for(int i = 0;i<list.size();i++){
                    user[i] = list.get(i).getUser();
                    number[i] = list.get(i).getNumber();
                    start[i] = list.get(i).getStart();
                    end[i] = list.get(i).getEnd();
                    time[i] = list.get(i).getTime();
                    money[i] = list.get(i).getMoney();
                    status[i] = list.get(i).getStatus();
                    System.out.println("****************************");
                    //list.get(i).getIcon();//loadImageThumbnail(GroundActivity.this, imageView, 300, 300);
                    //image[i] = list.get(i).getIcon();
                    System.out.println("****************************2222");


                }
/**
 *
 */
                class MyAdapter extends BaseAdapter {
                    private Context context;
                    public MyAdapter(Context context){
                        this.context = context;
                    }

                    @Override
                    public int getCount() {
                        return user.length;
                    }

                    @Override
                    public Object getItem(int position) {
                        return user[position];
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        ViewHolder viewHolder;
                        if (convertView == null){
                            LayoutInflater inflater = LayoutInflater.from(context);
                            convertView = inflater.inflate(R.layout.third_item, null);//实例化一个布局文件
                            viewHolder = new ViewHolder();
                            //viewHolder.iv_img = (ImageView)convertView.findViewById(R.id.iv);
                            viewHolder.order_name = (TextView)convertView.findViewById(R.id.order_name);
                            viewHolder.order_number = (TextView)convertView.findViewById(R.id.order_number);
                            viewHolder.order_start = (TextView)convertView.findViewById(R.id.order_start);
                            viewHolder.order_end = (TextView)convertView.findViewById(R.id.order_end);
                            viewHolder.order_time = (TextView)convertView.findViewById(R.id.order_time);
                            viewHolder.order_money = (TextView)convertView.findViewById(R.id.order_money);
                            viewHolder.order_status = (TextView)convertView.findViewById(R.id.order_status);

                            convertView.setTag(viewHolder);
                        }else {
                            viewHolder = (ViewHolder) convertView.getTag();
                        }
                        //viewHolder.iv_img.setImageURI(image[position]);
                        viewHolder.order_name.setText(user[position]);
                        viewHolder.order_number.setText(number[position]);
                        viewHolder.order_start.setText(start[position]);
                        viewHolder.order_end.setText(end[position]);
                        viewHolder.order_time.setText(time[position]);
                        viewHolder.order_money.setText(money[position]);
                        viewHolder.order_status.setText(status[position]);
                        return convertView;
                    }
                    class ViewHolder{
                        //ImageView iv_img;
                        TextView order_name;
                        TextView order_number;
                        TextView order_start;
                        TextView order_end;
                        TextView order_time;
                        TextView order_money;
                        TextView order_status;
                    }
                }
                /**
                 *
                 */
                listView.setAdapter(new MyAdapter(getActivity()));
            }

 //           @Override
//            public void onError(int i, String s) {
//                System.out.println("查询数据失败");
//            }
        });

    }


}
