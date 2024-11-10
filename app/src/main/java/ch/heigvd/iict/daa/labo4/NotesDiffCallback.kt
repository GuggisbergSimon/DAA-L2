package ch.heigvd.iict.daa.labo4

import androidx.recyclerview.widget.DiffUtil
import ch.heigvd.iict.daa.labo4.models.Note

class NotesDiffCallback(private val oldList: List<Note>, private val newList: List<Note>) :
    DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].noteId == newList[newItemPosition].noteId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]
        return old::class == new::class && old.title == new.title
    }
}