package com.syz.magicbox.magicbox;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.syz.magicbox.magicbox.adapter.MyFragmentPagerAdapter;
import com.syz.magicbox.magicbox.fragment.HomePageFragment;
import com.syz.magicbox.magicbox.fragment.MeFragment;
import com.syz.magicbox.magicbox.fragment.PublishFragment;
import com.syz.magicbox.magicbox.view.ImageViewPager;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ImageViewPager vp_container;
    private TabLayout tl_bottom_tab;
    private MyFragmentPagerAdapter mFragmentPagerAdapter;

    private  TabLayout.Tab[] tabs;
    private String[] mFragmentTitles;
    private List<Fragment> fragments;
    private int[] icon = new int[]{R.drawable.selector_homepage_ic,
            R.drawable.selector_box_ic,R.drawable.selector_me_ic};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUI();
        initFragment();
    }

    private void initFragment() {
        mFragmentTitles = new String[]{"首页","发布","我的"};
        fragments = new ArrayList<Fragment>();
        //添加Fragment
        fragments.add(new HomePageFragment());
        fragments.add(new PublishFragment());
        fragments.add(new MeFragment());
         //设置Fragment适配器
        mFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),mFragmentTitles, fragments);
        vp_container.setAdapter(mFragmentPagerAdapter);
        tl_bottom_tab.setupWithViewPager(vp_container);
        tabs = new TabLayout.Tab[mFragmentTitles.length];
        //设置底部导航图标
        for (int i = 0;i < mFragmentTitles.length;i++){
            tabs[i] = tl_bottom_tab.getTabAt(i);
            tabs[i].setIcon(icon[i]);
        }
    }
    private void initUI() {
        vp_container = (ImageViewPager) findViewById(R.id.vp_home_container);
        tl_bottom_tab = (TabLayout) findViewById(R.id.tl_home_bottom_tab);
    }

}
