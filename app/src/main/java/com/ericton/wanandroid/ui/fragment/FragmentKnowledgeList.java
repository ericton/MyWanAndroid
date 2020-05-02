package com.ericton.wanandroid.ui.fragment;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ericton.wanandroid.R;
import com.ericton.wanandroid.base.BaseFragment;
import com.ericton.wanandroid.bean.hierarchy.KnowledgeHierarchyData;
import com.ericton.wanandroid.bean.main.banner.BannerData;
import com.ericton.wanandroid.bean.main.collect.FeedArticleListData;
import com.ericton.wanandroid.ui.activity.WebActivity;
import com.ericton.wanandroid.ui.adapter.HomeArticleAdapter;
import com.ericton.wanandroid.utils.SizeUtils;
import com.ericton.wanandroid.vm.HomePageViewModel;
import com.ericton.wanandroid.vm.KnowledgeListViewModel;
import com.hujiang.permissiondispatcher.NeedPermission;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Tang.
 * Date: 2020-05-02
 */
public class FragmentKnowledgeList extends BaseFragment {
    public static FragmentKnowledgeList newInstance(int id){
        FragmentKnowledgeList fragment=new FragmentKnowledgeList();
        Bundle bundle=new Bundle();
        bundle.putInt("id",id);
        fragment.setArguments(bundle);
        return fragment;
    }
    int mId;
    SmartRefreshLayout mSmartRefreshLayout;
    RecyclerView mRecyclerView;
    KnowledgeListViewModel mViewModel;
    HomeArticleAdapter mAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mId=getArguments().getInt("id");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSmartRefreshLayout=view.findViewById(R.id.srl_knowledge_list);
        mRecyclerView= view.findViewById(R.id.rv_knowledge_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mSmartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mViewModel.requestListData(mViewModel.getData().getValue().getCurPage(),mId);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mViewModel.requestListData(0,mId);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mViewModel= ViewModelProviders.of(this).get(KnowledgeListViewModel.class);
        mViewModel.requestListData(0,mId);
        mViewModel.getData().observe(getViewLifecycleOwner(), new Observer<FeedArticleListData>() {
            @Override
            public void onChanged(final FeedArticleListData feedArticleListData) {
                mSmartRefreshLayout.finishLoadMore();
                mSmartRefreshLayout.finishRefresh();
                if(feedArticleListData.getCurPage()==feedArticleListData.getPageCount()){
                    mSmartRefreshLayout.setEnableLoadMore(false);
                    mSmartRefreshLayout.setNoMoreData(true);
                }else{
                    mSmartRefreshLayout.setEnableLoadMore(true);
                    mSmartRefreshLayout.setEnableAutoLoadMore(true);
                }
                if(mAdapter==null){
                    mAdapter=new HomeArticleAdapter(feedArticleListData.getDatas());
                    mRecyclerView.setAdapter(mAdapter);
                }else{
                    mAdapter.setNewData(feedArticleListData.getDatas());
                    mAdapter.notifyDataSetChanged();
                }
                mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
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
    protected int getLayoutResource() {
        return R.layout.fragment_knowledge_list;
    }
}
