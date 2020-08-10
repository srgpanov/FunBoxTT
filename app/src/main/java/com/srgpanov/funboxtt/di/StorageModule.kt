package com.srgpanov.funboxtt.di

import com.srgpanov.funboxtt.data.storage.GoodsStorage
import com.srgpanov.funboxtt.data.storage.RoomStorage
import com.srgpanov.funboxtt.domain.FillerGoods
import com.srgpanov.funboxtt.domain.StoreFiller
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@InstallIn(ApplicationComponent::class)
@Module
abstract class StorageModule {
    @Binds
    abstract fun produceStorage(roomStorage: RoomStorage): GoodsStorage

    @Binds
    abstract fun produceFiller(storeFiller: StoreFiller): FillerGoods
}