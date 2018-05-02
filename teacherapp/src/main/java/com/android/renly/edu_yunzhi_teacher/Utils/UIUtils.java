package com.android.renly.edu_yunzhi_teacher.Utils;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.android.renly.edu_yunzhi_teacher.Common.MyApplication;
import com.android.renly.edu_yunzhi_teacher.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

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

    //learningFragment专用
    public static DisplayImageOptions.Builder getDisplayImageBuilder() {
        return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .displayer(new FadeInBitmapDisplayer(500));
    }

    public static int getStatusBarHeightPixel(Context context) {
        int result = 0;
        int resourceId =
                context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getActionBarHeightPixel(Context context) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data,
                    context.getResources().getDisplayMetrics());
        } else if (context.getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data,
                    context.getResources().getDisplayMetrics());
        } else {
            return 0;
        }
    }

    public static int getTabHeight(Context context) {
        return context.getResources().getDimensionPixelSize(R.dimen.tab_height);
    }

    public static Point getDisplayDimen(Context context) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        Point size = new Point();
        if (Build.VERSION.SDK_INT >= 13) {
            display.getSize(size);
        } else {
            size.x = display.getWidth();
            size.y = display.getHeight();
        }
        return size;
    }

    public static boolean isLand(Context context) {
        return context.getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 获取屏幕尺寸
     */
    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static Point getScreenSize(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2){
            return new Point(display.getWidth(), display.getHeight());
        }else{
            Point point = new Point();
            display.getSize(point);
            return point;
        }
    }
}
