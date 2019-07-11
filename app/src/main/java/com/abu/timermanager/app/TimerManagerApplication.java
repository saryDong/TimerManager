package com.abu.timermanager.app;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

public class TimerManagerApplication extends Application {
    protected static Application instance;
    private static Context context;                 //上下文

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    /**
     * 初始化
     */
    private void init() {

        //初始化LitePal
        LitePal.initialize(this);
        context = getApplicationContext();
        instance=this;
    }

    /**
     * 获取上下文context
     * @return  context
     */
    public static Context getContext(){
        return context;
    }

    public static Application getInstance() {
        return instance;
    }
}
