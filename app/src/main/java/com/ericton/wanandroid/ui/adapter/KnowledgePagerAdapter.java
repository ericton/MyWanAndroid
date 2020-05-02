package com.ericton.wanandroid.ui.adapter;

import com.ericton.wanandroid.bean.hierarchy.KnowledgeHierarchyData;
import com.ericton.wanandroid.ui.fragment.FragmentKnowledgeList;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * Created by Tang.
 * Date: 2020-05-02
 */
public class KnowledgePagerAdapter  extends FragmentStatePagerAdapter {
    private List<KnowledgeHierarchyData> listData;



    public KnowledgePagerAdapter(@NonNull FragmentManager fragmentManager,
                                 @NonNull Lifecycle lifecycle, List<KnowledgeHierarchyData> list) {
        super(fragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.listData = list;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return FragmentKnowledgeList.newInstance(listData.get(position).getId());
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return listData.get(position).getName();
    }
}
