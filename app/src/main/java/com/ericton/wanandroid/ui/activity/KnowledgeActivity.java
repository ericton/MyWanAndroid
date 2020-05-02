package com.ericton.wanandroid.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.ericton.wanandroid.R;
import com.ericton.wanandroid.base.BaseActivity;
import com.ericton.wanandroid.bean.hierarchy.KnowledgeHierarchyData;
import com.ericton.wanandroid.ui.adapter.KnowledgePagerAdapter;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

/**
 * Created by Tang.
 * Date: 2020-05-02
 */
public class KnowledgeActivity extends BaseActivity {
    SlidingTabLayout mTabLayout;
    ViewPager mViewPager;
    public static void open(Context context, ArrayList<KnowledgeHierarchyData> data){
        Intent intent=new Intent(context,KnowledgeActivity.class);
        intent.putParcelableArrayListExtra("data",data);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTabLayout=findViewById(R.id.tl_knowledge);
        mViewPager=findViewById(R.id.vp_activity_knowledge);
        ArrayList<KnowledgeHierarchyData> data=getIntent().getParcelableArrayListExtra("data");
        KnowledgePagerAdapter adapter=new KnowledgePagerAdapter(getSupportFragmentManager(),getLifecycle(),data);
        mViewPager.setAdapter(adapter);
        mTabLayout.setViewPager(mViewPager);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_knowledge;
    }
}
