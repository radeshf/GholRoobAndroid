package com.mrprojects.gholrob.view.profile

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.viewbinding.ViewBinding
import com.mrprojects.gholrob.R
import com.mrprojects.gholrob.databinding.ProfileImageInfoDialogBinding
import com.mrprojects.gholrob.databinding.ProfileInfoDialogBinding
import com.mrprojects.gholrob.databinding.ProfileOtpDialogBinding
import com.mrprojects.gholrob.databinding.ProfileRestoreDialogBinding
import com.mrprojects.gholrob.helper.doOnTry
import com.mrprojects.gholrob.helper.openFragment
import com.mrprojects.gholrob.helper.showSuccessDialog
import com.mrprojects.gholrob.helper.warningDialog
import com.mrprojects.gholrob.model.User
import com.mrprojects.gholrob.model.events.OnProfileChanged
import com.mrprojects.gholrob.repository.Provider
import ir.radesh.basemodule.baseViews.BaseFragment
import ir.radesh.basemodule.commons.basicConfig
import ir.radesh.basemodule.commons.disableAlphaByBoolean
import ir.radesh.basemodule.commons.toMoneyString
import ir.radesh.basemodule.commons.visibleByBoolean


@SuppressLint("SetTextI18n")
fun <B : ViewBinding> BaseFragment<B>.showProfileImageInfoDialog(user: User, item: ProfileImage, onSetProfile: (item: ProfileImage) -> Unit, onBuyProfile: (item: ProfileImage) -> Unit) {
    val binding = ProfileImageInfoDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)
    binding.tvUserCoin.text = user.coins.toMoneyString()
    binding.ivImage.setImageResource(item.drawableRes)
    binding.tvTitleDialog.text = item.name
    binding.tvBio.text = item.description
    val isUserPurchased = user.hasPurchasedProfile(item)

    binding.cardCoinInfo.visibleByBoolean(!isUserPurchased)
    binding.btnBuyProfile.visibleByBoolean(!isUserPurchased)
    binding.tvPrice.text = item.price.toString()
    binding.btnSetProfile.disableAlphaByBoolean(isUserPurchased)

    binding.btnSetProfile.setOnClickListener {
        if (isUserPurchased){
            onSetProfile(item)
            dialog.dismiss()
        }
    }
    binding.btnBuyProfile.setOnClickListener {

        onBuyProfile(item)
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
fun <B : ViewBinding> BaseFragment<B>.showProfileInfoDialog(user: User, isSelf: Boolean=false) {
    val binding = ProfileInfoDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)
    binding.ivProfileImage.setImageResource(user.getProfileResource())
    binding.tvUsername.text = user.name
    binding.tvBadge.visibleByBoolean(!user.badge.isNullOrEmpty())
    binding.tvBadge.text = user.badge.toString()
    binding.tvBio.text = user.bio
    binding.tvBattles.text = user.totalGames.toString()
    binding.tvWins.text = user.totalWins.toString()
    binding.tvKills.text = user.totalKills.toString()
    binding.btnEditProfile.visibleByBoolean(isSelf)
    binding.btnAccount.visibleByBoolean(isSelf)
    binding.ivProfileImage.setOnClickListener {
        openFragment(ProfileFragment.newInstance())
        dialog.dismiss()

    }
    binding.btnEditProfile.setOnClickListener {
        openFragment(ProfileFragment.newInstance())
        dialog.dismiss()
    }
    binding.btnAccount.setOnClickListener {
        showProfileRestoreDialog(user)

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
fun <B : ViewBinding> BaseFragment<B>.showProfileRestoreDialog(user: User) {
    val binding = ProfileRestoreDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)
    if (!user.mobile.isNullOrEmpty()){
        binding.etCreate.setText(user.mobile.toString())
        binding.ivCreate.visibleByBoolean(true)
    }
    binding.btnCreateAccount.setOnClickListener {
        val mobile = binding.etCreate.text.toString()
        val type = "create"
        if (!mobile.startsWith("09")){
            warningDialog("شماره باید با 09 شروع شود")
            return@setOnClickListener
        }
        if (mobile.length != 11){
            warningDialog("شماره موبایل وارد شده صحیح نمیباشد")
            return@setOnClickListener
        }
        Provider.provideApiHelper(this).sendOtp(mobile, type) {
            showOtpDialog(type, mobile)
            dialog.dismiss()
        }
    }
    binding.btnRestoreAccount.setOnClickListener {
        val mobile = binding.etRestore.text.toString()
        val type = "restore"
        if (!mobile.startsWith("09")){
            warningDialog("شماره باید با 09 شروع شود")
            return@setOnClickListener
        }
        if (mobile.length != 11){
            warningDialog("شماره موبایل وارد شده صحیح نمیباشد")
            return@setOnClickListener
        }
        Provider.provideApiHelper(this).sendOtp(mobile, type) {
            showOtpDialog(type, mobile)
            dialog.dismiss()
        }
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
fun <B : ViewBinding> BaseFragment<B>.showOtpDialog(type: String, mobile: String) {
    val binding = ProfileOtpDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)
    binding.lnrSubmit.setOnClickListener {
        val code = binding.etCode.text.toString()
        if (type == "create"){
            Provider.provideApiHelper(this).createAccount(mobile, code, type) {
                postEvent(OnProfileChanged(it.data))
                showSuccessDialog(it.getMessage())
                dialog.dismiss()
            }
        }else if (type == "restore"){
            Provider.provideApiHelper(this).restoreAccount(mobile, code, type) {
                postEvent(OnProfileChanged(it.data))
                showSuccessDialog(it.getMessage())
                dialog.dismiss()
            }
        }
    }

    binding.btnDismiss.tvDismiss.text = "بستن"
    binding.btnDismiss.root.setOnClickListener {
        dialog.dismiss()
    }
    doOnTry({
        dialog.show()
    })
}
