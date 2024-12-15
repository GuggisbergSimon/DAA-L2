package ch.heigvd.iict.and.rest

import ch.heigvd.iict.and.rest.database.ContactsDao
import ch.heigvd.iict.and.rest.models.Contact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactsRepository(private val contactsDao: ContactsDao) {

    val allContacts = contactsDao.getAllContactsLiveData()

    companion object {
        private val TAG = "ContactsRepository"
    }

    fun update(contact: Contact) {
        CoroutineScope(Dispatchers.IO).launch {
            contactsDao.update(contact)
        }
    }

    fun create(contact: Contact) {
        CoroutineScope(Dispatchers.IO).launch {
            contactsDao.insert(contact)
        }
    }

    fun delete(contact: Contact) {
        CoroutineScope(Dispatchers.IO).launch {
            contactsDao.delete(contact)
        }
    }
}