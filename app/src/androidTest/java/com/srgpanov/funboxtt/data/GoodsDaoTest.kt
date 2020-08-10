package com.srgpanov.funboxtt.data

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.srgpanov.funboxtt.data.entity.Goods
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GoodsDaoTest {
    lateinit var db: GoodsDataBase
    lateinit var dao: GoodsDao

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            GoodsDataBase::class.java
        ).build()
        dao = db.goodsDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertAndRead() = runBlocking {
        val goods = GoodsTestHelper.getRandomGoods()
        dao.insertOrUpdateGoods(goods)
        val flow = dao.getAllGoods()
        flow.take(1).collect { list ->
            assertEquals(list[0], goods)
        }

    }

    @Test
    fun insertListAndRead() = runBlocking {
        val goods = GoodsTestHelper.createListOfGoods().sortedBy { it.id }
        dao.insertList(goods)
        val flow = dao.getAllGoods()
        flow.take(1).collect { list ->
            assertEquals(goods, list.sortedBy { it.id })
        }
    }




}