package ir.radesh.basemodule.helper

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.radesh.basemodule.R
import com.radesh.basemodule.databinding.DialogBinding
import ir.radesh.basemodule.commons.basicConfig
import ir.radesh.basemodule.commons.doOnTry
import ir.radesh.basemodule.commons.visibleByBoolean

class DialogHelper(
    val context: Context,
    val inflater: LayoutInflater,
    val title: String ="هشدار",
    private val msg: String ="پیغام خالی",
    private val icon: Int = R.drawable.ic_warning,
    private val showCancel: Boolean = false,
    private val confirmButtonText: String = context.getString(R.string.ok),
    private val cancelButtonText: String = context.getString(R.string.back),
    private val warningText: String? = null,
    private val cancellable: Boolean = true,
    val doOnConfirm: (dialog: Dialog)-> Unit = { it.dismiss() },
    val doOnCancel: (dialog: Dialog)-> Unit = { it.dismiss() }
)

{
    lateinit var dialog: Dialog

    fun show(){
        dialog = Dialog(context)
        val binding = DialogBinding.inflate(inflater)
        dialog.basicConfig( binding.root, cancellable)
        binding.btnDismissDialog.visibleByBoolean(showCancel)
        binding.tvWarningDialog.visibleByBoolean(warningText != null)
        binding.tvWarningDialog.text = warningText
        binding.tvTitleDialog.text = title
        binding.tvMessageDialog.text = msg
        binding.btnConfirmDialog.text = confirmButtonText
        binding.btnDismissDialog.text = cancelButtonText
        binding.ivIconDialog.setImageResource(icon)
        binding.btnConfirmDialog.setOnClickListener {
            doOnConfirm(dialog)
            dialog.cancel()
        }
        binding.btnDismissDialog.setOnClickListener {
            doOnCancel(dialog)
            dialog.cancel()
        }
        doOnTry({
            dialog.show()
        })
    }

    fun cancel(){
        dialog.dismiss()
    }
}