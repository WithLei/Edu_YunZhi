package com.android.renly.edu_yunzhi.Bean;

public class Note {
    public String content;
    public String time;
    public boolean isStar;
    public String src;

    public Note() {
    }

    public Note(String content, String time, boolean isStar) {
        this.content = content;
        this.time = time;
        this.isStar = isStar;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isStar() {
        return isStar;
    }

    public void setStar(boolean star) {
        isStar = star;
    }
}
