package ch.heigvd.iict.and.rest.models

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class ContactDTO(
    val id: Long?,
    val name: String?,
    val firstname: String?,
    val birthday: String?,
    val email: String?,
    val address: String?,
    val zip: String?,
    val city: String?,
    val type: String?,
    val phoneNumber: String?
)

fun ContactDTO.toContact(): Contact {
    return Contact(
        name = this.name!!,
        firstname = this.firstname,
        birthday = this.birthday?.let {
            Calendar.getInstance().apply {
                time = SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
                    Locale.getDefault()
                ).parse(it)!!
            }
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