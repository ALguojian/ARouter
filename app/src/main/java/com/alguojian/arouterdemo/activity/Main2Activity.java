package com.alguojian.arouterdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alguojian.arouterdemo.R;
import com.alguojian.arouterdemo.Utils;
import com.alibaba.android.arouter.facade.annotation.Route;

@Route(path = Utils.MAIN_2_ACTIVITY)
public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }
}
