package com.android.renly.edu_yunzhi.Bean;

import java.util.List;

public class Course {
    Integer id;//课程编号
    String name;//课程名称
    String descript;//课程描述
    Teacher teacher;//任课老师
    List<KnowledgePoint> knowledgePoints;//知识点
//    List<Test> tests;//�Ծ����
    List<Task> tasks;//�γ�����
    List<Student> students;//�Ͽ�ѧ��
    String remark;//��
}
