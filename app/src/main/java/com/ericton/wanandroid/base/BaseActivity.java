package com.ericton.wanandroid.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ericton.wanandroid.R;
import com.ericton.wanandroid.inject.AutoClick;
import com.ericton.wanandroid.inject.AutoClickImp;
import com.ericton.wanandroid.utils.UIUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Tang.
 * Date: 2020-04-06
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG=getClass().getSimpleName();
    protected View vBack;
    protected TextView tvTitle;
    protected Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIUtils.setSteep(this,true);
        mContext=this;
        setContentView(getLayoutResource());
        AutoClickImp.inject(this);
        vBack=findViewById(R.id.iv_title_back);
        tvTitle=findViewById(R.id.tv_title_text);
    }
    protected abstract int getLayoutResource();

    @AutoClick(id = {R.id.iv_title_back})
    protected void onSelfBack(){
        finish();
    }
    protected TextView getTvTitle(){
        if(tvTitle==null){
            tvTitle=findViewById(R.id.tv_title_text);
        }
        return tvTitle;
    }
}
