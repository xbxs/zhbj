package com.example.atry.zhbj.utils;

public class ConstantValues {
    // APP的SP 存储的名称
    public static final String CONFIG= "config";
    // 是否为第一次开启APP
    public static final String IS_FIRSTENTER= "is_firstenter";

    //webservice
    //1.服务器域名
    public static final String SERVER_URL = "http://10.0.2.2:8080/zhbj";
    // 2. 分类信息
    public static final String CATEGORY_URL = SERVER_URL+"/categories.json";
    //3.组图的URI
    public static final String PHOTOS_URL = SERVER_URL+"/photos/photos_1.json";
    //存储用户已读和未读的id
    public static String READ_IDS;
}
