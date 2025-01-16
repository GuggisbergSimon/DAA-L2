# NFC - Near Field Communication

Auteurs :

- Patrick Furrer
- Simon Guggisberg
- Jonas Troeltsch

## Introduction

NFC est un ensemble de protocoles de communication sans fil qui permettent l'échange de données et
le chargement de batterie et ce jusqu'à ~4cm.
Se basant sur la technologie RFID (radio frequency identification), l'histoire de NFC remonte jusqu'
en 1983.
Ce n'est qu'en 2003 que la norme ISO NFC a été approuvée, et en 2006 que les spécifications pour NFC
Tags ont été définies.

### NFC Tag

Il s'agit d'un format de donnée écrit sur le tag : NDEF (NFC Data Exchange Format).
Ce standard est défini par le NFC Forum.

- Simple : sémantique Read/Write (ou Read-Only)
- Avancé : opérations mathématiques, hardware cryptographique, environnement permettant exécution de
  code sur le tag

### Android

Il faut demander la permission dans le manifest pour utiliser la technologie.

```xml

<uses-permission android:name="android.permission.NFC" />
```

Pas tous les téléphones ne disposent d'un lecteur NFC.
Pour que l'application ne s'affiche sur Google Play Store que pour les téléphones disposant d'un
lecteur NFC, il faut ajouter ceci dans le manifest :

```xml

<uses-feature android:name="android.hardware.nfc" android:required="true" />
```

Il est également recommendé de requérir une certaine version d'API.

- 9+ : `ACTION_NDEF_DISCOVERED` et `EXTRA_NDEF_MESSAGES`
- 10+ : Bien meilleur support pour Reader/Writer et Foreground Dispatch System
- 14+ : Méthodes additionnelles pour créer des records NDEF

```xml

<uses-sdk android:minSdkVersion="10" />
```

Deux modes :

- Reader/Writer mode : pour lire et écrire des tags NFC
- Émulation de carte : pour émuler une carte NFC via un téléphone, qui peut ensuite être accédé par
  un lecteur de carte NFC externe.

Pour écrire dans un NFC tag, il faut établir son propre protocole de communication.

#### Données NDEF

Les données sont encapsulées dans un message `NdefMessage` qui contient un ou plusieurs
enregistrements `NdefRecord`.
Chaque enregistrement doit correspondre aux spécifications au type de données choisi.

#### Tag Dispatch System

Lorsque le téléphone est déverouillé, et que l'option NFC est activée, le téléphone va scanner les
tags NFC à proximité.

En cas de détection de tag, le TAG Dispatch System va :

1. Parser le NFC tag pour trouver le MIME type ou une URI qui identifie le payload de données dans
   le tag
2. Encapsuler le MIME type ou l'URI ainsi que le payload dans un intent
3. Démarre une activité basée sur l'intent

Cet intent est pour éviter à l'utilisateur, une fois proche du NFC tag, de devoir choisir une
application pour lire les données.
En effectuant ce type de mouvement, la connexion pourrait être perdue, en raison de la très courte
distance de connexion (~4cm).

Il est possible de déclarer un intent filter pour intercepter les intents et traiter les données.

Il existe plusieurs types d'actions, chacune en fallback en fonction de la précédente :

- `ACTION_NDEF_DISCOVERED` : pour lire des données NDEF (MIME type ou URI)
- `ACTION_TECH_DISCOVERED` : pour lire technologiques (technologies NFC : NfcA, NfcB, NfcF ...)
- `ACTION_TAG_DISCOVERED` : si aucune des deux actions précédentes n'est définie

![NFC Tag Dispatch System](https://developer.android.com/static/images/nfc_tag_dispatch.png)

##### Exemple NDEF

Exemple de déclaration d'intent, dans le manifest, pour le type `text/plain` :

```xml

<intent-filter>
    <action android:name="android.nfc.action.NDEF_DISCOVERED" />
    <category android:name="android.intent.category.DEFAULT" />
    <data android:mimeType="text/plain" />
</intent-filter>
```

Exemple de déclaration d'intent, pour une URI :

```xml

<intent-filter>
    <action android:name="android.nfc.action.NDEF_DISCOVERED" />
    <category android:name="android.intent.category.DEFAULT" />
    <data android:scheme="https" android:host="developer.android.com"
        android:pathPrefix="/index.html" />
</intent-filter>
```

##### Exemple Technologies

Pour déclarer une intent au niveau des technologies, il faut d'abord créer une ressource les
listant, sous `project-root/res/xml/tech_list.xml`. Le nom du fichier n'importe pas. :

Exemple de fichier de ressource acceptant technologies NfcA ET Ndef OU NfcB ET Ndef

```xml 

<resources xmlns:xliff="urn:oasis:names:tc:xliff:document:1.2">
    <tech-list>
        <tech>android.nfc.tech.NfcA</tech>
        <tech>android.nfc.tech.Ndef</tech>
    </tech-list>
    <tech-list>
        <tech>android.nfc.tech.NfcB</tech>
        <tech>android.nfc.tech.Ndef</tech>
    </tech-list>
</resources>
```

Puis, dans le manifest, il faut ajouter un lien vers la ressource créée :

```xml

<activity>
    ...
    <intent-filter>
        <action android:name="android.nfc.action.TECH_DISCOVERED" />
    </intent-filter>

    <meta-data android:name="android.nfc.action.TECH_DISCOVERED"
        android:resource="@xml/nfc_tech_filter" />
    ...
</activity>
```

##### Exemple Tag Discovered

En cas de fallback sur l'action `ACTION_TAG_DISCOVERED`, alors cela se fait ainsi dans le manifest :

```xml

<intent-filter>
    <action android:name="android.nfc.action.TAG_DISCOVERED" />
</intent-filter>
```

#### Foreground Dispatch System

Foreground Dispatch System permet de bypasser le Tag Dispatch System pour lire des tags NFC qui
auraient ouverts une autre activité, par l'activité courante, en premier plan.

Ceci se fait au travers de méthodes de la classe `NfcAdapter` qui doivent être activée et désactivée
lorsque l'application est mise en pause, ou résumée.

À noter que l'on peut passer en argument une liste de filtres d'intent et de technologies à écouter.

```kotlin
private lateinit var adapter: NfcAdapter
private lateinit var pendingIntent: PendingIntent
private lateinit var intentFiltersArray: Array<IntentFilter>
private lateinit var techListsArray: Array<Array<String>>

override fun onResume() {
    super.onResume()
    adapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray)
}

override fun onPause() {
    super.onPause()
    adapter.disableForegroundDispatch(this)
}
```

Exemple de paramétrage de ces filtres :

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val intent = Intent(this, javaClass).apply {
        addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }
    pendingIntent = PendingIntent.getActivity(
        this, 0, intent,
        PendingIntent.FLAG_MUTABLE
    )
    val ndef = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED).apply {
        try {
            addDataType("*/*")
        } catch (e: IntentFilter.MalformedMimeTypeException) {
            throw RuntimeException("fail", e)
        }
    }

    intentFiltersArray = arrayOf(ndef)
    techListsArray = arrayOf(arrayOf(NfcA::class.java.name, NfcV::class.java.name))
    adapter = NfcAdapter.getDefaultAdapter(this)

    setContentView(R.layout.activity_main)
}
```

## Liens utiles

- NFC Forum : https://nfc-forum.org/
- Documentation Android NFC : https://developer.android.com/develop/connectivity/nfc
