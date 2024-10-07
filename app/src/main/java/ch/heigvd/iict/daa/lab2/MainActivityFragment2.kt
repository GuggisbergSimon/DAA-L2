package ch.heigvd.iict.daa.lab2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

private const val ARG_COUNTER = "param_counter"

//TODO eventually move in its own file
class SimpleFragment : Fragment() {
    private var counter: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            counter = it.getInt(ARG_COUNTER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_counter, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(counter: Int = 0) = SimpleFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_COUNTER, counter)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val counterTv = view.findViewById<TextView>(R.id.f_counter)
        val button = view.findViewById<Button>(R.id.f_counter_increment)
        counterTv.text = "$counter"
        button.setOnClickListener {
            ++counter
            counterTv.text = "$counter"
        }
    }
}

class MainActivityFragment2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_fragment2)
        val buttonBack = findViewById<Button>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            supportFragmentManager.popBackStack()
        }
        val buttonClose = findViewById<Button>(R.id.buttonClose)
        buttonClose.setOnClickListener {
            finish()
        }
        val buttonNext = findViewById<Button>(R.id.buttonNext)
        buttonNext.setOnClickListener {
            val fragment =
                SimpleFragment.newInstance(supportFragmentManager.backStackEntryCount + 1)
            supportFragmentManager.beginTransaction().replace(R.id.framelayout, fragment)
                .addToBackStack(null).commit()
        }
    }
}