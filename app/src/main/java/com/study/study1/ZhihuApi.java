package com.study.study1;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ZhihuApi {
    @GET("latest")
    Call<ZhiHuNews> getLatestNews();
}
