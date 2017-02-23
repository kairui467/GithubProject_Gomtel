package com.igeak.customwatchface.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.os.Bundle;

import com.igeak.customwatchface.R;
import com.igeak.customwatchface.view.fragment.CustomFaceFragment;
import com.igeak.customwatchface.view.fragment.InnerFaceFragment;
import com.igeak.customwatchface.view.view.slide.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {

    ViewPager viewPager = null;
    MainActivityPagerAdapter mMyPagerAdapter = null;
    SlidingTabLayout mSlidingTabLayout = null;
    List<Fragment> fragments = null;
    List<String> titleList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //addViewToContainerById(R.layout.activity_main);
        initFragment();
        initViewpager();
    }

    private void initFragment() {
        InnerFaceFragment innerFaceFragment = new InnerFaceFragment();
        CustomFaceFragment myFaceFragment = new CustomFaceFragment();
        fragments = new ArrayList<>();
        fragments.add(innerFaceFragment);
        fragments.add(myFaceFragment);
        titleList = new ArrayList<>();
        titleList.add(getResources().getString(R.string.frg_innerface));
        titleList.add(getResources().getString(R.string.frg_myface));
    }

    private void initViewpager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        mMyPagerAdapter = new MainActivityPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mMyPagerAdapter);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color
                .slide_indicator));
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(viewPager);
    }

    class MainActivityPagerAdapter extends FragmentPagerAdapter {
        public MainActivityPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return titleList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // TODO Auto-generated method stub
            return titleList.get(position);
        }
    }


}
