package com.mrprojects.gholrob.view.profile

import android.os.Bundle
import android.view.View
import com.mrprojects.gholrob.databinding.ProfileFragmentBinding
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

class ProfileFragment : BaseFragment<ProfileFragmentBinding>(ProfileFragmentBinding::inflate), OnItemClickListener<ProfileImage> {

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
        binding.rvProfiles.initGrid(3)
        binding.rvProfiles.adapter = ProfileImageAdapter(this)
        binding.rvProfiles.getAdp<ProfileImageAdapter>().setData(getProfileImages())
        userConfig()
        clicks()
    }

    fun userConfig() {
        userViewModel = Provider.provideUserViewModel(this)
        userViewModel.loadUser()
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            Timber.e("user: ${user.profileImage}, ${user.getProfileResource()}")
            binding.etProfileName.setText(user.name)
            binding.ivProfileImage.setImageResource(user.getProfileResource())
        }

    }

    private fun clicks() {
        binding.btnSubmit.setOnClickListener {
            submitData()
        }

    }


    private fun getProfileImages(): List<ProfileImage> {
        return listOf(
            ProfileImage("profile_1"),
            ProfileImage("profile_2"),
            ProfileImage("profile_3"),
            ProfileImage("profile_4"),
            ProfileImage("profile_5"),
            ProfileImage("profile_6"),
            ProfileImage("profile_7"),
            ProfileImage("profile_8"),
            ProfileImage("profile_9"),
        )
    }


    private fun submitData() {
        val profileImage = binding.rvProfiles.getAdp<ProfileImageAdapter>().getSelectedItem()
        val nickName = binding.etProfileName.text.toString()
        if (nickName.isEmpty()){
            showToast("لطفا نام کاربری خود را وارد نمایید")
            return
        }

        Provider.provideApiHelper(this).editProfile(nickName, profileImage?.image?: "") {
            userViewModel.storeUser(it.data)
            showSuccessDialog("پروفایل شما آبدیت شد")
            postEvent(OnProfileChanged(it.data))
        }
    }

    override fun onItemClick(item: ProfileImage) {
        binding.ivProfileImage.setImageResource(AvatarMapper.getResourceId(item.image))
    }

    override fun showMsg(s: String) {
        super.showMsg(s)
        warningDialog(s)
    }

}