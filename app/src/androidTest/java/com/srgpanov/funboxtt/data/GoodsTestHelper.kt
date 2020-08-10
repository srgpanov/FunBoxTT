package com.srgpanov.funboxtt.data

import com.srgpanov.funboxtt.data.entity.Goods
import kotlin.random.Random

class GoodsTestHelper {
    companion object {
        fun createListOfGoods(): List<Goods> {
            val list = mutableListOf<Goods>()
            for (item in 0..40) {
                list.add(getRandomGoods())
            }
            return list
        }

        fun getRandomGoods(): Goods {
            val list = listOf("samsung", "sony", "apple", "xiomi")
            return Goods(
                id = (0..Int.MAX_VALUE).random().toLong(),
                price = (0..1000000).random().toFloat(),
                quantity = (0..100).random(),
                name = list.random()
            )
        }
    }
}