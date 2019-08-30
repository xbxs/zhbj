package com.example.atry.zhbj.imple;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.atry.zhbj.base.BasePager;

public class HomePager extends BasePager {
    public HomePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        TextView textView = new TextView(mactivity);
        textView.setText("首页");
        textView.setTextSize(35);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);

        fl_content.addView(textView);

        btnMenu.setVisibility(View.INVISIBLE);
    }
}
