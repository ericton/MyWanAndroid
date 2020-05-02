package com.ericton.wanandroid.vm;

import com.ericton.wanandroid.bean.BaseResponse;
import com.ericton.wanandroid.bean.hierarchy.KnowledgeHierarchyData;
import com.ericton.wanandroid.bean.main.collect.FeedArticleListData;
import com.ericton.wanandroid.http.GeeksApis;
import com.ericton.wanandroid.http.HttpManager;
import com.ericton.wanandroid.utils.LogUtil;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Tang.
 * Date: 2020-05-02
 */
public class KnowledgeMainViewModel extends ViewModel {
    MutableLiveData<List<KnowledgeHierarchyData>> list;

    public MutableLiveData<List<KnowledgeHierarchyData>> getList() {
        if(list==null){
            list=new MutableLiveData<>();
        }
        return list;
    }

    public Disposable requestListData(){
        return HttpManager.getInstance().getAPIService(GeeksApis.HOST,GeeksApis.class).getKnowledgeHierarchyData()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse<List<KnowledgeHierarchyData>>>() {
                    @Override
                    public void accept(BaseResponse<List<KnowledgeHierarchyData>> data) throws Exception {
                            getList().setValue(data.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.i(" article error:"+throwable.toString());
                    }
                });
    }
}
