package com.orange.tpms.ue.kt_frag


import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.*
import com.orange.jzchi.jzframework.JzActivity
import com.orange.tpms.R
import com.orange.tpms.RootFragement
import com.orango.electronic.orange_og_lib.Util.fixLanguage
import kotlinx.android.synthetic.main.fragment_frag__web_view.view.*


class Frag_WebView_Link(var url:String) : RootFragement(R.layout.fragment_frag__web_view) {
    @SuppressLint("SetJavaScriptEnabled")
    override fun viewInit() {
        try{
            rootview.webview.clearCache(true)
            rootview.webview.settings.setAllowUniversalAccessFromFileURLs(true)
            rootview.webview.settings.javaScriptEnabled = true
            rootview.webview.loadUrl(url)
            rootview.webview.settings.pluginState = WebSettings.PluginState.ON;
            rootview.webview.settings.pluginState = WebSettings.PluginState.ON_DEMAND;
            rootview.webview.settings.setJavaScriptCanOpenWindowsAutomatically(true)
            rootview.webview.settings.setSupportMultipleWindows(true)
            rootview.webview.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    url: String
                ): Boolean {
                    Log.e("OverrideUrlLoading", url)

                    return false
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    rootview.webview.evaluateJavascript(
                        "JzFragment.viewInit()",
                        null
                    )
                    super.onPageFinished(view, url)
                }

                override fun shouldInterceptRequest(
                    view: WebView,
                    request: WebResourceRequest
                ): WebResourceResponse? {
                    Log.e("shouldInterceptRequest", "" + request.url)
                    return super.shouldInterceptRequest(view, request)
                }
            }
        }catch (e:Exception){e.printStackTrace()}
    }





}
