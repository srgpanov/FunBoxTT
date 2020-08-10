package com.srgpanov.funboxtt.domain

import com.srgpanov.funboxtt.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageGenerator @Inject constructor() {
    private val imageList: List<Int> = listOf(
        R.drawable.ic_mock_image_1,
        R.drawable.ic_mock_image_2,
        R.drawable.ic_mock_image_3,
        R.drawable.ic_mock_image_4
    )
    fun generateImage():Int{
        return imageList.random()
    }
}