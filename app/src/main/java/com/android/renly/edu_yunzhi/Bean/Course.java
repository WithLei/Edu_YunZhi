package com.android.renly.edu_yunzhi.Bean;

import java.util.List;

public class Course {
    public int id;//课程编号
    public String name;//课程名称
    public String descript;//课程描述
    public String teacherName;//任课老师姓名
    public Teacher teacher;//任课老师
    public List<KnowledgePoint> knowledgePoints;//知识点
//    List<Test> tests;
    public List<Task> tasks;
    public List<Student> students;
    public String remark;
    public String imgUrl;//课程图片地址
}
