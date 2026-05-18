package com.mrprojects.gholrob.view.donate

import com.mrprojects.gholrob.databinding.BuyCoinItemBinding
import ir.radesh.basemodule.interfaces.OnItemClickListener
import ir.radesh.basemodule.helper.RvAdapter
import ir.radesh.basemodule.commons.toMoneyString


class DonateAdapter(data: List<DonateItem>, listener: OnItemClickListener<DonateItem>?) :
    RvAdapter<DonateItem, BuyCoinItemBinding>(BuyCoinItemBinding::inflate, data, listener = listener) {

    override fun onBindView(view: BuyCoinItemBinding, item: DonateItem, position: Int, listener: OnItemClickListener<DonateItem>?) {
        view.tvValue.text = item.price.toMoneyString()
        view.tvMsg.text = item.name
        view.tvCoin.text = "${item.coin.toMoneyString()} سکه"
        view.ivCoin.setImageResource(item.image)
        view.root.setOnClickListener {
            listener?.onItemClick(item)
        }
    }


}
