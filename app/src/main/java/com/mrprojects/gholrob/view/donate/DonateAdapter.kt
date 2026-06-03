package com.mrprojects.gholrob.view.donate

import com.mrprojects.gholrob.databinding.BuyCoinItemBinding
import com.mrprojects.gholrob.helper.payment.LifePacks
import ir.radesh.basemodule.interfaces.OnItemClickListener
import ir.radesh.basemodule.helper.RvAdapter
import ir.radesh.basemodule.commons.toMoneyString


class DonateAdapter(data: List<LifePacks>, listener: OnItemClickListener<LifePacks>?) :
    RvAdapter<LifePacks, BuyCoinItemBinding>(BuyCoinItemBinding::inflate, data, listener = listener) {

    override fun onBindView(view: BuyCoinItemBinding, item: LifePacks, position: Int, listener: OnItemClickListener<LifePacks>?) {
        view.tvValue.text = item.price.toMoneyString()
        view.tvMsg.text = item.title
        view.tvCoin.text = "${item.amount.toMoneyString()} سکه"
        view.ivCoin.setImageResource(item.iconRes)

        view.root.setOnClickListener {
            listener?.onItemClick(item)
        }
    }


}
