package com.mrprojects.gholrob.view.profile

import android.os.Bundle
import android.view.View
import com.mrprojects.gholrob.databinding.ProfileFragmentBinding
import com.mrprojects.gholrob.helper.initToolbar
import com.mrprojects.gholrob.helper.showSuccessDialog
import com.mrprojects.gholrob.helper.warningDialog
import com.mrprojects.gholrob.model.events.OnProfileChanged
import com.mrprojects.gholrob.repository.Provider
import com.mrprojects.gholrob.viewmodel.UserViewModel
import ir.radesh.basemodule.baseViews.BaseFragment
import ir.radesh.basemodule.commons.getAdp
import ir.radesh.basemodule.commons.initGrid
import ir.radesh.basemodule.commons.showLoading
import ir.radesh.basemodule.commons.showToast
import ir.radesh.basemodule.interfaces.OnItemClickListener
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
            if (item.isComingSoon){
                warningDialog("این آیتم در آبدیت بعدی اضافه می شود")
            }else{
                showProfileImageInfoDialog(item) {
                    binding.ivProfileImage.setImageResource(AvatarMapper.getResourceId(item.image))
                    binding.rvProfiles.getAdp<ProfileImageAdapter>().selectItem(position)
                }
            }
        }
        binding.rvProfiles.getAdp<ProfileImageAdapter>().setData(Profiles.getAllAsProfileImages())
        userConfig()
        clicks()
    }

    fun userConfig() {
        userViewModel = Provider.provideUserViewModel(this)
        userViewModel.loadUser()
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            Timber.e("user: ${user.profileImage}, ${user.getProfileResource()}")
            binding.etProfileName.setText(user.name)
            binding.etProfileBio.setText(user.bio)
            binding.ivProfileImage.setImageResource(user.getProfileResource())
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
        if (nickName.isEmpty()){
            showToast("لطفا نام کاربری خود را وارد نمایید")
            return
        }

        Provider.provideApiHelper(this).editProfile(nickName,bio, profileImage?.image?: "") {
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