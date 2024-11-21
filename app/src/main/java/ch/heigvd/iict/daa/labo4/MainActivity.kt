package ch.heigvd.iict.daa.labo4

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ch.heigvd.iict.daa.labo4.fragments.NotesFragment

class MainActivity : AppCompatActivity() {
    private val notesViewModel: NotesViewModel by viewModels {
        var app = application as MyApp
        NotesViewModelFactory(app.repository)
    }

    private var notesFragment = supportFragmentManager.findFragmentById(R.id.notesFragmentContainer) as? NotesFragment


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.main_menu_sortDate -> {
                notesFragment?.sortByDate()
                true
            }

            R.id.main_menu_sortETA -> {
                notesFragment?.sortByETA()
                true
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
        notesFragment = supportFragmentManager.findFragmentById(R.id.notesFragmentContainer) as? NotesFragment
    }
}