package com.example.atry.zhbj.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * 李维: TZZ on 2019-09-02 20:36
 * 邮箱: 3182430026@qq.com
 */
public class TopNewsViewPager extends ViewPager {
    int startx,starty;
    public TopNewsViewPager(@NonNull Context context) {
        super(context);
    }

    public TopNewsViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startx = (int)ev.getX();
                starty = (int)ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int endx = (int)ev.getX();
                int endy = (int)ev.getY();
                // dx 是由于item外面的框移动产生的，dx 大于0 ，则表示
                int dx = endx - startx;
                int dy = endy - starty;
                Log.e("TAG","dx:"+dx+"  endx:"+endx+"  startx:"+startx);
                //左右滑动
                if(Math.abs(dy) < Math.abs(dx)){
                    int currentitem = getCurrentItem();
                    //如歌向左滑动
                    if(dx > 0){
                        if(currentitem == 0){
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }else{
                        int count = getAdapter().getCount();
                        if(currentitem == count -1){
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                }else{
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
