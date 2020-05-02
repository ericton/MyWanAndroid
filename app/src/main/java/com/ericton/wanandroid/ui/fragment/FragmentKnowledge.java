package com.ericton.wanandroid.ui.fragment;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ericton.wanandroid.R;
import com.ericton.wanandroid.base.BaseFragment;
import com.ericton.wanandroid.bean.hierarchy.KnowledgeHierarchyData;
import com.ericton.wanandroid.bean.main.collect.FeedArticleListData;
import com.ericton.wanandroid.ui.activity.KnowledgeActivity;
import com.ericton.wanandroid.ui.activity.WebActivity;
import com.ericton.wanandroid.ui.adapter.HomeArticleAdapter;
import com.ericton.wanandroid.ui.adapter.KnowledgeMainAdapter;
import com.ericton.wanandroid.vm.HomePageViewModel;
import com.ericton.wanandroid.vm.KnowledgeMainViewModel;
import com.hujiang.permissiondispatcher.NeedPermission;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

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
public class FragmentKnowledge extends BaseFragment {
    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_knowledge;
    }
    private KnowledgeMainViewModel mViewModel;
    private RecyclerView rvList;
    private KnowledgeMainAdapter mAdapter;
    private SmartRefreshLayout mRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v= super.onCreateView(inflater, container, savedInstanceState);
        rvList=v.findViewById(R.id.rv_knowledge_main);
        mRefreshLayout=v.findViewById(R.id.srl_knowledge_main);
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mViewModel.requestListData();
            }
        });
        return v;
    }

    @Override
    protected void initData() {
        super.initData();
        mViewModel = ViewModelProviders.of(this).get(KnowledgeMainViewModel.class);
        mViewModel.requestListData();
        mViewModel.getList().observe(getViewLifecycleOwner(), new Observer<List<KnowledgeHierarchyData>>() {
            @Override
            public void onChanged(final List<KnowledgeHierarchyData> listData) {
                mRefreshLayout.finishLoadMore();
                mRefreshLayout.finishRefresh();
                mRefreshLayout.setEnableLoadMore(false);
                if(mAdapter==null){
                    mAdapter=new KnowledgeMainAdapter(listData);
                    rvList.setAdapter(mAdapter);
                }else{
                    mAdapter.setNewData(listData);
                    mAdapter.notifyDataSetChanged();
                }
                mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        KnowledgeActivity.open(mContext, (ArrayList<KnowledgeHierarchyData>) listData.get(position).getChildren());
                    }
                });
//                mArticleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                    @Override
//                    @NeedPermission(permissions = {Manifest.permission.RECORD_AUDIO})
//                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                        WebActivity.open(mContext,feedArticleListData.getDatas().get(position).getLink());
//                    }
//                });

            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
