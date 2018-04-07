package com.android.renly.edu_yunzhi.Bean;

import java.util.List;

public class Student extends User{
    List<Course> courses;//课程
    List<Task> tasks;//任务
    String school;//所属学校
//    List<Praxis> errors;//题目

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
}
