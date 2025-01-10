package ch.heigvd.iict.and.rest.database

import android.util.Log
import ch.heigvd.iict.and.rest.models.Contact
import ch.heigvd.iict.and.rest.models.ContactDTO
import ch.heigvd.iict.and.rest.models.Status
import ch.heigvd.iict.and.rest.models.toContact
import ch.heigvd.iict.and.rest.models.toDTO
import ch.heigvd.iict.and.rest.viewmodels.Uuid
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class RemoteSyncManager {

    companion object {
        const val REMOTE_URL = "https://daa.iict.ch"
        const val TOKEN_KEY = "token"
    }
    private val uuid : Uuid = Uuid()
    //TODO Remove the potentially useless logs. They are here for debugging

    suspend fun enroll() : String {
        val token : String
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
        uuid.getData().postValue(token)
        return token
    }

    private fun getToken() : String {
        return uuid.getData().value!!
    }

    suspend fun getAllContacts(): List<Contact> {
        return withContext(Dispatchers.IO) {
            val url = URL("$REMOTE_URL/contacts")
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"
                setRequestProperty("X-UUID", getToken())
                inputStream.bufferedReader().use {
                    val response = it.readText()
                    val dtos = Gson().fromJson<List<ContactDTO>>(
                        response,
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
                setRequestProperty("X-UUID", getToken())
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
        if (contact.serverId == null) {
            registerContact(contact)
            return
        }
        return withContext(Dispatchers.IO) {
            val url = URL("$REMOTE_URL/contacts/${contact.serverId}")
            val connection = url.openConnection() as HttpURLConnection
            try {
                connection.apply {
                    requestMethod = "PUT"
                    setRequestProperty("X-UUID", getToken())
                    setRequestProperty("Content-Type", "application/json")
                    doOutput = true

                    // Write JSON payload
                    val json = Gson().toJson(contact.toDTO())
                    outputStream.bufferedWriter(Charsets.UTF_8).use { writer ->
                        writer.append(json)
                    }

                    // Check the response code
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        inputStream.bufferedReader(Charsets.UTF_8).use { reader ->
                            Log.d("MainActivity", reader.readText())
                        }
                        contact.status = Status.OK
                    } else {
                        Log.w("MainActivity", "Edit failed with response code: $responseCode")
                    }
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error editing contact: ${e.message}", e)
            } finally {
                connection.disconnect()
            }
        }
    }

    suspend fun registerContact(contact: Contact) {
        return withContext(Dispatchers.IO) {
            val url = URL("$REMOTE_URL/contacts/")
            val connection = url.openConnection() as HttpURLConnection
            try {
                connection.apply {
                    requestMethod = "POST"
                    setRequestProperty("X-UUID", getToken())
                    setRequestProperty("Content-Type", "application/json")
                    doOutput = true

                    // Write JSON payload
                    val json = Gson().toJson(contact.toDTO())
                    outputStream.bufferedWriter(Charsets.UTF_8).use { writer ->
                        writer.append(json)
                    }

                    // Check the response code
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        inputStream.bufferedReader(Charsets.UTF_8).use { reader ->
                            val response = reader.readText()
                            contact.serverId = Gson().fromJson(response, ContactDTO::class.java).id
                        }
                        contact.status = Status.OK
                    } else {
                        Log.w("MainActivity", "Register failed with response code: $responseCode")
                    }
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error registering contact: ${e.message}", e)
            } finally {
                connection.disconnect()
            }
        }
    }

    suspend fun deleteContact(contact: Contact) {
        return withContext(Dispatchers.IO) {
            val url = URL("$REMOTE_URL/contacts/${contact.serverId}")
            val connection = url.openConnection() as HttpURLConnection
            try {
                connection.apply {
                    requestMethod = "DELETE"
                    setRequestProperty("X-UUID", getToken())
                    setRequestProperty("Content-Type", "application/json")
                    doOutput = true

                    // Write JSON payload
                    val json = Gson().toJson(contact.toDTO())
                    outputStream.bufferedWriter(Charsets.UTF_8).use { writer ->
                        writer.append(json)
                    }

                    // Check the response code
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        inputStream.bufferedReader(Charsets.UTF_8).use { reader ->
                            Log.d("MainActivity", reader.readText())
                        }
                        contact.status = Status.OK
                    } else {
                        Log.w("MainActivity", "Delete failed with response code: $responseCode")
                    }
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error deleting contact: ${e.message}", e)
            } finally {
                connection.disconnect()
            }
        }
    }


}
