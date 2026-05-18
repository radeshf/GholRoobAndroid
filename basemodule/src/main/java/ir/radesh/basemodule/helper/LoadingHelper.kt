package ir.radesh.basemodule.helper

import android.app.Activity
import android.content.Context
import cn.pedant.SweetAlert.SweetAlertDialog
import com.radesh.basemodule.R
import ir.radesh.basemodule.commons.doOnTry


object LoadingHelper {

    private var dialog: SweetAlertDialog? = null

    fun baseShowLoading(context: Context){
        if (isDialogShowing()) return

        dialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
            .setTitleText(context.getString(R.string.loading))

        doOnTry({
            dialog?.show()
        },{
            (context as Activity).runOnUiThread {
                if (isDialogShowing()) return@runOnUiThread
                doOnTry({
                    dialog?.show()
                })
            }
        })

    }

    private fun isDialogShowing()
        = dialog != null && dialog?.isShowing!= null && dialog?.isShowing!!

    fun baseHideLoading(context: Context) {
        if (dialog == null) return
        doOnTry({
            dialog?.dismiss()
        },{
            (context as Activity).runOnUiThread {
                dialog?.dismiss()
            }
        })
    }

}