package ch.heigvd.iict.daa.labo4

import androidx.lifecycle.MutableLiveData
import ch.heigvd.iict.daa.labo4.models.Note
import ch.heigvd.iict.daa.labo4.models.NoteAndSchedule
import ch.heigvd.iict.daa.labo4.models.Schedule
import ch.heigvd.iict.daa.labo4.room.NoteDAO
import kotlin.concurrent.thread

class DataRepository(private val dao: NoteDAO) {
    //TODO check notes change are updated properly
    var allNotes = dao.getAllNotes()
    var countNotes = dao.countNotes()

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
            allNotes = dao.getAllNotesByDate()
        }
    }

    fun sortByETA() {
        thread {
            allNotes = dao.getAllNotesByETA()
        }
    }

    fun deleteAll() {
        thread {
            dao.deleteAll()
        }
    }
}
