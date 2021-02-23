package com.uni.customer.features.address.selectAddress

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uni.data.models.Feedback
import com.uni.customer.databinding.ItemFeedbackBinding
import com.uni.customer.databinding.ItemRecentAddressBinding
import com.uni.customer.features.address.runsheetsList.RunsheetsListAdapter
import com.uni.data.models.Runsheet
import com.uni.data.roomDatabase.RecentAddress

class RecentAddressListAdapter(private val callback: ItemSelectionCallback) : ListAdapter<RecentAddress, RecentAddressListAdapter.RecentAddressViewHolder>(DIFF_UTILS) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentAddressViewHolder {
        return RecentAddressViewHolder(ItemRecentAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecentAddressListAdapter.RecentAddressViewHolder, pos: Int) {
        holder.bind(getItem(pos))
    }

    inner class RecentAddressViewHolder(private val item: ItemRecentAddressBinding) : RecyclerView.ViewHolder(item.root) {
        init {
            item.layoutRecentAddress.setOnClickListener {
                callback.onSelectRunsheetClick(getItem(adapterPosition))
            }
        }

        fun bind(data: RecentAddress) {
            item.data = data
        }
    }

    interface ItemSelectionCallback {
        fun onSelectRunsheetClick(recentAddress: RecentAddress)
    }
    companion object {

        val TAG: String = RecentAddressListAdapter::class.java.name

        val DIFF_UTILS: DiffUtil.ItemCallback<RecentAddress> = object : DiffUtil.ItemCallback<RecentAddress>() {
            override fun areItemsTheSame(old: RecentAddress, new: RecentAddress): Boolean {
                return old.name == new.name
            }

            override fun areContentsTheSame(old: RecentAddress, new: RecentAddress): Boolean {
                return old == new
            }
        }
    }
}