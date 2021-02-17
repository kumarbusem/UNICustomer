package com.uni.customer.features.feedback

import android.annotation.SuppressLint
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.uni.customer.R
import com.uni.customer.databinding.FragmentFeedbackBinding
import com.uni.customer.features.dialogs.FeedbackDialog
import com.google.android.gms.location.*
import com.uni.customer.common.*
import kotlinx.android.synthetic.main.fragment_runsheets.*
import java.util.*

class FeedbackFragment : BaseAbstractFragment<FeedbackViewModel, FragmentFeedbackBinding>(R.layout.fragment_feedback){

    override fun setViewModel(): FeedbackViewModel =
            ViewModelProvider(this@FeedbackFragment, ViewModelFactory {
                FeedbackViewModel(requireActivity().application)
            }).get(FeedbackViewModel::class.java)

    private val mUserAdapter: FeedbackListAdapter by lazy { FeedbackListAdapter()}
    private val mPermissionManager: PermissionManager by lazy { PermissionManager(this@FeedbackFragment) }
    private val feedbackDialog: FeedbackDialog by lazy {
        FeedbackDialog(
                onSubmitFeedbackCLicked = { orderId: String, name: String, phone: String ->
                    getLocation(requireContext()) { location ->
                        Log.e("DETAILS:::", "$orderId, $name, $phone, $location")
                        mViewModel.saveFeedback(orderId, name, phone, location)
                    }
                })
    }

    override fun setupViews(): FragmentFeedbackBinding.() -> Unit = {
        toggleBottomBarVisibility(true)
        rvFeedbacksList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mUserAdapter
        }
        srlFeedbacks.setOnRefreshListener {
            mBinding.srlFeedbacks.isRefreshing = false
            mViewModel.getFeedbackList()
        }
        mBinding.btnAddFeedback.setOnClickListener {
            initLoation(requireActivity(), requireContext())
            if(mPermissionManager.areAllPermissionsGranted())
                feedbackDialog.show (childFragmentManager, FeedbackFragment::class.java.simpleName)
            else
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mPermissionManager.requestAllPermissions()
                }
        }

    }

    override fun setupObservers(): FeedbackViewModel.() -> Unit = {

        obsFeedbackList.observe(viewLifecycleOwner, Observer { runsheets ->
            if (runsheets.isNullOrEmpty()) {
                mUserAdapter.submitList(emptyList())
            } else {
                mUserAdapter.submitList(runsheets.toMutableList())
            }
        })
        obsIsDetailsSubmitted.observe(viewLifecycleOwner, Observer { it ->
            if (it == true) {
                if (feedbackDialog.isVisible){
                    feedbackDialog.dismiss()
                    mViewModel.getFeedbackList()
                }
            }
        })

    }



    override fun onResume() {
        super.onResume()
    }


}