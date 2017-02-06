package com.xht.meizhi.net;

import com.xht.meizhi.bean.DGankData;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by xht on 2016/11/8 17:29.
 */

public interface DGankApi {
    @Headers({"X-LC-Id: 0azfScvBLCC9tAGRAwIhcC40",
            "X-LC-Key: gAuE93qAusvP8gk1VW8DtOUb",
            "Content-Type: application/json"})
    @GET("/Gank?limit=1")
    Observable<DGankData> getDGankData(
            @Query("where") String where);// format {"tag":"2015-11-10"}
}
