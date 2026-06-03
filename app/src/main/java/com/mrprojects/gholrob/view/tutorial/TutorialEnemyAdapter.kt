package com.mrprojects.gholrob.view.tutorial

import android.annotation.SuppressLint

import com.mrprojects.gholrob.databinding.RatingItemBinding
import com.mrprojects.gholrob.databinding.TutorialEnemyItemBinding
import com.mrprojects.gholrob.model.CellTypes
import ir.radesh.basemodule.commons.visibleByBoolean
import ir.radesh.basemodule.helper.RvAdapter
import ir.radesh.basemodule.interfaces.OnItemClickListener


class TutorialEnemyAdapter(listener: OnItemClickListener<CellTypes>?)
    : RvAdapter<CellTypes, TutorialEnemyItemBinding>(TutorialEnemyItemBinding::inflate, listener = listener) {


    @SuppressLint("SetTextI18n")
    override fun onBindView(view: TutorialEnemyItemBinding, item: CellTypes, position: Int, listener: OnItemClickListener<CellTypes>?) {
        view.tvEnemyName.text = item.title
        view.tvEnemyDamage.text = item.damage.toString()
        view.tvEnemyCount.text = "x${item.count}"
        view.ivEnemy.setImageResource(item.image)
        view.tvMsg.text = item.msg
        view.lnrDamage.visibleByBoolean(item.damage != null && item.damage > 0)
        view.lnrMsg.visibleByBoolean(!item.msg.isNullOrEmpty())

        view.root.setOnClickListener {
            listener?.onItemClick(item)
        }

    }

}
