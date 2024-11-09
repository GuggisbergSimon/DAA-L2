package ch.heigvd.iict.daa.labo4

import androidx.lifecycle.MutableLiveData
import ch.heigvd.iict.daa.labo4.models.NoteAndSchedule

class DataRepository {
    val allNotes = MutableLiveData<List<NoteAndSchedule>>(listOf())
    val countNotes = MutableLiveData(0)
}
