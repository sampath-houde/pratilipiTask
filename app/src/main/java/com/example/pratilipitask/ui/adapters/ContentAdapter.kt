package com.example.pratilipitask.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pratilipitask.data.entities.Data
import com.example.pratilipitask.databinding.ViewDataBinding
import com.example.pratilipitask.ui.ui_utils.NoteDiffUtil
import com.example.pratilipitask.utils.UtilFunctions.fromHtml


class ContentAdapter(val listener: (Long)->Unit) : RecyclerView.Adapter<ContentAdapter.ViewHolder>() {

    private var dataList: MutableList<Data> = mutableListOf()

    inner class ViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(holder: ViewHolder, position: Int) {
            with(holder) {
                with(dataList[position]) {
                    binding.titleText.text = this.title.fromHtml()
                    this.description?.let {
                        binding.descriptionText.text = this.description.fromHtml()
                    }

                    binding.parentLay.setOnClickListener {
                        listener(this.id)
                    }
                }
            }
        }
    }

    fun setData(list: List<Data>) {
        val diffCallback = NoteDiffUtil(oldList = dataList, newList = list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        dataList.clear()
        dataList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
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