package ch.heigvd.iict.daa.labo4.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ch.heigvd.iict.daa.labo4.MyApp
import ch.heigvd.iict.daa.labo4.NotesViewModel
import ch.heigvd.iict.daa.labo4.NotesViewModelFactory
import ch.heigvd.iict.daa.labo4.R

class ControlsFragment : Fragment() {
    private val notesViewModel: NotesViewModel by activityViewModels {
        NotesViewModelFactory((requireActivity().application as MyApp).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_controls, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val counterNotes = view.findViewById<TextView>(R.id.counter)
        val generateBtn = view.findViewById<Button>(R.id.btnGenerate)
        val deleteBtn = view.findViewById<Button>(R.id.btnDelete)

        notesViewModel.countNotes.observe(viewLifecycleOwner) {
            counterNotes.text = it.toString()
        }

        generateBtn.setOnClickListener {
            notesViewModel.generateANote()
        }

        deleteBtn.setOnClickListener {
            notesViewModel.deleteAllNote()
        }
    }
}