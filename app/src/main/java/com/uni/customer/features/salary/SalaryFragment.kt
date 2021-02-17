package com.uni.customer.features.salary

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.uni.customer.R
import com.uni.customer.common.BaseAbstractFragment
import com.uni.customer.common.ViewModelFactory
import com.uni.customer.databinding.FragmentSalaryBinding

class SalaryFragment : BaseAbstractFragment<SalaryViewModel, FragmentSalaryBinding>(R.layout.fragment_salary) {

    override fun setViewModel(): SalaryViewModel =
            ViewModelProvider(this@SalaryFragment, ViewModelFactory {
                SalaryViewModel(requireActivity().application)
            }).get(SalaryViewModel::class.java)

    private val mUserAdapter: SalaryListAdapter by lazy { SalaryListAdapter() }

    override fun setupViews(): FragmentSalaryBinding.() -> Unit = {
        toggleBottomBarVisibility(true)

        toggleBottomBarVisibility(true)
        rvSalaryList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mUserAdapter
        }
        srlSalary.setOnRefreshListener {
            mBinding.srlSalary.isRefreshing = false
            mViewModel.getSalaryList()
        }
    }

    override fun setupObservers(): SalaryViewModel.() -> Unit = {

        obsSalaryList.observe(viewLifecycleOwner, Observer { salaryList ->
            if (salaryList.isNullOrEmpty()) {
                mUserAdapter.submitList(emptyList())
            } else {
                mUserAdapter.submitList(salaryList.toMutableList())
            }
        })
    }

    override fun onResume() {
        super.onResume()
    }

}
