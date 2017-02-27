package com.syz.magicbox.magicbox.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.syz.magicbox.magicbox.R;
import com.syz.magicbox.magicbox.view.ImageViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 注意：使用ViewPager会预加载，一般一次加载两个fragment，只有fragment切到第三个时，第一个才会销毁
 * Created by chx on 2016/12/2.
 */

public class HomePageFragment extends BaseFragment {
    @ViewInject(R.id.ivp_homepage_top)
    private ImageViewPager vp_home_top;
    @ViewInject(R.id.tv_homepage_vp_des)
    private TextView tv_vp_des;
    @ViewInject(R.id.ll_homepage_vp_points)
    private LinearLayout ll_vp_points;

    private MyPagerAdapter mPagerAdapter;

    private List<ImageView> mVpImgList;
    private String[] contentDes;

    private  int nowPointPosition = 0;
    private  boolean isRunning;   //控制轮播线程
    private final static String tag = "HomePageFragment";

    @Override
    public View initView() {
        View view = View.inflate(mActivity,R.layout.fragment_homepage,null);
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        int[] nativeImg = {R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e};
        mVpImgList = new ArrayList<ImageView>();
        ImageView imageView;
        View point;
        LinearLayout.LayoutParams layoutParams;
        for (int i = 0;i < nativeImg.length;i++){
            imageView = new ImageView(mActivity);
            imageView.setImageResource(nativeImg[i]);
            mVpImgList.add(imageView);

            point = new View(mActivity);
            point.setBackgroundResource(R.drawable.selector_point_bg);
            //设置点对应的布局
            layoutParams = new LinearLayout.LayoutParams(5,5);
            if (i != 0)
                layoutParams.leftMargin = 10;
            //设置所有点不可用
            point.setEnabled(false);
            ll_vp_points.addView(point,layoutParams);
        }
        //假数据，轮播图片内容描述
        contentDes = new String[]{
                "净网行动从我做起",
                "可不可以不勇敢",
                "Too bad，So Love",
                "Rellay lIke the white dog",
                "特大喜讯，男足进入2026年世界杯八强"
        };
        //初始化适配器，设置图片数据，轮播为true
        if(mVpImgList != null) {
            mPagerAdapter = new MyPagerAdapter();
            vp_home_top.setAdapter(mPagerAdapter);
        }
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        
        //设置第一个点可见
        ll_vp_points.getChildAt(0).setEnabled(true);
        tv_vp_des.setText(contentDes[0]);
        nowPointPosition = 0;


        // 无限轮播默认设置到中间的某个位置，无论往左或往右都能够切换无限次（）
        int pos = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2 % mVpImgList.size());
        // 2147483647 / 2 = 1073741823 - (1073741823 % 5)
        vp_home_top.setCurrentItem(5000000); // 设置到某个位置

        vp_home_top.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //新增条目时调用
                int newPosition = position % mVpImgList.size();

                tv_vp_des.setText(contentDes[newPosition]);

                ll_vp_points.getChildAt(nowPointPosition).setEnabled(false);
                ll_vp_points.getChildAt(newPosition).setEnabled(true);

                nowPointPosition = newPosition;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void loadData() {
        //可以再次加载网络数据
    }

    @Override
    protected void consumingTimeTask() {
        super.consumingTimeTask();
        Log.i(tag,"NowThread:"+isRunning);
        new Thread(){
            @Override
            public void run() {
                Log.i(tag,"NowThread:"+isRunning);
                while (isRunning){
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (vp_home_top.getCurrentItem()< mVpImgList.size()-1) {
                                vp_home_top.setCurrentItem(vp_home_top.getCurrentItem() + 1);
                            }else {
                                vp_home_top.setCurrentItem(0);
                            }
                        }
                    });
                }
            }
        }.start();
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        //轮播
        isRunning = true;
        if (isPapred){
            loadData();
        }
    }

    @Override
    protected void onInvisible() {
        super.onInvisible();
        isRunning = false;
        Log.i(tag,"NowThread:"+isRunning);
    }
    class MyPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mVpImgList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = mVpImgList.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
