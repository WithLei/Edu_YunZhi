package com.android.renly.edu_yunzhi.Common;

/**
 *配置网络请求相关的地址
 */

public class AppNetConfig {
    public static final String HOST = "192.168.155.1";//提供IP地址

    public static final String BASE_URL = "http://" + HOST + ":8080/sshSimple/";

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
     * 需要更改！！！
     */

    public static final String TEACHER_REGISTER = BASE_URL + "app-teacherRegister";
    /**
     * String username
     * String password
     * 需要更改！！！
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
     * long id
     */

    public static final String GET_MYMESSAGE = BASE_URL + "app-getMyMessage";
    /**
     * 校方公告
     * long id
     */

    public static final String GET_MYTRAIN = BASE_URL + "app-getMyTrain";
    /**
     * 综合实训
     * long id
     */

    public static final String GET_MYGRADE = BASE_URL + "app-getMyGrade";
    /**
     * 成绩查询
     * long id
     */

    public static final String SET_AVATAR = BASE_URL + "app-updateAvatar";
    /**
     * 更改头像
     * 
     */

    public static String LiveUrl = "http://27564.liveplay.myqcloud.com/live/27564_desheng.flv";

    public static String PosterUrl = "rtmp://27564.livepush.myqcloud.com/live/27564_desheng?bizid=27564&txSecret=46934f86ec955ecab8305d7628e0d60e&txTime=5B60877F";

    //学生登录
//    public static final String STUDENTLOGIN = BASE_URL + "app-studentRegister?username="+"&password=";
}
