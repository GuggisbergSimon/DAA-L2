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
            println("Contact status: ${contact.status}")
            contactsDao.update(contact)
            println("Contact created: $contact")
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

            val contacts = contactsDao.getAllContacts()
            val remoteContacts = remoteSyncManager.getAllContacts()
            val remoteContactsMap = remoteContacts.associateBy { it.serverId }

            // check if the contact is already in the local database
            remoteContacts.forEach { remoteContact ->
                val localContact = contacts.find { it.serverId == remoteContact.serverId }
                if (localContact == null) {
                    contactsDao.insert(remoteContact)
                }
            }

            // check if some contacts are not in sync with the remote server
            contacts.forEach { contact ->
                val remoteContact = remoteContactsMap[contact.serverId]
                if (remoteContact != null) {
                    if (contact.status == Status.NEW) {
                        contact.status = Status.OK
                        contactsDao.update(contact)
                    } else if (contact.status == Status.MODIFIED) {
                        contact.status = Status.OK
                        contactsDao.update(contact)
                    }
                } else {
                    if (contact.status == Status.NEW) {
                        remoteSyncManager.registerContact(contact)
                    } else if (contact.status == Status.MODIFIED) {
                        remoteSyncManager.editContact(contact)
                    } else if (contact.status == Status.DELETED) {
                        remoteSyncManager.deleteContact(contact)
                    }
                }
            }
        }
    }

    fun enroll() {
        CoroutineScope(Dispatchers.IO).launch {
            clearAllContacts()
            remoteSyncManager.enroll()
            syncAllContacts()
        }
    }
}