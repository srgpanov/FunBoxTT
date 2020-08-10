package com.srgpanov.funboxtt.data.storage

import com.srgpanov.funboxtt.data.GoodsDao
import com.srgpanov.funboxtt.data.entity.Goods
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomStorage @Inject constructor(private val dao: GoodsDao) :
    GoodsStorage {

    override fun getAllGoods(): Flow<List<Goods>> {
        return dao.getAllGoods()
    }

    override suspend fun insertGoods(goods: Goods) {
        dao.insertOrUpdateGoods(goods)
    }
}