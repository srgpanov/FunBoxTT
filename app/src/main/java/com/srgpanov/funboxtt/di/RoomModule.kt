package com.srgpanov.funboxtt.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.srgpanov.funboxtt.data.storage.AssetsStorage
import com.srgpanov.funboxtt.data.GoodsDao
import com.srgpanov.funboxtt.data.GoodsDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module()
class RoomModule {
    private lateinit var database: GoodsDataBase

    @ExperimentalCoroutinesApi
    @Singleton
    @Provides
    fun provideDataBase(
        @ApplicationContext context: Context,
        assetsStorage: AssetsStorage
    ): GoodsDataBase {
        database = Room.databaseBuilder(context, GoodsDataBase::class.java, "goodsDatabase")
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    fillDatabaseFromAsset(assetsStorage)
                }
            })
            .build()
        return database
    }

    @ExperimentalCoroutinesApi
    private fun fillDatabaseFromAsset(assetsStorage: AssetsStorage) {
        CoroutineScope(Dispatchers.IO).launch {
            val goodsDao = database.goodsDao()
            assetsStorage.getAllGoods().flowOn(Dispatchers.IO).collect {list->
                for (goods in list) {
                    goodsDao.insertGoods(goods)
                }
            }
            Log.d("RoomModule", "fillDatabaseFromAsset: database filled")
        }
    }

    @Singleton
    @Provides
    fun provideGoodsDao(dataBase: GoodsDataBase): GoodsDao {
        return dataBase.goodsDao()
    }
}