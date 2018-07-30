package com.android.renly.edu_yunzhi.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.android.renly.edu_yunzhi.Activity.PlayActivity;
import com.android.renly.edu_yunzhi.Common.AppNetConfig;
import com.android.renly.edu_yunzhi.Common.BaseFragment;
import com.android.renly.edu_yunzhi.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;

public class CourseLiveFragment extends BaseFragment {
    private static final String ARG_TITLE = "title";
    @BindView(R.id.open_btn)
    Button openBtn;

    public static CourseLiveFragment getInstance(String title) {
        CourseLiveFragment fra = new CourseLiveFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, title);
        fra.setArguments(bundle);
        return fra;
    }

    @Override
    protected void initData(Context content) {

    }

    @Override
    public int getLayoutid() {
        return R.layout.fragment_courselive;
    }

    private Unbinder unbinder;

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

    @OnClick(R.id.open_btn)
    public void onViewClicked() {
        /**
         * 跳转到视频播放
         *
         * 首先获取直播地址
         * @param activity
         * @param view
         */
        RequestParams params = new RequestParams();
        params.put("room", "rua");

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(AppNetConfig.GET_PLAY_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                JSONArray jsonArray = JSON.parseArray(response);
                String playUrl = jsonArray.get(2).toString();
                Log.e("print",playUrl);

                Bundle playBundle = new Bundle();
                String PlayRoomName = "噜啦噜啦直播间";
                playBundle.putString("RoomName",PlayRoomName);
                playBundle.putString("PlayUrl",playUrl);
                Intent playIntent = new Intent(getContext(),PlayActivity.class);
                playIntent.putExtras(playBundle);

                startActivity(playIntent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), "未获取到直播信息", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
