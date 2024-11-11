package ch.heigvd.iict.daa.labo4.room

import androidx.room.TypeConverter
import java.util.Calendar
import java.util.Date

class CalendarConverter {
    @TypeConverter
    fun toCalendar(dateLong: Long) =
        Calendar.getInstance().apply {
            time = Date(dateLong)
        }

    @TypeConverter
    fun fromCalendar(date: Calendar) =
        date.time.time // Long
}