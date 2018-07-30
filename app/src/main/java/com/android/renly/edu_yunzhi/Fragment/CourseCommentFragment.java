package com.android.renly.edu_yunzhi.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.renly.edu_yunzhi.Common.BaseFragment;
import com.android.renly.edu_yunzhi.Common.MyApplication;
import com.android.renly.edu_yunzhi.R;
import com.loopj.android.http.RequestParams;
import com.xiaweizi.library.EvaluationCardView;
import com.xiaweizi.library.EvaluationRatingBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

public class CourseCommentFragment extends BaseFragment {
    private static final String ARG_TITLE = "title";
    @BindView(R.id.evaluate)
    EvaluationRatingBar evaluate;

    public static CourseCommentFragment getInstance(String title) {
        CourseCommentFragment fra = new CourseCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, title);
        fra.setArguments(bundle);
        return fra;
    }

    @Override
    protected void initData(Context content) {

    }

    EvaluationCardView cardView;

    public void click() {
        cardView = new EvaluationCardView(this.getActivity());
        List<String> reasonsData = new ArrayList<>();
        reasonsData.add("上课人数过多");
        reasonsData.add("老师上课不认真");
        reasonsData.add("课程难度较大");
        reasonsData.add("作业量过多");
        cardView.setReasonsData(reasonsData);
        cardView.show();
        cardView.setOnEvaluationCallback(new EvaluationCardView.OnEvaluationCallback() {
            @Override
            public void onEvaluationCommitClick(int starCount, Set<String> reasons) {
                StringBuilder sb = new StringBuilder();
                for (String reason : reasons) {
                    sb.append("\n").append(reason);
                }
                handler.sendEmptyMessage(CARDVIEWDISMISS);
                Toasty.success(MyApplication.context, "评价成功\n" + "星星数量：" + starCount + "\n差评理由：" + sb.toString(), Toast.LENGTH_LONG, true).show();
            }
        });
    }

    @Override
    public int getLayoutid() {
        return R.layout.fragment_coursecomment;
    }


    private Unbinder unbinder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public static final int CARDVIEWDISMISS = 3;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CARDVIEWDISMISS:
                    cardView.dismiss();
                    break;
            }
        }
    };

    @OnClick(R.id.evaluate)
    public void onViewClicked() {
        click();
    }
}
