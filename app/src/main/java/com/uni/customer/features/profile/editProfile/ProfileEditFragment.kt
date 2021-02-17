package com.uni.customer.features.profile.editProfile

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.uni.customer.R
import com.uni.customer.common.*
import com.uni.customer.databinding.FragmentProfileEditBinding
import com.uni.customer.features.dialogs.OTPDialog

class ProfileEditFragment : BaseAbstractFragment<ProfileEditViewModel, FragmentProfileEditBinding>(R.layout.fragment_profile_edit) {
    private val otpDialog: OTPDialog by lazy {
        OTPDialog(
                onSendOTPCLicked = { mViewModel.sendOTP() },
                onSubmitOTPCLicked = { mViewModel.submitOtp(it) })
    }

    override fun setViewModel(): ProfileEditViewModel =
            ViewModelProvider(this@ProfileEditFragment, ViewModelFactory {
                ProfileEditViewModel(requireActivity().application)
            }).get(ProfileEditViewModel::class.java)

    override fun setupViews(): FragmentProfileEditBinding.() -> Unit = {

        toggleBottomBarVisibility(false)
        ivBack.setOnClickListener { navigateBack() }
        btnSave.setOnClickListener {
            saveProfile()
        }
    }

    private fun saveProfile() {

        if (checkProfileParams()){
            if(checkIsBankChanged()){
                mViewModel.sendOTP()
            }else{
                mViewModel.saveProfile {
                    requireActivity().runOnUiThread {
                        showToast(it)
                        mBinding.btnSave.enable()
                    }
                }
            }
        }
    }

    override fun setupObservers(): ProfileEditViewModel.() -> Unit = {

        obsIsProfileUpdated.observe(viewLifecycleOwner, Observer {
            if (it == true)
                navigateBack()
        })
        obsMessage.observe(viewLifecycleOwner, Observer {
            if (!(it.isNullOrEmpty())) {
                showToast(it)
            }
        })
        obsIsOtpSent.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == true) {
                if (!otpDialog.isVisible)
                    otpDialog.show(
                            childFragmentManager,
                            ProfileEditFragment::class.java.simpleName
                    )
            }
        })
        obsIsOtpVerified.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == true) {
                mViewModel.saveProfile {
                    requireActivity().runOnUiThread {
                        showToast(it)
                        mBinding.btnSave.enable()
                    }
                }
            }
        })
    }

    private fun checkProfileParams(): Boolean {
        val aadhar = mViewModel.obsAadhar.value
        val pan = mViewModel.obsPan.value
        val email = mViewModel.obsEmail.value
        val account = mViewModel.obsAccount.value
        val ifsc = mViewModel.obsIFSC.value
        val branch = mViewModel.obsBranch.value
        val alternate = mViewModel.obsAlternate.value

        if (aadhar.isNullOrBlank() || aadhar.length != 12) {
            showToast("Invalid Aadhar Number")
            return false
        } else if (pan.isNullOrBlank() || pan.length != 10) {
            showToast("Invalid Pan Number")
            return false
        } else if (account.isNullOrBlank() || ifsc.isNullOrEmpty() || branch.isNullOrEmpty()) {
            showToast("Invalid Bank Details")
            return false
        } else if (email.isNullOrBlank() || !email.contains('@')) {
            showToast("Invalid Email")
            return false
        } else if (alternate.isNullOrBlank() || alternate.length != 10) {
            showToast("Invalid Phone Number")
            return false
        } else
            return true

    }

    private fun checkIsBankChanged(): Boolean {
        val previous = mViewModel.obsUser.value
        return previous?.bank_account_no != mViewModel.obsAccount.value || previous?.ifsc_no != mViewModel.obsIFSC.value ||
                previous?.branch_name != mViewModel.obsBranch.value
    }
}
