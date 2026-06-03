package com.mrprojects.gholrob.view.tutorial

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.mrprojects.gholrob.databinding.TutorialFragmentBinding
import com.mrprojects.gholrob.helper.initToolbar
import com.mrprojects.gholrob.helper.openFragment
import com.mrprojects.gholrob.helper.showEnemyInfoDialog
import com.mrprojects.gholrob.model.CellTypes
import com.mrprojects.gholrob.model.play.GameKill
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
        binding.tvHint1.makeWordRed("ایرانی", color = Color.RED)
        binding.tvHint1.makeWordRed("مین\u200Cروب (Minesweeper)", color = Color.YELLOW)
        binding.tvHint2.makeWordRed("ضحاک", color = Color.WHITE)
        binding.tvHint2.makeWordRed("ارژنگ دیو", color = Color.WHITE)
        binding.tvHint2.makeWordRed("دیو سپید", color = Color.WHITE)
        binding.tvHint2.makeWordRed("10", color = Color.WHITE)
        binding.tvHint2.makeWordRed("15", color = Color.WHITE)
        binding.tvHint2.makeWordRed("چاه\u200Cهای شغاد", color = Color.RED)

        initToolbar(title = "راهنمای بازی")

    }

    private fun clicks() {

        binding.lnrTutorial.setOnClickListener {
            openFragment(TutorialWebFragment.newInstance())
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