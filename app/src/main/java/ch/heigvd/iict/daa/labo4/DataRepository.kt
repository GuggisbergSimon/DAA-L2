package ch.heigvd.iict.daa.labo4

import ch.heigvd.iict.daa.labo4.models.Note
import ch.heigvd.iict.daa.labo4.models.Schedule
import ch.heigvd.iict.daa.labo4.room.NoteDAO
import kotlin.concurrent.thread

class DataRepository(private val dao: NoteDAO) {
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

    fun deleteAll() {
        thread {
            dao.deleteAll()
        }
    }
}
