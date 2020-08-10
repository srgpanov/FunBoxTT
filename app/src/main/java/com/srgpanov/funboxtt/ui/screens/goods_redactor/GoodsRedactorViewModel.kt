package com.srgpanov.funboxtt.ui.screens.goods_redactor

import android.os.Bundle
import android.text.Editable
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.srgpanov.funboxtt.data.entity.Goods

class GoodsRedactorViewModel constructor(
    val goods: Goods?,
    private val savedState: SavedStateHandle
) : ViewModel() {

    val name: MutableLiveData<String> =
        savedState.getLiveData<String>(KEY_NAME, goods?.name)

    val price: LiveData<Float> = savedState.getLiveData<Float>(KEY_PRICE, goods?.price)


    val quantity = savedState.getLiveData<Int>(KEY_QUANTITY, goods?.quantity)


    companion object {
        const val KEY_NAME = "KEY_NAME"
        const val KEY_PRICE = "KEY_PRICE"
        const val KEY_QUANTITY = "KEY_QUANTITY"
    }


    fun saveFields(name: String?, price: Float?, quantity: Int?) {
        savedState.set(KEY_NAME, name)
        savedState.set(KEY_PRICE, price)
        savedState.set(KEY_QUANTITY, quantity)
    }


    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val goods: Goods?, owner: SavedStateRegistryOwner, defArgs: Bundle?
    ) : AbstractSavedStateViewModelFactory(owner, defArgs) {
        override fun <T : ViewModel?> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T {
            if (modelClass.isAssignableFrom(GoodsRedactorViewModel::class.java)) {
                return (GoodsRedactorViewModel(goods, handle) as T)
            } else {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

}
