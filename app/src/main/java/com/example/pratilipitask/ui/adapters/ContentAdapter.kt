package com.example.pratilipitask.ui.adapters

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.toSpannable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pratilipitask.data.entities.Data
import com.example.pratilipitask.databinding.ViewDataBinding
import com.example.pratilipitask.ui.ui_utils.NoteDiffUtil
import com.example.pratilipitask.utils.UtilFunctions.fromHtml
import com.example.pratilipitask.utils.UtilFunctions.span


class ContentAdapter(val listener: (Long)->Unit) : RecyclerView.Adapter<ContentAdapter.ViewHolder>() {

    private var dataList: MutableList<Data> = mutableListOf()

    inner class ViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(holder: ViewHolder, position: Int) {
            with(holder) {
                with(dataList[position]) {
                    this.imagePos?.forEach {
                        if(it.isTitle) {
                            val spannableStringBuilder = SpannableStringBuilder(this.title.fromHtml())
                            binding.titleText.text = spannableStringBuilder.apply {
                                setSpan(it.image.span(), it.position,it.position+1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                            }
                        } else {

                            val spannableStringBuilder = SpannableStringBuilder(this.description?.fromHtml())
                            binding.descriptionText.text =
                                spannableStringBuilder.apply {
                                    setSpan(it.image.span(), it.position,it.position+1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                                }
                        }
                    }
                    binding.titleText.text = this.title.fromHtml()
                    binding.descriptionText.text = this.description?.fromHtml()

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