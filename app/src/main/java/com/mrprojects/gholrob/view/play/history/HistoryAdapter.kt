package com.mrprojects.gholrob.view.play.history

import android.annotation.SuppressLint
import com.mrprojects.gholrob.R
import com.mrprojects.gholrob.databinding.HistoryItemBinding
import com.mrprojects.gholrob.model.AttemptHistory
import ir.radesh.basemodule.commons.inVisibleByBoolean
import ir.radesh.basemodule.commons.setTextCollor
import ir.radesh.basemodule.helper.RvAdapter
import ir.radesh.basemodule.interfaces.OnItemClickListener


class HistoryAdapter(listener: OnItemClickListener<AttemptHistory>?)
    : RvAdapter<AttemptHistory, HistoryItemBinding>(HistoryItemBinding::inflate, listener = listener) {


    @SuppressLint("SetTextI18n")
    override fun onBindView(view: HistoryItemBinding, item: AttemptHistory, position: Int, listener: OnItemClickListener<AttemptHistory>?) {
        view.tvSpeed.text = item.speed
        view.tvGameDate.text = item.finishedAt
        view.tvKills.text = item.defeatedMonsters.toString()
        view.tvScore.text = item.score.toString()
        view.tvResult.text = item.gameStatusTitle
        view.tvResult.setTextCollor(if(item.isPassed) R.color.green else R.color.red)
        view.tvGameTitle.text = item.title
        view.lnrEnemy.inVisibleByBoolean(!item.isPassed)
        if (item.killedBy != null && !item.isPassed){
            view.tvEnemy.text = item.killedBy!!.name
            view.ivEnemy.setImageResource(item.killedBy!!.image())
        }


        view.root.setOnClickListener {
            listener?.onItemClick(item)
        }

    }

}
