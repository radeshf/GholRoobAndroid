package com.mrprojects.gholrob.view.profile

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.mrprojects.gholrob.R

import com.mrprojects.gholrob.databinding.OptionItemBinding
import com.mrprojects.gholrob.databinding.ProfileImageItemBinding
import com.mrprojects.gholrob.databinding.RatingItemBinding
import com.mrprojects.gholrob.model.Rating
import ir.radesh.basemodule.commons.visibleByBoolean
import ir.radesh.basemodule.helper.RvAdapter
import ir.radesh.basemodule.interfaces.OnItemClickListener


class ProfileImageAdapter(listener: OnItemClickListener<ProfileImage>?)
    : RvAdapter<ProfileImage, ProfileImageItemBinding>(ProfileImageItemBinding::inflate, listener = listener) {

    var selectedPosition: Int = RecyclerView.NO_POSITION

    @SuppressLint("SetTextI18n")
    override fun onBindView(view: ProfileImageItemBinding, item: ProfileImage, position: Int, listener: OnItemClickListener<ProfileImage>?) {
        view.ivImage.setImageResource(AvatarMapper.getResourceId(item.image))
        val isSelected = position == selectedPosition
        view.lnrCard.setBackgroundResource(if (isSelected) R.drawable.box_btn_selected else R.drawable.box_btn)
        view.root.setOnClickListener {
            selectItem(position)
            listener?.onItemClick(item)
        }

    }
    fun selectItem(position: Int) {
        val oldPos = selectedPosition
        selectedPosition = position

        // refresh old + new item UI
        if (oldPos != RecyclerView.NO_POSITION) notifyItemChanged(oldPos)
        notifyItemChanged(position)
    }

    fun getSelectedItem(): ProfileImage? {
        return if (selectedPosition != RecyclerView.NO_POSITION) {
            getData().getOrNull(selectedPosition)
        } else null
    }
}
