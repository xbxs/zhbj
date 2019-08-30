package com.example.atry.zhbj.imple.menu;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.atry.zhbj.R;
import com.example.atry.zhbj.base.BaseMenuDetailPager;
import com.example.atry.zhbj.domain.NewsMenu;

public class TabDetailPager extends BaseMenuDetailPager {
    private NewsMenu.NewsTabData mnewsTabData;
    private TextView mtextView;

    public TabDetailPager(Activity activity, NewsMenu.NewsTabData newsTabData) {
        super(activity);
        this.mnewsTabData = newsTabData;
    }

    @Override
    public View initView() {
        mtextView = new TextView(mactivity);
        mtextView.setTextSize(35);
        mtextView.setTextColor(Color.RED);
        mtextView.setGravity(Gravity.CENTER);
        return mtextView;
    }

    @Override
    public void initData() {
        mtextView.setText(mnewsTabData.title);
    }
}
