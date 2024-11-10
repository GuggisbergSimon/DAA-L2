package ch.heigvd.iict.daa.labo4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.iict.daa.labo4.models.Note
import ch.heigvd.iict.daa.labo4.models.NoteAndSchedule

class NotesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycler = view.findViewById<RecyclerView>(R.id.recycler)
        val adapter = RecyclerAdapter()
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this.context)
        adapter.items = listOf(
            NoteAndSchedule(Note.generateRandomNote(), Note.generateRandomSchedule()),
            NoteAndSchedule(Note.generateRandomNote(), Note.generateRandomSchedule()),
            NoteAndSchedule(Note.generateRandomNote(), Note.generateRandomSchedule()),
            NoteAndSchedule(Note.generateRandomNote(), Note.generateRandomSchedule()),
            NoteAndSchedule(Note.generateRandomNote(), Note.generateRandomSchedule()),
            NoteAndSchedule(Note.generateRandomNote(), Note.generateRandomSchedule()),
        )
    }
}