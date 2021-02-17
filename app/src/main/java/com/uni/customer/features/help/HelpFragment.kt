package com.uni.customer.features.help

import androidx.lifecycle.ViewModelProvider
import com.uni.customer.R
import com.uni.customer.common.BaseAbstractFragment
import com.uni.customer.common.ViewModelFactory
import com.uni.customer.databinding.FragmentHelpBinding

class HelpFragment : BaseAbstractFragment<HelpViewModel, FragmentHelpBinding>(R.layout.fragment_help) {

    override fun setViewModel(): HelpViewModel =
            ViewModelProvider(this@HelpFragment, ViewModelFactory {
                HelpViewModel(requireActivity().application)
            }).get(HelpViewModel::class.java)

    override fun setupViews(): FragmentHelpBinding.() -> Unit = {
        toggleBottomBarVisibility(false)
        backButton.setOnClickListener {
            navigateBack()
        }
    }

    override fun setupObservers(): HelpViewModel.() -> Unit = {


    }

}
