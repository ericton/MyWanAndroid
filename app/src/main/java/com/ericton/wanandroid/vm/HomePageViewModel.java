package com.ericton.wanandroid.vm;

import android.app.Application;

import com.ericton.wanandroid.bean.BaseResponse;
import com.ericton.wanandroid.bean.main.banner.BannerData;
import com.ericton.wanandroid.bean.main.collect.FeedArticleListData;
import com.ericton.wanandroid.http.GeeksApis;
import com.ericton.wanandroid.http.HttpManager;
import com.ericton.wanandroid.utils.LogUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
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
public class HomePageViewModel extends ViewModel{
    private String TAG = getClass().getSimpleName();
    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCleared() {
        super.onCleared();
        mCompositeDisposable.clear();
        mCompositeDisposable = null;
    }

    public HomePageViewModel() {
        super();
        mCompositeDisposable = new CompositeDisposable();


    }

    MutableLiveData<List<BannerData>> listBannerData;
    MutableLiveData<FeedArticleListData> mArticleListDataMutableLiveData;

    public MutableLiveData<List<BannerData>> getListBannerData() {
        if (listBannerData == null) {
            listBannerData = new MutableLiveData<>();
        }
        return listBannerData;
    }

    public MutableLiveData<FeedArticleListData> getArticleListDataMutableLiveData() {
        if (mArticleListDataMutableLiveData == null) {
            mArticleListDataMutableLiveData = new MutableLiveData<>();
        }
        return mArticleListDataMutableLiveData;
    }
    public void requestBanner(){
        mCompositeDisposable.add(HttpManager.getInstance().getAPIService(GeeksApis.HOST,
                GeeksApis.class).getBannerData()
                .observeOn(Schedulers.io())
                .flatMap(new Function<BaseResponse<List<BannerData>>, ObservableSource<BaseResponse<FeedArticleListData>>>() {
                    @Override
                    public ObservableSource<BaseResponse<FeedArticleListData>> apply(BaseResponse<List<BannerData>> listBaseResponse) throws Exception {
                        getListBannerData().postValue(listBaseResponse.getData());
                        return HttpManager.getInstance().getAPIService(GeeksApis.HOST,GeeksApis.class).getFeedArticleList(0);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<BaseResponse<FeedArticleListData>>() {
                    @Override
                    public void accept(BaseResponse<FeedArticleListData> listBaseResponse) throws Exception {
                        getArticleListDataMutableLiveData().setValue(listBaseResponse.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.i(TAG,throwable.toString());
                    }
                }));
    }
    public Disposable requestListData(final int num){
        return HttpManager.getInstance().getAPIService(GeeksApis.HOST,GeeksApis.class).getFeedArticleList(num)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse<FeedArticleListData>>() {
                    @Override
                    public void accept(BaseResponse<FeedArticleListData> feedArticleListDataBaseResponse) throws Exception {
                        if(num==0) {
                            getArticleListDataMutableLiveData().setValue(feedArticleListDataBaseResponse.getData());
                        }else{
                            feedArticleListDataBaseResponse.getData().getDatas().addAll(0,getArticleListDataMutableLiveData().getValue().getDatas());
                            getArticleListDataMutableLiveData().setValue(feedArticleListDataBaseResponse.getData());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.i(TAG," article error:"+throwable.toString());
                    }
                });
    }
}
