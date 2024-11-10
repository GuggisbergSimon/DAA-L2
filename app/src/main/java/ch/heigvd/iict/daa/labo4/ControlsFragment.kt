package ch.heigvd.iict.daa.labo4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class ControlsFragment : Fragment() {
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

        //TODO link through livedata

        generateBtn.setOnClickListener {
            //TODO
        }

        deleteBtn.setOnClickListener {
            //TODO
        }
    }
}