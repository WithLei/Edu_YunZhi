package com.android.renly.edu_yunzhi.Fragment;

import android.os.Bundle;

import com.android.renly.edu_yunzhi.Common.BaseFragment;
import com.android.renly.edu_yunzhi.R;
import com.loopj.android.http.RequestParams;

public class CourseLiveFragment extends BaseFragment {
    private static final String ARG_TITLE = "title";
    public static CourseLiveFragment getInstance(String title) {
        CourseLiveFragment fra = new CourseLiveFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, title);
        fra.setArguments(bundle);
        return fra;
    }

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

    }

    @Override
    public int getLayoutid() {
        return R.layout.fragment_courselive;
    }
}
