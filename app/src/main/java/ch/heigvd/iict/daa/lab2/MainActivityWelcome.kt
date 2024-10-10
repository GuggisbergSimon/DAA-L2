package ch.heigvd.iict.daa.lab2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity

//TODO eventually move in its own file
class PickNameContract : ActivityResultContract<Void?, String?>() {
    override fun createIntent(context: Context, input: Void?) =
        Intent(context, MainActivityEdit::class.java)

    override fun parseResult(resultCode: Int, result: Intent?): String? {
        if (resultCode != Activity.RESULT_OK) {
            return null
        }
        return result?.getStringExtra(MainActivityEdit.ASK_FOR_NAME_RESULT_KEY)
    }
}

class MainActivityWelcome : AppCompatActivity() {
    private lateinit var textView: TextView

    private val getName = this.registerForActivityResult(PickNameContract()) {
        if (it == null || it == "") {
            textView.text = getString(R.string.welcome_unnamed)
        } else {
            textView.text = getString(R.string.welcome_named, it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_welcome)
        textView = findViewById(R.id.nameView)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            getName.launch(null)
        }
    }
}