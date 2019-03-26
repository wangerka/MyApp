package com.study.study1;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ZhihuApi {
    //https://news-at.zhihu.com/api/4/news/latest
    @GET("latest")
    Call<ZhiHuNews> getLatestNews();

    //https://news-at.zhihu.com/api/4/news/before/20131119
    @GET("before/{time}")
    Call<ZhiHuNews> getbeforeNews(@Path("time") String time);
}
