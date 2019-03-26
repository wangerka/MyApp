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
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class ZhiHuFragment extends Fragment implements ZhihuRequest.RequestCallback {

    RecyclerView listview;
    SwipeRefreshLayout swip;
    ProgressBar bar;
    int amount = 1;
    List<Stories> totalList = new ArrayList<>();
    ZhiHuAdapter adapter;
    boolean loadMore = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, null);
        listview = view.findViewById(R.id.listview);
        listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        listview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    RecyclerView.LayoutManager layoutManager =
                            listview.getLayoutManager();
                    int count = listview.getAdapter().getItemCount();
                    if (layoutManager instanceof LinearLayoutManager && count > 0) {
                        LinearLayoutManager linearLayoutManager
                                = (LinearLayoutManager) layoutManager;
                        if (linearLayoutManager.findLastVisibleItemPosition() == count - 1) {
                            loadMore = true;
                            ZhihuRequest.getBeforeZhiHuNews(ZhiHuFragment.this,amount);
                        }
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        swip = view.findViewById(R.id.swiprefresh);
        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swip.setRefreshing(true);
                ZhihuRequest.getBeforeZhiHuNews(ZhiHuFragment.this, amount++);
            }
        });

        bar = view.findViewById(R.id.progressbar);

        ZhihuRequest.getZhiHuNews(ZhiHuFragment.this);

        return view;
    }

    class ZhiHuAdapter extends RecyclerView.Adapter<StoryVH> {
        int ITEM_NORMAL = 0;
        int ITEM_FOOT = 1;
        List<Stories> storiesList;

        public ZhiHuAdapter(List<Stories> stories) {
            storiesList = stories;
        }

        public void setData(List<Stories> stories) {
            storiesList = stories;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == getItemCount() - 1) {
                return ITEM_FOOT;
            } else {
                return ITEM_NORMAL;
            }
        }

        @NonNull
        @Override
        public StoryVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view;
            if (i == ITEM_NORMAL) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.zhihu_story, viewGroup, false);
            } else {
                view = LayoutInflater.from(getContext()).inflate(R.layout.zhihu_foot, viewGroup, false);
            }
            return new StoryVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StoryVH storyVH, int i) {
            if (getItemViewType(i) == ITEM_NORMAL) {
                storyVH.fillData(storiesList.get(i));
            }
        }

        @Override
        public int getItemCount() {
            return storiesList.size() + 1;
        }
    }

    class StoryVH extends RecyclerView.ViewHolder {
        TextView content;
        ImageView pic;

        public StoryVH(@NonNull View itemView) {
            super(itemView);

            content = itemView.findViewById(R.id.content);
            pic = itemView.findViewById(R.id.pic);
        }

        public void fillData(Stories stories) {
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

    @Override
    public void requestSuccess(Response response) {
        if(swip.isRefreshing()) swip.setRefreshing(false);
        ZhiHuNews news = (ZhiHuNews) response.body();
        Log.i(MyLog.TAG, "onResponse: " + news);
        List<Stories> list = news.getStories();
        if(!loadMore){
            totalList.clear();
            totalList.addAll(list);
            bar.setVisibility(View.GONE);
            adapter = new ZhiHuAdapter(list);
            listview.setAdapter(adapter);
        }else {
            loadMore = false;
            totalList.addAll(list);
            adapter.setData(totalList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void requestFail(Throwable t) {
        Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
    }
}
