package com.srgpanov.funboxtt.ui.screens.back_end

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.srgpanov.funboxtt.R
import com.srgpanov.funboxtt.data.entity.Goods

import com.srgpanov.funboxtt.databinding.ItemGoodsBackendBinding
import com.srgpanov.funboxtt.ui.shared_components.goods_components.GoodsDiffCallBack
import com.srgpanov.funboxtt.ui.shared_components.goods_components.ShopAdapter
import kotlinx.coroutines.*


class BackendAdapter : ShopAdapter<BackendAdapter.GoodsAdapterViewHolder>() {
    var clickListener: ((goods: Goods, view: View) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodsAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return GoodsAdapterViewHolder(
            ItemGoodsBackendBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GoodsAdapterViewHolder, position: Int) {
        holder.bind(items[position], clickListener)
    }


    class GoodsAdapterViewHolder(val binding: ItemGoodsBackendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: Goods,
            clickListener: ((goods: Goods, view: View) -> Unit)?
        ) {
            binding.tvName.text = item.listFields?.name
            binding.tvPrice.text = item.listFields?.price
            binding.tvQuantity.text = item.listFields?.quantity
            binding.ivGoods.setImageResource(
                item.listFields?.imageDrawable ?: R.drawable.ic_mock_image_1
            )
            binding.ivGoods.transitionName = item.name
            binding.container.setOnClickListener {
                clickListener?.invoke(item, binding.ivGoods)
            }
        }
    }
}
