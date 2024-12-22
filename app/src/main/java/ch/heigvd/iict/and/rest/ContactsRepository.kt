package ch.heigvd.iict.and.rest

import ch.heigvd.iict.and.rest.database.ContactsDao
import ch.heigvd.iict.and.rest.database.RemoteSyncManager
import ch.heigvd.iict.and.rest.models.Contact
import ch.heigvd.iict.and.rest.models.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactsRepository(private val contactsDao: ContactsDao, private val remoteSyncManager: RemoteSyncManager) {

    val allContacts = contactsDao.getAllContactsLiveData()

    companion object {
        private val TAG = "ContactsRepository"
    }

    // TODO update the status of the contact in the local database
    // TODO send the updated contact to the remote server
    // TODO if the remote server returns a success status, update the contact in the local database

    fun update(contact: Contact) {
        CoroutineScope(Dispatchers.IO).launch {
            contact.status = Status.MODIFIED
            contactsDao.update(contact)
            remoteSyncManager.editContact(contact)
            contactsDao.update(contact)
        }
    }

    fun create(contact: Contact) {
        CoroutineScope(Dispatchers.IO).launch {
            contact.status = Status.NEW
            contactsDao.insert(contact)
            remoteSyncManager.registerContact(contact)
            contactsDao.update(contact)
        }
    }

    fun delete(contact: Contact) {
        CoroutineScope(Dispatchers.IO).launch {
            contact.status = Status.DELETED
            contactsDao.update(contact)
            remoteSyncManager.deleteContact(contact)
            if (contact.status == Status.OK) {
                contactsDao.delete(contact)
            }
        }
    }

    fun clearAllContacts() {
        CoroutineScope(Dispatchers.IO).launch {
            contactsDao.clearAllContacts()
        }
    }

    fun syncAllContacts() {
        CoroutineScope(Dispatchers.IO).launch {
            val contacts = remoteSyncManager.getAllContacts()

            contacts.forEach {
                contactsDao.insert(it)
            }
        }
    }
}