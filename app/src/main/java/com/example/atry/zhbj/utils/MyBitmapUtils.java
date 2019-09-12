package com.example.atry.zhbj.utils;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

/**
 * 李维: TZZ on 2019-09-09 10:05
 * 邮箱: 3182430026@qq.com
 */
public class MyBitmapUtils {

    private final NetCacheUtils mNetCacheUtils;
    private LocalCacheUtils mLocalCacheUtils;
    private MemoryUtils mMemoryUtils;
    public MyBitmapUtils(){
        mMemoryUtils = new MemoryUtils();
        mLocalCacheUtils = new LocalCacheUtils(mMemoryUtils);
        mNetCacheUtils = new NetCacheUtils(mLocalCacheUtils,mMemoryUtils);
    }
    public void display(ImageView imageView,String uri){
        Bitmap bitmap = mMemoryUtils.getMemory(uri);
        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
            Log.i("TAG","来自内存");
            return;
        }
        bitmap =mLocalCacheUtils.getLocalCache(uri);
        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
            Log.i("TAG","来自存储");
            return;
        }
        mNetCacheUtils.getBitmapFromNet(imageView,uri);
    }
}
