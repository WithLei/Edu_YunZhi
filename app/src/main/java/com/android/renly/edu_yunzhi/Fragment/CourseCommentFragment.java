package com.android.renly.edu_yunzhi.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.renly.edu_yunzhi.Common.BaseFragment;
import com.android.renly.edu_yunzhi.R;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CourseCommentFragment extends BaseFragment {
    private static final String ARG_TITLE = "title";
    public static CourseCommentFragment getInstance(String title) {
        CourseCommentFragment fra = new CourseCommentFragment();
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

    public void click() {
        EvaluationCardView cardView = new EvaluationCardView(this);
        List<String> reasonsData = new ArrayList<>();
        reasonsData.add("回复太慢");
        reasonsData.add("对业务不了解");
        reasonsData.add("服务态度差");
        reasonsData.add("问题没有得到解决");
        cardView.setReasonsData(reasonsData);
        cardView.show();
        cardView.setOnEvaluationCallback(new EvaluationCardView.OnEvaluationCallback() {
            @Override
            public void onEvaluationCommitClick(int starCount, Set<String> reasons) {
                StringBuilder sb = new StringBuilder();
                for (String reason : reasons) {
                    sb.append("\n").append(reason);
                }
                Toasty.success(EvaluationCardViewActivity.this, "评价成功\n" + "星星数量：" + starCount + "\n差评理由：" + sb.toString(), Toast.LENGTH_LONG, true).show();
            }
        });
    }

    public void evaluate(View view) {
        click();
    }


    @Override
    public int getLayoutid() {
        return R.layout.fragment_coursecomment;
    }
}
