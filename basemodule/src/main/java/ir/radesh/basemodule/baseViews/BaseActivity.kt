package ir.radesh.basemodule.baseViews

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ir.radesh.basemodule.commons.hideLoading
import ir.radesh.basemodule.commons.showLoading
import ir.radesh.basemodule.commons.showToast
import org.greenrobot.eventbus.EventBus

abstract class BaseActivity<B : ViewBinding>(val bindingFactory: (LayoutInflater) -> B) : AppCompatActivity(), ApiSubscriber {
    lateinit var binding: B
    private val disposables = CompositeDisposable()

    override fun subscribe(disposable: Disposable) : Disposable{
        disposables.add(disposable)
        return disposable
    }

    override fun destroyApi() {
        disposables.clear()
    }


    override fun showRefresher() {
//        showLoading()
    }

    override fun showNoData() {

    }

    override fun hideRefresher() {
//        hideLoading()
    }


    override fun logout() {

    }

    override fun showMsg(s: String) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingFactory(layoutInflater)
        setContentView(binding.root)
    }

    override fun attachBaseContext(newBase: Context?) {
        if (newBase == null){
            super.attachBaseContext(newBase)
        }else{
            super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    fun postEvent(obj : Any){
        EventBus.getDefault().post(obj)
    }
}