package ch.heigvd.iict.daa.labo4

import android.app.Application
import android.content.res.Configuration
import ch.heigvd.iict.daa.labo4.room.MyDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MyApp : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    val repository by lazy {
        val database = MyDatabase.getDatabase(this)
        DataRepository(database.noteDao(), applicationScope)
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }
}