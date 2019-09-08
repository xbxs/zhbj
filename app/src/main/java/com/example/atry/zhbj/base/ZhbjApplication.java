package com.example.atry.zhbj.base;

import android.app.Application;

import com.mob.MobSDK;

import org.xutils.x;

public class ZhbjApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        MobSDK.init(this);
    }
}
