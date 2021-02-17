package com.uni.customer.features.address.runsheetsList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uni.data.models.Runsheet
import com.uni.customer.databinding.ItemRunsheetBinding

class RunsheetsListAdapter(private val callback: ItemSelectionCallback) : ListAdapter<Runsheet, RunsheetsListAdapter.RunsheetViewHolder>(DIFF_UTILS) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunsheetViewHolder {
        return RunsheetViewHolder(ItemRunsheetBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RunsheetViewHolder, pos: Int) {
        holder.bind(getItem(pos))
    }

    inner class RunsheetViewHolder(private val item: ItemRunsheetBinding) : RecyclerView.ViewHolder(item.root) {

        init {

            item.layoutRunsheet.setOnClickListener {
                callback.onSelectRunsheetClick(getItem(adapterPosition))
            }
        }

        fun bind(data: Runsheet) {
            item.data = data
        }
    }

    interface ItemSelectionCallback {
        fun onSelectRunsheetClick(runsheet: Runsheet)
    }

    companion object {

        val TAG: String = RunsheetsListAdapter::class.java.name

        val DIFF_UTILS: DiffUtil.ItemCallback<Runsheet> = object : DiffUtil.ItemCallback<Runsheet>() {
            override fun areItemsTheSame(old: Runsheet, new: Runsheet): Boolean {
                return old.id == new.id
            }

            override fun areContentsTheSame(old: Runsheet, new: Runsheet): Boolean {
                return old == new
            }
        }
    }
}