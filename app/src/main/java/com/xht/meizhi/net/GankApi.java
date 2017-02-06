package com.xht.meizhi.net;

import com.xht.meizhi.bean.GankData;
import com.xht.meizhi.bean.MeizhiList;
import com.xht.meizhi.bean.VideoList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by xht on 2016/11/5.
 */

public interface GankApi {
    //http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/1
    //一页10条数据
    @GET("api/data/福利/" + GankFactory.meizhiSize + "/{page}")
    Observable<MeizhiList> getMeizhiList(@Path("page") int page);


    //http://gank.io/api/data/%E4%BC%91%E6%81%AF%E8%A7%86%E9%A2%91/10/1
    @GET("api/data/休息视频/" + GankFactory.meizhiSize + "/{page}")
    Observable<VideoList> getVideoList(@Path("page") int page);

    //http://gank.io/api/day/2016/11/1
    @GET("api/day/{year}/{month}/{day}")
    Observable<GankData> getGankData(
            @Path("year") int year,
            @Path("month") int month,
            @Path("day") int day);


    @GET("api/day/{year}/{month}/{day}")
    Call<GankData> getGankData1(
            @Path("year") int year,
            @Path("month") int month,
            @Path("day") int day);

}
