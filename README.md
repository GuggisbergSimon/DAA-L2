# Labo4

Auteurs :
- Patrick Furrer
- Simon Guggisberg
- Jonas Troeltsch

## 6.1 Quelle est la meilleure approche pour sauver, même après la fermeture de l’app, le choix de l’option de tri de la liste des notes ?

### Vous justifierez votre réponse et l’illustrez en présentant le code mettant en œuvre votre approche.

En l'état la liste d'objets est triée dans la vue. Pour sauvegarder l'état de celle-ci même après la
fermeture de l'app, il faudrait que cela se fasse soit au niveau de la base de donnée soit au niveau
des préférences utilisateurs.

En l'occurence, comme notre donnée peut être stockée (un type de tri) est très simple, il n'est pas
nécessaire de stocker cette information dans la base de donnée. Une simple utilisation de DataStore
suffit.

Classe utilissant le datastore:

```kotlin
import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

// Extension pour obtenir une instance DataStore
private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class PreferencesManager(context: Context) {

    companion object {
        private val SORT_OPTION_KEY =
            stringPreferencesKey("sort_option") // Clé pour l'option de tri
    }

    private val dataStore = context.dataStore

    // Fonction pour lire l'option de tri
    suspend fun getSortOption(): String {
        val preferences = dataStore.data.first() // Récupère les données
        return preferences[SORT_OPTION_KEY] ?: "date" // Retourne la valeur par défaut si absente
    }

    // Fonction pour mettre à jour l'option de tri
    suspend fun setSortOption(option: String) {
        dataStore.edit {
            preferences[SORT_OPTION_KEY] = option
        }
    }
}

```

Il suffit ensuite d'ajouter cette logique dans les classes de notes:

```kotlin
class NotesViewModel(
    private val repository: DataRepository,
    private val preferencesManager: PreferencesManager
) {

    val allNotes = repository.allNotes
    val countNotes = repository.countNotes

    fun generateANote() {
        val n = Note.generateRandomNote()
        val s = Note.generateRandomSchedule()
        repository.insertNote(n, s)
    }

    fun deleteAllNote() {
        repository.deleteAll()
    }

    // Fonction pour récupérer l'option de tri
    fun getSortOption(): String {
        return preferencesManager.getSortOption()
    }

    // Fonction pour mettre à jour l'option de tri
    fun updateSortOption(option: String) {
        preferencesManager.setSortOption(option)
    }
}
```

L'option de tri peut donc ensuite être modifiée et appelée par l'UI comme n'importe quelle autre
méthode de NotesViewModel.

## 6.2 L’accès à la liste des notes issues de la base de données Room se fait avec une LiveData. Est-ce que cette solution présente des limites ?

### Si oui, quelles sont-elles ? Voyez-vous une autre approche plus adaptée ?

Oui.

- Les LiveData peuvent être cast en objet mutable, ce qui peut poser des problèmes de sécurité.
- Les LiveData vont être mises à jour en bloc, il convient de modifier le DAO pour limiter l'impact
  sur la performance en ne visant que les modifications
- Mise à jour fréquente de l'UI, ce qui peut poser problème pour une quantité considérable de
  données, nombres de likes sur une app pour un réseau social, par exemple, alors que ce genre
  d'informations n'est pas nécessaire de mettre à jour tout le temps.

Une solution a cette problèmatique est d'utiliser le "Flow", qui est entre autre prévu pour ça.
Ce block Kotlin permet d'utiliser les concepts de coroutine pour réagir au événements et mettre à
jour plus facilement l'UI par exemple. De plus, il se combine bien avec Android Room pour l'update
dans la base de donnée, et pour réagir aux modifications de celle-ci.

## 6.3 Les notes affichées dans la RecyclerView ne sont pas sélectionnables ni cliquables. Comment procéderiez-vous si vous souhaitiez proposer une interface permettant de sélectionner une note pour l’éditer ?

Il faudrait passer par un ListView qui possède des callbacks tels que OnItemClickListener. Ensuite
cela commencerait un Intent vers une nouvelle vue/activités, par exemple, afin d'éditer celle-ci,
qui impacterait la DB directement sans passer par un contrat pour une valeur de retour puisque la
valeur est partagée grâce au ViewModel.
