package com.syz.magicbox.magicbox.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;

/**
 * Created by chx on 2016/12/6.
 */

public abstract class BaseFragment extends Fragment{

    private static final String TAG = "BaseFragment";
    protected boolean isVisible;
    //视图是否加载完毕
    protected boolean isPapred;
    protected Activity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = getActivity();
        View view = initView();
        ViewUtils.inject(this,view);
        isPapred = true;
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initEvent();
    }


    protected void initData(){
        if (isPapred && isVisible){
            //界面显示并且视图初始化完成后加载数据
            Log.i(TAG, "setUserVisibleHint: initData()------" + "loadData" );
            loadData();
            //开启任务
            consumingTimeTask();
        }
    }
    protected void initEvent(){

    }

    //填充布局
    public abstract View initView();
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //判断该Fragment页面是否可见
        if (getUserVisibleHint()){
            isVisible = true;
            Log.i(TAG, "setUserVisibleHint: " + isVisibleToUser);
            onVisible();
        }else {
            Log.i(TAG, "setUserVisibleHint: " + isVisibleToUser);
            isVisible =false;
            onInvisible();
        }
    }


    //fragment不可见时
    //非延时加载
    protected void onInvisible() {
        Log.i(TAG, "setUserVisibleHint: " + "onInvisible");
    }
    //ViewPager会预加载fragment，为防止耗时加载，耗损多余流量
    //当该页面fragment显示时才会调用，可以实现耗时加载（网络请求等），
    protected void onVisible() {
        //该方法会在onCreateView之前调用
        Log.i(TAG, "setUserVisibleHint: " + "onVisible");
        if (isPapred){
            consumingTimeTask();
            //加载数据
            loadData();
        }
    }
    //实现加载数据
    protected abstract void loadData();
    //一直执行的任务，如图片无限轮播
    protected void consumingTimeTask(){

    }
}
