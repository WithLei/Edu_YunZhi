package com.android.renly.edu_yunzhi.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.renly.edu_yunzhi.Bean.News;
import com.android.renly.edu_yunzhi.Common.AppNetConfig;
import com.android.renly.edu_yunzhi.Common.BaseFragment;
import com.android.renly.edu_yunzhi.Common.MyApplication;
import com.android.renly.edu_yunzhi.R;
import com.android.renly.edu_yunzhi.UI.CustomLinearLayoutManager;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeFragment extends BaseFragment implements View.OnClickListener{
    @Bind(R.id.banner)
    Banner banner;
    @Bind(R.id.iv_home_first)
    ImageView ivHomeFirst;
    @Bind(R.id.tv_home_first)
    TextView tvHomeFirst;
    @Bind(R.id.ll_home_first)
    LinearLayout llHomeFirst;
    @Bind(R.id.iv_home_second)
    ImageView ivHomeSecond;
    @Bind(R.id.tv_home_second)
    TextView tvHomeSecond;
    @Bind(R.id.ll_home_second)
    LinearLayout llHomeSecond;
    @Bind(R.id.iv_home_third)
    ImageView ivHomeThird;
    @Bind(R.id.tv_home_third)
    TextView tvHomeThird;
    @Bind(R.id.ll_home_third)
    LinearLayout llHomeThird;
    @Bind(R.id.iv_home_fourth)
    ImageView ivHomeFourth;
    @Bind(R.id.tv_home_fourth)
    TextView tvHomeFourth;
    @Bind(R.id.ll_home_fourth)
    LinearLayout llHomeFourth;
    @Bind(R.id.iv_home_fifth)
    ImageView ivHomeFifth;
    @Bind(R.id.tv_home_fifth)
    TextView tvHomeFifth;
    @Bind(R.id.ll_home_fifth)
    LinearLayout llHomeFifth;
    @Bind(R.id.iv_home_sixth)
    ImageView ivHomeSixth;
    @Bind(R.id.tv_home_sixth)
    TextView tvHomeSixth;
    @Bind(R.id.ll_home_sixth)
    LinearLayout llHomeSixth;
    @Bind(R.id.iv_home_seventh)
    ImageView ivHomeSeventh;
    @Bind(R.id.tv_home_seventh)
    TextView tvHomeSeventh;
    @Bind(R.id.ll_home_seventh)
    LinearLayout llHomeSeventh;
    @Bind(R.id.iv_home_eighth)
    ImageView ivHomeEighth;
    @Bind(R.id.tv_home_eighth)
    TextView tvHomeEighth;
    @Bind(R.id.ll_home_eighth)
    LinearLayout llHomeEighth;
    @Bind(R.id.lv_home_newsList)
    RecyclerView lvHomeNewsList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected String getUrl() {
        return AppNetConfig.INDEX;
    }

    @Override
    protected RequestParams getParams() {
        return new RequestParams();
    }

    protected static final int WHAT_REQUEST_SUCCESS = 1;
    protected static final int WHAT_REQUEST_ERROR = 2;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_REQUEST_SUCCESS:
                    lvHomeNewsList.setAdapter(adapter);
                    break;
                case WHAT_REQUEST_ERROR:
                    Toast.makeText(MyApplication.context, "加载数据失败", Toast.LENGTH_LONG).show();
                    break;

            }
        }
    };

    @Override
    protected void initData(String content) {
        initNewsdata();
        adapter = new itemInfoAdapter(data);
        CustomLinearLayoutManager layoutmanager = new CustomLinearLayoutManager(MyApplication.context);
        layoutmanager.setScrollEnabled(false);
        //设置RecyclerView 布局
        lvHomeNewsList.setLayoutManager(layoutmanager);
        new Thread() {
            @Override
            public void run() {
                try {
                    //暂时模拟读取json数据
                    handler.sendEmptyMessage(WHAT_REQUEST_SUCCESS);
                } catch (Exception e) {
                    handler.sendEmptyMessage(WHAT_REQUEST_ERROR);
                    Log.e("TAG", "加载数据失败");
                }

            }
        }.start();
        //初始化轮播图
        initBanner();

    }

    private void initBanner() {
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片地址构成的集合
        ArrayList<String> imagesUrl = new ArrayList<>(4);
        imagesUrl.add("http://m.qpic.cn/psb?/V13Hh3Xy2gxYy4/D7gx6zteCgux70QDC*nRwyaHeYgg1Yju9Zd570YdMNs!/b/dPIAAAAAAAAA&bo=AAocAgAAAAARFzQ!&rf=viewer_4");
        imagesUrl.add("http://m.qpic.cn/psb?/V13Hh3Xy2gxYy4/x5bOR*YlDgUeyzILeiva2RZMRQkoDHvj6KA*8GNdlf0!/b/dEUBAAAAAAAA&bo=AAocAgAAAAARFzQ!&rf=viewer_4");
        imagesUrl.add("http://m.qpic.cn/psb?/V13Hh3Xy2gxYy4/DStr.yGNWRFrqYSGCFmQ42*xqrImIydbrZ74erwfpMo!/b/dPIAAAAAAAAA&bo=sAQYAQAAAAARF40!&rf=viewer_4");
        imagesUrl.add("http://m.qpic.cn/psb?/V13Hh3Xy2gxYy4/umhRQOXz6eIBa.p5DOq4bSHgej2r2R7pMop*x8c28yQ!/b/dFYBAAAAAAAA&bo=sAQYAQAAAAARF40!&rf=viewer_4");
//        ArrayList<String> imagesUrl = new ArrayList<>(index.images.size());
//        for (int i = 0; i < index.images.size(); i++) {
//            imagesUrl.add(index.images.get(i).ImaPAURL);
//        }
        banner.setImages(imagesUrl);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时)
        String[] titles = new String[]{"高考锋芒行动", "一人，一名师", "用一场旅行，玩转暑期长假", "跳槽去新加坡，互联网圈怎么样"};
        banner.setBannerTitles(Arrays.asList(titles));
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播事件
        banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    @Override
    public int getLayoutid() {
        return R.layout.fragment_home;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_home_first:
                Toast.makeText(MyApplication.context,"我的课程",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_home_second:
                Toast.makeText(MyApplication.context,"校方公告",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_home_third:
                Toast.makeText(MyApplication.context,"任务中心",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_home_fourth:
                Toast.makeText(MyApplication.context,"各类活动",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_home_fifth:
                Toast.makeText(MyApplication.context,"成绩查询",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_home_sixth:
                Toast.makeText(MyApplication.context,"错题分析",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_home_seventh:
                Toast.makeText(MyApplication.context,"综合实训",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_home_eighth:
                Toast.makeText(MyApplication.context,"能力档案",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public class GlideImageLoader extends ImageLoader {
        //picasso 加载图片简单用法
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Picasso.with(context).load((String) path).into(imageView);
        }
    }

    public List<News> data;
    public itemInfoAdapter adapter;

    //广告【暂时写死
    public void initNewsdata() {
        data = new ArrayList<>();
        for(int i=0;i<10;i++){
        //1.
        News firstAd = new News();
        firstAd.title = "点赞！我校新增1个博士学位授权一级学科";
        firstAd.content = "目前，《国务院学位委员会关于下达2017年审核增列的博士、硕士学位...";
        firstAd.replyCount = 233;
        firstAd.username = "微社区";
        firstAd.img = "http://m.qpic.cn/psb?/V13Hh3Xy2gxYy4/FRp*yIwJptgrSPi272ndSLj3OyHQnVqfiCU.AARr6Rc!/b/dAgBAAAAAAAA&bo=wAY4BEALCAcDCZI!&rf=viewer_4";
        firstAd.time = 1;

        //2.
        News secondAd = new News();
        secondAd.title = "一起来看看色彩与树洞的故事";
        secondAd.content = "由福建师范大学学生社团联合会主办、观鸟协会联合美术学院承办的...";
        secondAd.replyCount = 568;
        secondAd.username = "资讯";
        secondAd.img = "http://m.qpic.cn/psb?/V13Hh3Xy2gxYy4/dpXhe5yTB4cUOd7h16wy*P3EwgYd24tcF7WedTIFGbA!/b/dEMBAAAAAAAA&bo=wAY4BEALCAcDGYI!&rf=viewer_4";secondAd.time = 2;

        //3.
        News thirdAd = new News();
        thirdAd.title = "水情教育进校园，传递节水正能量";
        thirdAd.content = "3月22日至28日期间，肇庆学院在发展门口正门、紫荆校道悬挂中国水周宣传口号...";
        thirdAd.replyCount = 1024;
        thirdAd.username = "浙江理工大学";
        thirdAd.img = "http://m.qpic.cn/psb?/V13Hh3Xy2gxYy4/OLlz35YPjnY23QvJaVbfJhEh0tbQPn28DF49A6XE5jw!/b/dPMAAAAAAAAA&bo=wAY4BEALCAcDCZI!&rf=viewer_4";
        thirdAd.time = 10;

        data.add(firstAd);
        data.add(secondAd);
        data.add(thirdAd);
        }
    }


    public class itemInfoAdapter extends RecyclerView.Adapter<itemInfoAdapter.ViewHolder> {
        private List<News> newsList;

         class ViewHolder extends RecyclerView.ViewHolder{
            ImageView iv_news_img;
            TextView tv_item_title;
            TextView tv_item_content;
            TextView tv_item_commentCount;
            TextView tv_item_name;
            TextView tv_item_time;

            public ViewHolder(View view) {
                super(view);
                iv_news_img = view.findViewById(R.id.iv_news_img);
                tv_item_title = view.findViewById(R.id.tv_item_title);
                tv_item_content = view.findViewById(R.id.tv_item_content);
                tv_item_commentCount = view.findViewById(R.id.tv_item_commentCount);
                tv_item_name = view.findViewById(R.id.tv_item_name);
                tv_item_time = view.findViewById(R.id.tv_item_time);
            }
        }

        public itemInfoAdapter(List<News> newsList) {
            this.newsList = newsList;
        }

        //加载item 的布局  创建ViewHolder实例
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.newsitem,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        //对RecyclerView子项数据进行赋值
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            News news = newsList.get(position);
            //设置数据
            holder.tv_item_name.setText(news.username);
            holder.tv_item_title.setText(news.title);
            holder.tv_item_content.setText(news.content);
            holder.tv_item_commentCount.setText(news.replyCount + "");
            holder.tv_item_time.setText(news.time + "");
            String imagePath = news.img;
            Picasso.with(MyApplication.context).load(imagePath).into(holder.iv_news_img);
        }

        //返回子项个数
        @Override
        public int getItemCount() {
            return newsList.size();
        }
    }

//        private ImageLoader imageLoader;
//
//        public itemInfoAdapter(){
//            imageLoader = new ImageLoader
//        }

//        @Override
//        public int getCount() {
//            return data.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return data.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            if (convertView == null) {
//                convertView = View.inflate(MyApplication.context, R.layout.newsitem, null);
//            }
//            //得到当前行的数据对象
//            News news = data.get(position);
//            //得到当前的子view
//            ImageView iv_news_img = convertView.findViewById(R.id.iv_news_img);
//            TextView tv_item_title = convertView.findViewById(R.id.tv_item_title);
//            TextView tv_item_content = convertView.findViewById(R.id.tv_item_content);
//            TextView tv_item_commentCount = convertView.findViewById(R.id.tv_item_commentCount);
//            TextView tv_item_name = convertView.findViewById(R.id.tv_item_name);
//            TextView tv_item_time = convertView.findViewById(R.id.tv_item_time);
//
//            //设置数据
//            tv_item_name.setText(news.username);
//            tv_item_title.setText(news.title);
//            tv_item_content.setText(news.content);
//            tv_item_commentCount.setText(news.replyCount + "");
//            tv_item_time.setText(news.time + "");
//            String imagePath = news.img;
//            Picasso.with(MyApplication.context).load(imagePath).into(iv_news_img);
//
//            return convertView;
//        }
}
