package com.example.atry.zhbj.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.atry.zhbj.MainActivity;
import com.example.atry.zhbj.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

// 用作主页各个页面的基类
public class BasePager {
    public Activity mactivity;
    public TextView tvTitle;
    public FrameLayout fl_content;
    public ImageButton btnMenu,ib_photoshown;

    public View mRootView;

    public BasePager(Activity activity){
        this.mactivity = activity;
        mRootView = initView();
    }
    public View initView(){
        View view = View.inflate(mactivity, R.layout.base_pager,null);
        tvTitle = view.findViewById(R.id.tv_title);
        btnMenu = view.findViewById(R.id.ib_title);
        ib_photoshown = view.findViewById(R.id.ib_photoshown);
        fl_content = view.findViewById(R.id.fl_content);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
        return view;
    }

    public void initData(){

    }
    /**
     *  打开或者关闭侧边栏 如果打开则会关闭，相反则打开
     */
    private void toggle() {
        MainActivity mainActivity = (MainActivity) mactivity;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        slidingMenu.toggle();
    }


}
