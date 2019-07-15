package com.abu.timermanager.bao;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by lenovo on 2018/8/17.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            JPushInterface.init(this);
        }catch (Exception e){

        }

    }
}
