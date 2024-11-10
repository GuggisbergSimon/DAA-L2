package ch.heigvd.iict.daa.labo4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class ControlsFragment : Fragment() {
    //private val notesViewModel: NotesViewModel by activityViewModels()

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

        //TODO fix livedata linkage
        /*
        notesViewModel.countNotes.observe(viewLifecycleOwner) {
            counterNotes.text = it.toString()
        }
        */

        generateBtn.setOnClickListener {
            //notesViewModel.generateANote()
            print("generate")
        }

        deleteBtn.setOnClickListener {
            //notesViewModel.deleteAllNote()
            print("delete all")
        }
    }
}