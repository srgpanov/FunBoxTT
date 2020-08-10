package com.srgpanov.funboxtt.ui.screens.store_front

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.razir.progressbutton.*
import com.srgpanov.funboxtt.R
import com.srgpanov.funboxtt.data.entity.Goods
import com.srgpanov.funboxtt.databinding.ItemGoodsStorefrontBinding
import com.srgpanov.funboxtt.other.showLoading
import com.srgpanov.funboxtt.ui.screens.back_end.BackendAdapter
import com.srgpanov.funboxtt.ui.screens.store_front.StoreAdapter.GoodsAdapterViewHolder
import com.srgpanov.funboxtt.ui.shared_components.goods_components.GoodsDiffCallBack
import com.srgpanov.funboxtt.ui.shared_components.goods_components.ShopAdapter
import kotlinx.coroutines.*


class StoreAdapter : ShopAdapter<GoodsAdapterViewHolder>() {

    var purchaseCallback: ((goods:Goods) -> Unit) ? = null
    var lifecycleOwner:LifecycleOwner?=null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GoodsAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return GoodsAdapterViewHolder(
            ItemGoodsStorefrontBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: StoreAdapter.GoodsAdapterViewHolder, position: Int) {
        holder.bind(items[position])
    }


    inner class GoodsAdapterViewHolder(private val binding: ItemGoodsStorefrontBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val btnText = binding.root.context.getString(R.string.buy_button_text)
        private val btnLoadingText = binding.root.context.getString(R.string.purchasing_loading)
        init {
            lifecycleOwner?.bindProgressButton(binding.btnBuy)
        }

        fun bind(
            item: Goods
        ) {
            binding.tvName.text = item.listFields?.name
            binding.tvPrice.text = item.listFields?.price
            binding.tvQuantity.text = item.listFields?.quantity
            binding.btnBuy.isEnabled=!(item.listFields?.showLoading ?:false)
            binding.ivPhoto.setImageResource(
                item.listFields?.imageDrawable ?: R.drawable.ic_mock_image_1
            )
            val loading =item.listFields?.showLoading==true
            binding.btnBuy.showLoading(loading,btnText){
                buttonText = btnLoadingText
            }

            binding.btnBuy.setOnClickListener {
                purchaseCallback?.invoke(item)

            }
        }
    }
}
