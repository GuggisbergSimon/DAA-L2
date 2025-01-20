#import "@preview/codly:1.2.0": *
#import "@preview/codly-languages:0.1.1": *
#show: codly-init.with()

// The project function defines how your document looks.
// It takes your content and some metadata and formats it.
// Go ahead and customize it to your liking!
#let project(title: "DAA - NFC", authors: (), body) = {
  // Set the document's basic properties.
  set document(author: authors, title: title)
  set page(
    numbering: "1", 
    number-align: center,
    header: [
      #grid(
        columns: (1fr, auto),
        align(left)[
          *HEIG-VD* \
          DAA - NFC
        ],
        align(right)[
          P. Furrer, S. Guggisberg, J. Troeltsch
        ]
      )
      #line(length: 100%, stroke: 0.5pt)
    ]
  )
  set text(font: "Libertinus Serif", lang: "fr")

  // Title row.
  align(center)[
    #block(text(weight: 700, 1.75em, title))
  ]

  // Author information.
  pad(
    top: 0.5em,
    bottom: 0.5em,
    x: 2em,
    grid(
      columns: (1fr,) * calc.min(3, authors.len()),
      gutter: 1em,
      ..authors.map(author => align(center, strong(author))),
    ),
  )

  // Main body.
  set par(justify: true)

  body
}
#show heading: set block(below: 1em)

#show: project.with(
  title: "NFC - Near Field Communication",
  authors: (
    "Patrick Furrer",
    "Simon Guggisberg",
    "Jonas Troeltsch"
  ),
)


#codly(
  languages: (
    rust: (name: "Kotlin", version: "1.5.31"),
  )
)
= Introduction

NFC est un ensemble de protocoles de communication sans fil qui permettent l'\échange de données et le chargement de batterie et ce jusqu'à \~4cm.
Se basant sur la technologie RFID (radio frequency identification), l'histoire de NFC remonte jusqu'en 1983.
Ce n'est qu'en 2003 que la norme ISO NFC a été approuvée, et en 2006 que les spécifications pour NFC Tags ont été définies.

== NFC Tag

Il s'agit d'un format de donnée écrit sur le tag : NDEF (NFC Data Exchange Format).
Ce standard est défini par le NFC Forum.

- *Simple* : sémantique Read/Write (ou Read-Only)
- *Avancé* : opérations mathématiques, hardware cryptographique, environnement permettant exécution de code sur le tag

== Android

Afin de pouvoir utiliser le lecteur NFC d'un telephone Android dans une application, il faut demander la permission dans le manifest pour utiliser la technologie.

```xml
<uses-permission android:name="android.permission.NFC" />
```

Tous les téléphones ne disposent pas d'un lecteur NFC.
Pour que l'application ne s'affiche sur Google Play Store que pour les téléphones disposant d'un lecteur NFC, il faut ajouter ceci dans le manifest :

```xml
<uses-feature android:name="android.hardware.nfc" android:required="true" />
```

Il est également recommandé de requérir une certaine version d'API car les fonctionnalités NFC ont été améliorées au fil des versions d'Android :

- 9+ : `ACTION_NDEF_DISCOVERED` et `EXTRA_NDEF_MESSAGES`
- 10+ : Bien meilleur support pour Reader/Writer et Foreground Dispatch System
- 14+ : Méthodes additionnelles pour créer des records NDEF

```xml
<uses-sdk android:minSdkVersion="10" />
```

Deux modes distincts existent pour pouvoir interagir avec la technologie NFC du point de vue Android :
- *Reader/Writer mode* : pour lire et écrire des tags NFC
- *Émulation de carte* : pour émuler une carte NFC via un téléphone, qui peut ensuite être accédé par un lecteur de carte NFC externe.

Pour écrire dans un NFC tag, il faut établir son propre protocole de communication.

#pagebreak()

=== Données NDEF

Les données sont encapsulées dans un message `NdefMessage` qui contient un ou plusieurs enregistrements `NdefRecord`.
Chaque enregistrement doit correspondre aux spécifications du type de données choisi.

=== Tag Dispatch System

Lorsque le téléphone est déverrouillé, et que l'option NFC est activée, le téléphone va scanner les tags NFC à proximité.

En cas de détection de tag, le TAG Dispatch System va :

1. Parser le NFC tag pour trouver le MIME type ou une URI qui identifie le payload de données dans le tag
2. Encapsuler le MIME type ou l'URI ainsi que le payload dans un intent
3. Démarrer une activité basée sur l'intent

Cet intent est pour éviter à l'utilisateur, une fois proche du NFC tag, de devoir choisir une application pour lire les données.
En effectuant ce type de mouvement, la connexion pourrait être perdue, en raison de la très courte distance de connexion (\~4cm).

Il est possible de déclarer un intent filter pour intercepter les intents et traiter les données.

=== Types d'actions

- `ACTION_NDEF_DISCOVERED` : pour lire des données NDEF (MIME type ou URI)
- `ACTION_TECH_DISCOVERED` : pour lire technologiques (technologies NFC : NfcA, NfcB, NfcF ...)
- `ACTION_TAG_DISCOVERED` : si aucune des deux actions précédentes n'est définie

#figure(
  image("nfc_tag_dispatch.png", width: 95%),
  caption: [
    NFC Tag Dispatch System
  ],
)

#pagebreak()

==== Exemple NDEF

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
    <data android:scheme="https" android:host="developer.android.com" android:pathPrefix="/index.html" />
</intent-filter>
```

==== Exemple Technologies

Pour déclarer une intent au niveau des technologies, il faut d'abord créer une ressource les listant, sous `project-root/res/xml/tech_list.xml`.

Exemple de fichier de ressource acceptant technologies NfcA ET Ndef OU NfcB ET Ndef :

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

    <meta-data android:name="android.nfc.action.TECH_DISCOVERED" android:resource="@xml/nfc_tech_filter" />
    ...
</activity>
```

#pagebreak()

==== Exemple Tag Discovered

En cas de fallback sur l'action `ACTION_TAG_DISCOVERED`, cela se fait ainsi dans le manifest :

```xml
<intent-filter>
    <action android:name="android.nfc.action.TAG_DISCOVERED" />
</intent-filter>
```

=== Foreground Dispatch System

Foreground Dispatch System permet de bypasser le Tag Dispatch System pour lire des tags NFC qui auraient ouverts une autre activité, par l'activité courante, en premier plan.

Ceci se fait au travers de méthodes de la classe `NfcAdapter` qui doivent être activées et désactivées lorsque l'application est mise en pause, ou résumée.

À noter que, si l'on ne connaît ni les MIME type ni les technologies à filtrer, il est recommandé de les laisser à null lors de l'appel de `enableForegroundDispatch`.

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

Exemple de paramétrage de ces filtres. :

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

=== Émulation de carte - Host Card Emulation - HCE

Cela peut se faire : 
  - par un élément sécurisé, une puce séparée, souvent hébergé sur une carte SIM, demande un matériel dédié.
  - Entièrement côté software. Utilise les sécurités Android telles que Application Sandbox pour s'assurer que les données ne soient pas accessibles par d'autres applications.

Les services HCE sont basés sur `Service` de Android, ce qui les autorise à tourner en background sans interface utilisateur requise.



Pour pouvoir créer un Service HCE, il faut hériter de `HostApduService` et implémenter les méthodes `processCommandApdu` et `onDeactivated`.

À noter qu'un Application Protocol Data Unit (ADPU) sont des paquets échangés entre un NFC reader et un HCE service.

```kt
class MyHostApduService : HostApduService() {

    override fun processCommandApdu(commandApdu: ByteArray, extras: Bundle?): ByteArray {
       //called whenever a NFC reader sends an APDU
       //TODO
    }

    override fun onDeactivated(reason: Int) {
      //The link between the NFC reader and the device is broken
       //TODO
    }
}
```

Il faut ensuite modifier le manifest pour pointer, ici, vers un fichier `apduservice.xml` :

```xml
<service android:name=".MyHostApduService" android:exported="true"
         android:permission="android.permission.BIND_NFC_SERVICE">
    <intent-filter>
        <action android:name="android.nfc.cardemulation.action.HOST_APDU_SERVICE"/>
    </intent-filter>
    <meta-data android:name="android.nfc.cardemulation.host_apdu_service"
               android:resource="@xml/apduservice"/>
</service>
```

Puis, dans le fichier `apduservice.xml` il faut déclarer les groupes d'Application Identification (AID).

Une AID est une chaîne de charactères unique qui permet d'identifier le type de service que l'application donne au lecteur NFC.
Il est également possible de spécifier quelle application a la priorité, lorsqu'elle est en premier plan, pour quelle AID, ce qui peut être utile en cas de conflit si plusieurs services souhaitent traiter la même AID.

Ici, le groupe AID déclaré contient deux AIDs, plus d'informations ci-dessous.
```xml
<host-apdu-service xmlns:android="http://schemas.android.com/apk/res/android"
           android:description="@string/servicedesc"
           android:requireDeviceUnlock="false">
    <aid-group android:description="@string/aiddescription"
               android:category="other">
        <aid-filter android:name="F0010203040506"/>
        <aid-filter android:name="F0394148148100"/>
    </aid-group>
</host-apdu-service>
```

= Liens utiles

- NFC Forum : #link("https://nfc-forum.org/")
- Documentation Android NFC : #link("https://developer.android.com/develop/connectivity/nfc")
