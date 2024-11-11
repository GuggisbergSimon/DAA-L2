package ch.heigvd.iict.daa.labo4.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ch.heigvd.iict.daa.labo4.models.Note
import ch.heigvd.iict.daa.labo4.models.Schedule

@Dao
interface ScheduleDAO {
    @Insert
    fun insert(schedule: Schedule): Long

    @Update
    fun update(schedule: Schedule)

    @Delete
    fun delete(schedule: Schedule)

    @Query("SELECT * FROM Schedule")
    fun getAllNotes(): LiveData<List<Note>>

    //TODO implement query properly
    @Query("SELECT * FROM Schedule ORDER BY date ASC")
    fun getAllNotesByDate() : LiveData<List<Schedule>>

    @Query("SELECT * FROM Schedule ORDER BY date ASC")
    fun getAllNotesByETA() : LiveData<List<Schedule>>

    @Query("DELETE FROM Schedule")
    suspend fun deleteAll()
}