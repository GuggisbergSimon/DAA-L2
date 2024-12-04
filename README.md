# DAA Lab 05

Auteurs :

- Patrick Furrer
- Simon Guggisberg
- Jonas Troeltsch

## 3 Mise en place de l’Adapteur et des Coroutines

### 3.1 Veuillez expliquer comment votre solution s’assure qu’une éventuelle Couroutine associée à une vue (item) de la RecyclerView soit correctement stoppée lorsque l’utilisateur scrolle dans la galerie et que la vue est recyclée.

Lors des événements `onBindViewHolder` et `onViewRecycled` de l'`ImageAdapter`, nous lançons, et
stoppons, respectivement, la coroutine associée à la vue.

```kotlin
override fun onViewRecycled(holder: ImageViewHolder) {
    super.onViewRecycled(holder)
    val position = holder.adapterPosition
    jobs[position]?.cancel()
    jobs.remove(position)
}
```

### 3.2 Comment pouvons-nous nous assurer que toutes les Coroutines soient correctement stoppées lorsque l’utilisateur quitte l’Activité ?

Veuillez expliquer la solution que vous avez mise en œuvre, est-ce la plus adaptée ?

Nous vérifions, à la destruction de l'activité, si il s'agit d'un événement appelé en cas de fin
d'activité.
Si c'est le cas, nous appelons la fonction `clearJobs()` de l'`ImageAdapter` qui arrête toutes les
coroutines en cours.

```kotlin
override fun onDestroy() {
    super.onDestroy()
    if (isFinishing) {
        imageAdapter.clearJobs()
    }
}
```

Ceci nous semble la solution la plus adaptée, en effet.

### 3.3 Est-ce que l’utilisation du Dispatchers.IO est la plus adaptée pour des tâches de téléchargement ?

Ou faut-il plutôt utiliser un autre Dispatcher, si oui lequel ?
Veuillez illustrer votre réponse en effectuant quelques tests.

IO est le plus adapté pour des tâches d'entrées/sorties telles que le téléchargement de fichiers, la
lecture ou l'écriture de fichiers, etc.

TODO faire des tests

### 3.4 Nous souhaitons que l’utilisateur puisse cliquer sur une des images de la galerie afin de pouvoir, par exemple, l’ouvrir en plein écran.

Comment peut-on mettre en place cette fonctionnalité avec une RecyclerView?
Comment faire en sorte que l’utilisateur obtienne un feedback visuel lui indiquant que son clic a
bien été effectué, sur la bonne vue.

Cela peut se faire via un événement `ViewHolder`, en particulier `OnClickListener`.

Pour afficher un feedback visuel, nous pouvons par exemple changer la couleur de fond de la vue.
Mais à noter qu'il faudra restaurer les paramètres d'origine lors du binding des vues, en raison du
recyclage de celles-ci.

En effet, sans effectuer cela, les vues recyclées pourraient conserver les paramètres de la vue
précédente, ce qui pourrait donner l'impression, en scrollant, que l'utilisateur a cliqué sur une
vue alors que cela n'était pas du tout le cas.

```kotlin
class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
    val imageView: ImageView = view.findViewById(R.id.imageView)
    val progressBar: ProgressBar = view.findViewById(R.id.progressBar)

    init {
        view.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        println("Item clicked at position $adapterPosition")
    }
}
```

## 4 Nettoyage automatique du cache

### 4.1 Lors du lancement de la tâche ponctuelle, comment pouvons-nous faire en sorte que la galerie soit rafraîchie ?

### 4.2 Comment pouvons-nous nous assurer que la tâche périodique ne soit pas enregistrée plusieurs fois ?

Vous expliquerez comment la librairie WorkManager procède pour enregistrer les différentes tâches
périodiques et en particulier comment celles-ci sont ré-enregistrées lorsque le téléphone est
redémarré.