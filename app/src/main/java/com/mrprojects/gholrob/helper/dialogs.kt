package com.mrprojects.gholrob.helper

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.view.LayoutInflater
import android.view.animation.DecelerateInterpolator
import androidx.viewbinding.ViewBinding
import com.mrprojects.gholrob.AppConfig
import com.mrprojects.gholrob.R
import com.mrprojects.gholrob.databinding.CloseGameDialogBinding
import com.mrprojects.gholrob.databinding.ClosePlayDialogBinding
import com.mrprojects.gholrob.databinding.CoinNotEnoughDialogBinding
import com.mrprojects.gholrob.databinding.DialogAppUpdateBinding
import com.mrprojects.gholrob.databinding.DialogSuccessBinding
import com.mrprojects.gholrob.databinding.EmptyCellInfoDialogBinding
import com.mrprojects.gholrob.databinding.EnemyInfoDialogBinding
import com.mrprojects.gholrob.databinding.NoInternetDialogBinding
import com.mrprojects.gholrob.databinding.TutorialDoneDialogBinding
import com.mrprojects.gholrob.databinding.UseLifeDialogBinding
import com.mrprojects.gholrob.databinding.WarningDialogBinding
import com.mrprojects.gholrob.model.CellTypes
import com.mrprojects.gholrob.model.GameCell
import com.mrprojects.gholrob.model.NewVersion
import com.mrprojects.gholrob.model.User
import com.mrprojects.gholrob.model.events.OnShowCoinShopCalled
import com.mrprojects.gholrob.model.events.OnShowLifeShopCalled
import com.mrprojects.gholrob.model.play.GameKill
import ir.radesh.basemodule.baseViews.BaseActivity
import ir.radesh.basemodule.baseViews.BaseFragment
import ir.radesh.basemodule.commons.basicConfig
import ir.radesh.basemodule.commons.disableAlphaByBoolean
import ir.radesh.basemodule.commons.makeWordRed
import ir.radesh.basemodule.commons.openLink
import ir.radesh.basemodule.commons.visibleByBoolean


fun <B : ViewBinding> BaseActivity<B>.showClosePlayDialog(onCloseClicked: () -> Unit) {
    val binding = ClosePlayDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(this, R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)
    binding.tvMessageDialog.makeWordRed("خروج")
    binding.lnrExit.setOnClickListener {
        dialog.dismiss()
        onCloseClicked()
    }
    binding.btnDismiss.tvDismiss.text = "بستن"
    binding.btnDismiss.root.setOnClickListener {
        dialog.dismiss()
    }

    doOnTry({
        dialog.show()
    })
}

fun <B : ViewBinding> BaseActivity<B>.showCloseGameDialog(onCloseClicked: () -> Unit) {
    val binding = CloseGameDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(this, R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)
    binding.lnrExit.setOnClickListener {
        dialog.dismiss()
        onCloseClicked()
    }

    binding.btnDismiss.tvDismiss.text = "بستن"
    binding.btnDismiss.root.setOnClickListener {
        dialog.dismiss()
    }

    doOnTry({
        dialog.show()
    })
}


fun <B : ViewBinding> BaseActivity<B>.showCoinNotEnoughDialog() {
    val binding = CoinNotEnoughDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(this, R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)
    binding.btnShop.setOnClickListener {
        postEvent(OnShowCoinShopCalled())
        dialog.dismiss()
    }

    binding.btnDismiss.tvDismiss.text = "بستن"
    binding.btnDismiss.root.setOnClickListener {
        dialog.dismiss()
    }

    doOnTry({
        dialog.show()
    })
}


@SuppressLint("SetTextI18n")
fun LayoutInflater.warningDialog(msg: String, title: String = "توجه", icon: Int = R.drawable.ig_error) {
    val binding = WarningDialogBinding.inflate(this)
    val dialog = Dialog(context, R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)
    binding.ivIcon1.setImageResource(icon)
    binding.tvTitleDialog.text = title
    binding.tvMessageDialog.text = msg
    binding.btnDismiss.root.setOnClickListener {
        dialog.dismiss()
    }

    doOnTry({
        dialog.show()
    })
}
@SuppressLint("SetTextI18n")
fun <B : ViewBinding> BaseFragment<B>.warningDialog(msg: String, title: String = "توجه", icon: Int = R.drawable.ig_error) {
    layoutInflater.warningDialog(msg, title, icon)
}

@SuppressLint("SetTextI18n")
fun <B : ViewBinding> BaseActivity<B>.warningDialog(msg: String, title: String = "توجه", icon: Int = R.drawable.ig_error) {
    layoutInflater.warningDialog(msg, title, icon)
}



@SuppressLint("SetTextI18n")
fun <B : ViewBinding> BaseFragment<B>.noInternetDialog(title: String?=null, msg: String?=null, onRetryClicked: () -> Unit) {
    val binding = NoInternetDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)
    binding.btnDismiss.root.visibleByBoolean(false)
    if (!title.isNullOrEmpty()){
        binding.tvTitleDialog.text = title
    }
    if (!msg.isNullOrEmpty()){
        binding.tvMessageDialog.text = msg
    }
    binding.lnrRetry.setOnClickListener {
        onRetryClicked()
        dialog.dismiss()
    }
    binding.btnDismiss.root.setOnClickListener {
        dialog.dismiss()
    }

    doOnTry({
        dialog.show()
    })
}



@SuppressLint("SetTextI18n")
fun LayoutInflater.showSuccessDialog(message: String, onOkClicked: (() -> Unit)? = null) {

    val binding = DialogSuccessBinding.inflate(this)

    val dialog = Dialog(context, R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)

    binding.tvMessageDialog.text = message

    binding.btnDismiss.root.setOnClickListener {
        onOkClicked?.invoke()
        dialog.dismiss()
    }

    doOnTry({
        dialog.show()
    })
}

@SuppressLint("SetTextI18n")
fun <B : ViewBinding> BaseFragment<B>.showSuccessDialog(message: String, onOkClicked: (() -> Unit)? = null) {
    layoutInflater.showSuccessDialog(message, onOkClicked)
}

@SuppressLint("SetTextI18n")
fun <B : ViewBinding> BaseActivity<B>.showSuccessDialog(message: String, onOkClicked: (() -> Unit)? = null) {
    layoutInflater.showSuccessDialog(message, onOkClicked)
}



@SuppressLint("SetTextI18n")
fun <B : ViewBinding> BaseFragment<B>.showUseLifeDialog(user: User, onUsedLifeCLicked: (dialog: Dialog) -> Unit) {
    val binding = UseLifeDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)
    binding.tvLifeCount.text = user.lives.toString()
    binding.btnUseLife.disableAlphaByBoolean(user.haveLife())
    binding.btnBuyLife.setOnClickListener {
        postEvent(OnShowLifeShopCalled())
        dialog.dismiss()
    }
    binding.btnUseLife.setOnClickListener {
        if (!user.haveLife()) {
            warningDialog("شما هیچ نوش دارویی ندارید!")
            return@setOnClickListener
        }
        onUsedLifeCLicked(dialog)
    }

    binding.btnDismiss.tvDismiss.text = "بستن"
    binding.btnDismiss.root.setOnClickListener {
        dialog.dismiss()
    }
    doOnTry({
        dialog.show()
    })
}


@SuppressLint("SetTextI18n")
fun <B : ViewBinding> BaseFragment<B>.showTutorialDoneDialog(title: String, msg: String, btn: String="آموزش بعدی", onOkCLicked: (dialog: Dialog) -> Unit) {
    val binding = TutorialDoneDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)
    binding.tvTitleDialog.text = title
    binding.tvMessageDialog.text = msg
    binding.tvBtn.text = btn

    binding.winConfettiView.startConfetti()
    binding.btnContinue.setOnClickListener {
        onOkCLicked(dialog)
    }

    doOnTry({
        dialog.show()
    })
}

@SuppressLint("SetTextI18n")
fun <B : ViewBinding> BaseFragment<B>.showEnemyInfoDialog(item: GameKill, isInPlay: Boolean = false) {
    val binding = EnemyInfoDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)
    binding.ivHeader.setImageResource(item.image())
    binding.tvTitleDialog.text = item.name
    binding.tvMessageDialog.text = item.description
    binding.tvDamage.text = item.damage.toString()
    binding.line2.visibleByBoolean(isInPlay)
    binding.lnrRemain.visibleByBoolean(isInPlay)

    val total = item.total
    binding.tvTotal.text = total.toString()

    if (isInPlay) {
        val remaining = item.remained
        binding.pbRemaining.max = 100f

        val killed = total - remaining
        val progressPercent = if (total > 0) (killed * 100) / total else 0
        binding.pbRemaining.animateProgress(progressPercent.toFloat())

        binding.tvRemainCounter.text = killed.toString()
        binding.tvRemainTotal.text = " / $total"
        binding.pbRemaining.customText = "$progressPercent%"
    }

    binding.btnDismiss.root.setOnClickListener {
        dialog.dismiss()
    }
    doOnTry({
        dialog.show()
    })
}


@SuppressLint("SetTextI18n")
fun <B : ViewBinding> BaseFragment<B>.showEmptySellInfoDialog(item: GameCell, totalDamage: Int) {
    val binding = EmptyCellInfoDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)
    val isHidden = totalDamage == AppConfig.HIDDEN_KEY
    if(isHidden){
        binding.tvTitleDialog.text = "مربع کور"
        binding.tvMessageDialog.text = "این مربع تحت تاثیر بیابان کور شده. برای رهایی از کوری باید بیابان را شکست دهید"
        binding.ivHeader.setImageResource(CellTypes.EYE.image)

    }else{
        binding.ivHeader.visibleByBoolean(!item.isEmpty())
        binding.ivHeader.setImageResource(item.image())
    }

    if(!isHidden) binding.ivHeader.setImageResource(item.image())
    val damage = if(!isHidden) totalDamage else "?"
    val pits = if(!isHidden) totalDamage / 100 else "?"
    val enemies = if(!isHidden) totalDamage % 100 else "?"

    binding.tvDamage.text = damage.toString()
    binding.tvPits.text = pits.toString()
    binding.tvEnemies.text = enemies.toString()

    binding.btnDismiss.root.setOnClickListener {
        dialog.dismiss()
    }
    doOnTry({
        dialog.show()
    })
}


@SuppressLint("SetTextI18n")
fun <B : ViewBinding> BaseFragment<B>.showUpdateDialog(version: NewVersion?) {
    if (version == null) return

    val binding = DialogAppUpdateBinding.inflate(layoutInflater)
    val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)
    binding.tvTitleDialog.text = version.title
    binding.tvMessageDialog.text = version.msg
    binding.tvVersionTag.text = version.latestVersionName

    binding.btnBazaar.visibleByBoolean(!version.bazaarLink.isNullOrEmpty())
    binding.btnMyket.visibleByBoolean(!version.myketLink.isNullOrEmpty())
    binding.btnBazaar.setOnClickListener{
        requireContext().openLink(version.bazaarLink.toString())
    }
    binding.btnMyket.setOnClickListener{
        requireContext().openLink(version.myketLink.toString())
    }
    binding.btnDismiss.root.visibleByBoolean(!version.isMandatory)
    binding.btnDismiss.tvDismiss.text = "فعلا نه، بعدا آبدیت میکنم!"
    binding.btnDismiss.root.setOnClickListener {
        dialog.dismiss()
    }
    doOnTry({
        dialog.show()
    })
}


