package com.study.study1;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ZhihuRequest {

    public static void getZhiHuNews(RequestCallback callback){
        Call<ZhiHuNews> zhihuRequest = getZhiHuApi().getLatestNews();
        enqueue(zhihuRequest,callback);
    }

    public static void getBeforeZhiHuNews(RequestCallback callback, int amount){
        Call<ZhiHuNews> zhihuRequest = getZhiHuApi().getbeforeNews(Util.getRefreshTime(amount));
        enqueue(zhihuRequest,callback);
    }

    public static void enqueue(Call<ZhiHuNews> request, RequestCallback callback){
        request.enqueue(new Callback<ZhiHuNews>() {
            @Override
            public void onResponse(Call<ZhiHuNews> call, Response<ZhiHuNews> response) {
                callback.requestSuccess(response);
            }

            @Override
            public void onFailure(Call<ZhiHuNews> call, Throwable t) {
                callback.requestFail(t);
            }
        });
    }

    public static ZhihuApi getZhiHuApi(){
        /*
         **打印retrofit信息部分
         */
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                //打印retrofit日志
                Log.i(MyLog.TAG, "log: "+message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()//okhttp设置部分，此处还可再设置网络参数
                .addInterceptor(loggingInterceptor)
                .build();
        //*/

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Util.ZHIHU_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ZhihuApi zhihu = retrofit.create(ZhihuApi.class);
        return zhihu;
    }

    interface RequestCallback{
        void requestSuccess(Response<ZhiHuNews> response );
        void requestFail(Throwable t);
    }
}
