package ch.heigvd.iict.daa.labo4

import androidx.lifecycle.MutableLiveData
import ch.heigvd.iict.daa.labo4.models.Note
import ch.heigvd.iict.daa.labo4.models.NoteAndSchedule
import ch.heigvd.iict.daa.labo4.models.Schedule
import ch.heigvd.iict.daa.labo4.room.NoteDAO
import kotlin.concurrent.thread

class DataRepository(private val dao: NoteDAO) {
    //TODO check notes change are updated properly
    val allNotes = MutableLiveData<List<NoteAndSchedule>>(listOf())
    val countNotes = MutableLiveData(0L)

    init {
        thread {
            allNotes.postValue(dao.getAllNotes().value)
            countNotes.postValue(dao.countNotes())
        }
    }

    fun insertNote(note : Note, schedule: Schedule?) {
        thread {
            val pid = dao.insert(note)
            if (schedule != null) {
                schedule.ownerId = pid
                dao.insert(schedule)
            }
        }
    }

    fun sortByDate() {
        thread {
            allNotes.postValue(dao.getAllNotesByDate().value)
        }
    }

    fun sortByETA() {
        thread {
            allNotes.postValue(dao.getAllNotesByETA().value)
        }
    }

    fun deleteAll() {
        thread {
            dao.deleteAll()
        }
    }
}
