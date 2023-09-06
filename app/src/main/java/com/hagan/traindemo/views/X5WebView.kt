package com.hagan.traindemo.views

import android.app.AlertDialog
import android.content.Context
import android.util.AttributeSet
import com.hagan.traindemo.jsInterface.MyJavaScriptInterface
import com.tencent.smtt.export.external.interfaces.JsResult
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

class X5WebView : WebView {
    private var mContext: Context? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        init(context)
    }

    constructor(context: Context, attributeSet: AttributeSet?, i: Int) : super(
        context, attributeSet, i
    ) {
        init(context)
    }

    constructor(context: Context, attributeSet: AttributeSet?, i: Int, b: Boolean) : super(context, attributeSet, i, b) {
        init(context)
    }

    constructor(context: Context, attributeSet: AttributeSet?, i: Int, map: Map<String?, Any?>?, b: Boolean) : super(context, attributeSet, i, map, b) {
        init(context)
    }

    private fun init(context: Context) {
        mContext = context
        /**
         * 基础配置
         */
        initWebViewSettings()
        initWebViewClient()
        initChromeClient()
        /**
         * 构建 JSBridge 对象，这里提供的 JSBridge 字符串会被挂载到
         * 网页中的 window 对象下面。
         *
         * window.AndroidJSBridge
         */
        addJavascriptInterface(
            MyJavaScriptInterface(mContext!!, this), "AndroidJSBridge"
        )
    }

    /**
     * 对 webview 进行基础配置
     */
    private fun initWebViewSettings() {
        val webSettings = settings
        /**
         * 允许加载的网页执行 JavaScript 方法
         */
        webSettings.javaScriptEnabled = true
        /**
         * 设置网页不允许缩放
         */
        webSettings.setSupportZoom(false)
        webSettings.builtInZoomControls = false
        webSettings.displayZoomControls = true
        /**
         * 设置网页缓存方式为不缓存，方便我们的调试
         */
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
    }

    /**
     * 设置 webviewClient ，如果不进行这层设置，则网页打开默认会使用
     * 系统中的浏览器进行打开，而不是在本 APP 中进行打开。
     */
    private fun initWebViewClient() {
        webViewClient = object : WebViewClient() {}
    }

    /**
     * 监听网页中的url加载事件
     */
    private fun initChromeClient() {
        webChromeClient = object : WebChromeClient() {
            /**
             * alert()
             * 监听alert弹出框，使用原生弹框代替alert。
             */
            override fun onJsAlert(webView: WebView, s: String, s1: String, jsResult: JsResult): Boolean {
                val builder = AlertDialog.Builder(mContext)
                builder.setMessage(s1)
                builder.setNegativeButton("确定", null)
                builder.create().show()
                jsResult.confirm()
                return true
            }
        }
    }
}