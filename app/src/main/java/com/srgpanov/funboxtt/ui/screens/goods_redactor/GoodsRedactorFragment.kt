package com.srgpanov.funboxtt.ui.screens.goods_redactor

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.github.razir.progressbutton.bindProgressButton
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.transition.MaterialContainerTransform
import com.srgpanov.funboxtt.R
import com.srgpanov.funboxtt.data.entity.Goods
import com.srgpanov.funboxtt.databinding.FragmentGoodsRedactorBinding
import com.srgpanov.funboxtt.other.showLoading
import com.srgpanov.funboxtt.ui.shared_components.goods_components.GoodsSharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class GoodsRedactorFragment : Fragment() {
    private var _binding: FragmentGoodsRedactorBinding? = null
    private val binding: FragmentGoodsRedactorBinding
        get() = _binding!!
    private lateinit var viewModel: GoodsRedactorViewModel
    private val sharedViewModel: GoodsSharedViewModel by activityViewModels()
    private lateinit var takePicture: ActivityResultLauncher<Intent>

    private var goodsArgs: Goods? = null


    companion object {
        const val KEY_GOODS = "KEY_GOODS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }


    private fun initViewModel() {
        goodsArgs = arguments?.getParcelable(KEY_GOODS)
        val factory = GoodsRedactorViewModel.Factory(goodsArgs, this, arguments)
        viewModel = ViewModelProvider(this, factory)[GoodsRedactorViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGoodsRedactorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parseArguments()
        observeViewModel()
        setupListeners()
        setupAnimations()
        setupViews()
    }

    private fun setupViews() {
        if (goodsArgs == null) {
            binding.ivGoods.setImageResource(R.drawable.ic_baseline_image_search)
        }
        viewLifecycleOwner.bindProgressButton(binding.btnSave)
    }

    private fun setupAnimations() {
        val enterTransitions = if (goodsArgs == null) {
            MaterialContainerTransform().apply {
                fadeMode = MaterialContainerTransform.FADE_MODE_CROSS
                duration = 500
                endContainerColor =Color.WHITE
            }
        } else {
            TransitionInflater.from(context).inflateTransition(R.transition.shared_enter_transition)
        }
        sharedElementEnterTransition = enterTransitions
        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.shared_exit_transition)
        val goods: Goods = goodsArgs ?: return
        val imageDrawable = goods.listFields?.imageDrawable ?: return
        val transitionName = goods.name
        binding.ivGoods.transitionName = transitionName
        binding.ivGoods.setImageResource(imageDrawable)
    }

    private fun setupListeners() {
        binding.btnSave.setOnClickListener {
            val goods = parseFields()
            Log.d("GoodsRedactorFragment", "setupListeners: $goods")
            if (goods == null) {
                showError(getString(R.string.redactor_parse_fields_error))
            } else {
                sharedViewModel.saveGoods(goods)
            }
        }


        takePicture = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val selectedImage = it.data?.data
            if (selectedImage != null) {
                Glide.with(this).load(selectedImage).into(binding.ivGoods)
                //сохранение картинки не сделал, потому что модель товара не соержит информации о изоражении
            } else {
                Log.e("GoodsRedactorFragment", "setupListeners: selectedImage null")
            }
        }

        binding.ivGoods.setOnClickListener {
            val takePictureIntent = Intent(Intent.ACTION_PICK)
            takePictureIntent.type = "image/*"
            takePicture.launch(takePictureIntent)
        }

        val nameValidate = binding.textInputLayoutName.validate(
            ::checkName,
            getString(R.string.error_name_message)
        )
        val priceValidate = binding.textInputLayoutPrice.validate()
        val quantityValidate = binding.textInputLayoutQuantity.validate()
        val fieldsIsValid = combine(nameValidate, priceValidate, quantityValidate) { booleans ->
            Log.d("GoodsRedactorFragment", "setupListeners: ${booleans.toList()}")
            booleans.all { it == true }
        }
        lifecycleScope.launch {
            fieldsIsValid
                .onStart { emit(viewModel.goods!=null) }
                .collect { fieldsIsValid ->
                    Log.d("GoodsRedactorFragment", "setupListeners collect: $fieldsIsValid")
                    binding.btnSave.isEnabled = fieldsIsValid
                }
        }
    }

    private fun TextInputLayout.validate(
        checkFunction: suspend (CharSequence) -> Boolean = { it.isNotBlank() },
        errorMessage: String = getString(R.string.error_field_message)
    ): Flow<Boolean> {
        return editText?.textInputAsFlow()
            ?.onStart { emit(this@validate.editText?.text)}
            ?.filterNotNull()
            ?.drop(if (viewModel.goods==null) 1 else 0)
            ?.map(checkFunction)
            ?.onCompletion { error = null }
            ?.onEach { isValid ->
                Log.d("GoodsRedactorFragment", "validate: $isValid")
                error = if (isValid) null else errorMessage
                isErrorEnabled = !isValid
            } ?: flow { emit(false) }
    }

    private fun parseFields(): Goods? {
        val name = binding.etName.text.toString()
        val price = binding.etPrice.text.toString().toFloatOrNull() //отбрасываю копейки
        val quantity = binding.etQuantity.text.toString().toIntOrNull()
        return if (price != null && quantity != null) {
            Goods(
                id = viewModel.goods?.id,
                name = name,
                price = price,
                quantity = quantity
            )
        } else {
            null
        }
    }

    private fun observeViewModel() {
        sharedViewModel.btnSaveEnabled.observe(viewLifecycleOwner) { isEnabled ->
            binding.btnSave.isEnabled = isEnabled
            binding.btnSave.showLoading(!isEnabled,R.string.btn_redactor_save){
                this.buttonText = getString(R.string.saving_in_progress)
            }
        }

        sharedViewModel.savingGoodsCompleted.observe(viewLifecycleOwner) {isSuccess->
            if (isSuccess==true){
                showMessage(getString(R.string.goods_saved))
                findNavController().popBackStack()
            }else{
                showError(getString(R.string.goods_not_saved))
                findNavController().popBackStack()
            }
        }
    }

    private fun parseArguments() {
        binding.etName.setText(viewModel.name.value)
        binding.etPrice.setText(viewModel.price.value?.toFloat()?.toInt()?.toString()?:"")
        binding.etQuantity.setText(viewModel.quantity.value?.toString())
    }

    @Suppress("SameParameterValue")
    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveFields(
            binding.etName.text.toString(),
            binding.etPrice.text.toString().toFloatOrNull(),
            binding.etQuantity.text.toString().toIntOrNull()
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    @ExperimentalCoroutinesApi
    private fun EditText.textInputAsFlow() = callbackFlow {
        val watcher: TextWatcher = doOnTextChanged { textInput: CharSequence?, _, _, _ ->
            offer(textInput)
        }
        awaitClose { this@textInputAsFlow.removeTextChangedListener(watcher) }
    }

    @Suppress("RedundantSuspendModifier")
    private suspend fun checkName(text: CharSequence): Boolean {
        return text.length >= 3
    }

}




