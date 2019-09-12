package com.example.atry.zhbj.base;

import android.app.Application;

import com.mob.MobSDK;
import com.umeng.commonsdk.UMConfigure;

import org.xutils.x;

public class ZhbjApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        MobSDK.init(this);
        UMConfigure.init(this,"5d78e6f14ca3575f5c0007e2","default",0,null);
    }
}
