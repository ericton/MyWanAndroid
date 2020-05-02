package com.ericton.wanandroid.ui.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.ericton.wanandroid.R;
import com.ericton.wanandroid.base.BaseActivity;
import com.ericton.wanandroid.ui.adapter.MainActivityVpAdapter;
import com.ericton.wanandroid.ui.fragment.FragmentHome;
import com.ericton.wanandroid.ui.fragment.FragmentKnowledge;
import com.ericton.wanandroid.ui.fragment.FragmentNav;
import com.ericton.wanandroid.ui.fragment.FragmentProject;
import com.ericton.wanandroid.ui.fragment.FragmentWxArticle;
import com.ericton.wanandroid.utils.UIUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends BaseActivity {
    private BottomNavigationView mBottomNavigationView;
    private Class<? extends Fragment>[] fragmentList = new Class[]{FragmentHome.class,
            FragmentKnowledge.class, FragmentWxArticle.class, FragmentNav.class, FragmentProject.class};
    private ViewPager2 mViewPager2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewPager2=findViewById(R.id.vp_activity_main);
        mViewPager2.setAdapter(new MainActivityVpAdapter(getSupportFragmentManager(),getLifecycle(),fragmentList));
        mViewPager2.setOffscreenPageLimit(fragmentList.length-1);
        mViewPager2.setUserInputEnabled(false);
        mBottomNavigationView = findViewById(R.id.bnv_main_activity);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int index;
                switch (menuItem.getItemId()) {
                    case R.id.tab_main_pager:
                        index = 0;
                        break;
                    case R.id.tab_knowledge_hierarchy:
                        index = 1;
                        break;
                    case R.id.tab_wx_article:
                        index = 2;
                        break;
                    case R.id.tab_navigation:
                        index = 3;
                        break;
                    case R.id.tab_project:
                        index = 4;
                        break;
                    default:
                        index = 0;
                        break;
                }
                mViewPager2.setCurrentItem(index,false);
                return true;
            }
        });
    }
    @Override
    protected int getLayoutResource() {
        return R.layout.main_activity;
    }
}
