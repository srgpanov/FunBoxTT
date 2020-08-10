package com.srgpanov.funboxtt.ui.screens.back_end

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.srgpanov.funboxtt.R
import com.srgpanov.funboxtt.data.entity.Goods
import com.srgpanov.funboxtt.databinding.FragmentBackendBinding
import com.srgpanov.funboxtt.ui.screens.goods_redactor.GoodsRedactorFragment.Companion.KEY_GOODS
import com.srgpanov.funboxtt.ui.shared_components.goods_components.GoodsSharedViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


@ExperimentalCoroutinesApi
class BackendFragment : Fragment() {
    private var _binding: FragmentBackendBinding? = null
    private val binding: FragmentBackendBinding
        get() = _binding!!
    private val viewModel: GoodsSharedViewModel by activityViewModels()
    private val adapter: BackendAdapter by lazy { BackendAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBackendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupListeners()
        observeViewModel()
        setupAnimations()
    }

    private fun setupAnimations() {
        postponeEnterTransition(300, TimeUnit.MILLISECONDS)
        binding.root.doOnPreDraw { startPostponedEnterTransition() }
    }

    private fun setupListeners() {
        adapter.clickListener = { goods: Goods, view: View ->
            val extras =
                FragmentNavigatorExtras(view to view.transitionName)
            navigateToRedactor(goods, extras)
        }
        binding.fab.setOnClickListener {
            val extras =
                FragmentNavigatorExtras(binding.fab to getString(R.string.no_args_transition))
            navigateToRedactor(null,extras)
        }
    }

    private fun navigateToRedactor(
        goods: Goods? = null,
        extras: Navigator.Extras? = null
    ) {
        val args = bundleOf(KEY_GOODS to goods)

        findNavController().navigate(
            R.id.action_backendFragment_to_goodsRedactorFragment,
            args,
            null,
            extras
        )
    }

    private fun setupViews() {
        binding.rv.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.goodsList.observe(viewLifecycleOwner) { goodsList ->
            lifecycleScope.launch { adapter.setData(goodsList) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d("BackendFragment", "onDestroyView: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("BackendFragment", "onDestroy: ")
    }

}
