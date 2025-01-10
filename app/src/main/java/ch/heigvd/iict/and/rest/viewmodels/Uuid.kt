package ch.heigvd.iict.and.rest.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ch.heigvd.iict.and.rest.ContactsRepository

class Uuid : ViewModel() {

    val data = MutableLiveData<String?>(null)
}

