#import "@preview/cram-snap:0.2.1": cram-snap, theader

#set page(
  paper: "a4",
  flipped: true,
  margin: 0.1cm,
)
#set text(font: "Arial", size: 7.6pt)

#show: cram-snap.with(
  title: [DAA - Furrer - Guggisberg - Troeltsch],
  column-number: 2,
)

#table(
  theader[Introduction Kotlin],
  [`val unmutable` - assignable une fois],[`var mutable`],
  [`val p : Person?; p?.name`], [Null safety: Pour chaque type T, il existe T? qui prend T ou null],
  [`c?.name ?: "default"`], [Elvis Operator: Prendre valeur en cas de null],
  [`fun String.doSomething():String {...}`], [Extension functions: ajouter fonctions à classes sans sources],
  [`class Person(var i:Int) {init {...}}`], [init: constructeur par défaut],
  [`constructor(i:Int,j:Int):this(i) {...}`], [constructor: constructeurs supplémentaire],
  [`fun add(x: Int, y: Int):Int {...}`],[Arguments nommés possible: `add(y = 1, x = 2)`],
  [`class Student(...): Person(...) {...}`],[Héritage: appel super-constructeur. `open class Person() {...}`],
  [`interface Flying {
  val w:Wings 
  fun fly(){...}
}`],
  [`class Bird: Flying {
  override val wings:Wings=...
}`],
  [`data class Person(var name:String)`], [Génère: getter - setters - toString()],
  [`val(name, surname) = p`], [Déstructuration pour data class],
  [`var p2 = p1.copy(surname="Bob")`],[Copie pour data class, arguments optionnels],
  [`val s = "Person[name:${p.name},id:$id"]`],[String templates],
  [Collections],[List, MutableList, Set, MutableSet, Map, MutableMap],
  [`val l = listOf(1, 2, 3, 4)`],[
`l.any{...} l.count{...} l.max() l.filter{...} l.map{...} l.partition{...} l+l` etc],
  [*Operators overload*: `            ` Array:],[`get(i) set(i)        a..b b.rangeTo(b) a in b b.contains(a)`],
  [Unary: `unaryPlus() not() inc() dec()`],[Binary: `plus(b) minus(b) times(b) div(b) mod(b) plusAssign()`],
  [`val res = when(x) {1->"a" else->{"b"}}`],[*When* - Pattern Matching],
  [`p?.let {it.name="Bob" println(it)}`],[Scope functions - *let* - retourne dernière ligne du bloc],
  [`val p2 = p.apply {this.name="Bob" println(this)}`],[Scope functions - *apply* - retourne l'objet lui-même],
  [`BufferedWriter(FileWriter("a.txt")).use{
  it.appendLine("Hello")
}`],[Scope functions - *use* - appelé sur objets _Closeable_, ferme la variable it automatiquement en fin de bloc ou en cas d'exception],
  [`companion object {var a  fun  getA():A}`],[Companion objects contient variables/fonctions *statiques*],
)

#table(
  theader[Ressources],
  [*Manifest*],[décrit informations essentielles pour build, OS, et store],
  [Doit contenir],[Composants d'app(Activités, Services, Broadcast receivers, Content providers), Permissions, Fonctionnalités (hard/software)],
  [*Ressources*],[différentes ressources et classe _R_],
  [Contextualisation - ⚠ Lire doc pour ordre précis],[Plusieurs ressources pour différents contextes/configurations. chargées automatiquement lors exécution la mieux adaptée. ex : _values-fr_ _sw600dp_ _night_ _land_ densité écran:_hdpi_ taille écran:_normal_],
  [*Valeurs* - string.xml],[peut être regroupées en tableau],
  [Placeholders],[`<string name="welcome">Hello, %1$s!</string>`],
  [Plurals - `zero one two few many`],[`<plurals name="test">
  <item quantity="one">one</item>
  <item quantity="other">more</item>
</plurals>`],
  [*Dimensions* - dimension.xml],[dp:density independent pixel, sp:scale independent pixel],
  [*Couleurs* - colors.xml],[-> themes.xml],
  [*Drawables*],[],
  [Bitmap - png, webp, jpeg, gif],[Plusieurs résolutions pour chaque type d'écran. résolutions différentes via contexte de densité.],
  [Vector - xml],[outil pour convertir depuis svg ou psd],
  [Nine-Patch - \*.9.[png]],[Redimensionnement contrôlé],
  [State List],[Drawable matchant différents states de la vue],
  [Level List],[Drawable matchant une valeur numérique],
  [*Layouts* - LinearLayout - RelativeLayout - ConstraintLayout - ScrollView],[ possible de les imbriquer mais moins bonnes performances. ScrollView est vertical (mais HorizontalScrollView existe)],
  [*classe R*],[regroupe les ids uniques des ressources. Accessible code ou ressources, autogénérée lors du build dans dossier _gen_ à la racine du package. setContentView(R.Layout.activity_main)],
  [*Build* - Gradle],[build.gradle app et projet],
  [Dépendances - Maven],[via _build.gradle.kts_ ou _libs.versions.toml_ (mieux)],
)

#colbreak()

#table(
  theader[Activités, Fragments, Services],
  [*Activity* - Stack],[Doivent être déclarées dans le manifest],
  [Cycle de vie - Inact->Stopped->Paused->Active->Paused->Stopped->Inact],[Active:foreground, can not be killed Paused:visible, no focus Stopped:invisible Inactive:temporary when created/killed],
  [*viewBinding*],[option buildFeatures dans build.gradle app, automatise linkage des vues, génère une classe pour chaque layout],
  [*Cycles de vie*],[application réagit aux changement d'états du lifecycle via méthodes de callback (onCreate, onStart, ...)],
  [Initialisation],[initialiser vue via viewBinding ou classe R et findViewById],
  [Nouvelle activité],[nouvelle lancée via Intent, précédente dans la pile en _Stopped_],
  [Navigation entre activités],[configurations système peuvent entrainer recréation activité, si celle-ci supprimée car plus de ressource entre temps également -> sauver état courant dans bundle],
  [Terminer activité courante],[Back -> onPause -> onStop -> onDestroy -> finish],
  [État activité],[activités sur stack peuvent être détruites. changement de config va recréer activité active],
  [*Sauvegarde-Restoration*],[onSaveInstanceState -> Bundle -> onCreate/onRestoreInstanceState],
  [*Intents*],[Lance une activité sur la stack avec paramètres éventuels],
  [Intents Explicites - activité précise],[Intents Implicites - appel générique via un lien/type intent],
  [Contracts],[résultat d'une activité (moderne, non déprécié)],
  [*Fragment*],[plusieurs par Activity. Gérés par Fragment Manager],
  [*Service*],[Réaliser longues opérations en arrière-plan],
  [Foreground - par intent ou méthode],[notification visible: Lecteur Audio, Téléchargement,...],
  [Background - par intent ou méthode],[sans interface utilisateur, limité dans le temps: sync. serveur],
  [Bounded - par méthode, bind],[lié composant d'une app, détruit lorsque plus aucun n'est bind.],
  [*Broadcast Receivers*],[publish-subscribe, via manifest, runtime (moins de limitations)],
  [*Content Providers*],[accéder DB, Uri unique par donnée, CRUD],
  [FileProvider - sous classe],[partage sécurisé de fichiers entre apps à private storage],
  [*Permissions* - Bonnes pratiques],[Contrôle, Transparence, Minimisation],
  [Installation],[listées dans manifest, automatiquement accordées],
  [Exécution],[permissions dangereuses, listées dans manifest, pop up],
  [Spéciales],[réservées à OS ou constructeur téléphone],
)

#colbreak()

#set table(columns: (1fr, 1fr))
#table(
  theader[Diagrams],
  image("img/Screenshot 2024-11-13 225106.png", height: 9cm),image("img/Screenshot 2024-11-13 222058.png", height: 9cm),
  image("img/Screenshot 2024-11-13 232329.png", height: 9cm),image("img/Screenshot 2024-11-13 232347.png", height: 9cm),
)

#colbreak()

#set table(columns: (1fr, 2fr))
#table(
  theader[Interface graphique],
  [*UI-Thread* - ex. fréquence 16ms],[affichage graphique, ne doit pas être surchargé car usage fréquent],
  [Autres threads],[Communications(HTTP,Socket), DB, Périphériques(Bluetooth,NFC)],
  [*Material Components*],[librairie Google de widgets respectant ligne graphique],
  [*Types de vue*],[],
  [Visibilité],[VISIBLE, INVISIBLE (prend de la place), GONE (n'en prend pas)],
  [_TextView_],[afficher du texte à l'utilisateur],
  [_EditText_],[saisie de texte par l'utilisateur, spécifier type de valeur],
  [_TextField_],[meilleur que EditText. propriétés erreur, icone, ...],
  [_Button_],[interagir avec utilisateur via clic],
  [_ImageView_],[afficher une image depuis ressources ou mémoire],
  [_ImageButton_],[_Button_ et _ImageView_],
  [Choix binaires],[_CheckBox_, _Switch_ (simple bouton on-off), _ToggleButton_ (avec texte)],
  [_RadioGroup_],[qui contient des _RadioButton_],
  [_Spinner_],[liste de choix dans un menu déroulant],
  [_ProgressBar_],[barre de chargement animée. indéterminé ou non],
  [_SeekBar_],[renseigner un avancement (lecture youtube/musique)],
  [_WebView_],[afficher contenu web. basé sur chrome. requière permission Internet. utiliser pour charger contenu local. Js peut appeler code app et vice-versa],
  [*Feedback*],[],
  [_Toast_],[pop up furtif, en bas de l'écran],
  [_SnackBar_],[pop up en bas d'écran, possible d'ajouter action],
  [_Dialog_],[pop up demandant de prendre une décision: _AlertDialog_, _DatePickerDialog_, _ProgressDialog_, _FragmentDialog_],
  [*Notifications*],[message asynchrone affiché en dehors de l'app. actions possibles. Associé à un canal lors de sa création (identifiant unique)],
  [Expendable],[permet d'aggrandir la notification pour afficher plus de texte],
  [*ActionBar*],["menu", affiche par défaut titre app, peut accueillir actions, icones,...],
  [*ListView*],[affichage vertical défilant d'une collection d'éléments],
  [*RecyclerView*],[impose recyclage vues, objets différents types et layouts, animations intégrées ajout/update/delete éléments, scroll vertical/horizontal, DiffUtils, \+ performant que ListView, + complexe, manque fonctionnalités (OnItemClickListener)],
  [DiffUtils],[ne rafraichit que les éléments modifiés],
  [*Autres widgets*],[],
  [_Floating Action Button_ - FAB],[bouton flottant au dessus de interface, en bas à droite,.],
  [_GestureDetector_],[détecte les actions utilisateurs habituelles avec plusieurs doigts],
)

#colbreak()

#table(
  theader[LiveData et MVVM],
  [*LiveData* - librairie Android Jetpack],[observable, lifecycle aware (ne va notifier que si actif/visible)],
  [avantages:],[update visuelle auto, pas de memory leaks],
  [souvent créés dans un ViewModel],[remplace sauvegarde état, partage données entre activité/fragment],
  [MutableLiveData],[LiveData ne sont pas modifiables],
  [MVC - Responsabilités Activités/Fragments],[gestion vues (init, update), réaction user input, API system calls, ...],
  [MVC - Problèmes Activités/Fragments],[destruction/restoration à tout moment, perte d'appels asynchrones],
  [*MVVM*],[Model (Room) `<->` ViewModel `<->` View (UI controller)],
  [*ViewModel*],[si associé à une activité survit aux recréations (rotation), référencé Activités/Fragments, stockage données court terme/sans persistance],
  [particularités],[n'est instancié que si utilisé, si utilisé lorsqu'activité inactive->échec],
  [bonnes pratiques],[ne pas avoir de réf vers Vue/Activité/Fragment, plusieurs par Activité, exposer LiveData uniquement et pas MutableLiveData (mais possible de cast en MutableLiveData...)],
)

#colbreak()

#table(
  theader[Persistance des données],
  [dossier _assets_],[disponible lecture seule],
  [*Fichiers privés - interne*: chiffré],[géré par application - mise en cache: sans garantie],
  [*Fichiers privés - externe*: chemin absolu peut changer],[géré par application - mise en cache],
  [*Fichiers média partagés*],[API _Mediastore_ requêtes et résultats en Uri],
  [*SharedPreferences*],[key-value, géré par SDK, stockés dans XML privé interne],
  [*Base de données*],[Données stockées dans un fichier, SQLite],
  [*Android Room*],[ORM pour SQLite],
  [KSP - Kotlin Symbol Processing API],[génération de code par annotation],
  [Repository],[Single source of truth, point d'accès aux données],
  [Entités],[data class pour stocker objets, annotations],
  [DAO],[Data Access Object, interfaces pour accéder à la DB, requêtes, code généré automatiquement],
  [Database],[Définition DB, mise en relation composants, singleton],
  [Converters],[convertit types complexes afin de les stocker, par ex date],
  [Requêtes sur LiveData],[Exécuter chaque fois que table est modifiée -> _distinctUntilChanged_],
  [Requêtes sur toutes les données],[Charger en mémoire -> _Flow_ ou pagination],
  [non chiffrement des données],[DB: Android 10 pour stockage interne. applicatif: SQLite+SQLCipher],
  [Architecture],image("img/Screenshot 2024-11-14 152438.png"),
)

#set table(columns: (2fr, 2fr, 1fr, 1fr))

#table(
  theader[Persistance des données - Résumé],
  [Type],[Permissions],[Accès autres applications],[Suppression lors désinstallation],
  [Fichiers privés: Internes],[aucune], [non], [oui],
  [Fichiers privés: Externes],[aucune (19+)],[possible],[oui],
  [Média - _Mediastore API_],[READ_EXTERNAL_STORAGE],[oui],[non],
  [Autres fichiers partagés (téléchargements)],[aucune via _Storage Access Framework_],[oui],[non],
  [Préférences],[aucune],[non],[oui],
  [Base de données locale],[aucune],[non],[non],
)

#colbreak()
#colbreak()
#set table(columns: (1fr, 3fr))
#table(
  theader[Threads, coroutines],
  [*Threads*],[],
  [TODO],[],
  [*Coroutines*],[],
  [TODO],[],
  [Dispatchers],[Main : UI Thread, unique thread, Default : CPU, nb de threads = nb de CPU, IO : méthodes bloquantes, nb de threads = dynamique, max 64],
  [GlobalScope],[Scope application, au développeur de les stopper, déconseillé],
  [LifeCycleScope],[associé à l'objet avec cycle de vie (Activity ou Fragment), automatiquement stoppés],
  [ViewModelScope],[comme LifeCycleScope mais pour ViewModel],
  [*WorkManager*],[Immediate, Long Running (+10min), Deferrable(programmed/periodic)],
  [périodique],[Android Doze, App Standyby Buckets, App hibernation],
  [Android Doze],[si verrouillé sans chargeur -> veille profonde interrompue par tâches lors d'une maintenance window],
  [App Standyby Buckets],[classer apps selon utilisation : Active, Working set (~quotidienne), Frequent (~hebdomadaire), Rare (sporadique), Restricted (pas ouvert depuis +8 jours)],
  [App hibernation],[],
)