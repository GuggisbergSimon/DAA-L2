package ch.heigvd.iict.daa.labo4.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ch.heigvd.iict.daa.labo4.models.Note

@Dao
interface NoteDAO {
    @Insert
    fun insert(note: Note): Long

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("SELECT * FROM Note")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM Note ORDER BY creationDate ASC")
    fun getAllNotesByDate() : LiveData<List<Note>>

    //TODO implement query properly
    @Query("SELECT * FROM Note ORDER BY creationDate ASC")
    fun getAllNotesByETA() : LiveData<List<Note>>

    @Query ("SELECT COUNT(*) FROM Note")
    fun countNotes(): Long

    @Query("DELETE FROM Note")
    fun deleteAll()
}