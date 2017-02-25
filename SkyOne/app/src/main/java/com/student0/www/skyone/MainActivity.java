package com.student0.www.skyone;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.student0.www.Config;
import com.student0.www.adapter.MyFragmentPagerAdapter;
import com.student0.www.fragment.CameraFragment;
import com.student0.www.fragment.PhotosFragment;
import com.student0.www.fragment.TempFragment;
import com.student0.www.view.ViewPagerIndicator;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private ViewPager viewPager;
    private ViewPagerIndicator viewPagerIndicator;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private MyFragmentPagerAdapter mAdapter;
    private FragmentManager fragmentManager;

    private CameraFragment cameraFragment;
    private TempFragment tempFragment;
    private PhotosFragment photosFragment;

    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initDatas();
        mAdapter = new MyFragmentPagerAdapter(fragmentManager, fragmentList);
        viewPager.setAdapter(mAdapter);
        initEvent();
        MyCache.setListCache();
        //create
        createSkyOneDir();
    }

    private void createSkyOneDir() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)){
            File dir = new File(Config.PHOTOS_DIR);
            if (!dir.exists()){
                dir.mkdir();
            }
        }
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
                //Toast.makeText(MainActivity.this, position + "" , Toast.LENGTH_SHORT).show();
                if (position == Config.TEMPS_FRAGMENT_POSITION_IN_VIEWPAGER){
                    mProgressDialog = mProgressDialog.show(MainActivity.this,null, "Loading ...");
                    tempFragment.notifyView();
                    mProgressDialog.dismiss();
                }
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
        cameraFragment = new CameraFragment();
        tempFragment = new TempFragment();
        photosFragment = new PhotosFragment();

        fragmentList.add(cameraFragment);
        fragmentList.add(tempFragment);
        fragmentList.add(photosFragment);
        fragmentManager=getSupportFragmentManager();
    }


}
