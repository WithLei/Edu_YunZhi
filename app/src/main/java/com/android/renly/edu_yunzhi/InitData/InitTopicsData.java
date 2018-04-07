package com.android.renly.edu_yunzhi.InitData;

import com.android.renly.edu_yunzhi.Bean.Topics;

import java.util.ArrayList;
import java.util.List;

public class InitTopicsData {
    public List<Topics> data;
    public List<Topics> initTopicData(){
        data = new ArrayList<>();
        for(int i=0;i<5;i++){
            //1.
            Topics firstTopic = new Topics();
            firstTopic.title = "图书馆4.23世界图书日系列活动第一弹";
            firstTopic.name = "西电图书馆";
            firstTopic.comment = 151;
            firstTopic.hasImg = false;

            //2.
            Topics secondTopic = new Topics();
            secondTopic.title = "马上生日了，还在图书馆自习";
            secondTopic.name = "doubihui";
            secondTopic.comment = 47;
            secondTopic.hasImg = false;

            //3.
            Topics thirdTopic = new Topics();
            thirdTopic.title = "大佬们，学校附近有没有专业的给眼睛验光的医院啊";
            thirdTopic.name = "合伙人";
            thirdTopic.comment = 100;
            thirdTopic.hasImg = false;

            //4.
            Topics forthTopic = new Topics();
            forthTopic.title = "求C++大佬";
            forthTopic.name = "vimm";
            forthTopic.comment = 71;
            forthTopic.hasImg = false;

            //5
            Topics fifthTopic = new Topics();
            fifthTopic.title = "Paperpass18% 笔杆8%，知网大概什么水平？";
            fifthTopic.name = "依旧彷徨";
            fifthTopic.comment = 54;
            fifthTopic.hasImg = true;

            //6
            Topics sixthTopic = new Topics();
            sixthTopic.title = "和省内流量说拜拜！移动新版流量包上线：5元30M成最大槽点";
            sixthTopic.name = "激萌路小叔";
            sixthTopic.comment = 25;
            sixthTopic.hasImg = true;

            //7
            Topics seventhTopic = new Topics();
            seventhTopic.title = "欲望总是不可控制";
            seventhTopic.name = "zhjcyh";
            seventhTopic.comment = 268;
            seventhTopic.hasImg = false;

            //8
            Topics eigthTopic = new Topics();
            eigthTopic.title = "和喜欢的人提分手是一种什么体验？";
            eigthTopic.name = "eigthTopic";
            eigthTopic.comment = 64;
            eigthTopic.hasImg = false;

            data.add(firstTopic);
            data.add(secondTopic);
            data.add(thirdTopic);
            data.add(forthTopic);
            data.add(fifthTopic);
            data.add(sixthTopic);
            data.add(seventhTopic);
            data.add(eigthTopic);
        }
        return data;
    }
}
