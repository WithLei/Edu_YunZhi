package com.android.renly.edu_yunzhi_teacher.Common;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.renly.edu_yunzhi_teacher.UI.LoadingPage;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    private LoadingPage loadingPage;
    private Unbinder unbinder;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        loadingPage = new LoadingPage(container.getContext()) {
            @Override
            public int layoutId() {
                return getLayoutid();
            }

            @Override
            protected void onSuccss(ResultState resultState, View view_success) {
                unbinder = ButterKnife.bind(BaseFragment.this,view_success);
                initData(resultState.getContent());
            }

            @Override
            protected RequestParams params() {
                return getParams();
            }

            @Override
            public String url() {
                return getUrl();
            }
        };
//        View view = UIUtils.getView(getLayoutid());
//        ButterKnife.bind(this,view);
//        initTitle();
//        initData();
        initImageLoader(MyApplication.context);
        return loadingPage;
    }

    protected abstract String getUrl();

    protected abstract RequestParams getParams();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        show();
    }

    private void show() {
        loadingPage.show();
    }

    //初始化界面的数据
    protected abstract void initData(String content);

    public abstract int getLayoutid();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //Toolbar
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config =
                new ImageLoaderConfiguration.Builder(context).threadPoolSize(5).build();

        ImageLoader.getInstance().init(config);
    }
}
