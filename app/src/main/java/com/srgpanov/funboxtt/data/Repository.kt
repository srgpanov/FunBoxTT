package com.srgpanov.funboxtt.data

import com.srgpanov.funboxtt.data.entity.Goods
import com.srgpanov.funboxtt.data.storage.GoodsStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val storage: GoodsStorage
) {

    @ExperimentalCoroutinesApi
    fun getAllGoods(): Flow<List<Goods>> = storage.getAllGoods()


    suspend fun addOrUpdateGoods(good: Goods) {
        storage.insertGoods(good)
    }
}