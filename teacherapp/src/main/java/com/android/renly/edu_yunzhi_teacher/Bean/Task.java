package com.android.renly.edu_yunzhi_teacher.Bean;

import java.io.Serializable;
import java.util.List;
//综合实训

public class Task  implements Serializable{
    int id;//����
    String remark;//��ע
    String decript;//����
//    List<Test> Tests;//������
    public String title;
    public String startTime;//开始时间
    public String teacherName;//发布老师
    public int joinNum;//参与活动人数
    public String type;//活动类型
    public String process;//活动进度
    /**
     * 类型
     * 答疑讨论 - 课外活动 - 实验设计
     * 进度
     * 进行中 - 已结束
     */
    String endTime;//����ʱ��
    Teacher teacher;//������ʦ
    List<Student> students;//ִ��ѧ��

}
