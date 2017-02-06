package com.xht.meizhi.adapter;

import android.view.View;

import com.xht.meizhi.bean.MeizhiWithVideoInfo;

/**
 * RecyclerView的点击监听接口
 */
public interface OnMeizhiTouchListener {
    void onTouch(View v, View meizhiView, View card, MeizhiWithVideoInfo meizhi);
}

