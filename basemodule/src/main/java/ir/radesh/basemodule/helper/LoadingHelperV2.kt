package ir.radesh.basemodule.helper

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.radesh.basemodule.R

object LoadingHelperV2 {

    private var loadingView: View? = null

    fun show(activity: Activity, message: String = "لطفا کمی منتظر بمانید...") {

        if (loadingView != null) return

        val root = activity.findViewById<ViewGroup>(android.R.id.content)

        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_game_loading, root, false)

        view.findViewById<TextView>(R.id.tvLoading).text = message

        root.addView(view)

        loadingView = view
    }

    fun hide(activity: Activity) {

        loadingView?.let {
            val root = activity.findViewById<ViewGroup>(android.R.id.content)
            root.removeView(it)
        }

        loadingView = null
    }

}