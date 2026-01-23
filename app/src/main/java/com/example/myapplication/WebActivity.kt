package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.fragment.app.FragmentActivity


class WebActivity : FragmentActivity() {
    private var mCustomView: View? = null //用于全屏渲染视频的View

    private var mCustomViewCallback: WebChromeClient.CustomViewCallback? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        );
        setContentView(R.layout.activity_web)
        val webView = findViewById<WebView>(R.id.webview)
        webView.settings.apply {
            //Android11 webView直接访问文件需要设置为true
            this.allowFileAccess = true
            //设置是否开启DOM存储API权限，默认false，未开启，设置为true，WebView能够使用DOM storage API
            this.domStorageEnabled = true
            // 设置WebView是否使用viewport，当该属性被设置为false时，加载页面的宽度总是适应WebView控件宽度；当被设置为true，当前页面包含viewport属性标签，在标签中指定宽度值生效，如果页面不包含viewport标签，无法提供一个宽度值，这个时候该方法将被使用
            this.useWideViewPort = true
            //设置WebView是否使用预览模式加载界面。
            this.loadWithOverviewMode = true
            // 设置WebView是否允许执行JavaScript脚本，默认false，不允许
            this.javaScriptEnabled = true
            // 保存密码
            this.savePassword = false
            //设置WebView是否支持使用屏幕控件或手势进行缩放，默认是true，支持缩放
            this.setSupportZoom(true)
            //设置WebView是否使用其内置的变焦机制，该机制集合屏幕缩放控件使用，默认是false，不使用内置变焦机制。
            this.builtInZoomControls = true
            //设置WebView使用内置缩放机制时，是否展现在屏幕缩放控件上，默认true，展现在控件上
            this.displayZoomControls = false

            //设置WebView底层的布局算法，参考LayoutAlgorithm#NARROW_COLUMNS，将会重新生成WebView布局
            this.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS

            // Google issue 4641
            val defaultUserAgent: String = userAgentString

            //设置WebView代理字符串，如果String为null或为空，将使用系统默认值
            this.userAgentString = "$defaultUserAgent changan-sda-base"  //添加了特殊标记 服务端会区分来源

            //设置当一个安全站点企图加载来自一个不安全站点资源时WebView的行为
            this.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

            this.allowUniversalAccessFromFileURLs = true

            this.setSupportMultipleWindows(true)

        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                super.onShowCustomView(view, callback)
                if (mCustomViewCallback != null) {
                    mCustomViewCallback?.onCustomViewHidden()
                    mCustomViewCallback = null
                    return
                }
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
                val parent = webView.getParent().getParent() as ViewGroup
                parent.visibility = View.GONE
                (parent.parent as ViewGroup).addView(
                    view,
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )
                mCustomView = view
                mCustomViewCallback = mCustomViewCallback
            }

            override fun onHideCustomView() {
                super.onHideCustomView()
                if (mCustomView != null) {
                    if (mCustomViewCallback != null) {
                        mCustomViewCallback!!.onCustomViewHidden()
                        mCustomViewCallback = null
                    }
                    window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                    if (mCustomView != null && mCustomView!!.parent != null) {
                        val parent = mCustomView!!.parent as ViewGroup
                        parent.removeView(mCustomView)
                        if (webView.getParent().getParent() != null) {
                            (webView.getParent().getParent() as View).visibility = View.VISIBLE
                        }
                    }
                    mCustomView = null
                }
            }
        }
        webView.loadUrl("https://m.huanqiu.com/article/4HXqahuRGuL")

    }


}