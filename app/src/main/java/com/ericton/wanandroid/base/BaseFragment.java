package com.ericton.wanandroid.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ericton.wanandroid.utils.LogUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Tang.
 * Date: 2020-04-23
 */
public abstract class BaseFragment extends Fragment {
    protected Context mContext;
    protected String TAG=getClass().getSimpleName();
    protected abstract int getLayoutResource();
    private boolean hasData;
    protected CompositeDisposable mCompositeDisposable;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.i(TAG,"life:onCreateView");
        hasData=false;
        mCompositeDisposable=new CompositeDisposable();
        return inflater.inflate(getLayoutResource(),null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.i(TAG,"life:onCreate");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mCompositeDisposable!=null){
            mCompositeDisposable.clear();
            mCompositeDisposable=null;
        }
        LogUtil.i(TAG,"life:onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.i(TAG,"life:onDestroy");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.i(TAG,"life:onStop");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.i(TAG,"life:onResume");
        if(!hasData){
            initData();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.i(TAG,"life:onPause");
    }


    protected void initData(){
        LogUtil.i(TAG,"life:initData");
        hasData=true;
    }
    protected void addObserve(Disposable o){
        if(mCompositeDisposable!=null){
            mCompositeDisposable.add(o);
        }
    }
}
