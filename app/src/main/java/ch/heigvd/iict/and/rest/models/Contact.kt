package ch.heigvd.iict.and.rest.models

import androidx.room.Entity
import androidx.room.PrimaryKey
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