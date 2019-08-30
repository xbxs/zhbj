package com.example.atry.zhbj.imple;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.atry.zhbj.base.BasePager;

public class GovPager extends BasePager {
    public GovPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        TextView textView = new TextView(mactivity);
        textView.setText("政务");
        textView.setTextSize(35);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        tvTitle.setText("人口管理");
        fl_content.addView(textView);

        btnMenu.setVisibility(View.VISIBLE);
    }
}
