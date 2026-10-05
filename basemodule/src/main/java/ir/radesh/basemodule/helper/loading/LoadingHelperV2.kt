package ir.radesh.basemodule.helper.loading

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.widget.TextView
import com.radesh.basemodule.R

object LoadingHelperV3 {

    private var dialog: Dialog? = null

    fun show(activity: Activity?, message: String = "لطفا کمی منتظر بمانید...") {
        if (activity == null || activity.isFinishing || activity.isDestroyed) return
        
        // اگر قبلاً در حال نمایش است، فقط متنش را آپدیت کن
        if (dialog?.isShowing == true) {
            dialog?.findViewById<TextView>(R.id.tvLoading)?.text = message
            return
        }

        try {
            dialog = Dialog(activity).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                val view = LayoutInflater.from(activity).inflate(R.layout.dialog_game_loading, null)
                view.findViewById<TextView>(R.id.tvLoading)?.text = message
                setContentView(view)

                window?.apply {
                    setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    // مات/تاریک کردن پس‌زمینه بازی پشت لودینگ
                    setDimAmount(0.65f)
                }

                setCancelable(true)
                setCanceledOnTouchOutside(false)
                show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hide(activity: Activity? = null) {
        try {
            if (dialog?.isShowing == true) {
                dialog?.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            dialog = null
        }
    }
}

fun Activity.showLoading(message: String = "لطفا کمی منتظر بمانید...") {
    LoadingHelperV3.show(this, message)
}

fun Activity.hideLoading() {
    LoadingHelperV3.hide(this)
}
