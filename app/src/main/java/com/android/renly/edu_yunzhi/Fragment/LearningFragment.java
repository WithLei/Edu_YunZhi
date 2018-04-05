package com.android.renly.edu_yunzhi.Fragment;

import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.android.renly.edu_yunzhi.Common.BaseActivity;
import com.android.renly.edu_yunzhi.Common.BaseFragment;
import com.android.renly.edu_yunzhi.Common.MyApplication;
import com.android.renly.edu_yunzhi.R;
import com.android.renly.edu_yunzhi.Utils.AppBarStateChangeListener;
import com.android.renly.edu_yunzhi.Utils.UIUtils;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.loopj.android.http.RequestParams;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LearningFragment extends BaseFragment {
    ViewPagerAdapter mAdapter;

    @Bind(R.id.imageView_header)
    KenBurnsView mHeaderImageView;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTextView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tabLayout)
    TabLayout mTabLayout;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @Bind(R.id.app_bar)
    AppBarLayout mAppBarLayout;
    @Bind(R.id.viewPager)
    ViewPager mViewPager;

    @Override
    protected String getUrl() {
        return null;
    }

    @Override
    protected RequestParams getParams() {
        return null;
    }

    @Override
    protected void initData(String content) {
        setUpViews();
    }

    @Override
    public int getLayoutid() {
        return R.layout.fragment_learning;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mHeaderImageView != null) {
            mHeaderImageView.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mHeaderImageView != null) {
            mHeaderImageView.pause();
        }
    }

    private void setUpViews() {
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setHasOptionsMenu(true);
        mCollapsingToolbar.setTitleEnabled(false);
        mCollapsingToolbar.getLayoutParams().height =
                (UIUtils.isLand(MyApplication.context) ? UIUtils.getDisplayDimen(MyApplication.context).y :
                        UIUtils.getDisplayDimen(MyApplication.context).x) * 9 / 16 +
                        UIUtils.getStatusBarHeightPixel(MyApplication.context);
        mCollapsingToolbar.requestLayout();

        // TODO : Hack for CollapsingToolbarLayout
        mToolbarTextView.setText("Demo");
        actionBarResponsive();
        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (mToolbarTextView != null) {
                    if (state == State.COLLAPSED) {
                        if (Build.VERSION.SDK_INT >= 11) {
                            mToolbarTextView.setAlpha(1);
                        } else {
                            setAlphaForView(mToolbarTextView, 1);
                        }
                        mCollapsingToolbar.setContentScrimColor(
                                ContextCompat.getColor(MyApplication.context, R.color.colorPrimary));
                    } else if (state == State.EXPANDED) {
                        if (Build.VERSION.SDK_INT >= 11) {
                            mToolbarTextView.setAlpha(0);
                        } else {
                            setAlphaForView(mToolbarTextView, 0);
                        }
                        mCollapsingToolbar.setContentScrimColor(ContextCompat
                                .getColor(MyApplication.context, android.R.color.transparent));
                    }
                }
            }

            @Override
            public void onOffsetChanged(State state, float offset) {
                if (mToolbarTextView != null) {
                    if (state == State.IDLE) {
                        if (Build.VERSION.SDK_INT >= 11) {
                            mToolbarTextView.setAlpha(offset);
                            mCollapsingToolbar.setContentScrimColor(
                                    (int) new android.animation.ArgbEvaluator().evaluate(offset,
                                            ContextCompat.getColor(MyApplication.context,
                                                    android.R.color.transparent), ContextCompat
                                                    .getColor(MyApplication.context,
                                                            R.color.colorPrimary)));
                        } else {
                            setAlphaForView(mToolbarTextView, offset);
                            mCollapsingToolbar.setContentScrimColor((int) new ArgbEvaluator()
                                    .evaluate(offset, ContextCompat.getColor(MyApplication.context,
                                            android.R.color.transparent), ContextCompat
                                            .getColor(MyApplication.context, R.color.colorPrimary)));
                        }
                    }
                }
            }
        });

        mAdapter = new ViewPagerAdapter((getActivity()).getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(mAdapter.getCount());
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                switch (position) {
                    case 0:
                        ImageLoader.getInstance().displayImage(
                                "https://fs01.androidpit.info/a/63/0e/android-l-wallpapers-630ea6-h900.jpg",
                                mHeaderImageView, UIUtils.getDisplayImageBuilder().build());
                        break;
                    case 1:
                        ImageLoader.getInstance().displayImage(
                                "http://cdn1.tnwcdn.com/wp-content/blogs.dir/1/files/2014/06/wallpaper_51.jpg",
                                mHeaderImageView, UIUtils.getDisplayImageBuilder().build());
                        break;
                    case 2:
                        ImageLoader.getInstance().displayImage(
                                "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg",
                                mHeaderImageView, UIUtils.getDisplayImageBuilder().build());
                        break;
                    case 3:
                        ImageLoader.getInstance().displayImage(
                                "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg",
                                mHeaderImageView, UIUtils.getDisplayImageBuilder().build());
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setAlphaForView(View v, float alpha) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(alpha, alpha);
        alphaAnimation.setDuration(0);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    private void actionBarResponsive() {
        int actionBarHeight = UIUtils.getActionBarHeightPixel(MyApplication.context);
        int tabHeight = UIUtils.getTabHeight(MyApplication.context);
        if (actionBarHeight > 0) {
            mToolbar.getLayoutParams().height = actionBarHeight + tabHeight;
            mToolbar.requestLayout();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return new DemoFragment();
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Demo " + position;
        }
    }
}
