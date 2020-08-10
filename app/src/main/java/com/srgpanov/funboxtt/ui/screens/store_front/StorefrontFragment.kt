package com.srgpanov.funboxtt.ui.screens.store_front

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.google.android.material.snackbar.Snackbar
import com.srgpanov.funboxtt.R
import com.srgpanov.funboxtt.data.entity.Goods

import com.srgpanov.funboxtt.databinding.FragmentStorefrontBinding
import com.srgpanov.funboxtt.ui.shared_components.goods_components.GoodsSharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class StorefrontFragment : Fragment() {
    private var _binding: FragmentStorefrontBinding? = null
    private val binding: FragmentStorefrontBinding
        get() = _binding!!
    private val viewModel: GoodsSharedViewModel by activityViewModels()
    private val adapter: StoreAdapter by lazy { StoreAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStorefrontBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        adapter.purchaseCallback = {item: Goods ->
            viewModel.onGoodsPurchased(item)
        }

    }

    private fun setupViews() {
        binding.pager.adapter = adapter
        adapter.lifecycleOwner=viewLifecycleOwner
    }


    private fun observeViewModel() {
        viewModel.goodsList.observe(viewLifecycleOwner) { goodsList ->
            lifecycleScope.launch { adapter.setData(goodsList) }
        }
        viewModel.purchaseCompleted.observe(viewLifecycleOwner) { goods ->
            val message = goods?.name + getString(R.string.message_purchased_success)
            binding.pager.showSnackBar(message)
        }
        viewModel.errorEvent.observe(viewLifecycleOwner){
            binding.pager.showSnackBar(it?:"error")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun View.showSnackBar(text: String, duration: Int = Snackbar.LENGTH_SHORT) {
        Snackbar.make(this, text, duration).show()
    }
}
