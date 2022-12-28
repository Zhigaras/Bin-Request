package com.zhigaras.binrequest.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.zhigaras.binrequest.databinding.FragmentMainBinding
import kotlinx.coroutines.launch

class MainFragment : Fragment() {
    
    companion object {
        fun newInstance() = MainFragment()
    }
    
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: MainViewModel by viewModels { MainViewModelFactory() }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        
        startBinSearchListener()
        
        binding.binNumberInput.addTextChangedListener(textWatcher)
        
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.replyFlow.collect {
                    if (it != null) {
                        binding.message.text = it.toString()
                        binding.cardInfoViewGroup.setUpCard(it)
                    } else {
                        binding.message.text = "Oooops"
                    }
                }
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.requestErrorChannel.collect {
                    binding.errorTextView.text = it
                }
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.inputErrorFlow.collect {
                    binding.binInputLayout.error = it
                }
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    private fun startBinSearchListener() {
        binding.startBinSearchButton.setOnClickListener {
            val number = binding.binNumberInput.text.toString()
            viewModel.checkBin(number)
        }
    }
    
    /**Хотел использовать TextWatcher, но ограничивает лимит в 10 запросов в минуту.
     * Понятно, что для реального приложение будет куплен ключ, но без ключа и с TextWatcher даже
     * протестировать толком не получится.
     * А также, учитывая пункт 2 ТЗ, предположил, что запрос должен быть именно по нажатию
     * на кнопку.*/
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val isValid = viewModel.validateInput(s!!)
            binding.startBinSearchButton.isEnabled = isValid
        }
        
        override fun afterTextChanged(s: Editable?) {}
    }
    
}