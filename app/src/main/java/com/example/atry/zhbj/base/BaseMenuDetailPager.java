package com.example.atry.zhbj.base;

import android.app.Activity;
import android.view.View;

public abstract class BaseMenuDetailPager {
    public Activity mactivity;
    public View mRootView;
    public BaseMenuDetailPager(Activity activity){
        this.mactivity = activity;
        mRootView = initView();
    }

    public abstract View initView();

    public void initData(){

    }
}
