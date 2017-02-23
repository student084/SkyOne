package com.student0.www.skyone;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.student0.www.adapter.MyFragmentPagerAdapter;
import com.student0.www.fragment.FriendsFragment;
import com.student0.www.fragment.PhotosFragment;
import com.student0.www.fragment.SharesFragment;
import com.student0.www.view.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private ViewPager viewPager;
    private ViewPagerIndicator viewPagerIndicator;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private MyFragmentPagerAdapter mAdapter;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initDatas();
        mAdapter = new MyFragmentPagerAdapter(fragmentManager, fragmentList);
        viewPager.setAdapter(mAdapter);
        initEvent();
    }

    private void initEvent() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //position	int: Position index of the first page currently being displayed. Page position+1 will be visible if positionOffset is nonzero.
                //positionOffset	float: Value from [0, 1) indicating the offset from the page at position.
                //三角形的位置变化为:tableWidth * positionOffset + position * tableWidth
                viewPagerIndicator.resetTextViewColor();
                viewPagerIndicator.scroll(position,positionOffset);

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initView() {
        viewPager = (ViewPager)findViewById(R.id.view_page);
        viewPagerIndicator = (ViewPagerIndicator) findViewById(R.id.id_indicator);
    }

    private void initDatas() {
        //The page's order
        //Please not change before sure
        fragmentList.add(new PhotosFragment());
        fragmentList.add(new FriendsFragment());
        fragmentList.add(new SharesFragment());
        fragmentManager=getSupportFragmentManager();
    }
}
