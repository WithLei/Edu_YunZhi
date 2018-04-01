package com.android.renly.edu_yunzhi.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.renly.edu_yunzhi.Bean.News;
import com.android.renly.edu_yunzhi.Common.AppNetConfig;
import com.android.renly.edu_yunzhi.Common.BaseFragment;
import com.android.renly.edu_yunzhi.Common.MyApplication;
import com.android.renly.edu_yunzhi.R;
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

public class HomeFragment extends BaseFragment {
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
//    @Bind(R.id.lv_home_newsList)
//    android.widget.WrapContentListView lvHomeNewsList;
//    @Bind(R.id.lv_home_newsList)
//    ListView lvHomeNewsList;

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
        adapter = new itemInfoAdapter();
        new Thread() {
            @Override
            public void run() {
                try {
                    //暂时模拟读取json数据
                    initNewsdata();
                    handler.sendEmptyMessage(WHAT_REQUEST_SUCCESS);
                } catch (Exception e) {
                    handler.sendEmptyMessage(WHAT_REQUEST_ERROR);
                    Log.e("TAG", "加载数据失败");
                }

            }
        }.start();
        //初始化轮播图
        initBanner();
        ScrollView sv = findViewById(R.id.sv_home);
        sv.smoothScrollTo(0, 0);
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
        //1.
        News firstAd = new News();
        firstAd.title = "点赞！我校新增1个博士学位授权一级学科";
        firstAd.content = "目前，《国务院学位委员会关于下达2017年审核增列的博士、硕士学位";
        firstAd.replyCount = 233;
        firstAd.username = "微社区";
        firstAd.img = "http://m.qpic.cn/psb?/V13Hh3Xy2gxYy4/FRp*yIwJptgrSPi272ndSLj3OyHQnVqfiCU.AARr6Rc!/b/dAgBAAAAAAAA&bo=wAY4BEALCAcDCZI!&rf=viewer_4";
        firstAd.time = 1;

        //2.
        News secondAd = new News();
        secondAd.title = "点赞！我校新增1个博士学位授权一级学科";
        secondAd.content = "目前，《国务院学位委员会关于下达2017年审核增列的博士、硕士学位";
        secondAd.replyCount = 233;
        secondAd.username = "微社区";
        secondAd.img = "http://m.qpic.cn/psb?/V13Hh3Xy2gxYy4/dpXhe5yTB4cUOd7h16wy*P3EwgYd24tcF7WedTIFGbA!/b/dEMBAAAAAAAA&bo=wAY4BEALCAcDGYI!&rf=viewer_4";
        secondAd.time = 2;


        data.add(firstAd);
        data.add(secondAd);
    }


    class itemInfoAdapter extends BaseAdapter {

//        private ImageLoader imageLoader;
//
//        public itemInfoAdapter(){
//            imageLoader = new ImageLoader
//        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(MyApplication.context, R.layout.newsitem, null);
            }
            //得到当前行的数据对象
            News news = data.get(position);
            //得到当前的子view
            ImageView iv_news_img = convertView.findViewById(R.id.iv_news_img);
            TextView tv_item_title = convertView.findViewById(R.id.tv_item_title);
            TextView tv_item_content = convertView.findViewById(R.id.tv_item_content);
            TextView tv_item_commentCount = convertView.findViewById(R.id.tv_item_commentCount);
            TextView tv_item_name = convertView.findViewById(R.id.tv_item_name);
            TextView tv_item_time = convertView.findViewById(R.id.tv_item_time);

            //设置数据
            tv_item_name.setText(news.username);
            tv_item_title.setText(news.title);
            tv_item_content.setText(news.content);
            tv_item_commentCount.setText(news.replyCount + "");
            tv_item_time.setText(news.time + "");
            String imagePath = news.img;
            Picasso.with(MyApplication.context).load(imagePath).into(iv_news_img);

            return convertView;
        }
    }
}
