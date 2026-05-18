package com.mrprojects.gholrob.view.play.history

import android.os.Bundle
import android.view.View
import com.mrprojects.gholrob.databinding.HistoryFragmentBinding
import com.mrprojects.gholrob.helper.openFragment
import com.mrprojects.gholrob.model.AttemptHistory
import com.mrprojects.gholrob.repository.Provider
import ir.radesh.basemodule.baseViews.BaseFragment
import ir.radesh.basemodule.commons.getAdp
import ir.radesh.basemodule.commons.init
import ir.radesh.basemodule.interfaces.OnItemClickListener

class HistoryFragment : BaseFragment<HistoryFragmentBinding>(HistoryFragmentBinding::inflate), OnItemClickListener<AttemptHistory> {



    companion object {
        fun newInstance(): HistoryFragment {
            val frag = HistoryFragment()
            val b = Bundle()
            frag.arguments = b
            return frag
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvData.init()
        binding.rvData.adapter = HistoryAdapter(this)
        getData()
        clicks()
    }



    private fun clicks() {

    }


    private fun getData() {
        Provider.provideApiHelper(this).getGameHistory {
            binding.rvData.getAdp<HistoryAdapter>().setData(it.data)

        }
    }

    override fun onItemClick(item: AttemptHistory) {
        openFragment(PlayHistoryFragment.newInstance(item.id))
    }


}