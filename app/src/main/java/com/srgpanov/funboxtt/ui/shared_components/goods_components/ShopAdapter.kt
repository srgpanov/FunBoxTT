package com.srgpanov.funboxtt.ui.shared_components.goods_components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.srgpanov.funboxtt.data.entity.Goods
import com.srgpanov.funboxtt.databinding.ItemGoodsStorefrontBinding
import kotlinx.coroutines.*

abstract class ShopAdapter<VH:RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    val items: MutableList<Goods> = mutableListOf()
    private var changeDataJob: Job? = null


    override fun getItemCount(): Int {
        return items.size
    }


    suspend fun setData(newList: List<Goods>) {
        changeDataJob?.cancel()
        changeDataJob = coroutineScope {
            launch {
                val diffResult = withContext(Dispatchers.Default) {
                    val diff = GoodsDiffCallBack(items, newList)
                    return@withContext DiffUtil.calculateDiff(diff)
                }
                withContext(Dispatchers.Main) {
                    items.clear()
                    items.addAll(newList)
                    diffResult.dispatchUpdatesTo(this@ShopAdapter)
                }
            }
        }

    }

}