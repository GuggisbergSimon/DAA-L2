package ch.heigvd.iict.daa.lab2

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

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