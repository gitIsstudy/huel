package com.hueljk.ibeacon.ui.setting;


import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.location.GpsStatus;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hueljk.ibeacon.R;
import com.hueljk.ibeacon.manager.PreferenceManager;
import com.hueljk.ibeacon.ui.BaseFragment;
import com.hueljk.ibeacon.ui.navigation.NavFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 我的个人中心
 */
public class SettingFragment extends BaseFragment implements View.OnClickListener {
    private String[] mItems = new String[]{"我发布的", "我卖出的", "我买到的", "我收藏的", "反馈信息", "设置",};
    private ListView mListView;
    private View mSettingFragment;
    private TextView mset_tx;
    private ImageView mperson_headimg;
    private TextView mperson_name;
    private TextView mvip_value;
    private TextView mmy_collect;
    private TextView mmy_foucs;
    private LinearLayout mlogin;
    private PreferenceManager mPreferenceManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);


    }


    @Override
    protected void initView(View view) {
        super.initView(view);
        mset_tx = view.findViewById(R.id.set_tx);
        mlogin = view.findViewById(R.id.register_ly);
        mperson_headimg = view.findViewById(R.id.person_headimg);
        mperson_name = view.findViewById(R.id.person_name);
        mvip_value = view.findViewById(R.id.vip_value);
        mmy_collect = view.findViewById(R.id.my_collect);
        mmy_foucs = view.findViewById(R.id.my_foucs);
        mset_tx.setOnClickListener(this);
        mperson_headimg.setOnClickListener(this);
        mperson_name.setOnClickListener(this);
        mvip_value.setOnClickListener(this);
        mmy_collect.setOnClickListener(this);
        mmy_foucs.setOnClickListener(this);
        mlogin.setOnClickListener(this);

        mListView = view.findViewById(R.id.set_listview);
        mListView.setAdapter(new ArrayAdapter<>(getContext(), R.layout.array_adapter, mItems));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "listview被点击了，位置是" + position, Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }

            }
        });


    }


    public void onClick(View v) {
        //参数view是当前被点击的控件
        switch (v.getId()) {
            case R.id.set_tx:
                mMainActivity.showFragment(PersonSettingFragment.class,"setting_2_per");
                break;
            case R.id.register_ly:
                mMainActivity.showFragment(LoginFragment.class, "setting_2_login");
                break;
            case R.id.person_headimg:
                mMainActivity.showFragment(LoginFragment.class, "setting_2_login");
                break;
            case R.id.person_name:
                mMainActivity.showFragment(LoginFragment.class, "setting_2_login");
                break;

            case R.id.vip_value:
                Toast.makeText(getContext(), "你点击了我的会员值", Toast.LENGTH_SHORT).show();
                break;
            case R.id.my_collect:
                Toast.makeText(getContext(), "你点击了我的收藏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.my_foucs:
                Toast.makeText(getContext(), "你点击了我关注的品牌", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    @Override
    protected void setData() {
        super.setData();
        mPreferenceManager = PreferenceManager.getInstance();
        if (mPreferenceManager.getLoginStatus()) {
            mperson_name.setText(mPreferenceManager.getUserName());
        }

    }

    /**
     * 有一个方法能够在登录成功的时候执行，在这个方法内部执行刷新、跳转操作
     */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(String message) {
        //回到主页面
        //mMainActivity.toHomeFragment();
        //执行个人信息刷新操作
        mperson_name.setText(message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}












