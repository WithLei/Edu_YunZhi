package com.android.renly.edu_yunzhi.Fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.edu_yunzhi.Bean.MessageEvent;
import com.android.renly.edu_yunzhi.Common.AppNetConfig;
import com.android.renly.edu_yunzhi.Common.MyApplication;
import com.android.renly.edu_yunzhi.MainActivity;
import com.android.renly.edu_yunzhi.R;
import com.android.renly.edu_yunzhi.UI.DrawableTextView;
import com.android.renly.edu_yunzhi.Utils.KeyboardWatcher;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;

import cz.msebera.android.httpclient.Header;

import static com.android.renly.edu_yunzhi.Fragment.HomeFragment.*;


/**
 * Created by wenzhihao on 2017/8/18.
 */

public class LoginFragment extends Fragment implements View.OnClickListener, KeyboardWatcher.SoftKeyboardStateListener {
    private DrawableTextView logo;
    private EditText et_mobile;
    private EditText et_password;
    private ImageView iv_clean_phone;
    private ImageView clean_password;
    private ImageView iv_show_pwd;
    private Button btn_login;
    private TextView forget_password;
    private RadioButton rb_stu;
    private RadioButton rb_teacher;

    private int screenHeight = 0;//屏幕高度
    private float scale = 0.8f; //logo缩放比例
    private View body;
    private RadioGroup service;
    private KeyboardWatcher keyboardWatcher;

    private View root;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_login, container, false);
        initView(view);
        initListener();
        keyboardWatcher = new KeyboardWatcher(getActivity().findViewById(Window.ID_ANDROID_CONTENT));
        keyboardWatcher.addSoftKeyboardStateListener(this);
        return view;
    }

    private void initView(View view) {
        logo = view.findViewById(R.id.logo);
        et_mobile = view.findViewById(R.id.et_mobile);
        et_password = view.findViewById(R.id.et_password);
        iv_clean_phone = view.findViewById(R.id.iv_clean_phone);
        clean_password = view.findViewById(R.id.clean_password);
        iv_show_pwd = view.findViewById(R.id.iv_show_pwd);
        btn_login = view.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        forget_password = view.findViewById(R.id.forget_password);
        rb_stu = view.findViewById(R.id.rb_login_stu);
        rb_teacher = view.findViewById(R.id.rb_login_teacher);
        service = view.findViewById(R.id.service);
        service.setOnCheckedChangeListener(new MyRadioButtonListener());
        body = view.findViewById(R.id.body);
        screenHeight = this.getResources().getDisplayMetrics().heightPixels; //获取屏幕高度
        root = view.findViewById(R.id.root);
    }

    class MyRadioButtonListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.rb_login_stu:
                    //当用户选中学生时
                    root.setBackground(getContext().getDrawable(R.mipmap.ic_login_background));
                    rb_stu.setEnabled(false);
                    rb_teacher.setEnabled(true);
                    break;
                case R.id.rb_login_teacher:
                    //当用户选中老师时
                    root.setBackground(getContext().getDrawable(R.drawable.four_screen_bg));
                    rb_stu.setEnabled(true);
                    rb_teacher.setEnabled(false);
                    break;
            }
        }
    }

    private void initListener() {
        root.setOnClickListener(this);
        iv_clean_phone.setOnClickListener(this);
        clean_password.setOnClickListener(this);
        iv_show_pwd.setOnClickListener(this);
        et_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && iv_clean_phone.getVisibility() == View.GONE) {
                    iv_clean_phone.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    iv_clean_phone.setVisibility(View.GONE);
                }
            }
        });
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && clean_password.getVisibility() == View.GONE) {
                    clean_password.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    clean_password.setVisibility(View.GONE);
                }
                if (s.toString().isEmpty())
                    return;
                if (!s.toString().matches("[A-Za-z0-9]+")) {
                    String temp = s.toString();
                    Toast.makeText(getActivity(), R.string.please_input_limit_pwd, Toast.LENGTH_SHORT).show();
                    s.delete(temp.length() - 1, temp.length());
                    et_password.setSelection(s.length());
                }
            }
        });
    }

    /**
     * 缩小
     *
     * @param view
     */
    public void zoomIn(final View view, float dist) {
        view.setPivotY(view.getHeight());
        view.setPivotX(view.getWidth() / 2);
        AnimatorSet mAnimatorSet = new AnimatorSet();
        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, scale);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, scale);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", 0.0f, -dist);

        mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX).with(mAnimatorScaleY);

        mAnimatorSet.setDuration(300);
        mAnimatorSet.start();

    }

    /**
     * f放大
     *
     * @param view
     */
    public void zoomOut(final View view) {
        if (view.getTranslationY() == 0) {
            return;
        }
        view.setPivotY(view.getHeight());
        view.setPivotX(view.getWidth() / 2);
        AnimatorSet mAnimatorSet = new AnimatorSet();

        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", scale, 1.0f);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", scale, 1.0f);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), 0);

        mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX).with(mAnimatorScaleY);
        mAnimatorSet.setDuration(300);
        mAnimatorSet.start();

    }

    private boolean flag = false;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.root:
                //收起软键盘
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                break;
            case R.id.iv_clean_phone:
                et_mobile.setText("");
                break;
            case R.id.clean_password:
                et_password.setText("");
                break;
            case R.id.iv_show_pwd:
                if (flag == true) {
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    iv_show_pwd.setImageResource(R.drawable.pass_gone);
                    flag = false;
                } else {
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    iv_show_pwd.setImageResource(R.drawable.pass_visuable);
                    flag = true;
                }
                String pwd = et_password.getText().toString();
                if (!TextUtils.isEmpty(pwd))
                    et_password.setSelection(pwd.length());
                break;
            case R.id.btn_login:
                String password = et_password.getText().toString();
                String username = et_mobile.getText().toString();

                if(!TextUtils.isEmpty(password) && !TextUtils.isEmpty(username)){
                    String url = AppNetConfig.LOGIN;
                    RequestParams params = new RequestParams();
                    params.put("username",username);
                    params.put("password",password);
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.post(url, params, new AsyncHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            if(statusCode != 200){
                                Toast.makeText(getContext(),"登陆失败",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String response = new String(responseBody);
                            JSONObject jsonObject = JSON.parseObject(response);
                            String realName = jsonObject.getString("realname");
                            String schoolName = JSON.parseObject(jsonObject.get("department").toString()).getString("name");

                            doLogin(username,password,realName,schoolName);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(getContext(),"联网失败" + statusCode,Toast.LENGTH_SHORT).show();
                            String realName = "测试姓名";
                            String schoolName = "测试学校";
                            doLogin(username,password,realName,schoolName);
                        }
                    });
                }else{
                    //如果用户未输入全部信息
                    Toast.makeText(this.getContext(),"请输入用户名及密码",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void doLogin(String username,String password,String realName,String schoolName) {
        // 获取SharedPreferences对象
        SharedPreferences sharedPre = getContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        // 获取Editor对象
        SharedPreferences.Editor editor = sharedPre.edit();
        // 设置参数
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("realName",realName);
        editor.putString("schoolName",schoolName);


        Toast.makeText(MyApplication.context,"登录成功",Toast.LENGTH_SHORT).show();

        if(!rb_teacher.isChecked()){//学生登录
            //发送事件
            editor.putBoolean("isStudent",true);
            // 提交
            editor.commit();
            EventBus.getDefault().post(new MessageEvent("studentLogin"));
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.refresh();
            mainActivity.gotoHomeFragment();
            getActivity().finish();
        }
        else{//老师登录
            editor.putBoolean("isStudent",false);
            // 提交
            editor.commit();
            EventBus.getDefault().post(new MessageEvent("teacherLogin"));
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.refresh();
            mainActivity.gotoHomeFragment();
            getActivity().finish();
        }


    }


    @Override
    public void onSoftKeyboardOpened(int keyboardSize) {
        int[] location = new int[2];
        body.getLocationOnScreen(location); //获取body在屏幕中的坐标,控件左上角
        int x = location[0];
        int y = location[1];
        int bottom = screenHeight - (y + body.getHeight());
        if (keyboardSize > bottom) {
            ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(body, "translationY", 0.0f, -(keyboardSize - bottom));
            mAnimatorTranslateY.setDuration(300);
            mAnimatorTranslateY.setInterpolator(new AccelerateDecelerateInterpolator());
            mAnimatorTranslateY.start();
            zoomIn(logo, keyboardSize - bottom);

        }
    }

    @Override
    public void onSoftKeyboardClosed() {
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(body, "translationY", body.getTranslationY(), 0);
        mAnimatorTranslateY.setDuration(300);
        mAnimatorTranslateY.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorTranslateY.start();
        zoomOut(logo);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        keyboardWatcher.removeSoftKeyboardStateListener(this);
    }

}
