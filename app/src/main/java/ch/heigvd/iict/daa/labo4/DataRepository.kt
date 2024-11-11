package ch.heigvd.iict.daa.labo4

import androidx.lifecycle.MutableLiveData
import ch.heigvd.iict.daa.labo4.models.Note
import ch.heigvd.iict.daa.labo4.models.NoteAndSchedule
import ch.heigvd.iict.daa.labo4.room.NoteDAO
import kotlin.concurrent.thread

class DataRepository(private val noteDao: NoteDAO) {
    val allNotes = MutableLiveData<List<NoteAndSchedule>>(listOf())
    val countNotes = MutableLiveData(0)

    fun insertNote(note : Note) {
        thread {
            noteDao.insert(note)
        }
    }

    fun deleteAll() {
        thread {
            noteDao.deleteAll()
        }
    }
}
