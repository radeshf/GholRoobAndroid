package com.mrprojects.gholrob.view.profile

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.viewbinding.ViewBinding
import com.mrprojects.gholrob.R
import com.mrprojects.gholrob.databinding.ProfileImageInfoDialogBinding
import com.mrprojects.gholrob.databinding.ProfileInfoDialogBinding
import com.mrprojects.gholrob.helper.doOnTry
import com.mrprojects.gholrob.helper.openFragment
import com.mrprojects.gholrob.model.User
import ir.radesh.basemodule.baseViews.BaseFragment
import ir.radesh.basemodule.commons.basicConfig
import ir.radesh.basemodule.commons.visibleByBoolean


@SuppressLint("SetTextI18n")
fun <B : ViewBinding> BaseFragment<B>.showProfileInfoDialog(user: User, isSelf: Boolean=false) {
    val binding = ProfileInfoDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)
    binding.ivProfileImage.setImageResource(user.getProfileResource())
    binding.tvUsername.text = user.name
    binding.tvBio.text = user.bio
    binding.tvBattles.text = user.totalGames.toString()
    binding.tvWins.text = user.totalWins.toString()
    binding.tvKills.text = user.totalKills.toString()
    binding.btnEditProfile.visibleByBoolean(isSelf)

    binding.btnEditProfile.setOnClickListener {
        openFragment(ProfileFragment.newInstance())
        dialog.dismiss()
    }
    binding.lnrDismiss.root.setOnClickListener {
        dialog.dismiss()
    }
    doOnTry({
        dialog.show()
    })
}


@SuppressLint("SetTextI18n")
fun <B : ViewBinding> BaseFragment<B>.showProfileImageInfoDialog(item: ProfileImage, onSetProfile: (item: ProfileImage) -> Unit) {
    val binding = ProfileImageInfoDialogBinding.inflate(layoutInflater)
    val dialog = Dialog(requireContext(), R.style.MyDialogTheme)
    dialog.basicConfig(binding.root)
    binding.ivImage.setImageResource(item.drawableRes)
    binding.tvName.text = item.name
    binding.tvBio.text = item.description
    binding.lnrSelect.btnText.text = "انتخاب پروفایل"
    binding.lnrSelect.root.setOnClickListener {
        onSetProfile(item)
        dialog.dismiss()
    }
    binding.lnrDismiss.root.setOnClickListener {
        dialog.dismiss()
    }
    doOnTry({
        dialog.show()
    })
}
