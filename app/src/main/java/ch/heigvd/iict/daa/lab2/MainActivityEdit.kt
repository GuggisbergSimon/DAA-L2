package ch.heigvd.iict.daa.lab2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivityEdit : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_edit)
        val textField = findViewById<EditText>(R.id.nameView)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val fieldValue = textField.text.toString()
            val data = Intent()
            data.putExtra(ASK_FOR_NAME_RESULT_KEY, fieldValue)
            setResult(RESULT_OK, data)
            finish()
        }
    }

    companion object {
        const val ASK_FOR_NAME_RESULT_KEY = "NAME_KEY"
    }
}