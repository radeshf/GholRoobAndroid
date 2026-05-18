package com.mrprojects.gholrob.view.menu

import android.os.Bundle
import android.view.View
import com.mrprojects.gholrob.databinding.RulesFragmentBinding
import com.mrprojects.gholrob.helper.initToolbar
import ir.radesh.basemodule.baseViews.BaseFragment


class RulesFragment : BaseFragment<RulesFragmentBinding>(RulesFragmentBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(title = getString(com.radesh.basemodule.R.string.rules))

    }

}