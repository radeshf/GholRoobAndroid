package ir.radesh.basemodule.baseViews

import io.reactivex.disposables.Disposable

interface ApiSubscriber {
    fun subscribe(disposable: Disposable) : Disposable
    fun destroyApi()
    fun hideRefresher()
    fun showRefresher()
    fun showNoData()

    fun showMsg(s: String)
    fun logout()
}