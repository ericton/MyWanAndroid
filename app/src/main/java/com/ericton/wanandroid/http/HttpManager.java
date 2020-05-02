package com.ericton.wanandroid.http;

import android.text.TextUtils;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Tang.
 * Date: 2020-04-23
 */
public class HttpManager {
    public static RequestBody buildBody(JSONObject jsonObject) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        return body;
    }

    private static volatile HttpManager instance;


    private HttpManager() {

    }

    public static HttpManager getInstance() {
        if (instance == null) {
            synchronized (HttpManager.class) {
                if (instance == null) {
                    instance = new HttpManager();
                }
            }
        }

        return instance;
    }


    /**
     * 初始化OKHttpClient,设置缓存,设置超时时间,设置打印日志
     *
     * @return
     */
    public  <T>T getAPIService(String baseUrl,Class<T> clazz) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder mBuilder = new OkHttpClient.Builder()
//                                .cache(cache)
                .addInterceptor(interceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS);

        Retrofit retrofit;
        T service = null;
        if (!TextUtils.isEmpty(baseUrl)) {
            retrofit = new Retrofit.Builder()
                    .client(mBuilder.build())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(baseUrl).build();
            service = retrofit.create(clazz);
        }
        return service;
    }
}
