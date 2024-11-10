package ch.heigvd.iict.daa.labo4

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.iict.daa.labo4.models.Note

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
        val recycler = findViewById<RecyclerView>(R.id.recycler)
        val adapter = MyRecyclerAdapter()
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)
        adapter.items = listOf(
            Note.generateRandomNote(),
            Note.generateRandomNote(),
            Note.generateRandomNote(),
            Note.generateRandomNote(),
            Note.generateRandomNote(),
            Note.generateRandomNote(),
            Note.generateRandomNote(),
        )

        /* TODO phase 2
        - La MainActivity et ses Layouts (smartphone et tablette) ;
        - Le Fragment et son Layout affichant la liste des Notes ;
        - Le Fragment et son Layout qui contiendra le compteur et deux boutons de contrôles (sur
          tablette uniquement, remplaçant les entrées du Menu permettant la création aléatoire d’une
          Note et la suppression de toutes les Notes).
         */
    }
}

abstract class Animal(var name : String, val id: Long = ++Animal.id) {
    companion object {
        private var id = 0L
    }
}
class Mammal(name : String) : Animal(name)
class Aves(name : String) : Animal(name)