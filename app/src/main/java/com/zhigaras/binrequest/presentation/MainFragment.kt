package com.zhigaras.binrequest.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.zhigaras.binrequest.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {
    
    companion object {
        fun newInstance() = MainFragment()
    }
    
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: MainViewModel by viewModels { viewModelFactory }
    
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    
    private val searchHistoryAdapter = SearchRequestAdapter {number -> onItemClick(number)}
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.recyclerView.adapter = searchHistoryAdapter

        setUpReplyCollector()
        setUpErrorCollector()
        setUpLoadingStateWatcher()
        setUpInputWatcher()
        setUpBinSearchListener()
        setUpRequestHistoryFlow()
        setUpClearButtonListener()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    private fun setUpReplyCollector() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.replyFlow.collect { binReply ->
                    binReply?.let { it -> binding.cardInfoViewGroup.setUpCard(it) }
                }
            }
        }
    }
    
    /**
     * Ошибки поска номера выводятся тостами. */
    private fun setUpErrorCollector() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.requestErrorChannel.collect {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    private fun setUpLoadingStateWatcher() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect {
                    binding.startBinSearchButton.isEnabled = !it
                    binding.progressBar.isVisible = it
                }
            }
        }
    }
    
    private fun setUpInputWatcher() {
        binding.binNumberInput.addTextChangedListener(textWatcher)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.inputErrorFlow.collect {
                    binding.binInputLayout.error = it
                }
            }
        }
    }
    
    private fun setUpBinSearchListener() {
        binding.startBinSearchButton.setOnClickListener {
            val number = binding.binNumberInput.text.toString()
            binding.binNumberInput.text = null
            viewModel.addRequestToPrefs(number)
            viewModel.checkBin(number)
        }
    }
    
    private fun setUpRequestHistoryFlow() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.requestHistoryFlow.collect {
                    val historyList = it.toMutablePreferences()
                        .asMap()
                        .mapKeys { (k, _) -> k.name }
                        .mapValues { (_, v) -> v.toString().toLongOrNull() }
                        .toList()
                        .sortedBy { (_,v) -> v }
                        .reversed()
                        .map { (k,_) -> k }
                    withContext(Dispatchers.Main) {
                        searchHistoryAdapter.setData(historyList)
                    }
                }
            }
        }
    }
    
    private fun setUpClearButtonListener() {
        binding.clearText.apply {
            paint.isUnderlineText = true
            setOnClickListener {
                viewModel.clearPrefs()
            }
        }
    }
    
    /**
     * По клику на элемнт recyclerView номер из этого элемента попадает в поле для ввода номера
     * карты. */
    private fun onItemClick(number: String) {
        binding.binNumberInput.setText(number)
    }
    /**
     * TextWatcher через viewModel проверяет количество введенных символов и запрещает поиск
     * если их меньше 4х. */
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val isValid = viewModel.validateInput(s!!)
            binding.startBinSearchButton.isEnabled = isValid
        }
        
        override fun afterTextChanged(s: Editable?) {}
    }
    
}