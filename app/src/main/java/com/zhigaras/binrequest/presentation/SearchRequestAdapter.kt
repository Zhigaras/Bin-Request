package com.zhigaras.binrequest.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.zhigaras.binrequest.databinding.SearchRequestItemBinding

class SearchRequestAdapter : Adapter<SearchRequestViewHolder>() {
    
    private var data: List<String> = emptyList()
    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<String>) {
        this.data = data
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchRequestViewHolder {
        return SearchRequestViewHolder(
            SearchRequestItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
            )
        )
    }
    
    override fun onBindViewHolder(holder: SearchRequestViewHolder, position: Int) {
        val item = data.getOrNull(position)
        item?.let {
            holder.binding.itemTextview.text = item
        }
    }
    
    override fun getItemCount(): Int = data.size
}

class SearchRequestViewHolder(val binding: SearchRequestItemBinding) : ViewHolder(binding.root)