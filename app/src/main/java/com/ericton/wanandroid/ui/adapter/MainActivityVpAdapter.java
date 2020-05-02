package com.ericton.wanandroid.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * Created by Tang.
 * Date: 2020-04-23
 */
public class MainActivityVpAdapter extends FragmentStateAdapter {
    private Class<? extends Fragment>[] mFragments;
    public MainActivityVpAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public MainActivityVpAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public MainActivityVpAdapter(@NonNull FragmentManager fragmentManager,
                                 @NonNull Lifecycle lifecycle,Class<? extends Fragment>[] fragments) {
        super(fragmentManager, lifecycle);
        this.mFragments=fragments;
    }

    @Override
    public int getItemCount() {
        return mFragments.length;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        try {
            return (Fragment) mFragments[position].newInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
