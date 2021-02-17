package com.uni.customer.features.address.selectAddress


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.uni.customer.R
import com.uni.customer.common.*
import com.uni.customer.databinding.FragmentSelectAddressBinding
import java.util.*


class SelectAddressFragment :  BaseAbstractFragment<SelectAddressViewModel, FragmentSelectAddressBinding>(R.layout.fragment_select_address) {

    override fun setViewModel(): SelectAddressViewModel =
            ViewModelProvider(this@SelectAddressFragment, ViewModelFactory {
                SelectAddressViewModel(requireActivity().application)
            }).get(SelectAddressViewModel::class.java)

    override fun setupViews(): FragmentSelectAddressBinding.() -> Unit = {

        toggleBottomBarVisibility(false)

        initPlaceAutoComplete()

    }

    private fun initPlaceAutoComplete() {
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), getString(R.string.api_key), Locale.US);
        }
        // Initialize the AutocompleteSupportFragment.
        var autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {

                Log.e("PLACE:::::", "Place: ${place.name}, ${place.id}")
            }

            override fun onError(status: Status) {

                Log.e("PLACE:::::", "An error occurred: $status")
            }
        })

    }

    override fun setupObservers(): SelectAddressViewModel.() -> Unit = {

    }


}
