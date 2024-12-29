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
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var textView: TextView

    /**
     * Create an instance of the NFC adapter class.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get the default NFC adapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
    }

    /**
     * An NFC intent filter is a way to specify which types of NFC tags your app can read.
     */
    private fun createNFCIntentFilter(): Array<IntentFilter> {
        val intentFilter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        try {
            intentFilter.addDataType("*/*")
        } catch (e: IntentFilter.MalformedMimeTypeException) {
            throw RuntimeException("Failed to add MIME type.", e)
        }
        return arrayOf(intentFilter)
    }

    /**
     * Handle NFC intent
     */
    @OptIn(ExperimentalStdlibApi::class)
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.action == NfcAdapter.ACTION_TAG_DISCOVERED) {
            val tag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(NfcAdapter.EXTRA_TAG, Tag::class.java)
            } else {
                intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            }
            tag?.id?.let {
                val tagValue = it.toHexString()
                Toast.makeText(this, "NFC tag detected: $tagValue", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Handle foreground dispatch
     */
    override fun onResume() {
        super.onResume()
        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_IMMUTABLE
        )
        val intentFilters = arrayOf<IntentFilter>(
            IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED),
            IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED),
            IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
        )
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null)
    }

    /**
     * Disable foreground dispatch
     */
    override fun onPause() {
        super.onPause()
        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcAdapter.disableForegroundDispatch(this)
    }

    /**
     * Convert Tag ID to hexadecimal string
     */
    fun ByteArray.toHexString(): String {
        val hexChars = "0123456789ABCDEF"
        val result = StringBuilder(size * 2)

        map { byte ->
            val value = byte.toInt()
            val hexChar1 = hexChars[value shr 4 and 0x0F]
            val hexChar2 = hexChars[value and 0x0F]
            result.append(hexChar1)
            result.append(hexChar2)
        }

        return result.toString()
    }
}