package com.android.renly.edu_yunzhi.Common;

/**
 *配置网络请求相关的地址
 */

public class AppNetConfig {
    public static final String HOST = "172.20.10.6";//提供IP地址

    public static final String BASE_URL = "http://localhost:8080/sshSimple/";

    public static final String STUDENT_LOGIN = BASE_URL +"app-studentLogin";
    /**
     * String username
     * String password
     */

    public static final String TEACHER_LOGIN = BASE_URL + "app-teacherLogin";
    /**
     * String username
     * String password
     */

    public static final String STUDENT_REGISTER = BASE_URL + "app-studentRegister";
    /**
     * String username
     * String password
     *
     * return true/false
     */

    public static final String GET_ARTICLE = BASE_URL + "app-getAllArticle";
    /**
     * 获取所有资讯信息
     * 无参
     */

    public static final String GET_COURSE = BASE_URL + "app-getAllCourse";
    /**
     * 获取所有课程信息
     * 无参
     */

    public static final String GET_PUSH_URL = BASE_URL + "app-getPushUrl";
    /**
     * 获取直播地址
     * String room 房间名
     */

    public static final String GET_PLAY_URL = BASE_URL + "app-getPlayUrl";
    /**
     * 获取推流地址
     * String room 房间名
     * Long long 到期时间 null则默认为24小时有效期
     */

    public static final String GET_MYCOURSE = BASE_URL + "app-getMyCourse";
    /**
     * 获取我的课程
     * String username
     */

    public static String LiveUrl = "http://27564.liveplay.myqcloud.com/live/27564_desheng.flv";

    public static String PosterUrl = "rtmp://27564.livepush.myqcloud.com/live/27564_desheng?bizid=27564&txSecret=46934f86ec955ecab8305d7628e0d60e&txTime=5B60877F";

    //学生登录
//    public static final String STUDENTLOGIN = BASE_URL + "app-studentRegister?username="+"&password=";
}
