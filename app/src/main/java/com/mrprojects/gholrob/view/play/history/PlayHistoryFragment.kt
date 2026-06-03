package com.mrprojects.gholrob.view.play.history

import android.os.Bundle
import android.view.View
import com.mrprojects.gholrob.R
import com.mrprojects.gholrob.databinding.PlayHistoryFragmentBinding
import com.mrprojects.gholrob.helper.initToolbar
import com.mrprojects.gholrob.helper.showEnemyInfoDialog
import com.mrprojects.gholrob.model.Attempt
import com.mrprojects.gholrob.model.GameCell
import com.mrprojects.gholrob.model.play.GameKill
import com.mrprojects.gholrob.repository.Provider
import com.mrprojects.gholrob.view.play.CellsAdapter
import com.mrprojects.gholrob.view.play.KillsAdapter
import ir.radesh.basemodule.baseViews.BaseFragment
import ir.radesh.basemodule.commons.getAdp
import ir.radesh.basemodule.commons.initGrid
import ir.radesh.basemodule.commons.visibleByBoolean
import ir.radesh.basemodule.interfaces.OnItemClickListener

class PlayHistoryFragment : BaseFragment<PlayHistoryFragmentBinding>(PlayHistoryFragmentBinding::inflate), OnItemClickListener<GameCell> {

    var gameId: Int = 0


    companion object {
        fun newInstance(gameId: Int): PlayHistoryFragment {
            val frag = PlayHistoryFragment()
            val b = Bundle()
            b.putInt("gameId", gameId)
            frag.arguments = b
            return frag
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameId = arguments?.getInt("gameId")!!
        binding.rvOptions.initGrid(7, canScroll = false)
        binding.rvKills.initGrid(7, canScroll = false)
        val adp = CellsAdapter(this)
        binding.rvKills.adapter = KillsAdapter(object : OnItemClickListener<GameKill> {
            override fun onItemClick(item: GameKill) {
                onKillItemClicked(item)
            }
        })
        adp.isGlobalLoading = true
        binding.rvOptions.adapter = adp
        initToolbar(title = "تاریخچه بازی")

        getData()
        clicks()
    }



    private fun clicks() {

    }

    private fun getData(onEnded: (() -> Unit)? = null) {
        Provider.provideApiHelper(this).getGame(gameId) {
            loadGame(it)
            onEnded?.invoke()
        }
    }

    private fun loadGame(attempt: Attempt){
        binding.tvTotalHearts.text = attempt.totalHearts.toString()
        binding.tvHearts.text = attempt.hearts.toString()
        binding.rvOptions.getAdp<CellsAdapter>().setData(attempt.cells)
        binding.rvKills.getAdp<KillsAdapter>().setData(attempt.kills)
        binding.tvKills.text = attempt.defeatedMonsters.toString()
        binding.tvSpeed.text = attempt.speed
        binding.lnrVictory.visibleByBoolean(attempt.isPassed)
        binding.lnrFail.visibleByBoolean(!attempt.isPassed)
        if (attempt.isPassed){
            binding.tvScore.text = attempt.score.toString()
        }else{
            binding.ivEnemyImage.setImageResource(attempt.killedBy!!.image())
            binding.tvEnemyName.text = attempt.killedBy!!.name
        }

    }

    fun onKillItemClicked(item: GameKill) {
        showEnemyInfoDialog(item, true)
    }

    override fun onItemClick(item: GameCell) {

    }


}