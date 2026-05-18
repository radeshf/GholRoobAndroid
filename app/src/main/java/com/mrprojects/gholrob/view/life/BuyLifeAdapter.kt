package com.mrprojects.gholrob.view.life

import com.mrprojects.gholrob.databinding.BuyLifeItemBinding
import com.mrprojects.gholrob.helper.payment.LifePacks
import ir.radesh.basemodule.interfaces.OnItemClickListener
import ir.radesh.basemodule.helper.RvAdapter
import ir.radesh.basemodule.commons.toMoneyString


class BuyLifeAdapter(data: List<LifePacks>, listener: OnItemClickListener<LifePacks>?) :
    RvAdapter<LifePacks, BuyLifeItemBinding>(BuyLifeItemBinding::inflate, data, listener = listener) {

    override fun onBindView(view: BuyLifeItemBinding, item: LifePacks, position: Int, listener: OnItemClickListener<LifePacks>?) {
        view.tvPrice.text = item.price.toMoneyString()
        view.tvName.text = item.title
        view.tvValue.text = "${item.lifeCount.toMoneyString()} نوش دارو"
        view.ivIcon.setImageResource(item.iconRes)
        view.root.setOnClickListener {
            listener?.onItemClick(item)
        }
    }


}
