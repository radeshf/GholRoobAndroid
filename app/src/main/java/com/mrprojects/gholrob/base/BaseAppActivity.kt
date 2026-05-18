/*
 * Copyright (c) WhatsApp Inc. and its affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.mrprojects.gholrob.base;

//import com.google.firebase.analytics.FirebaseAnalytics
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.mrprojects.gholrob.helper.warningDialog
import ir.radesh.basemodule.baseViews.ApiSubscriber
import ir.radesh.basemodule.baseViews.BaseActivity
import ir.radesh.basemodule.commons.hideLoading
import ir.radesh.basemodule.commons.showLoading
import org.greenrobot.eventbus.EventBus

abstract class BaseAppActivity<B : ViewBinding>(bindingFactory: (LayoutInflater) -> B) : BaseActivity<B>(bindingFactory), ApiSubscriber {

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }



    override fun showRefresher() {
        showLoading()
    }

    override fun showNoData() {

    }

    override fun hideRefresher() {
        hideLoading()
    }


    override fun logout() {

    }

    override fun showMsg(s: String) {
        warningDialog(s)
    }



    fun postEvent(obj : Any){
        EventBus.getDefault().post(obj)
    }
}