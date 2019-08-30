package com.example.atry.zhbj.domain;

import java.util.ArrayList;
// 编写GSON 解析的类时，遇到{}创建对象
// 遇到[] 时创建集合
public class NewsMenu {
    public int retcode;
    public ArrayList<NewsMenuData> data;
    public ArrayList<Integer> extend;

    @Override
    public String toString() {
        return "NewsMenu{" +
                "retcode=" + retcode +
                ", data=" + data +
                ", extend=" + extend +
                '}';
    }

    public class NewsMenuData{
        public int id;
        public String title;
        public String type;
        public ArrayList<NewsTabData> children;


        @Override
        public String toString() {
            return "NewsMenuData{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", type='" + type + '\'' +
                    ", children=" + children +
                    '}';
        }
    }

    public class NewsTabData{
        public int id;
        public String title;
        public String type;
        public String url;

        @Override
        public String toString() {
            return "NewsTabData{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", type='" + type + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

}

