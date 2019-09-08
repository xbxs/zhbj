package com.example.atry.zhbj.domain;

import java.util.ArrayList;

/**
 * 李维: TZZ on 2019-09-07 16:39
 * 邮箱: 3182430026@qq.com
 */
public class PhotoBean {
    public PhotosData data;
    public class PhotosData{
        public ArrayList<PhotoInfo> news;
    }

    public class PhotoInfo{
        public int id;
        public String listimage;
        public String title;
    }
}
