package com.example.atry.zhbj.domain;

import java.util.ArrayList;

/**
 * 李维: TZZ on 2019-09-02 10:47
 * 邮箱: 3182430026@qq.com
 */
public class NewTabBean {
    public NewsTab data;

    public class NewsTab{
        public String more;
        public ArrayList<NewsData> news;
        public ArrayList<TopNewsData> topnews;
    }

    public class NewsData{
        public int id;
        public String listimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;
    }

    public class TopNewsData{
        public int id;
        public String topimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;
    }
}
