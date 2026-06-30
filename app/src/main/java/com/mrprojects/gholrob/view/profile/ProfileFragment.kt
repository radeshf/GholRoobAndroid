package com.mrprojects.gholrob.view.profile

import android.os.Bundle
import android.view.View
import com.mrprojects.gholrob.databinding.ProfileFragmentBinding
import com.mrprojects.gholrob.helper.initToolbar
import com.mrprojects.gholrob.helper.payment.LifePacks
import com.mrprojects.gholrob.helper.showBuyCoinDoneDialog
import com.mrprojects.gholrob.helper.showBuyLifeDoneDialog
import com.mrprojects.gholrob.helper.showSuccessDialog
import com.mrprojects.gholrob.helper.warningDialog
import com.mrprojects.gholrob.model.events.OnProfileChanged
import com.mrprojects.gholrob.model.rest.BuyItemPost
import com.mrprojects.gholrob.repository.Provider
import com.mrprojects.gholrob.viewmodel.UserViewModel
import ir.radesh.basemodule.baseViews.BaseFragment
import ir.radesh.basemodule.commons.getAdp
import ir.radesh.basemodule.commons.initGrid
import ir.radesh.basemodule.commons.showToast
import timber.log.Timber

class ProfileFragment : BaseFragment<ProfileFragmentBinding>(ProfileFragmentBinding::inflate) {

    private lateinit var userViewModel: UserViewModel

    companion object {
        fun newInstance(): ProfileFragment {
            val frag = ProfileFragment()
            val b = Bundle()
            frag.arguments = b
            return frag
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(title = "پروفایل")

        binding.rvProfiles.initGrid(3)
        binding.rvProfiles.adapter = ProfileImageAdapter { position, item ->
            if (item.isComingSoon) {
                warningDialog("این آیتم در آبدیت بعدی اضافه می شود")
            } else {
                showProfileImageInfoDialog(
                    userViewModel.user.value!!, item,
                    onSetProfile = { it ->
                        binding.ivProfileImage.setImageResource(AvatarMapper.getResourceId(it.image))
                        binding.rvProfiles.getAdp<ProfileImageAdapter>().selectItem(position)
                    }, onBuyProfile = { it ->
                        buyProfile(it)
                    })
            }
        }

        userConfig()
        clicks()
    }

    fun buyProfile(item: ProfileImage) {
        if (!userViewModel.checkUserCoin(item.price))
            return

        val post = LifePacks.BUY_PROFILE.convertToItemPost(item)
        Provider.provideApiHelper(this).buyItem(post) {
            userViewModel.storeUser(it.data)
            showSuccessDialog(it.getMessage())
        }
    }

    fun userConfig() {
        userViewModel = Provider.provideUserViewModel(this)
        userViewModel.loadUser()
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            binding.etProfileName.setText(user.name)
            binding.etProfileBio.setText(user.bio)
            binding.ivProfileImage.setImageResource(user.getProfileResource())
            val profiles = Profiles.getAllAsProfileImages()
            profiles.forEach { profile ->
                if (profile.image in user.purchasedItems) {
                    profile.isPurchased = true
                }
            }
            binding.rvProfiles.getAdp<ProfileImageAdapter>().setData(profiles)

        }

    }

    private fun clicks() {
        binding.btnSubmit.setOnClickListener {
            submitData()
        }

    }


    private fun submitData() {
        val profileImage = binding.rvProfiles.getAdp<ProfileImageAdapter>().getSelectedItem()
        val nickName = binding.etProfileName.text.toString()
        val bio = binding.etProfileBio.text.toString()
        if (nickName.isEmpty()) {
            showToast("لطفا نام کاربری خود را وارد نمایید")
            return
        }

        Provider.provideApiHelper(this).editProfile(nickName, bio, profileImage?.image ?: "") {
            userViewModel.storeUser(it.data)
            showSuccessDialog("پروفایل شما آبدیت شد")
            postEvent(OnProfileChanged(it.data))
        }
    }


    override fun showMsg(s: String) {
        super.showMsg(s)
        warningDialog(s)
    }

}