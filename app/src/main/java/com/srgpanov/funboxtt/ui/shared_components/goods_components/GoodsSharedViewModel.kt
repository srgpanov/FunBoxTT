package com.srgpanov.funboxtt.ui.shared_components.goods_components

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.srgpanov.funboxtt.R
import com.srgpanov.funboxtt.data.Repository
import com.srgpanov.funboxtt.data.entity.Goods
import com.srgpanov.funboxtt.domain.FillerGoods
import com.srgpanov.funboxtt.other.NonNullMutableLiveData
import com.srgpanov.funboxtt.other.SingleLiveEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
class GoodsSharedViewModel @ViewModelInject constructor(
    private val repository: Repository,
    private val goodsFiller: FillerGoods,
    @ApplicationContext val context: Context
) : ViewModel() {

    val goodsList: NonNullMutableLiveData<MutableList<Goods>> =
        NonNullMutableLiveData(mutableListOf())

    val btnSaveEnabled = NonNullMutableLiveData(true)


    val purchaseCompleted = SingleLiveEvent<Goods>()
    val savingGoodsCompleted = SingleLiveEvent<Boolean>()
    val errorEvent = SingleLiveEvent<String>()

    init {
        viewModelScope.launch {
            repository
                .getAllGoods()
                .map { list ->
                    list.filter { it.quantity > 0 }
                        .map(goodsFiller::fillGoodsDisplayFields)
                }
                .flowOn(Dispatchers.Default)
                .collect {
                    goodsList.value = it.toMutableList()
                }
        }
    }


    companion object {
        const val DELAY_GOODS_PURCHASED = 3000L //3 seconds
        const val DELAY_GOODS_CHANGED = 5000L //5 seconds
    }

    fun onGoodsPurchased(goods: Goods) {
        viewModelScope.launch {
            val list = goodsList.value
            for ((index, item) in list.withIndex()) {
                if (item == goods) {
                    list[index] =
                        item.copy(listFields = item.listFields?.copy(showLoading = true))
                    goodsList.value = goodsList.value
                    break
                }
            }
            delay(DELAY_GOODS_PURCHASED)
            if (goodsWasChanged(goods)) {
                errorEvent.value = context.getString(R.string.purchase_error)
            } else {
                repository.addOrUpdateGoods(goods.copy(quantity = goods.quantity - 1))
                purchaseCompleted.value = goods
            }
        }
    }

    private fun goodsWasChanged(goods: Goods): Boolean {
        val list = goodsList.value
        val changedGoods = list.find { it.id != null && it.id == goods.id } ?: return true
        if (changedGoods.quantity < 1) return true
        return false
    }

    fun saveGoods(goods: Goods) {
        viewModelScope.launch {
            btnSaveEnabled.value = false
            delay(DELAY_GOODS_CHANGED)
            if (goodsWasChanged(goods)){
                savingGoodsCompleted.value=false
            }else{
                repository.addOrUpdateGoods(goods)
                savingGoodsCompleted.value =true
            }
            btnSaveEnabled.value = true
        }
    }



}
