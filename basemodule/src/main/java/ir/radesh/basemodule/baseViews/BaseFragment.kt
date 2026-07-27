package ir.radesh.basemodule.baseViews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ir.radesh.basemodule.commons.hideLoading
import ir.radesh.basemodule.commons.showLoading
import org.greenrobot.eventbus.EventBus

open abstract class  BaseFragment<B : ViewBinding>(private var bindingClass: (LayoutInflater, ViewGroup?, Boolean) -> B) : Fragment(), ApiSubscriber {

    private val disposables = CompositeDisposable()

    private var _binding: B? = null
    val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = bindingClass(inflater, container, false)
        return binding.root
    }


    override fun subscribe(disposable: Disposable) : Disposable{
        disposables.add(disposable)
        return disposable
    }

    override fun destroyApi() {
        disposables.clear()
    }

    override fun showRefresher() {
        showLoading()
    }

    override fun showNoData() {

    }

    override fun hideRefresher() {
        hideLoading()
    }

    override fun showMsg(s: String) {

    }

    override fun logout() {

    }


    fun subscribeWithLoading(disposable: Disposable) : Disposable{
        showLoading()
        disposables.add(disposable)
        return disposable
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear()
    }


    fun onBackPressed(){
        activity?.onBackPressedDispatcher?.onBackPressed()
    }

    fun postEvent(obj : Any){
        EventBus.getDefault().post(obj)
    }

}