package ch.heigvd.iict.daa.template

import android.nfc.Tag
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import android.widget.TextView
import android.content.Intent
import android.content.IntentFilter
import android.app.PendingIntent
import android.nfc.tech.NfcF
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: NfcAdapter
    private lateinit var intentFiltersArray: Array<IntentFilter>
    private lateinit var techListsArray: Array<Array<String>>
    private lateinit var pendingIntent: PendingIntent

    /**
     * Create an instance of the NFC adapter class.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, javaClass).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_MUTABLE
        )
        val ndef = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED).apply {
            try {
                addDataType("*/*")    /* Handles all MIME based dispatches.
                                 You should specify only the ones that you need. */
            } catch (e: IntentFilter.MalformedMimeTypeException) {
                throw RuntimeException("fail", e)
            }
        }
        intentFiltersArray = arrayOf(ndef)
        techListsArray = arrayOf(arrayOf<String>(NfcF::class.java.name))
        adapter = NfcAdapter.getDefaultAdapter(this)

        setContentView(R.layout.activity_main)
    }

    /**
     * Handle NFC intent
     */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val tagFromIntent: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        if (tagFromIntent != null) {
            val tagId = tagFromIntent.id
            val tagIdString = tagId.joinToString(separator = "") { byte -> String.format("%02X", byte) }
            val textView = findViewById<TextView>(R.id.textView)
            textView.text = tagIdString
        } else {
            Toast.makeText(this, "No tag found", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Handle foreground dispatch
     */
    override fun onResume() {
        super.onResume()
        adapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray)
    }

    /**
     * Disable foreground dispatch
     */
    override fun onPause() {
        super.onPause()
        adapter.disableForegroundDispatch(this)
    }
}
