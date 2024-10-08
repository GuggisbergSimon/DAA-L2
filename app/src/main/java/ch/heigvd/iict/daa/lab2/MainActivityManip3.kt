package ch.heigvd.iict.daa.lab2

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ch.heigvd.iict.daa.template.R


class MainActivityManip3 : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val backButton = findViewById<Button>(R.id.back)
        backButton.setOnClickListener {
            println("Test")
            // Code here executes on main thread after user presses button
        }
        val exitButton = findViewById<Button>(R.id.exit)
        exitButton.setOnClickListener {
            println("Teest")
            // Code here executes on main thread after user presses button
        }
        val nextButton = findViewById<Button>(R.id.forward)
        nextButton.setOnClickListener{
            println("test2")
        }
    }
}