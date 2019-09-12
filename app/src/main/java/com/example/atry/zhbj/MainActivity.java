package com.example.atry.zhbj;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.example.atry.zhbj.fragment.ContentMenuFragment;
import com.example.atry.zhbj.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends SlidingFragmentActivity {

    private String FRAGMENT_LEFT_TAG = "fragment_left_tag";
    private String FRAGMENT_COTENT_TAG = "fragment_content_tag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //请求没有标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.left_menu);
        SlidingMenu slidingMenu = getSlidingMenu();

        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setBehindOffset(700);
        initFragment();
    }

    private void initFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction =  manager.beginTransaction();
        transaction.replace(R.id.fl_left_menu,new LeftMenuFragment(),FRAGMENT_LEFT_TAG);
        transaction.replace(R.id.fl_content_menu,new ContentMenuFragment(),FRAGMENT_COTENT_TAG);
        transaction.commit();

    }

    public LeftMenuFragment getLeftMenuuFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        LeftMenuFragment leftMenuFragment = (LeftMenuFragment) fragmentManager.findFragmentByTag(FRAGMENT_LEFT_TAG);
        return  leftMenuFragment;
    }
    public ContentMenuFragment getContentMenuuFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        ContentMenuFragment contentMenuFragment = (ContentMenuFragment) fragmentManager.findFragmentByTag(FRAGMENT_COTENT_TAG);
        return  contentMenuFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
