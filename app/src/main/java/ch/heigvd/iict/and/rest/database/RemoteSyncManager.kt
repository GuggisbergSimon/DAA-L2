package ch.heigvd.iict.and.rest.database

import android.util.Log
import ch.heigvd.iict.and.rest.models.Contact
import ch.heigvd.iict.and.rest.models.ContactDTO
import ch.heigvd.iict.and.rest.models.Status
import ch.heigvd.iict.and.rest.models.toContact
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class RemoteSyncManager {

    companion object {
        const val REMOTE_URL = "https://daa.iict.ch"
    }
    //TODO Remove the potentially useless logs. They are here for debugging
    private var token: String? = null
        private set

    private suspend fun getOrFetchToken(): String? {
        if (token === null) {
            withContext(Dispatchers.IO) {
                val url = URL("$REMOTE_URL/enroll")
                with(url.openConnection() as HttpURLConnection) {
                    requestMethod = "GET"
                    inputStream.bufferedReader().use {
                        token = it.readText()
                    }
                    println("Token: $token")
                }
            }
        }
        return token

    }

    suspend fun getAllContacts(): List<Contact> {
        return withContext(Dispatchers.IO) {
            val url = URL("$REMOTE_URL/contacts")
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"
                setRequestProperty("X-UUID", getOrFetchToken())
                inputStream.bufferedReader().use {
                    val response = it.readText()
                    val dtos = Gson().fromJson<List<ContactDTO>>(response,
                        object : TypeToken<List<ContactDTO>>() {}.type
                    )
                    dtos.map { it.toContact() }
                }
            }
        }
    }

    suspend fun getContact(id: Long): Contact? {
        return withContext(Dispatchers.IO) {
            val url = URL("$REMOTE_URL/contacts/$id")
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"
                setRequestProperty("X-UUID", getOrFetchToken())
                inputStream.bufferedReader().use {
                    val response = it.readText()
                    Gson().fromJson(response, ContactDTO::class.java).toContact()
                }
            }
        }
    }

    suspend fun saveOrRegisterContact(contact: Contact) {
        if (contact.id === null) {
            registerContact(contact)
        } else editContact(contact)
    }

    suspend fun editContact(contact: Contact) {
        var responseCodeHttp = 400
        withContext(Dispatchers.IO) {
            val url = URL("$REMOTE_URL/contacts/"+contact.serverId)
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "PUT"
                setRequestProperty("X-UUID", getOrFetchToken())
                val json = Gson().toJson(contact)
                doOutput = true
                setRequestProperty("Content-Type", "application/json")
                outputStream.bufferedWriter(Charsets.UTF_8).use {
                    it.append(json)
                }
                // on traite la réponse du service REST
                responseCodeHttp = responseCode
                Log.d("MainActivity", "responseCode: $responseCode")
                inputStream.bufferedReader(Charsets.UTF_8).use {
                    Log.d("MainActivity", it.readText())
                }
            }
        }
        if (responseCodeHttp < 400) {
            contact.status = Status.OK
        }
    }
    suspend fun registerContact(contact: Contact) {
        var responseCodeHttp = 400
        withContext(Dispatchers.IO) {
            val url = URL("$REMOTE_URL/contacts/")
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "POST"
                setRequestProperty("X-UUID", getOrFetchToken())
                val json = Gson().toJson(contact)
                doOutput = true
                setRequestProperty("Content-Type", "application/json")
                outputStream.bufferedWriter(Charsets.UTF_8).use {
                    it.append(json)
                }
                // on traite la réponse du service REST
                responseCodeHttp = responseCode
                Log.d("MainActivity", "responseCode: $responseCode")
                inputStream.bufferedReader(Charsets.UTF_8).use {
                    val response = it.readText()
                    contact.serverId = Gson().fromJson(response, ContactDTO::class.java).id
                }
            }
        }
        if (responseCodeHttp < 400) {
            contact.status = Status.OK
        }
    }

    suspend fun  deleteContact(contact: Contact) {
        var responseCodeHttp = 400
        withContext(Dispatchers.IO) {
            val url = URL("$REMOTE_URL/contacts/"+contact.serverId)
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "DELETE"
                setRequestProperty("X-UUID", getOrFetchToken())
                val json = Gson().toJson(contact)
                doOutput = true
                setRequestProperty("Content-Type", "application/json")
                outputStream.bufferedWriter(Charsets.UTF_8).use {
                    it.append(json)
                }
                // on traite la réponse du service REST
                responseCodeHttp = responseCode
                Log.d("MainActivity", "responseCode: $responseCode")
                inputStream.bufferedReader(Charsets.UTF_8).use {
                    Log.d("MainActivity", it.readText())
                }
            }
        }
        if (responseCodeHttp < 400) {
            contact.status = Status.OK
        }
    }


}
