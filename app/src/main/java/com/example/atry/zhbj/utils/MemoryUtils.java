package com.example.atry.zhbj.utils;

import android.graphics.Bitmap;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * 李维: TZZ on 2019-09-09 13:00
 * 邮箱: 3182430026@qq.com
 * 这里使用的是软引用,作用是防止出现内存溢出的现象，
 */
public class MemoryUtils {

    private HashMap<String, SoftReference<Bitmap>> mHashMap = new HashMap<>();
    public void setMemory(String uri,Bitmap bitmap){
        SoftReference<Bitmap> soft = new SoftReference<>(bitmap);
        mHashMap.put(uri,soft);
    }

    public Bitmap getMemory(String uri){
        SoftReference<Bitmap> soft = mHashMap.get(uri);
        if(soft != null){
            Bitmap bitmap = soft.get();
            return bitmap;
        }
        return null;
    }
}
