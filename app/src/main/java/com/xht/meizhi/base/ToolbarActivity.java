package com.xht.meizhi.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.xht.meizhi.R;

/**
 * Created by xht on 2016/11/6.
 */

public abstract class ToolbarActivity extends BaseActivity {

    protected AppBarLayout mAppBar;
    protected Toolbar mToolbar;
    protected boolean mIsHidden = false;

    /**
     * 子类实现该方法，提供布局id
     *
     * @return
     */
    abstract protected int provideContentViewId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(provideContentViewId());
        mAppBar = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        if (mToolbar == null || mAppBar == null) {
            Log.i("TAG", "ToolbarActivity------toolbar或者appbar为空");
            throw new IllegalStateException(
                    "The subclass of ToolbarActivity must contain a toolbar.");
        }

        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onToolbarClick();
            }
        });

        // 设置支持toolbar
        setSupportActionBar(mToolbar);

        // 判断是否支持返回箭头
        if (canBack()) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null)
                actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // 设置阴影？
        if (Build.VERSION.SDK_INT >= 21) {
            mAppBar.setElevation(10.6f);
        }

    }

    /**
     * toolbar中overflow中的菜单选项
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Log.i("TAG","ToolbarActivity---onOptionsItemSelected()---onBackPressed()");
            // 返回箭头
            onBackPressed();
            return true;
        } else {
            // 走父类方法
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 设置Toolbar的透明度
     *
     * @param alpha
     */
    protected void setAppBarAlpha(float alpha) {
        mAppBar.setAlpha(alpha);
    }

    /**
     * 设置toolbar隐藏或显示
     */
    protected void hideOrShowToolbar() {
        mAppBar.animate()
                .translationY(mIsHidden ? 0 : -mAppBar.getHeight())
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
        mIsHidden = !mIsHidden;
    }

    /**
     * 子类覆写该方法，设置toolbar的点击事件
     */
    public void onToolbarClick() {
    }

    /**
     * 子类覆写该方法，设置是否toolbar带返回箭头
     * 默认false
     *
     * @return
     */
    public boolean canBack() {
        return false;
    }

}
