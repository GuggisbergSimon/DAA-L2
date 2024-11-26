package ch.heigvd.iict.daa.labo4.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.iict.daa.labo4.R
import ch.heigvd.iict.daa.labo4.models.State
import ch.heigvd.iict.daa.labo4.models.NoteAndSchedule
import ch.heigvd.iict.daa.labo4.models.Type
import java.util.Calendar

class RecyclerAdapter(_items: List<NoteAndSchedule> = listOf()) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    var items = listOf<NoteAndSchedule>()
        set(value) {
            val diffCallback = NotesDiffCallback(items, value)
            val diffItems = DiffUtil.calculateDiff(diffCallback)
            field = value
            diffItems.dispatchUpdatesTo(this)
        }

    init {
        items = _items
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        if (items[position].schedule != null) {
            return SCHEDULE
        }
        return NOTE
    }

    companion object {
        private const val NOTE = 1
        private const val SCHEDULE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            SCHEDULE -> ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.list_item_schedule, parent, false)
            )
            else -> ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.list_item_note, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    enum class SortBy {
        DATE,
        ETA
    }

    fun sortByDate(sortBy: SortBy) {
        items = when (sortBy) {
            SortBy.DATE -> items.sortedBy { it.note.creationDate.timeInMillis }
            SortBy.ETA -> items.sortedBy { it.schedule?.date?.timeInMillis }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val icon = view.findViewById<ImageView>(R.id.list_item_icon)
        private val title = view.findViewById<TextView>(R.id.list_item_title)
        private val text = view.findViewById<TextView>(R.id.list_item_text)
        private val progressIcon = view.findViewById<ImageView>(R.id.list_item_progress_icon)
        private val progressText = view.findViewById<TextView>(R.id.list_item_progress_text)

        fun bind(ns: NoteAndSchedule) {
            icon?.setImageResource(
                when (ns.note.type) {
                    Type.NONE -> R.drawable.note
                    Type.SHOPPING -> R.drawable.shopping
                    Type.TODO -> R.drawable.todo
                    Type.WORK -> R.drawable.work
                    Type.FAMILY -> R.drawable.family
                }
            )

            if (ns.note.state == State.DONE) {
                icon?.setColorFilter(ContextCompat.getColor(itemView.context, R.color.green))
            }

            if (ns.schedule != null) {
                val today = Calendar.getInstance()
                val monthsDifference = ns.schedule.date.get(Calendar.MONTH) - today.get(Calendar.MONTH)
                if (monthsDifference < 0) {
                    progressText?.text = itemView.context.getString(R.string.Late)
                    progressIcon?.setColorFilter(ContextCompat.getColor(itemView.context, R.color.red))
                } else {
                    progressText?.text = String.format("$monthsDifference " + itemView.context.getString(
                        R.string.Months))
                }
            }

            title?.text = ns.note.title
            text?.text = ns.note.text
        }
    }
}