package ir.radesh.basemodule.baseViews


import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseBottomSheetFragment : BottomSheetDialogFragment() {

    private val disposables = CompositeDisposable()

    fun subcribe(disposable: Disposable) : Disposable{
        disposables.add(disposable)
        return disposable
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }


}