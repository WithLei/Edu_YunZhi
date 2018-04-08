package com.android.renly.edu_yunzhi.Fragment;

import com.android.renly.edu_yunzhi.Common.BaseFragment;
import com.android.renly.edu_yunzhi.R;
import com.loopj.android.http.RequestParams;

public class MynoteFragment extends BaseFragment{
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
        return R.layout.fragment_mynote;
    }
}
