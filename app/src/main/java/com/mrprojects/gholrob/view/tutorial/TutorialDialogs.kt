package com.mrprojects.gholrob.view.tutorial

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.viewbinding.ViewBinding
import com.mrprojects.gholrob.R
import com.mrprojects.gholrob.databinding.TutorialDialogBinding
import com.mrprojects.gholrob.helper.doOnTry
import com.mrprojects.gholrob.helper.setTutorialText
import ir.radesh.basemodule.baseViews.BaseFragment
import ir.radesh.basemodule.commons.basicConfig


@SuppressLint("SetTextI18n")
fun <B : ViewBinding> BaseFragment<B>.showTutorialDialog() {
    val binding = TutorialDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)
    binding.tvMessageDialog.setTutorialText(true)
    binding.lnrDismiss.root.setOnClickListener {
        dialog.dismiss()
    }
    doOnTry({
        dialog.show()
    })
}

