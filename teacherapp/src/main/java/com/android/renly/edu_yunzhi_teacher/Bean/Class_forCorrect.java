package com.android.renly.edu_yunzhi_teacher.Bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/14.
 */

public class Class_forCorrect implements Serializable {
    public String classId;//课程编号
    public String className;//课程名称
    public String teacherName;//授课教师姓名
    public String latestWork;//最近期作业名称
    public String latestWorkTime;//最近期作业发布时间
    public String classLogo;//课程图标

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getLatestWork() {
        return latestWork;
    }

    public void setLatestWork(String latestWork) {
        this.latestWork = latestWork;
    }

    public String getLatestWorkTime() {
        return latestWorkTime;
    }

    public void setLatestWorkTime(String latestWorkTime) {
        this.latestWorkTime = latestWorkTime;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassLogo() {
        return classLogo;
    }

    public void setClassLogo(String classLogo) {
        this.classLogo = classLogo;
    }
}
