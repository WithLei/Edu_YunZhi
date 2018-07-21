package com.android.renly.edu_yunzhi.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.android.renly.edu_yunzhi.Adapter.MyPagerAdapter;
import com.android.renly.edu_yunzhi.Fragment.CourseCommentFragment;
import com.android.renly.edu_yunzhi.Fragment.CourseIntroductionFragment;
import com.android.renly.edu_yunzhi.Fragment.CourseListFragment;
import com.android.renly.edu_yunzhi.Fragment.CourseLiveFragment;
import com.android.renly.edu_yunzhi.R;

import java.util.ArrayList;

import cn.hugeterry.coordinatortablayout.CoordinatorTabLayout;

public class CourseDetailsActivity extends AppCompatActivity {
    private CoordinatorTabLayout mCoordinatorTabLayout;
    private int[] mImageArray, mColorArray;
    private ArrayList<Fragment> mFragments;
    private final String[] mTitles = {"介绍", "知识清单", "评价", "课程直播"};
    private ViewPager mViewPager;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_coursedetails);
        initFragments();
        initViewPager();
        mImageArray = new int[]{
                R.mipmap.bg_android,
                R.mipmap.bg_ios,
                R.mipmap.bg_js,
                R.mipmap.bg_other};
        mColorArray = new int[]{
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light};

        mCoordinatorTabLayout = findViewById(R.id.coordinatortablayout);
        mCoordinatorTabLayout.setTranslucentStatusBar(this)
                .setTitle(intent.getStringExtra("title"))
                .setBackEnable(true)
                .setImageArray(mImageArray, mColorArray)
                .setupWithViewPager(mViewPager);
    }

    private void initFragments() {
        mFragments = new ArrayList<>();
        mFragments.add(CourseIntroductionFragment.getInstance(mTitles[0]));
        mFragments.add(CourseListFragment.getInstance(mTitles[1]));
        mFragments.add(CourseCommentFragment.getInstance(mTitles[2]));
        mFragments.add(CourseLiveFragment.getInstance(mTitles[3]));
    }

    private void initViewPager() {
        mViewPager = findViewById(R.id.coursedetails_vp);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragments, mTitles));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        switch (item.getItemId()) {
            case R.id.action_about:
//                IntentUtils.openUrl(this, "https://github.com/hugeterry/CoordinatorTabLayout");
                break;
            case R.id.action_about_me:
//                IntentUtils.openUrl(this, "http://hugeterry.cn/about");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
