package com.srgpanov.funboxtt.data.storage

import com.srgpanov.funboxtt.data.entity.Goods
import kotlinx.coroutines.flow.Flow


interface GoodsStorage {
    fun getAllGoods(): Flow<List<Goods>>
    suspend fun insertGoods(goods: Goods)
}