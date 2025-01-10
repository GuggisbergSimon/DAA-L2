package ch.heigvd.iict.and.rest.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ch.heigvd.iict.and.rest.ContactsRepository

class Uuid : ViewModel() {
    companion object {
        val data = MutableLiveData<String?>(null)
    }
    fun getData() : MutableLiveData<String?> {
        return data
    }
}

