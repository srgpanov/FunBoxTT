package com.srgpanov.funboxtt.domain

import android.content.Context
import com.srgpanov.funboxtt.R
import com.srgpanov.funboxtt.data.entity.Goods
import com.srgpanov.funboxtt.data.entity.GoodsListItem
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class StoreFiller @Inject constructor(
    private val imageGenerator: ImageGenerator,
    @ApplicationContext private val context: Context
) : FillerGoods {
    override fun fillGoodsDisplayFields(goods: Goods): Goods {
        val listFields = goods.listFields
        return if (listFields == null) {
            goods.copy(
                listFields = GoodsListItem(
                    name = goods.name,
                    price = "${goods.price.toInt()} ${context.getString(R.string.rub)}", //отбрасываю копейки
                    imageDrawable = imageGenerator.generateImage(),
                    quantity = context.getString(R.string.sets_available, goods.quantity)
                )
            )
        } else {
            goods.copy(
                listFields = goods.listFields.copy(
                    showLoading = false
                )
            )
        }
    }
}