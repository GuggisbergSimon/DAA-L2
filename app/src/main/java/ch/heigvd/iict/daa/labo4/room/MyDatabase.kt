package ch.heigvd.iict.daa.labo4.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ch.heigvd.iict.daa.labo4.models.Note
import ch.heigvd.iict.daa.labo4.models.NoteAndSchedule
import java.time.chrono.IsoChronology.INSTANCE
import kotlin.concurrent.thread

@Database(entities = [NoteAndSchedule::class],
    version = 1,
    exportSchema = true)
@TypeConverters(CalendarConverter::class)
abstract class MyDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDAO

    companion object {
        private var INSTANCE: MyDatabase? = null
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Person ADD COLUMN middleName INTEGER")
            }
        }

        fun getDatabase(context: Context): MyDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java, "mydatabase.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .addCallback(MyDatabaseCallBack())
                    .build()
                INSTANCE!!
            }
        }
    }
}

private class MyDatabaseCallBack : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        INSTANCE?.let { database ->
            thread {
                val isEmpty = database.noteDao().getCountDirect() == 0L
                if (isEmpty) {
                    // when the database is created for the 1st time, we can, for example, populate it
                    // should be done asynchronously
                    val note = NoteAndSchedule(Note.generateRandomNote(), Note.generateRandomSchedule())
                    database.noteDao.insert(note)
                }
            }
        }
    }

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
    }

    override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
        super.onDestructiveMigration(db)
    }
}