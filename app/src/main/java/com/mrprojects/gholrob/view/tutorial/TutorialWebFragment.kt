package com.mrprojects.gholrob.view.tutorial

import android.os.Bundle
import android.view.View
import com.mrprojects.gholrob.AppConfig
import com.mrprojects.gholrob.databinding.TutorialWebFragmentBinding
import com.mrprojects.gholrob.helper.initToolbar
import ir.radesh.basemodule.baseViews.BaseFragment
import ir.radesh.basemodule.helper.WebViewHelper

class TutorialWebFragment : BaseFragment<TutorialWebFragmentBinding>(TutorialWebFragmentBinding::inflate), WebViewHelper.Listener {
    lateinit var webViewHelper: WebViewHelper

    companion object {
        fun newInstance(): TutorialWebFragment {
            val frag = TutorialWebFragment()
            val b = Bundle()
            frag.arguments = b
            return frag
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webViewHelper = WebViewHelper(binding.webView, this)
        webViewHelper.execute(AppConfig.TUTORIAL_URL)
        getData()
        clicks()
        initToolbar(title = "راهنمای بازی")

    }

    private fun clicks() {

    }


    private fun getData() {

    }

    override fun onPageLoadStarted() {

    }

    override fun onPageLoadFinished() {

    }


}