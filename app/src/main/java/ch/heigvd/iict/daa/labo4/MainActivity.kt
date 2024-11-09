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
        /* TODO phase 2
        - La MainActivity et ses Layouts (smartphone et tablette) ;
        - Le Menu associé à l’Activité (tri par date de création, tri par date de réalisation prévue, création
          d’une Note (aléatoire) et suppression de toutes les Notes ;
        - Le Fragment et son Layout affichant la liste des Notes ;
        - Le Fragment et son Layout qui contiendra le compteur et deux boutons de contrôles (sur
          tablette uniquement, remplaçant les entrées du Menu permettant la création aléatoire d’une
          Note et la suppression de toutes les Notes).
         */
    }
}