package ir.radesh.basemodule.helper

import android.app.Activity
import ir.radesh.basemodule.baseViews.ApiSubscriber
import ir.radesh.basemodule.commons.showToast
import timber.log.Timber

abstract class RadResponseHelper<T: RadBaseResponse>(private val apiSubscriber: ApiSubscriber)
    : ResponseHelper<T>() {

    override fun onResponseOk(response: T) {
        if (response.isOk()){
            onSuccessful(response)
        }else{
            onError(response.getMessage(), response.getCode())
        }
    }

    override fun onAuthFail() {
        apiSubscriber.logout()
    }

    override fun onError(msg: String, code: Int) {
        Timber.e("RadResponseHelper onError($code): $msg")
        apiSubscriber.showMsg(msg)
    }

    override fun onHideLoading() {
        apiSubscriber.hideRefresher()
    }

    override fun onShowLoading() {
        apiSubscriber.showRefresher()
    }


    abstract fun onSuccessful(response: T)


    override fun onShowMsg(s: String) {

    }

}