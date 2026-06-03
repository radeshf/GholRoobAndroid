package com.mrprojects.gholrob.view.play

import android.annotation.SuppressLint
import com.mrprojects.gholrob.AppConfig
import com.mrprojects.gholrob.R
import com.mrprojects.gholrob.databinding.OptionItemBinding
import com.mrprojects.gholrob.model.CellTypes
import com.mrprojects.gholrob.model.GameCell
import ir.radesh.basemodule.commons.getCollor
import ir.radesh.basemodule.commons.visibleByBoolean
import ir.radesh.basemodule.helper.RvAdapter
import ir.radesh.basemodule.interfaces.OnItemClickListener
import timber.log.Timber


class CellsAdapter(listener: OnItemClickListener<GameCell>?) :
    RvAdapter<GameCell, OptionItemBinding>(OptionItemBinding::inflate, listener = listener) {

    var isGlobalLoading: Boolean = false

    fun handleFlagged(view: OptionItemBinding) {
        view.lnrRevealed.visibleByBoolean(true)
        view.ivCell.setImageResource(R.drawable.ig_flag)
        view.tvCell.visibleByBoolean(false)
        view.ivCellDefeated.visibleByBoolean(false)
    }

    fun handleHeart(view: OptionItemBinding, item: GameCell) {
        if (item.isRevealed) {
            if (item.isDefeated) {
                handleDefeated(view, item)
            } else {
                view.lnrRevealed.visibleByBoolean(true)
                view.ivCell.setImageResource(item.image())
                setDamage(view, item)
            }
        }
    }

    fun handleEmpty(view: OptionItemBinding, item: GameCell) {
        if (item.isDefeated) {
            view.lnrDefeated.visibleByBoolean(true)
        }
        setDamage(view, item)
    }

    fun handlePit(view: OptionItemBinding, item: GameCell) {
        if (item.isRevealed) {
            view.lnrRevealed.visibleByBoolean(true)
            view.ivCell.setImageResource(item.image())

        }
        view.tvCell.visibleByBoolean(false)
        view.tvCellDefeated.visibleByBoolean(false)
        view.ivCellDefeated.visibleByBoolean(false)

    }

    fun handleRevealed(view: OptionItemBinding, item: GameCell) {
        if (item.isDefeated) {
            handleDefeated(view, item)
        } else {
            view.lnrDefeated.visibleByBoolean(false)
            view.lnrRevealed.visibleByBoolean(true)
            view.ivCell.setImageResource(item.image())
            view.tvCell.setTextColor(view.root.context.getCollor(R.color.damage_color))
            view.tvCell.text = item.damage.toString()
            view.tvCell.visibleByBoolean(true)
            view.ivCellDamage.visibleByBoolean(true)
        }
    }

    fun handleDefeated(view: OptionItemBinding, item: GameCell) {
        view.lnrDefeated.visibleByBoolean(true)
        view.lnrRevealed.visibleByBoolean(false)

        view.ivCellDefeated.visibleByBoolean(true)
        view.ivCellDefeated.setImageResource(item.image())
        setDamage(view, item)

    }

    fun handleNotRevealed(view: OptionItemBinding, item: GameCell) {
        view.lnrDefeated.visibleByBoolean(false)
        view.lnrRevealed.visibleByBoolean(false)
    }

    fun flushViews(view: OptionItemBinding, item: GameCell) {
        view.lnrDefeated.visibleByBoolean(false)
        view.lnrRevealed.visibleByBoolean(false)
    }

    fun setDamage(view: OptionItemBinding, item: GameCell) {
        val damage = item.getTotalDamage(itemsList)
        if (damage == AppConfig.HIDDEN_KEY) {
            view.tvCell.text = "?"
            view.tvCellDefeated.text = "?"
            view.ivCellDefeated.visibleByBoolean(false)
        } else {
            view.tvCell.text = damage.toString()
            view.tvCellDefeated.text = damage.toString()
        }
        view.tvCell.visibleByBoolean(true)
        view.tvCellDefeated.visibleByBoolean(true)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindView(view: OptionItemBinding, item: GameCell, position: Int, listener: OnItemClickListener<GameCell>?) {

        view.lnrCell.setBackgroundResource(if (item.isRevealed) R.drawable.box_revealed else R.drawable.box)

        val cellType = CellTypes.fromKey(item.type)
        val isEmpty = cellType == CellTypes.EMPTY
        val isHeart = cellType == CellTypes.HEART
        val isBottomLessPit = cellType == CellTypes.BOTTOMLESS_PIT
        val isFlagged = item.isFlagged

        if (item.isLoading) {
            view.lnrLoading.visibleByBoolean(true)
        } else {
            flushViews(view, item)

            view.lnrLoading.visibleByBoolean(false)

            if (isFlagged) {
                handleFlagged(view)
            } else {
                when {
                    isBottomLessPit -> {
                        handlePit(view, item)
                    }

                    isEmpty -> {
                        handleEmpty(view, item)
                    }

                    isHeart -> {
                        handleHeart(view, item)
                    }

                    else -> {
                        if (item.isRevealed) {
                            handleRevealed(view, item)
                        } else if (item.isDefeated) {
                            handleDefeated(view, item)
                        } else {
                            handleNotRevealed(view, item)
                        }
                    }
                }

            }
        }





        view.root.setOnClickListener {
            if (isGlobalLoading) return@setOnClickListener

            if (!item.isDefeated){
                item.isLoading = true
                notifyItemChanged(position)
                listener?.onItemClick(item)
            }

        }

    }

}
