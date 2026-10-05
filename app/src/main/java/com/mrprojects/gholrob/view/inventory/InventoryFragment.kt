package com.mrprojects.gholrob.view.inventory

import android.os.Bundle
import android.view.View
import com.mrprojects.gholrob.databinding.InventoryFragmentBinding
import com.mrprojects.gholrob.helper.initToolbar
import com.mrprojects.gholrob.helper.openFragment
import com.mrprojects.gholrob.helper.payment.LifePacks
import com.mrprojects.gholrob.helper.setTime
import com.mrprojects.gholrob.helper.showSuccessDialog
import com.mrprojects.gholrob.helper.updateHearts
import com.mrprojects.gholrob.helper.warningDialog
import com.mrprojects.gholrob.model.events.OnProfileChanged
import com.mrprojects.gholrob.repository.Provider
import com.mrprojects.gholrob.view.dialogs.showBuyCoinDialog
import com.mrprojects.gholrob.view.dialogs.showBuyEnergyDialog
import com.mrprojects.gholrob.view.dialogs.showBuyEyeDialog
import com.mrprojects.gholrob.view.dialogs.showBuyLifeDialog
import com.mrprojects.gholrob.view.dialogs.showBuyShieldDialog
import com.mrprojects.gholrob.view.profile.ProfileFragment
import com.mrprojects.gholrob.view.profile.ProfileImage
import com.mrprojects.gholrob.view.profile.ProfileImageAdapter
import com.mrprojects.gholrob.view.profile.Profiles
import com.mrprojects.gholrob.view.profile.showProfileImageInfoDialog
import com.mrprojects.gholrob.viewmodel.UserViewModel
import ir.radesh.basemodule.baseViews.BaseActivity
import ir.radesh.basemodule.baseViews.BaseFragment
import ir.radesh.basemodule.commons.getAdp
import ir.radesh.basemodule.commons.initGrid
import ir.radesh.basemodule.commons.toMoneyString
import ir.radesh.basemodule.commons.visibleByBoolean

class InventoryFragment : BaseFragment<InventoryFragmentBinding>(InventoryFragmentBinding::inflate) {

    private lateinit var userViewModel: UserViewModel

    companion object {
        fun newInstance(): InventoryFragment {
            val frag = InventoryFragment()
            val b = Bundle()
            frag.arguments = b
            return frag
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(title = "گنجه")

        binding.rvProfiles.initGrid(3, canScroll = false)
        binding.rvProfiles.adapter = ProfileImageAdapter(false) { position, item ->
            if (item.isComingSoon) {
                warningDialog("این آیتم در آبدیت بعدی اضافه می شود")
            } else {
                showProfileImageInfoDialog(
                    userViewModel.user.value!!, item,
                    onSetProfile = { it ->
                        setProfile(it.image)
                    }, onBuyProfile = { it ->

                    })
            }
        }

        userConfig()
        clicks()
    }

    fun userConfig() {
        userViewModel = Provider.provideUserViewModel(this)
        userViewModel.loadUser()
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            binding.tvCoins.text = user.coins.toMoneyString()
            binding.tvLifeCount.text = user.lives.toMoneyString()
            binding.tvEyeCounts.text = user.eyes.toMoneyString()
            binding.tvShieldCount.text = user.shields.toMoneyString()
            binding.HeartsLayout.updateHearts(user, showAdd = true, showTimer = true)

            val profiles = Profiles.Companion.getAllAsProfileImages()
            val purchasedProfiles = arrayListOf<ProfileImage>()
            user.purchasedItems.forEach { user_profile ->
                profiles.forEach { profile ->
                    if (user_profile == profile.image){
                        purchasedProfiles.add(profile)
                    }
                }
            }
            binding.lnrEmptyProfile.visibleByBoolean(purchasedProfiles.isEmpty())

            binding.rvProfiles.getAdp<ProfileImageAdapter>().setData(purchasedProfiles)
            binding.rvProfiles.getAdp<ProfileImageAdapter>().setSelectedByName(user.profileImage)

        }

        userViewModel.heartTimer.timer.observe(viewLifecycleOwner) { millis ->
            binding.HeartsLayout.lnrTimer.setTime(millis)
        }

    }

    private fun clicks() {

        binding.lnrEmptyProfile.setOnClickListener {
            openFragment(ProfileFragment.newInstance())
        }
        binding.btnViewAllProfiles.setOnClickListener {
            openFragment(ProfileFragment.newInstance())
        }

        binding.lnrCoin.setOnClickListener {
            (requireActivity() as? BaseActivity<*>)?.showBuyCoinDialog(userViewModel)
        }

        binding.lnrLife.setOnClickListener {
            (requireActivity() as? BaseActivity<*>)?.showBuyLifeDialog(userViewModel)
        }

        binding.lnrEye.setOnClickListener {
            (requireActivity() as? BaseActivity<*>)?.showBuyEyeDialog(userViewModel)
        }

        binding.lnrShield.setOnClickListener {
            (requireActivity() as? BaseActivity<*>)?.showBuyShieldDialog(userViewModel)
        }

        binding.HeartsLayout.ivHeartAdd.setOnClickListener {
            (requireActivity() as? BaseActivity<*>)?.showBuyEnergyDialog(userViewModel)
        }
    }

    // ------------- APIs --------------
    private fun setProfile(profileImage: String) {
        Provider.provideApiHelper(this).editProfileImage(profileImage) {
            userViewModel.storeUser(it.data)
            showSuccessDialog(it.getMessage())
            postEvent(OnProfileChanged(it.data))
        }
    }


    override fun showMsg(s: String) {
        super.showMsg(s)
        warningDialog(s)
    }

}