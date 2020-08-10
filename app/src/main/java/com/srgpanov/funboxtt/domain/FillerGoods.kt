package com.srgpanov.funboxtt.domain

import com.srgpanov.funboxtt.data.entity.Goods

interface FillerGoods {
    fun fillGoodsDisplayFields(goods: Goods): Goods
}