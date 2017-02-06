package com.xht.meizhi.net;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xht on 2016/11/5.
 * 这个类用来对外生成单例GankApi实例，为确保GankApi实例只生成一次。
 */

public class GankFactory {
    private static final Object monitor = new Object();
    private static final String baseUrl = "http://gank.io/";

    // 一次请求的数目
    public static final int meizhiSize = 10;
    // 详情页面中page的个数
    public static final int gankSize = 5;

    private static GankApi sGankIOSingleton = null;
    private static DGankApi sDGankIOSingleton = null;

    public static GankApi getGankIOSingleton() {
        synchronized (monitor) {
            if (sGankIOSingleton == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();
                sGankIOSingleton = retrofit.create(GankApi.class);
            }
            return sGankIOSingleton;
        }
    }

    public static DGankApi getDGankIOSingleton() {
        synchronized (monitor) {
            if (sDGankIOSingleton == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();
                sDGankIOSingleton = retrofit.create(DGankApi.class);
            }
            return sDGankIOSingleton;
        }
    }
}
