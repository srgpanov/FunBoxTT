package com.srgpanov.funboxtt.data

import androidx.room.*
import com.srgpanov.funboxtt.data.entity.Goods
import kotlinx.coroutines.flow.Flow

@Dao
abstract class GoodsDao {

    @Query("SELECT * FROM goods")
    abstract  fun getAllGoods(): Flow<List<Goods>>

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertGoods(goods: Goods):Long

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertList(goods: List<Goods>)

    @Update
    abstract suspend fun updateGoods(goods: Goods)

    @Transaction
    open suspend fun insertOrUpdateGoods(goods: Goods) {
        val id = insertGoods(goods)
        if (id == -1L) updateGoods(goods)
    }
}
