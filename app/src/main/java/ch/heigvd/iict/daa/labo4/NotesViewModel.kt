package ch.heigvd.iict.daa.labo4

import androidx.lifecycle.ViewModel
import ch.heigvd.iict.daa.labo4.models.Note
import ch.heigvd.iict.daa.labo4.models.NoteAndSchedule

class NotesViewModel(private val repository: DataRepository) : ViewModel() {
    val allNotes = repository.allNotes
    val countNotes = repository.countNotes

    fun generateANote() {
        val ns = NoteAndSchedule(Note.generateRandomNote(), Note.generateRandomSchedule())
        repository.allNotes.postValue(allNotes.value!! + listOf(ns))
        repository.countNotes.postValue(repository.countNotes.value!! + 1)
    }

    fun deleteAllNote() {
        allNotes.value = listOf()
        countNotes.value = 0
    }
}