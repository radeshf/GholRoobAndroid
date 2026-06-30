package com.mrprojects.gholrob.view.tutorial

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.mrprojects.gholrob.R
import com.mrprojects.gholrob.databinding.TutorialFragmentBinding
import com.mrprojects.gholrob.helper.initToolbar
import com.mrprojects.gholrob.helper.openFragment
import com.mrprojects.gholrob.helper.setTutorialText
import com.mrprojects.gholrob.helper.showEnemyInfoDialog
import com.mrprojects.gholrob.model.CellTypes
import com.mrprojects.gholrob.model.play.GameKill
import com.mrprojects.gholrob.view.play.TutorialPlayFragment
import ir.radesh.basemodule.baseViews.BaseFragment
import ir.radesh.basemodule.commons.getAdp
import ir.radesh.basemodule.commons.init
import ir.radesh.basemodule.commons.makeWordRed
import ir.radesh.basemodule.interfaces.OnItemClickListener

class TutorialFragment : BaseFragment<TutorialFragmentBinding>(TutorialFragmentBinding::inflate), OnItemClickListener<CellTypes> {


    companion object {
        fun newInstance(): TutorialFragment {
            val frag = TutorialFragment()
            val b = Bundle()
            frag.arguments = b
            return frag
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvEnemies.init()
        binding.rvEnemies.adapter = TutorialEnemyAdapter(this)
        getData()
        clicks()
        binding.tvHint.setTutorialText()

        initToolbar(title = "راهنمای بازی")

    }

    private fun clicks() {

        binding.lnrTutorial.setOnClickListener {
            openFragment(TutorialWebFragment.newInstance())
        }
        binding.lnrPlayTutorial.setOnClickListener {
            openFragment(TutorialPlayFragment.newInstance(1))
        }
        binding.tvHint.setOnClickListener {
            showTutorialDialog()
        }
    }

    fun getSortedEnemies(): List<CellTypes> {
        val enemies = arrayListOf<CellTypes>()

        enemies.add(CellTypes.HEART)
        enemies.add(CellTypes.BOTTOMLESS_PIT)
        enemies.add(CellTypes.EYE)

        enemies.add(CellTypes.BUG)
        enemies.add(CellTypes.RAT)
        enemies.add(CellTypes.BLUE_GHOST)
        enemies.add(CellTypes.SKELETON)
        enemies.add(CellTypes.RED_GHOST)
        enemies.add(CellTypes.SNAKE)
        enemies.add(CellTypes.PURPLE_GHOST)

        enemies.add(CellTypes.SMALL_BOSS)
        enemies.add(CellTypes.BIG_BOSS)
        enemies.add(CellTypes.FINAL_BOSS)

        return enemies
    }

    private fun getData() {
        binding.rvEnemies.getAdp<TutorialEnemyAdapter>().setData(getSortedEnemies())

    }

    override fun onItemClick(item: CellTypes) {
        showEnemyInfoDialog(GameKill.fromCellType(item))
    }


}