package com.uni.customer.features.login

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.uni.customer.R
import com.uni.customer.common.BaseAbstractFragment
import com.uni.customer.common.ViewModelFactory
import com.uni.customer.common.toast
import com.uni.customer.databinding.FragmentLoginBinding

class LoginFragment : BaseAbstractFragment<LoginViewModel, FragmentLoginBinding>(R.layout.fragment_login) {

    override fun setViewModel(): LoginViewModel =
            ViewModelProvider(this@LoginFragment, ViewModelFactory {
                LoginViewModel(requireActivity().application)
            }).get(LoginViewModel::class.java)

    override fun setupViews(): FragmentLoginBinding.() -> Unit = {

        toggleBottomBarVisibility(false)
        btnLogin.setOnClickListener {
            if (etUserName.text.toString().isEmpty() || etPassword.text.toString().isEmpty()) {
                requireActivity().toast("Please Enter Username & Password")
            } else {
                viewModel?.loginUser(etUserName.text.toString(), etPassword.text.toString())
            }
        }
    }

    override fun setupObservers(): LoginViewModel.() -> Unit = {

        obsIsUserAuthenticated.observe(viewLifecycleOwner, Observer {user ->
            if (user.self_declaration) {
                navigateToHome()
            }else{
                navigateToProfileUpdate()
            }
        })

        obsMessage.observe(viewLifecycleOwner, Observer {
            if (!(it.isNullOrEmpty())) {
                showToast(it)
            }
        })
    }

    private fun navigateToHome() {
        navigateById(R.id.action_loginFragment_to_homeFragment)
    }
    private fun navigateToProfileUpdate() {
        navigateById(R.id.action_loginFragment_to_homeFragment)
    }
}
