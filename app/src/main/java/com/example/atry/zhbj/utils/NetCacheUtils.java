package com.example.atry.zhbj.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 李维: TZZ on 2019-09-09 09:09
 * 邮箱: 3182430026@qq.com
 */
public class NetCacheUtils {
    private LocalCacheUtils mLocalCacheUtils;
    private MemoryUtils mMemoryUtils;
    public NetCacheUtils(LocalCacheUtils localCacheUtils,MemoryUtils memoryUtils) {
        this.mLocalCacheUtils = localCacheUtils;
        this.mMemoryUtils = memoryUtils;
    }

    public void getBitmapFromNet(ImageView imageView, String Uri){
        new BitmapTask().execute(imageView,Uri);
    }

    /**
     * 1. Object 为doInBackground方法的形参，这里的形参主要是用于请求的路径等参数
     * 2. Integer 为onProgressUpdate方法里的参数，即为加载更新的进度，
     * 3. Bitmap 为doBackground方法的返回值，同时也是onPostExecute的形参，这个参数为请求
     * 后的结果，在请求完成时传入onPostExecute
     */

    class BitmapTask extends AsyncTask<Object,Integer,Bitmap>{

        private ImageView mImageView;
        private String mUri;

        //1.预加载，运行在主线程
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        //2.正在加载，运行在分线程
        @Override
        protected Bitmap doInBackground(Object... objects) {
            mImageView = (ImageView) objects[0];
            mUri = (String) objects[1];
            // 由于ListView的重用机制，防止图不对文
            mImageView.setTag(mUri);
            Bitmap bitmap = download(mUri);
            return bitmap;
        }
        //3.加载进度更新 运行在主线程，可以直接更新UI
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
        //4.加载结束，运行在主线程
        @Override
        protected void onPostExecute(Bitmap result) {
            if(result != null){
                String uri = (String) mImageView.getTag();
                if(uri.equals(this.mUri)){
                    mImageView.setImageBitmap(result);
                    //本地缓存
                    mLocalCacheUtils.setLocalCaceh(uri,result);
                    //内存缓存
                    mMemoryUtils.setMemory(uri,result);
                }
            }
        }
    }

    private Bitmap download(String uri) {
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) new URL(uri).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();
            int resposecode = connection.getResponseCode();
            if(resposecode == 200){
                InputStream ips = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(ips);
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }
}
