package com.xht.meizhi.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.xht.meizhi.R;
import com.xht.meizhi.adapter.GankPagerAdapter;
import com.xht.meizhi.base.ToolbarActivity;
import com.xht.meizhi.util.DateUtils;

/**
 * Created by xht on 2016/11/7 16:13.
 */

public class GankActivity extends ToolbarActivity {

    public static final String EXTRA_GANK_DATE = "gank_date";

    // 时间
    private String mDate;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private GankPagerAdapter mPagerAdapter;

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_gank;
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewPager = (ViewPager) findViewById(R.id.vp_gank);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);

        mDate = getIntent().getStringExtra(EXTRA_GANK_DATE).substring(0, 10);

        setTitle(DateUtils.getDate(mDate));

        initViewPager();
        initTabLayout();
    }

    private void initTabLayout() {
        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            mTabLayout.addTab(mTabLayout.newTab());
        }
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initViewPager() {
        mPagerAdapter = new GankPagerAdapter(getSupportFragmentManager(), mDate);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gank, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        mViewPager.removeOnPageChangeListener(mOnPageChangeListener);
        super.onDestroy();
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setTitle(DateUtils.getDate(mDate, -position));
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


}
