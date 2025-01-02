# NFC - Near Field Communication

Auteurs :

- Patrick Furrer
- Simon Guggisberg
- Jonas Troeltsch

## Spécifications

Communication de 4cm ou moins entre un NFC tag et un téléphone Android, ou entre deux téléphones
Android.

### NFC Tag

Format de donnée écrit sur le tag : NDEF (NFC Data Exchange Format).
Ce standard est défini par le NFC Forum.

- Simple : sémantique Read/Write (ou Read-Only)
- Avancé : opérations mathématiques, hardware cryptographique, environnement permettant exécution de
  code sur le tag

### Android

Pas tous les téléphones sont compatibles.
Il faut demander la permission dans le manifest.

Deux modes :

- Reader/Writer mode : pour lire et écrire des tags NFC
- Émulation de carte : pour émuler une carte NFC via un téléphone, qui peut ensuite être accédé par
  un lecteur de carte NFC externe.

Foreground Dispatch System permet de bypasser le Tag Dispatch System pour lire des tags NFC qui
auraient ouverts une autre activité, par l'activité courante.

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
En effectuant ce type de mouvement, la connexion pourrait être perdue, en raison de la très petite
distance de contact.

Il est possible de déclarer un intent filter pour intercepter les intents et traiter les données

Il existe plusieurs types d'actions, chacune en fallback en fonction de la précédente :

- ACTION_NDEF_DISCOVERED : pour lire des données NDEF (MIME type ou URI)
- ACTION_TECH_DISCOVERED : pour lire technologiques (technologies NFC : NfcA, NfcB, NfcF ...)
- ACTION_TAG_DISCOVERED : si aucune des deux actions précédentes n'est définie

![NFC Tag Dispatch System](https://developer.android.com/static/images/nfc_tag_dispatch.png)

## Liens utiles

- NFC Forum : https://nfc-forum.org/
- https://developer.android.com/develop/connectivity/nfc
- https://medium.com/@meetjanani47/how-to-scan-and-read-nfc-tags-in-mobile-application-using-android-kotlin-dd31695b9a3e
