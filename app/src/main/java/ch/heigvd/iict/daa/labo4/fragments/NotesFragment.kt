package ch.heigvd.iict.daa.labo4.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.iict.daa.labo4.MyApp
import ch.heigvd.iict.daa.labo4.NotesViewModel
import ch.heigvd.iict.daa.labo4.NotesViewModelFactory
import ch.heigvd.iict.daa.labo4.R
import ch.heigvd.iict.daa.labo4.recyclerView.RecyclerAdapter

class NotesFragment : Fragment() {
    private lateinit var adapter : RecyclerAdapter

    private val notesViewModel: NotesViewModel by viewModels {
        NotesViewModelFactory((requireActivity().application as MyApp).repository)
    }

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
        this.adapter = RecyclerAdapter()
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this.context)
        notesViewModel.allNotes.observe(viewLifecycleOwner) {
            adapter.items = it
        }
    }

    fun sortByDate() {
        adapter.sortByDate(RecyclerAdapter.SortBy.DATE)
    }

    fun sortByETA() {
        adapter.sortByDate(RecyclerAdapter.SortBy.ETA)
    }
}