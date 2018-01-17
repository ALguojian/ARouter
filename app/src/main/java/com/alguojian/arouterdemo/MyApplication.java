package com.alguojian.arouterdemo;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;


/**
 * @author ALguojian
 * @date 2018/1/17
 * ${DESCRIPTION}
 */


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (isDebug) {
            ARouter.openLog();
            ARouter.openDebug();
        }

        ARouter.init(this);
    }
}
