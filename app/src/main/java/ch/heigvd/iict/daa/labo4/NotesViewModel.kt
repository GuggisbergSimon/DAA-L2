package ch.heigvd.iict.daa.labo4

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ch.heigvd.iict.daa.labo4.models.Note

class NotesViewModel(private val repository: DataRepository) : ViewModel() {
    val allNotes = repository.allNotes
    val countNotes = repository.countNotes
    
    fun generateANote() {
        val n = Note.generateRandomNote()
        val s = Note.generateRandomSchedule()
        repository.insertNote(n, s)
    }

    fun deleteAllNote() {
        repository.deleteAll()
    }
}

class NotesViewModelFactory(private val repository: DataRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            return NotesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}