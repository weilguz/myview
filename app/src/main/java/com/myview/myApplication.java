package com.myview;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * @author weilgu
 * @time 2017/10/8  16:42
 * @desc ${TODD}
 */

public class myApplication extends Application {//implements Thread.UncaughtExceptionHandler{

    @Override
    public void onCreate() {
        super.onCreate();
//        Thread.setDefaultUncaughtExceptionHandler(this);
        //leakCanary初始化
        if (LeakCanary.isInAnalyzerProcess(this)){
            return;
        }
        LeakCanary.install(this);
    }

    /*@Override
    public void uncaughtException(final Thread thread, final Throwable ex) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("weilgu","处没发现的异常啦"+ex.getMessage());
            }
        }){}.start();
    }*/
}
