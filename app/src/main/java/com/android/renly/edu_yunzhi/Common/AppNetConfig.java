package com.android.renly.edu_yunzhi.Common;

/**
 *配置网络请求相关的地址
 */

public class AppNetConfig {
    //校园基论坛
    public static final String BASE_URL_ME = "http://rsbbs.xidian.edu.cn/";

    public static final String HOST = "172.20.10.6";//提供IP地址

    public static final String BASE_URL = "http://" + HOST + ":8080/P2PInvest/";//本地后台地址

    public static final String INDEX = BASE_URL + "index";//访问首页数据

    public static final String LOGIN = BASE_URL +"login";//访问登陆的url

    public static final String PEODUCT = BASE_URL + "product";//访问“所有理财"的url

    public static final String UPDATE = BASE_URL + "update.json";
    //访问服务器当前应用的版本信息

    public static final String REGISTAR = BASE_URL + "UserRegister";//注册

    public static final String FEEDBACK = BASE_URL + "FeedBack";//用户反馈

    //学生登录
//    public static final String STUDENTLOGIN = BASE_URL + "app-studentRegister?username="+"&password=";
}
