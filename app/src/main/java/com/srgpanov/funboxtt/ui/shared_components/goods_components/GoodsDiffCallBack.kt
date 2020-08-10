package com.srgpanov.funboxtt.ui.shared_components.goods_components

import androidx.recyclerview.widget.DiffUtil
import com.srgpanov.funboxtt.data.entity.Goods

class GoodsDiffCallBack(
    private val oldList: List<Goods>,
    private val newList: List<Goods>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id==newList[newItemPosition].id
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return  oldList[oldItemPosition]==newList[newItemPosition]
    }
}