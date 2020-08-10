package com.srgpanov.funboxtt.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.srgpanov.funboxtt.data.entity.Goods

@Database(entities = [Goods::class], version = 1 )
abstract class GoodsDataBase : RoomDatabase() {
    abstract fun goodsDao(): GoodsDao
}