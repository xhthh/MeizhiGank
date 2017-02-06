package com.xht.meizhi.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xht.meizhi.net.GankFactory;
import com.xht.meizhi.ui.GankFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by xht on 2016/11/7 17:07.
 */

public class GankPagerAdapter extends FragmentPagerAdapter {

    private String mDate;

    public GankPagerAdapter(FragmentManager fm, String date) {
        super(fm);
        mDate = date;
    }

    @Override
    public Fragment getItem(int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(mDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -position);

        return GankFragment.newInstance(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public int getCount() {
        return GankFactory.gankSize;
    }

}
