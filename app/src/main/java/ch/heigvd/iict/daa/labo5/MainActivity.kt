package ch.heigvd.iict.daa.labo5

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var imageAdapter: ImageAdapter
    // Pour exécuter la tâche ponctuellement
    private val clearCacheRequest = OneTimeWorkRequestBuilder<ClearCacheWorker>().build()

    // Pour exécuter la tâche de manière périodique
    private val periodicClearCacheRequest = PeriodicWorkRequestBuilder<ClearCacheWorker>(15, TimeUnit.MINUTES).build()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.main_menu_clear_cache -> {
                WorkManager.getInstance(this).enqueue(clearCacheRequest)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            imageAdapter.clearJobs()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Pour exécuter la tâche de manière périodique
        WorkManager.getInstance(this).enqueue(periodicClearCacheRequest)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        imageAdapter = ImageAdapter(List(10000) { it })
        ImageAdapter.setInstance(imageAdapter)
        recyclerView.adapter = imageAdapter
    }
}