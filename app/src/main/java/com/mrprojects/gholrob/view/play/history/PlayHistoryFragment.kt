package com.mrprojects.gholrob.view.play.history

import CellTypes
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mrprojects.gholrob.R
import com.mrprojects.gholrob.databinding.GameOverDialogBinding
import com.mrprojects.gholrob.databinding.GamePassedDialogBinding
import com.mrprojects.gholrob.databinding.PlayFragmentBinding
import com.mrprojects.gholrob.databinding.PlayHistoryFragmentBinding
import com.mrprojects.gholrob.helper.doOnTry
import com.mrprojects.gholrob.helper.openNextLevel
import com.mrprojects.gholrob.helper.showClosePlayDialog
import com.mrprojects.gholrob.helper.showCoinNotEnoughDialog
import com.mrprojects.gholrob.helper.showUseLifeDialog
import com.mrprojects.gholrob.helper.submitHint
import com.mrprojects.gholrob.helper.tapsell.TapSellHelper
import com.mrprojects.gholrob.helper.warningDialog
import com.mrprojects.gholrob.model.Attempt
import com.mrprojects.gholrob.model.ErrorTypes
import com.mrprojects.gholrob.model.GameCell
import com.mrprojects.gholrob.model.HintType
import com.mrprojects.gholrob.model.Puzzle
import com.mrprojects.gholrob.model.User
import com.mrprojects.gholrob.model.events.OnShowLifeShopCalled
import com.mrprojects.gholrob.repository.Provider
import com.mrprojects.gholrob.view.play.CellsAdapter
import com.mrprojects.gholrob.viewmodel.UserViewModel
import ir.radesh.basemodule.baseViews.BaseFragment
import ir.radesh.basemodule.commons.basicConfig
import ir.radesh.basemodule.commons.clickOnTileAnimation
import ir.radesh.basemodule.commons.getAdp
import ir.radesh.basemodule.commons.inVisibleByBoolean
import ir.radesh.basemodule.commons.initGrid
import ir.radesh.basemodule.commons.makeWordRed
import ir.radesh.basemodule.commons.setTextCollor
import ir.radesh.basemodule.commons.showToast
import ir.radesh.basemodule.commons.visibleByBoolean
import ir.radesh.basemodule.interfaces.OnItemClickListener
import kotlinx.coroutines.launch
import timber.log.Timber

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
        val adp = CellsAdapter(this)
        adp.isGlobalLoading = true
        binding.rvOptions.adapter = adp

        getData()
        clicks()
    }



    private fun clicks() {
        binding.lnrDismiss.tvBtnName.text = getString(R.string.back)
        binding.lnrDismiss.root.setOnClickListener {
            onBackPressed()
        }

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

    override fun onItemClick(item: GameCell) {

    }


}