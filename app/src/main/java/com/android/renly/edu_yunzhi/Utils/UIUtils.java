package com.android.renly.edu_yunzhi.Utils;


import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.android.renly.edu_yunzhi.Common.MyApplication;

/**
 * 专门提供为处理一些UI相关的问题而创建的工具类
 */

public class UIUtils {
    public static Context getContext() {
        Log.e("TAG","package"+ MyApplication.context.getPackageName());
        return MyApplication.context;
    }

    public static Handler getHandler() {
        return MyApplication.handler;
    }

    public static int getColor(int colorID) {
        return getContext().getResources().getColor(colorID);
    }

    public static String[] getStringArr(int arrID) {
        return getContext().getResources().getStringArray(arrID);
    }

    public static View getView(int viewID){
        Log.e("TAG","1.viewID:"+viewID);
        View view = View.inflate(getContext(),viewID,null);
        Log.e("TAG","2.viewID:"+viewID);
        return view;
    }

    //与屏幕分辨率相关的

    public static int dp2px(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (density * dp + 0.5);
    }

    public static int px2dp(int px) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5);
    }

    public static void runOnUiThread(Runnable runnable){
        if(isMainThread()){
            runnable.run();
        }else{
            getHandler().post(runnable);
        }
    }

    private static boolean isMainThread() {
        int myTid = android.os.Process.myTid();
        if(myTid == MyApplication.mainThreadId){
            return true;
        }
        return false;
    }
}
