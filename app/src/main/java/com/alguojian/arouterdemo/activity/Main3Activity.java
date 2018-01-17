package com.alguojian.arouterdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alguojian.arouterdemo.R;
import com.alguojian.arouterdemo.Utils;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

@Route(path = Utils.MAIN_3_ACTIVITY)
public class Main3Activity extends AppCompatActivity {

    @Autowired
    public String bookId;

    @Autowired
    public String chapterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        ARouter.getInstance().inject(this);
        System.out.println("---" + bookId);
    }
}
