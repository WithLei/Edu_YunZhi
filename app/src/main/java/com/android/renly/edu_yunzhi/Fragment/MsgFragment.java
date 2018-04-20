package com.android.renly.edu_yunzhi.Fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.renly.edu_yunzhi.Common.BaseFragment;
import com.android.renly.edu_yunzhi.Common.MyApplication;
import com.android.renly.edu_yunzhi.R;
import com.loopj.android.http.RequestParams;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MsgFragment extends BaseFragment  implements OnMenuItemClickListener, OnMenuItemLongClickListener {
    @Bind(R.id.fl_msg)
    FrameLayout flMsg;

    @Bind(R.id.btn_1)
    RadioButton btn1;
    @Bind(R.id.btn_2)
    RadioButton btn2;
    @Bind(R.id.btn_3)
    RadioButton btn3;
    @Bind(R.id.btn_change)
    RadioGroup btnChange;
    @Bind(R.id.btn_test)
    Button btnTest;

    private FragmentManager fragmentManager;
    private ContextMenuDialogFragment mMenuDialogFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getActivity().getSupportFragmentManager();
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        initMenuFragment();
    }


    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize(56);
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
        mMenuDialogFragment.setItemLongClickListener(this);
    }

    private List<MenuObject> getMenuObjects() {
        // You can use any [resource, bitmap, drawable, color] as image:
        // item.setResource(...)
        // item.setBitmap(...)
        // item.setDrawable(...)
        // item.setColor(...)
        // You can set image ScaleType:
        // item.setScaleType(ScaleType.FIT_XY)
        // You can use any [resource, drawable, color] as background:
        // item.setBgResource(...)
        // item.setBgDrawable(...)
        // item.setBgColor(...)
        // You can use any [color] as text color:
        // item.setTextColor(...)
        // You can set any [color] as divider color:
        // item.setDividerColor(...)

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.drawable.icn_close);

        MenuObject send = new MenuObject("Send message");
        send.setResource(R.drawable.icn_1);

        MenuObject like = new MenuObject("Like profile");
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.icn_2);
        like.setBitmap(b);

        MenuObject addFr = new MenuObject("Add to friends");
        BitmapDrawable bd = new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.drawable.icn_3));
        addFr.setDrawable(bd);

        MenuObject addFav = new MenuObject("Add to favorites");
        addFav.setResource(R.drawable.icn_4);

        MenuObject block = new MenuObject("Block user");
        block.setResource(R.drawable.icn_5);

        menuObjects.add(close);
        menuObjects.add(send);
        menuObjects.add(like);
        menuObjects.add(addFr);
        menuObjects.add(addFav);
        menuObjects.add(block);
        return menuObjects;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_test:
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMenuItemClick(View clickedView, int position) {
        Toast.makeText(MyApplication.context, "Clicked on position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMenuItemLongClick(View clickedView, int position) {
        Toast.makeText(getContext(), "Long clicked on position: " + position, Toast.LENGTH_SHORT).show();
    }
//这里结束
    @Override
    protected String getUrl() {
        return null;
    }

    @Override
    protected RequestParams getParams() {
        return null;
    }

    private static HotNewsFragment hotNewsFragment;
    private static MyMsgFragment myMsgFragment;
    private FragmentTransaction transaction;

    @Override
    protected void initData(String content) {
        setSelect(0);
    }

    @Override
    public int getLayoutid() {
        return R.layout.fragment_msg;
    }

    private void setSelect(int select) {
        FragmentManager fragmentManager = this.getFragmentManager();
        transaction = fragmentManager.beginTransaction();
        //隐藏所有fragment
        hideFragments();
        switch (select) {
            case 0:
                if (hotNewsFragment == null) {
                    hotNewsFragment = new HotNewsFragment();//commit()后调用生命周期方法
                    transaction.add(R.id.fl_msg, hotNewsFragment);
                }
                transaction.show(hotNewsFragment);//显示当前的Fragment
                break;
            case 1:
                if (myMsgFragment == null) {
                    myMsgFragment = new MyMsgFragment();//commit()后调用生命周期方法
                    transaction.add(R.id.fl_msg, myMsgFragment);
                }
                transaction.show(myMsgFragment);//显示当前的Fragment
                break;
        }
        //提交事务
        transaction.commit();
    }

    private void hideFragments() {
        if (hotNewsFragment != null) {
            transaction.hide(hotNewsFragment);
        }
        if (myMsgFragment != null) {
            transaction.hide(myMsgFragment);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        btn1.setText("动态圈");
        btn2.setVisibility(View.GONE);
        btn3.setText("我的消息");
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn_1, R.id.btn_3,R.id.btn_test})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                setSelect(0);
                break;
            case R.id.btn_3:
                setSelect(1);
                break;
        }
    }
}
