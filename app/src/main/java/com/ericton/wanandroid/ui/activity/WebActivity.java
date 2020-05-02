package com.ericton.wanandroid.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.print.PrinterId;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.ericton.wanandroid.R;
import com.ericton.wanandroid.base.BaseActivity;
import com.ericton.wanandroid.utils.LogUtil;
import com.ericton.wanandroid.utils.UIUtils;
import com.google.gson.Gson;
import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.AgentWebView;
import com.just.agentweb.DefaultChromeClient;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.PermissionInterceptor;
import com.just.agentweb.WebChromeClientDelegate;
import com.just.agentweb.WebListenerManager;
import com.just.agentweb.WebViewClientDelegate;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;

/**
 * Created by Tang.
 * Date: 2020-04-25
 */
public class WebActivity extends BaseActivity {
    public static void open(Context context,String url){
        Intent intent=new Intent(context,WebActivity.class);
        intent.putExtra("url",url);
        context.startActivity(intent);
    }
    private String mUrl;
    private AgentWeb mAgentWeb;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIUtils.setSteep(this,true);
        mUrl=getIntent().getStringExtra("url");
        ViewGroup vgRoot=findViewById(R.id.vg_web_root);
        WebView webView=new WebView(this);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mAgentWeb=AgentWeb.with(this).setAgentWebParent(vgRoot,layoutParams)
                .useDefaultIndicator(-1, 3)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                .setAgentWebWebSettings(getSettings())//设置 IAgentWebSettings。
//                .setWebViewClient(mWebViewClient)//WebViewClient ， 与 WebView 使用一致 ，但是请勿获取WebView调用setWebViewClient(xx)方法了,会覆盖AgentWeb DefaultWebClient,同时相应的中间件也会失效。
                .setWebChromeClient(new MyChromeClient()) //WebChromeClient
                .setPermissionInterceptor(mPermissionInterceptor) //权限拦截 2.0.0 加入。
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK) //严格模式 Android 4.2.2 以下会放弃注入对象 ，使用AgentWebView没影响。
//                .setAgentWebUIController(new UIController(getActivity())) //自定义UI  AgentWeb3.0.0 加入。
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1) //参数1是错误显示的布局，参数2点击刷新控件ID -1表示点击整个布局都刷新， AgentWeb 3.0.0 加入。
//                .useMiddlewareWebChrome(getMiddlewareWebChrome()) //设置WebChromeClient中间件，支持多个WebChromeClient，AgentWeb 3.0.0 加入。
//                .additionalHttpHeader(getUrl(), "cookie", "41bc7ddf04a26b91803f6b11817a5a1c")
//                .useMiddlewareWebClient(getMiddlewareWebClient()) //设置WebViewClient中间件，支持多个WebViewClient， AgentWeb 3.0.0 加入。
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他页面时，弹窗质询用户前往其他应用 AgentWeb 3.0.0 加入。
                .interceptUnkownUrl() //拦截找不到相关页面的Url AgentWeb 3.0.0 加入。
                .createAgentWeb()//创建AgentWeb。
                .ready()//设置 WebSettings。
                .go(mUrl); //WebView载入该url地址的页面并显示。
    }
    private IAgentWebSettings getSettings(){
        IAgentWebSettings agentWebSettings=new AbsAgentWebSettings() {
            @Override
            protected void bindAgentWebSupport(AgentWeb agentWeb) {

            }

            @Override
            public WebSettings getWebSettings() {
                WebSettings settings=super.getWebSettings();
                settings.setUseWideViewPort(true);
                settings.setLoadWithOverviewMode(true);
                settings.setDomStorageEnabled(true);
                settings.setDefaultTextEncodingName("UTF-8");
                settings.setAllowContentAccess(true); // 是否可访问Content Provider的资源，默认值 true
                settings.setAllowFileAccess(true);    // 是否可访问本地文件，默认值 true
                // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
                settings.setAllowFileAccessFromFileURLs(false);
                // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
                settings.setAllowUniversalAccessFromFileURLs(false);
                //开启JavaScript支持
                settings.setJavaScriptEnabled(true);
                // 支持缩放
                settings.setSupportZoom(true);
                return settings;
            }
        };

        return agentWebSettings;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_web;
    }

    protected PermissionInterceptor mPermissionInterceptor = new PermissionInterceptor() {

        /**
         * PermissionInterceptor 能达到 url1 允许授权， url2 拒绝授权的效果。
         * @param url
         * @param permissions
         * @param action
         * @return true 该Url对应页面请求权限进行拦截 ，false 表示不拦截。
         */
        @Override
        public boolean intercept(String url, String[] permissions, String action) {
            LogUtil.i(TAG, "mUrl:" + url + "  permission:" + new Gson().toJson(permissions) + " action:" + action);
            return false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mAgentWeb.handleKeyEvent(keyCode, event) || super.onKeyDown(keyCode, event);
    }
    class  MyChromeClient extends WebChromeClient{
        @Override
        public void onPermissionRequest(PermissionRequest request) {
            request.grant(request.getResources());
            request.getOrigin();
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if(tvTitle!=null){
                tvTitle.setText(title);
            }
        }
    }

    @Override
    protected void onSelfBack() {
        if(!mAgentWeb.back()){
            finish();
        }
    }
}
