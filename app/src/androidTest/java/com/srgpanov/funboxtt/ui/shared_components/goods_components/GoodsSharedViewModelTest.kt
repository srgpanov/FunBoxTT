package com.srgpanov.funboxtt.ui.shared_components.goods_components

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.srgpanov.funboxtt.data.Repository
import com.srgpanov.funboxtt.data.entity.Goods
import com.srgpanov.funboxtt.domain.FillerGoods
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GoodsSharedViewModelTest {

    @Mock
    private lateinit var fakeRepository: Repository

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var filler: FillerGoods

    private lateinit var viewModel: GoodsSharedViewModel

    @Before
    fun setUp() {
        viewModel = GoodsSharedViewModel(fakeRepository, filler, context)
    }

    @After
    fun tearDown() {
    }


}