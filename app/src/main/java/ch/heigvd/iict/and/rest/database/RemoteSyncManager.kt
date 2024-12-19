package ch.heigvd.iict.and.rest.database

import ch.heigvd.iict.and.rest.models.Contact
import ch.heigvd.iict.and.rest.models.ContactDTO
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
        const val TOKEN = "a4d0e1ef-0882-4a5c-a89e-55a63d4f348e" // TODO use token from /enroll and store it in a persistent way
    }

    var token: String? = TOKEN
        private set

    suspend fun fetchToken() {
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

    suspend fun getAllContacts() : List<Contact> {
        return withContext(Dispatchers.IO) {
            val url = URL("$REMOTE_URL/contacts")
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"
                setRequestProperty("X-UUID", token)
                inputStream.bufferedReader().use {
                    val response = it.readText()
                    val dtos = Gson().fromJson<List<ContactDTO>>(response,
                        object : TypeToken<List<ContactDTO>>() {}.type)
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
                setRequestProperty("X-UUID", token)
                inputStream.bufferedReader().use {
                    val response = it.readText()
                    Gson().fromJson(response, ContactDTO::class.java).toContact()
                }
            }
        }
    }

    // TODO implement other methods to synchronize contacts with the remote server


}
