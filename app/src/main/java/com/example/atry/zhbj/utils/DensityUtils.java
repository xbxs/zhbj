package com.example.atry.zhbj.utils;

import android.content.Context;

/**
 * 李维: TZZ on 2019-09-09 21:45
 * 邮箱: 3182430026@qq.com
 */
public class DensityUtils {
    public static int dip3px(float dip, Context context){
        //得到设备密度
        float density = context.getResources().getDisplayMetrics().density;
        int px = (int) (dip * density + 0.5);
        return  px;
    }

    public static float px2dip(int px,Context context){
        //得到设备密度
        float density = context.getResources().getDisplayMetrics().density;
        float dp = px / density;
        return dp;
    }
}
