package com.example.chang.highway;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


import com.example.register.Login;
import com.example.register.Signin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chang on 2018/5/31.
 */

public class License extends AppCompatActivity {
    private EditText number;
    private EditText type;
    private Button button;
    private MyApplication myApp;
    private List<String> list = new ArrayList<String>();
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.license);
        number = (EditText) findViewById(R.id.number);
        type = (EditText) findViewById(R.id.type);
        button = (Button)findViewById(R.id.button);
        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Context context = License.this;
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.license_list, null);
                ListView myListView = (ListView) layout.findViewById(R.id.formcustomspinner_list);

                String[] list = {"≤7座","＞7座"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list);
                myListView.setAdapter(adapter);


                myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                        //在这里面就是执行点击后要进行的操作,这里只是做一个显示
                        String mlist = arg0.getItemAtPosition(position).toString();
                        type.setText(mlist);
//                        Toast.makeText(StartSearch.this, "您点击的是"+mlist, Toast.LENGTH_SHORT).show();
                        if (alertDialog != null) {
                            alertDialog.dismiss();
                        }
                    }
                });
                builder = new AlertDialog.Builder(context);
                builder.setView(layout);
                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String Number = number.getText().toString();
                String Type = type.getText().toString();

                myApp = (MyApplication) getApplicationContext();
                myApp.setAppnumber(Number);
                myApp.setApptype(Type);

                Intent lic = new Intent(License.this, MainActivity.class);
                lic.putExtra("licenseid",1);
                startActivity(lic);
            }
        });
    }
}
