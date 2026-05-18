package ir.radesh.basemodule.helper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ir.radesh.basemodule.interfaces.OnItemClickListener
import timber.log.Timber
import java.util.Collections

/**
    Copyright 2019 Radesh Farokh Manesh

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License. You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

    * updated to view binding in 2023
 */
abstract class RvAdapter<T, B : ViewBinding>(
    private var bindingClass: (LayoutInflater, ViewGroup, Boolean) -> B,
    var itemsList: List<T> = Collections.emptyList(),
    private var listener: OnItemClickListener<T>? = null)
    : RecyclerView.Adapter<RvAdapter<T, B>.Holder>() {

    fun setData(itemsList: List<T>){
        this.itemsList = itemsList
        notifyDataSetChanged()
    }

    fun removeData(){
        this.itemsList = emptyList()
        notifyDataSetChanged()
    }


    /**
     * only insert new items
     */
    fun addData(addList: List<T>){
        val initialSize = itemsList.size
        itemsList = itemsList + addList
        val updatedSize = itemsList.size
        notifyItemRangeInserted(initialSize, updatedSize)
    }


    fun getData(): List<T> {
        return this.itemsList
    }

    fun removeAt(position: Int) {
        val list = itemsList.toMutableList()
        list.removeAt(position)
        itemsList = list
        notifyItemRemoved(position)
    }

    fun updateItem(position:Int, model: T){
        val list = ArrayList(itemsList)
        list.removeAt(position)
        list.add(position, model)
        itemsList = list
        notifyItemChanged(position)
    }

    fun addItem(position:Int, model: T){
        val list = ArrayList(itemsList)
        list.add(position, model)
        itemsList = list
        notifyDataSetChanged()
    }
    fun removeItem(model: T){
        val list = ArrayList(itemsList)
        list.remove(model)
        itemsList = list
        Timber.e("itemList, $itemsList, $model")
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }


    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): Holder {
        val viewBinding = bindingClass(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return Holder(viewBinding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(itemsList[position], position)
    }



    inner class Holder(val viewBinding: B) : RecyclerView.ViewHolder(viewBinding.root)  {
        fun bind(item : T, position: Int){
            onBindView(viewBinding, item, position, listener)
        }
    }

    fun addItemClickListener(listener: OnItemClickListener<T>){
        this.listener = listener
    }

    abstract fun onBindView(view: B, item: T, position: Int, listener: OnItemClickListener<T>?)



}