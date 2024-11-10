package ch.heigvd.iict.daa.labo4

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.iict.daa.labo4.models.State
import ch.heigvd.iict.daa.labo4.models.Note
import ch.heigvd.iict.daa.labo4.models.Type
import java.util.Locale

class MyRecyclerAdapter(_items: List<Note> = listOf()) :
    RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>() {
    var items = listOf<Note>()
        set(value) {
            field = value
            notifyDataSetChanged() //à éviter, on préférera utiliser DiffUtil pour propager les modifications effectives uniquement (cf. slides suivants)
        }

    init {
        items = _items
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        //TODO
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val icon = view.findViewById<ImageView>(R.id.list_item_icon)
        private val title = view.findViewById<TextView>(R.id.list_item_title)
        private val text = view.findViewById<TextView>(R.id.list_item_text)
        private val progressIcon = view.findViewById<ImageView>(R.id.list_item_progress_icon)
        private val progressText = view.findViewById<TextView>(R.id.list_item_progress_text)
        fun bind(note: Note) {
            icon?.setImageResource(
                when (note.type) {
                    Type.SHOPPING -> R.drawable.shopping
                    Type.TODO -> R.drawable.todo
                    Type.WORK -> R.drawable.work
                    Type.FAMILY -> R.drawable.family
                    Type.NONE -> TODO()
                }
            )

            if (note.state == State.DONE) {
                icon?.setColorFilter(ContextCompat.getColor(itemView.context, R.color.green))
                progressIcon?.visibility = View.INVISIBLE
                progressText?.visibility = View.INVISIBLE
            } else {
                val dateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.LONG, Locale.getDefault())
                //TODO make the progressIcon red if past a due date
                progressText?.text = dateFormat.format(note.creationDate.time)
            }

            title?.text = note.title
            text?.text = note.text
        }
    }
}