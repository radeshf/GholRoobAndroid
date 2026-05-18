package ir.radesh.basemodule.helper

import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat.getString
import androidx.fragment.app.Fragment
import com.radesh.basemodule.R
import mehdi.sakout.dynamicbox.DynamicBox


object DialogLoadingHelper {

    private lateinit var loadingBox: DynamicBox
    private lateinit var tvMessage: AppCompatTextView
    private lateinit var btnRetry: AppCompatImageButton
    private lateinit var progress: ProgressBar
    private lateinit var loadingMsg : String

    fun init(fragment: Fragment, view: View, tag: String, msg: String, doOnRetry: ()->Unit) {
        loadingBox = DynamicBox(view.context, view)

        val customView: View = fragment.layoutInflater.inflate(R.layout.loading, null, false)

        loadingBox.addCustomView(customView, tag)

        progress = customView.findViewById(R.id.p_bar)
        tvMessage = customView.findViewById(R.id.tv_msg)
        btnRetry = customView.findViewById(R.id.btn_retry)

        tvMessage.text = msg
        loadingMsg = msg

        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )

        customView.layoutParams = params

        btnRetry.setOnClickListener { view1: View? ->
            hideLoading()
            doOnRetry()
        }
    }

    fun showIn(fragment: Fragment, view: View, tag: String, msg: String, doOnRetry: ()->Unit) {
        init(fragment, view, tag, msg, doOnRetry)

        btnRetry.visibility = View.GONE
        progress.visibility = View.VISIBLE
        tvMessage.visibility = View.VISIBLE
        tvMessage.text = loadingMsg

        loadingBox.showCustomView(tag)
        btnRetry.setOnClickListener {
            hideLoading()
            doOnRetry()
        }

    }

    fun showNoData() {
        btnRetry.visibility = View.VISIBLE
        progress.visibility = View.GONE
        tvMessage.visibility = View.VISIBLE
        tvMessage.text = getString(tvMessage.context, R.string.no_items)
    }

    fun hideLoading() {
        loadingBox.hideAll()
    }


}