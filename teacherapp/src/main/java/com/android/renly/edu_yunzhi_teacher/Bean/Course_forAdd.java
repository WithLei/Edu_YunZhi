package com.android.renly.edu_yunzhi_teacher.Bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/13.
 */

public class Course_forAdd implements Parcelable {
    public int id;//课程编号
    public String name;//课程名称
    public String descript;//课程描述
    public String teacherName;//任课老师姓名
    public Bitmap classLogo;//课程图标

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Bitmap getClassLogo() {
        return classLogo;
    }

    public void setClassLogo(Bitmap classLogo) {
        this.classLogo = classLogo;
    }

    public static final Parcelable.Creator<Course_forAdd> CREATOR = new Creator<Course_forAdd>() {
        public Course_forAdd createFromParcel(Parcel source) {
            Course_forAdd pb = new Course_forAdd();
            pb.id = source.readInt();
            pb.name = source.readString();
            pb.teacherName = source.readString();
            pb.descript = source.readString();
            pb.classLogo = Bitmap.CREATOR.createFromParcel(source);
            return pb;
        }

        public Course_forAdd[] newArray(int size) {
            return new Course_forAdd[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(teacherName);
        dest.writeString(descript);
        classLogo.writeToParcel(dest, 0);
    }
}
