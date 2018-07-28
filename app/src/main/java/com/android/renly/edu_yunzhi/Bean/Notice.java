package com.android.renly.edu_yunzhi.Bean;

public class Notice {
    public String title;//标题
    public String time;//发布时间
    public String teacherName;//发布人
    public String content;//消息内容
    public String status;//显示状态

    public Notice() {
        super();
    }

    public Notice(String title, String time, String teacherName, String content, String status) {
        this.title = title;
        this.time = time;
        this.teacherName = teacherName;
        this.content = content;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
