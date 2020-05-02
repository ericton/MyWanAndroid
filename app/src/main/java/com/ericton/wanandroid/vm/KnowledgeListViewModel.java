package com.ericton.wanandroid.vm;

import com.ericton.wanandroid.bean.BaseResponse;
import com.ericton.wanandroid.bean.hierarchy.KnowledgeHierarchyData;
import com.ericton.wanandroid.bean.main.banner.BannerData;
import com.ericton.wanandroid.bean.main.collect.FeedArticleListData;
import com.ericton.wanandroid.http.GeeksApis;
import com.ericton.wanandroid.http.HttpManager;
import com.ericton.wanandroid.utils.LogUtil;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Tang.
 * Date: 2020-04-23
 */
public class KnowledgeListViewModel extends ViewModel{
    private String TAG = getClass().getSimpleName();



    MutableLiveData<FeedArticleListData> mData;

    public MutableLiveData<FeedArticleListData> getData() {
        if (mData == null) {
            mData = new MutableLiveData<>();
        }
        return mData;
    }

    public Disposable requestListData(final int num,final int id){
        return HttpManager.getInstance().getAPIService(GeeksApis.HOST,GeeksApis.class).getKnowledgeHierarchyDetailData(num,id)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse<FeedArticleListData>>() {
                    @Override
                    public void accept(BaseResponse<FeedArticleListData> feedArticleListDataBaseResponse) throws Exception {
                        if(num==0) {
                            getData().setValue(feedArticleListDataBaseResponse.getData());
                        }else{
                            feedArticleListDataBaseResponse.getData().getDatas().addAll(0,getData().getValue().getDatas());
                            getData().setValue(feedArticleListDataBaseResponse.getData());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.i(" article error:"+throwable.toString());
                    }
                });
    }
}
