package com.example.atry.zhbj.imple;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.atry.zhbj.base.BasePager;

public class SettingPager extends BasePager {
    public SettingPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        TextView textView = new TextView(mactivity);
        textView.setText("设置");
        textView.setTextSize(35);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        tvTitle.setText("设置");
        fl_content.addView(textView);

        btnMenu.setVisibility(View.INVISIBLE);

        getDataFromServer();
    }

    private void getDataFromServer() {

    }
}
