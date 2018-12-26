package com.example.chang.highway;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by chang on 2018/3/6.
 */

public class SecondFragment extends Fragment {
    private TextView license;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.second_fragment, container, false);
        license = view.findViewById(R.id.txt_content);
        return view;
    }
}
