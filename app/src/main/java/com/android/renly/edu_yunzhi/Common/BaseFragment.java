package com.android.renly.edu_yunzhi.Common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.renly.edu_yunzhi.UI.LoadingPage;
import com.loopj.android.http.RequestParams;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {
    private LoadingPage loadingPage;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        loadingPage = new LoadingPage(container.getContext()) {
            @Override
            protected RequestParams params() {
                return getParams();
            }

            @Override
            protected String url() {
                return getUrl();
            }

            @Override
            protected void onSuccess(ResultState resultState, View successView) {
                ButterKnife.bind(BaseFragment.this,successView);
                initData(resultState.getContent());
                Log.e("TAG","content:" + resultState.getContent());
            }

            @Override
            public int layoutID() {
                return getLayoutid();
            }
        };
//        View view = UIUtils.getView(getLayoutid());
//        ButterKnife.bind(this,view);
//        initTitle();
//        initData();
        return loadingPage;
    }

    protected abstract String getUrl();

    protected abstract RequestParams getParams();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        UIUtils.getHandler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                show();
//            }
//        },2000);
        show();
    }

    private void show() {
        Log.e("TADG",(loadingPage == null) + " ");
        loadingPage.show();
    }

    //初始化界面的数据
    protected abstract void initData(String content);

    public abstract int getLayoutid();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
