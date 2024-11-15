package ch.heigvd.iict.daa.labo4

import android.app.Application
import ch.heigvd.iict.daa.labo4.room.MyDatabase

class MyApp : Application() {
    val repository by lazy {
        val database = MyDatabase.getDatabase(this)
        DataRepository(database.noteDao())
    }
}