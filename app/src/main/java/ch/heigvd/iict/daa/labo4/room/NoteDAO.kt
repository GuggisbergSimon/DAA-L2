package ch.heigvd.iict.daa.labo4.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import ch.heigvd.iict.daa.labo4.models.Note
import ch.heigvd.iict.daa.labo4.models.Schedule
import ch.heigvd.iict.daa.labo4.models.NoteAndSchedule

@Dao
interface NoteDAO {
    @Insert
    fun insert(note: Note): Long

    @Insert
    fun insert(schedule: Schedule): Long

    @Update
    fun update(note: Note)

    @Update
    fun update(schedule: Schedule)

    @Delete
    fun delete(note: Note)

    @Delete
    fun delete(schedule: Schedule)

    @Transaction
    @Query("SELECT * FROM Note")
    fun getAllNotes(): LiveData<List<NoteAndSchedule>>

    @Query ("SELECT COUNT(*) FROM Note")
    fun countNotes(): LiveData<Long>

    @Query("DELETE FROM Note")
    fun deleteAll()
}