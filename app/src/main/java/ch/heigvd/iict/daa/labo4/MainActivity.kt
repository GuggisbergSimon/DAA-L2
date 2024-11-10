package ch.heigvd.iict.daa.labo4

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val notesViewModel: NotesViewModel by viewModels()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            //TODO implement sorting
            R.id.main_menu_sortDate -> { /* do something */ true
            }

            R.id.main_menu_sortETA -> { /* do something */ true
            }

            R.id.main_menu_generate -> {
                notesViewModel.generateANote()
                true
            }

            R.id.main_menu_deleteAll -> {
                notesViewModel.deleteAllNote()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}