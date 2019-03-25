package com.study.study1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ZhiHuFragment extends Fragment {

    RecyclerView listview;
    SwipeRefreshLayout swip;
    ProgressBar bar;
    int amount=1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, null);
        listview = view.findViewById(R.id.listview);
        listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        swip = view.findViewById(R.id.swiprefresh);
        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swip.setRefreshing(true);
                /*
                 **打印retrofit信息部分
                 */
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        //打印retrofit日志
                        Log.e("RetrofitLog","retrofitBack = "+message);
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

                Call<ZhiHuBeforeNews> zhihuRequest = zhihu.getbeforeNews(Util.getRefreshTime(amount++));
                zhihuRequest.enqueue(new Callback<ZhiHuBeforeNews>() {
                    @Override
                    public void onResponse(Call<ZhiHuBeforeNews> call, Response<ZhiHuBeforeNews> response) {
                        swip.setRefreshing(false);
                        ZhiHuBeforeNews news = response.body();
                        Log.i(MyLog.TAG, "onResponse: "+news);
                        List<Stories> list = news.getStories();
                        listview.setAdapter(new ZhiHuAdapter(list));
                    }

                    @Override
                    public void onFailure(Call<ZhiHuBeforeNews> call, Throwable t) {

                    }
                });
            }
        });

        bar = view.findViewById(R.id.progressbar);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Util.ZHIHU_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ZhihuApi zhihu = retrofit.create(ZhihuApi.class);

        Call<ZhiHuNews> zhihuRequest = zhihu.getLatestNews();
        zhihuRequest.enqueue(new Callback<ZhiHuNews>() {
            @Override
            public void onResponse(Call<ZhiHuNews> call, Response<ZhiHuNews> response) {
                bar.setVisibility(View.GONE);
                ZhiHuNews news = response.body();
                List<Stories> list = news.getStories();
                listview.setAdapter(new ZhiHuAdapter(list));
            }

            @Override
            public void onFailure(Call<ZhiHuNews> call, Throwable t) {

            }
        });

        return view;
    }

    class ZhiHuAdapter extends RecyclerView.Adapter<StoryVH>{
        List<Stories> storiesList;
        public ZhiHuAdapter(List<Stories> stories) {
            storiesList = stories;
        }

        @NonNull
        @Override
        public StoryVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.zhihu_story,viewGroup, false);
            return new StoryVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StoryVH storyVH, int i) {
            storyVH.fillData(storiesList.get(i));
        }

        @Override
        public int getItemCount() {
            return storiesList.size();
        }
    }

    class StoryVH extends RecyclerView.ViewHolder{
        TextView content;
        ImageView pic;
        public StoryVH(@NonNull View itemView) {
            super(itemView);

            content = itemView.findViewById(R.id.content);
            pic = itemView.findViewById(R.id.pic);
        }

        public void fillData(Stories stories){
            content.setText(stories.getTitle());
            Glide.with(getActivity())
                    .load(stories.getImages().get(0))
                    .into(pic);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(MyLog.TAG, "onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(MyLog.TAG, "onDestroy: ");
    }
}
