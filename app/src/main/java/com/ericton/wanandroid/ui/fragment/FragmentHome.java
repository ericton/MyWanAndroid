package com.ericton.wanandroid.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ericton.wanandroid.R;
import com.ericton.wanandroid.base.BaseFragment;
import com.ericton.wanandroid.bean.main.banner.BannerData;
import com.ericton.wanandroid.bean.main.collect.FeedArticleListData;
import com.ericton.wanandroid.ui.activity.WebActivity;
import com.ericton.wanandroid.ui.adapter.HomeArticleAdapter;
import com.ericton.wanandroid.utils.SizeUtils;
import com.ericton.wanandroid.vm.HomePageViewModel;
import com.hujiang.permissiondispatcher.NeedPermission;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Tang.
 * Date: 2020-04-23
 */
public class FragmentHome extends BaseFragment {
    private HomePageViewModel mHomePageViewModel;
    private Banner mBanner;
    private View vBannerRoot;
    private RecyclerView rvList;
    private HomeArticleAdapter mArticleAdapter;
    private SmartRefreshLayout mRefreshLayout;
    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v= super.onCreateView(inflater, container, savedInstanceState);
        vBannerRoot=inflater.inflate(R.layout.layout_banner,null);
        mBanner=vBannerRoot.findViewById(R.id.banner_home);
        rvList=v.findViewById(R.id.rv_home_article_list);
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        mRefreshLayout=v.findViewById(R.id.srl_home);
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mHomePageViewModel.requestListData(mHomePageViewModel.getArticleListDataMutableLiveData().getValue().getCurPage());
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mHomePageViewModel.requestListData(0);
            }
        });
        return v;
    }

    @Override
    protected void initData() {
        super.initData();
        mHomePageViewModel= ViewModelProviders.of(this).get(HomePageViewModel.class);
        mHomePageViewModel.requestBanner();
        mHomePageViewModel.getListBannerData().observe(getViewLifecycleOwner(), new Observer<List<BannerData>>() {
            @Override
            public void onChanged(List<BannerData> bannerData) {
                showBanner(bannerData);
            }
        });
        mHomePageViewModel.getArticleListDataMutableLiveData().observe(getViewLifecycleOwner(), new Observer<FeedArticleListData>() {
            @Override
            public void onChanged(final FeedArticleListData feedArticleListData) {
                mRefreshLayout.finishLoadMore();
                mRefreshLayout.finishRefresh();
                if(feedArticleListData.getCurPage()==feedArticleListData.getPageCount()){
                    mRefreshLayout.setEnableLoadMore(false);
                    mRefreshLayout.setNoMoreData(true);
                }else{
                    mRefreshLayout.setEnableLoadMore(true);
                    mRefreshLayout.setEnableAutoLoadMore(true);
                }
                if(mArticleAdapter==null){
                    mArticleAdapter=new HomeArticleAdapter(feedArticleListData.getDatas());
                    mArticleAdapter.addHeaderView(vBannerRoot);
                    ViewGroup.LayoutParams layoutParams=mBanner.getLayoutParams();
                    layoutParams.width= ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height= SizeUtils.Dp2Px(mContext,200);
                    rvList.setAdapter(mArticleAdapter);
                }else{
                    mArticleAdapter.setNewData(feedArticleListData.getDatas());
                    mArticleAdapter.notifyDataSetChanged();
                }
                mArticleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    @NeedPermission(permissions = {Manifest.permission.RECORD_AUDIO})
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        WebActivity.open(mContext,feedArticleListData.getDatas().get(position).getLink());
                    }
                });

            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        if(mBanner!=null){
            mBanner.stopAutoPlay();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mBanner!=null){
            mBanner.startAutoPlay();
        }
    }

    private void showBanner(final List<BannerData> list){
        List<String> listTitle=new ArrayList<>();
        final List<String> listImageUrl=new ArrayList<>();
        for(BannerData data:list){
            listTitle.add(data.getTitle());
            listImageUrl.add(data.getImagePath());
        }
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                WebActivity.open(mContext,list.get(position).getUrl());
            }
        });
        mBanner.setBannerTitles(listTitle);
        mBanner.setImages(listImageUrl);
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
        //设置图片加载器
        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(mContext).load(path).into(imageView);
            }
        });
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.DepthPage);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);

        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
    }
}
