package ch.heigvd.iict.and.rest.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ch.heigvd.iict.and.rest.ContactsRepository
import ch.heigvd.iict.and.rest.models.Contact
import ch.heigvd.iict.and.rest.models.PhoneType
import kotlinx.coroutines.launch
import java.util.Calendar

class ContactsViewModel(private val repository: ContactsRepository) : ViewModel() {

    val allContacts = repository.allContacts

    private val _selectedContact = MutableLiveData<Contact?>()
    val selectedContact: LiveData<Contact?> = _selectedContact

    fun selectContact(contact: Contact?) {
        _selectedContact.value = contact
    }

    fun saveContact(
        name: String,
        firstname: String,
        email: String,
        birthday: Calendar,
        address: String,
        zip: String,
        city: String,
        phoneType: PhoneType,
        phoneNumber: String
    ) {
        if (_selectedContact.value != null) {
            // Update contact
            val contact = _selectedContact.value?.copy(
                name = name,
                firstname = firstname,
                email = email,
                birthday = birthday,
                address = address,
                zip = zip,
                city = city,
                type = phoneType,
                phoneNumber = phoneNumber
            )
            viewModelScope.launch {
                repository.update(contact!!)
                _selectedContact.value = null
            }
        } else {
            // Create contact
            val newContact = Contact(
                name = name,
                firstname = firstname,
                email = email,
                birthday = birthday,
                address = address,
                zip = zip,
                city = city,
                type = phoneType,
                phoneNumber = phoneNumber
            )
            viewModelScope.launch {
                repository.create(newContact)
                _selectedContact.value = null
            }
        }
    }

    fun deleteContact() {
        _selectedContact.value?.let { contact ->
            viewModelScope.launch {
                repository.delete(contact)
                _selectedContact.value = null
            }
        }
    }

    // actions
    fun enroll() {
        viewModelScope.launch {
            repository.enroll()
        }
    }

    fun refresh() {
        viewModelScope.launch {
            repository.syncAllContacts()
        }
    }

}

class ContactsViewModelFactory(private val repository: ContactsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContactsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}