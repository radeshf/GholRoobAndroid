package com.mrprojects.gholrob.view.menu

import android.os.Bundle
import android.view.View
import com.mrprojects.gholrob.R
import com.mrprojects.gholrob.databinding.ContactUsFragmentBinding
import com.mrprojects.gholrob.helper.initToolbar
import ir.radesh.basemodule.baseViews.BaseFragment
import ir.radesh.basemodule.commons.showLoading


class ContactUsFragment : BaseFragment<ContactUsFragmentBinding>(ContactUsFragmentBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(title = getString(R.string.contact_us))
        getData()
    }

    private fun getData() {
    }

}