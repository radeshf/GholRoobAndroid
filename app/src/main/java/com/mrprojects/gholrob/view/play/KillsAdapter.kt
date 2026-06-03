package com.mrprojects.gholrob.view.play

import android.annotation.SuppressLint
import com.mrprojects.gholrob.R
import com.mrprojects.gholrob.databinding.PlayKillsItemBinding
import com.mrprojects.gholrob.model.play.GameKill
import ir.radesh.basemodule.commons.setTextCollor
import ir.radesh.basemodule.helper.RvAdapter
import ir.radesh.basemodule.interfaces.OnItemClickListener


class KillsAdapter(listener: OnItemClickListener<GameKill>?) :
    RvAdapter<GameKill, PlayKillsItemBinding>(PlayKillsItemBinding::inflate, listener = listener) {



    @SuppressLint("SetTextI18n")
    override fun onBindView(view: PlayKillsItemBinding, item: GameKill, position: Int, listener: OnItemClickListener<GameKill>?) {
        view.lnrCell.setBackgroundResource(if(item.remained>0) R.drawable.box_btn else R.drawable.box_btn_green)
        view.tvCell.setTextCollor(if(item.remained>0) R.color.white else R.color.green)
        view.ivCell.setImageResource(item.image())
        view.tvCell.text = item.killed.toString()

        view.root.setOnClickListener {
            listener?.onItemClick(item)
        }

    }

}
