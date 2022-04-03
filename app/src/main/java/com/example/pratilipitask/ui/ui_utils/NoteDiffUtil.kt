package com.example.pratilipitask.ui.ui_utils

import androidx.recyclerview.widget.DiffUtil
import com.example.pratilipitask.data.entities.Data

class NoteDiffUtil(oldList: List<Data>, newList: List<Data>) : DiffUtil.Callback() {

    private val mOldNoteList: List<Data> = oldList
    private val mNewNoteList: List<Data> = newList

    override fun getOldListSize(): Int = mOldNoteList.size

    override fun getNewListSize(): Int = mNewNoteList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldNoteList.get(oldItemPosition).id == mNewNoteList.get(
            newItemPosition).id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldNote = mOldNoteList.get(oldItemPosition);
        val newNote = mNewNoteList.get(newItemPosition);

        return oldNote.id == newNote.id
    }
}