package ch.heigvd.iict.and.rest.models

import java.util.Calendar
import java.util.Date

data class ContactDTO(
    val id: Long,
    val name: String,
    val firstname: String?,
    val birthday: Date?,
    val email: String?,
    val address: String?,
    val zip: String?,
    val city: String?,
    val type: String?,
    val phoneNumber: String?
)

fun ContactDTO.toContact(): Contact {
    return Contact(
        name = this.name,
        firstname = this.firstname,
        birthday = this.birthday?.let { date ->
            Calendar.getInstance().apply { time = date }
        },
        email = this.email,
        address = this.address,
        zip = this.zip,
        city = this.city,
        type = this.type?.let { PhoneType.valueOf(it) },
        phoneNumber = this.phoneNumber,
        serverId = this.id,
        status = Status.OK
    )
}