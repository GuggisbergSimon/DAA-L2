package ch.heigvd.iict.and.rest.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

enum class Status {
    OK,
    NEW,
    MODIFIED,
    DELETED
}

@Entity
data class Contact(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    var name: String,
    var firstname: String?,
    var birthday: Calendar?,
    var email: String?,
    var address: String?,
    var zip: String?,
    var city: String?,
    var type: PhoneType?,
    var phoneNumber: String?,
    // New sync fields
    var serverId: Long? = null,
    var status: Status = Status.NEW
)

fun Contact.toDTO(): ContactDTO {
    return ContactDTO(
        id = this.serverId,
        name = this.name,
        firstname = this.firstname,
        birthday = birthday?.let {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
                .format(it.time)
        } ?: "",
        email = this.email,
        address = this.address,
        zip = this.zip,
        city = this.city,
        type = this.type?.name,
        phoneNumber = this.phoneNumber
    )
}