package com.example.atry.zhbj.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * 李维: TZZ on 2019-09-09 10:55
 * 邮箱: 3182430026@qq.com
 */
public class LocalCacheUtils {

    private static final String  LOCAL_CACHE_PATH= Environment.getExternalStorageDirectory().getAbsolutePath()+"/zhbj";
    private MemoryUtils mMemoryUtils;
    public LocalCacheUtils(MemoryUtils memoryUtils){
        this.mMemoryUtils = memoryUtils;
    }
    public void setLocalCaceh(String uri, Bitmap bitmap){
        File dir = new File(LOCAL_CACHE_PATH);
        if(!dir.exists() || !dir.isDirectory()){
            dir.mkdirs();
        }
        String name = MD5Encoder.encode(uri);
        File file = new File(dir,name);
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getLocalCache(String uri){
        try {
            File file = new File(LOCAL_CACHE_PATH, MD5Encoder.encode(uri));
            if(file.exists()){
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                //当从手机存储取数据时，将数据存进内存
                mMemoryUtils.setMemory(uri,bitmap);
                return bitmap;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
