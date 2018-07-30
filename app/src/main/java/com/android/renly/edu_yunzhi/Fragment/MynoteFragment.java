package com.android.renly.edu_yunzhi.Fragment;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.renly.edu_yunzhi.Common.BaseFragment;
import com.android.renly.edu_yunzhi.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MynoteFragment extends BaseFragment {
    @BindView(R.id.lv_animation)
    ImageView lvAnimation;
    Unbinder unbinder;

    @Override
    protected void initData(Context content) {
        ((AnimationDrawable) lvAnimation.getDrawable()).start();
    }

    @Override
    public int getLayoutid() {
        return R.layout.fragment_mynote;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
