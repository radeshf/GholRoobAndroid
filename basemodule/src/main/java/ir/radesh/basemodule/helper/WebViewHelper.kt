package ir.radesh.basemodule.helper

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import timber.log.Timber

class WebViewHelper(private val webView: WebView,val listener: Listener?=null) {

    @SuppressLint("SetJavaScriptEnabled")
    fun execute(url: String, headers: HashMap<String, String>? = null){
        webView.clearCache(true)
        webView.clearHistory()
        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.builtInZoomControls = false
        webView.isHorizontalScrollBarEnabled = false
        webView.settings.loadWithOverviewMode = true
        webView.settings.domStorageEnabled = true
        webView.settings.pluginState = WebSettings.PluginState.ON
        webView.webViewClient = RWebViewClient()
        if (headers!=null){
            webView.loadUrl(url, headers)
        }else{
            webView.loadUrl(url)
        }
    }

    inner class RWebViewClient: WebViewClient(){
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view?.loadUrl(url.toString())
            Timber.e("load url -> $url")
            return true
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            listener?.onPageLoadStarted()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            listener?.onPageLoadFinished()
        }
    }

    interface Listener{
        fun onPageLoadStarted()
        fun onPageLoadFinished()
    }
}