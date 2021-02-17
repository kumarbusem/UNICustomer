package com.uni.customer.features.services

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.uni.customer.R
import com.uni.customer.common.BaseAbstractFragment
import com.uni.customer.common.ViewModelFactory
import com.uni.customer.common.toast
import com.uni.customer.databinding.FragmentServicesBinding

class ServicesFragment : BaseAbstractFragment<ServicesViewModel, FragmentServicesBinding>(R.layout.fragment_services) {

    override fun setViewModel(): ServicesViewModel =
            ViewModelProvider(this@ServicesFragment, ViewModelFactory {
                ServicesViewModel(requireActivity().application)
            }).get(ServicesViewModel::class.java)

    override fun setupViews(): FragmentServicesBinding.() -> Unit = {
        toggleBottomBarVisibility(true)
    }

    override fun setupObservers(): ServicesViewModel.() -> Unit = {

        obsMessage.observe(viewLifecycleOwner, Observer {
            if (!(it.isNullOrEmpty())) {
                requireActivity().toast(it)
            }
        })
    }

}
