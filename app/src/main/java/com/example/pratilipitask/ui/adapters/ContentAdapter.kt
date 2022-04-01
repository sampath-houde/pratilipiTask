package com.example.pratilipitask.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.pratilipitask.R
import com.example.pratilipitask.data.entities.Data
import com.example.pratilipitask.databinding.ViewDataBinding
import com.example.pratilipitask.listeners.OnNoteClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContentAdapter(listener: OnNoteClickListener) : RecyclerView.Adapter<ContentAdapter.ViewHolder>() {

    private var dataList: List<Data> = mutableListOf()
    private var listener: OnNoteClickListener = listener

    inner class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(holder: ViewHolder, position: Int) {
            with(holder) {
                with(dataList[position]) {
                    binding.titleText.text = this.title
                    this.description?.let {
                        binding.descriptionText.text = this.description
                    }

                    binding.parentLay.setOnClickListener {
                        listener.onNoteClicked(this.id)
                    }
                }
            }
        }
    }

    fun setData(list: List<Data>) {
        dataList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(holder, position)
    }

    override fun getItemCount(): Int = dataList.size
}