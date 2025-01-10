package ch.heigvd.iict.and.rest

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import ch.heigvd.iict.and.rest.database.RemoteSyncManager
import ch.heigvd.iict.and.rest.database.RemoteSyncManager.Companion.TOKEN_KEY
import ch.heigvd.iict.and.rest.databinding.ActivityMainBinding
import ch.heigvd.iict.and.rest.fragments.EditContactFragment
import ch.heigvd.iict.and.rest.viewmodels.ContactsViewModel
import ch.heigvd.iict.and.rest.viewmodels.ContactsViewModelFactory
import ch.heigvd.iict.and.rest.viewmodels.Uuid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val contactsViewModel: ContactsViewModel by viewModels {
        ContactsViewModelFactory((application as ContactsApplication).repository)
    }
    private val uuid : Uuid = Uuid()
    private val remoteSyncManager : RemoteSyncManager = RemoteSyncManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainFabNew.setOnClickListener {
            contactsViewModel.selectContact(null)
            supportFragmentManager.commit {
                hideFab()
                replace(R.id.main_content_fragment, EditContactFragment())
                addToBackStack(null)
            }
        }

        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                binding.mainFabNew.show()
            }
        }
        setUuid()
    }

    fun hideFab() {
        binding.mainFabNew.hide()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_main_synchronize -> {
                contactsViewModel.refresh()
                true
            }
            R.id.menu_main_populate -> {
                contactsViewModel.enroll()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private fun setUuid() {
        val prefs: SharedPreferences = getPreferences(Context.MODE_PRIVATE)
        uuid.getData().value = prefs.getString(TOKEN_KEY, null)
        println("Gotten : " + uuid.getData().value + " from cache")
        uuid.getData().observe(this) { value ->
            println("Putting token into cache $value")
            prefs.edit().putString(TOKEN_KEY, value).apply()
        }
        if(uuid.getData().value == null) {
            println("Fetching new value")
            CoroutineScope(Dispatchers.IO).launch {
                remoteSyncManager.enroll()
            }
        } else {
            println("Token : " + uuid.getData().value + "gotten from cache")
        }
    }

}