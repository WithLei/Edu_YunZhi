package com.android.renly.edu_yunzhi_teacher.UI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by Administrator on 2018/4/11.
 */
//侧滑菜单item
public class SlideLayout extends FrameLayout {

    private View contentView;
    private View menuView;

    //滚动者
    private Scroller scroller;

    private int contentWidth;//Content的宽
    private int menuWidth;//Menu的宽
    private int viewHeight;///视图的高都是相同的
    public SlideLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
    }

    //当布局文件加载完成时回调此方法
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);
        menuView = getChildAt(1);
    }

    //在测量方法中得到各个控件的宽和高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        contentWidth = contentView.getMeasuredWidth();
//        contentWidth = getMeasuredWidth();
        menuWidth = menuView.getMeasuredWidth();
        viewHeight = getMeasuredHeight();
        Log.e("TAG","menuWidth =" + menuWidth);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //指定菜单的位置
        menuView.layout(contentWidth,0,contentWidth + menuWidth,viewHeight);
    }

    private float startX;
    private float startY;
    private float downX;//只赋值一次
    private float downY;//只赋值一次
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //1.按下记录坐标
                downX = startX = event.getX();
                downY = startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //2.记录结束值
                float endX = event.getX();
                float endY = event.getY();
                //3.计算偏移量
                float distanceX = endX - startX;
                int toSrcollX = (int)(getScaleX() -distanceX);
                if (toSrcollX < 0){
                    toSrcollX = 0;
                }else if (toSrcollX > menuWidth){
                    toSrcollX = menuWidth;
                }
                scrollTo(toSrcollX,getScrollY());

                startX = event.getX();
                startY = event.getY();
                //在X轴和Y轴滑动的距离
                float dx = Math.abs(endX -downX);
                float dy = Math.abs(endY -downY);
                if (dx > dy && dx >= 8){
                    //水平方向滑动，相应侧滑,反拦截-时间给SlideLayout
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                break;
            case MotionEvent.ACTION_UP:
                int totalScrollX = getScrollX();//偏移量
                if (totalScrollX < menuWidth/2){
                    //关闭Menu
                    Log.e("TAG","totalScrollX = " + totalScrollX);
                    closeMenu();
                }else{
                    //打开Menu
                    Log.e("TAG","totalScrollX = " + totalScrollX);
                    openMenu();
                }
                break;
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercept = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //1.按下记录坐标
                downX = startX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                //2.记录结束值
                float endX = event.getX();
                startX = event.getX();
                //在X轴和Y轴滑动的距离
                float dx = Math.abs(endX - downX);
                if (dx >= 8) {
                    intercept = true;
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return intercept;
    }

    //打开Menu
    public void openMenu() {
        //--->Menuwidth
        int distanceX = menuWidth - (int)getScaleX();
        Log.e("TAG","openMenu() distanceX = " + distanceX);
        scroller.startScroll(getScrollX(),getScrollY(),distanceX,getScrollY());
        invalidate();//强制刷新
    }

    //关闭Menu
    public void closeMenu() {
        //--->0
        int distanceX = 0 - (int)getScaleX();
        Log.e("TAG","closeMenu() distanceX = " + distanceX);
        scroller.startScroll(getScrollX(),getScrollY(),distanceX,getScrollY());
        invalidate();//强制刷新
        if(onStateChangeListenter != null){
            onStateChangeListenter.onClose(this);
        }
    }

    //监听SlideLayout状态的改变
    public interface OnStateChangeListenter{
        void onClose(SlideLayout layout);
        void onDown(SlideLayout layout);
        void onOpen(SlideLayout layout);
    }
    private  OnStateChangeListenter onStateChangeListenter;


     //设置SlideLayout状态的监听
     //onStateChangeListenter

    public void setOnStateChangeListenter(OnStateChangeListenter onStateChangeListenter) {
        this.onStateChangeListenter = onStateChangeListenter;
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()){
            Log.e("TAG","computeScroll() X :" + scroller.getCurrX() + " Y :" + scroller.getCurrY());
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            invalidate();
        }
    }
}
