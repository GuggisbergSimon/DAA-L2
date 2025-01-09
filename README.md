# Labo 6

Auteurs :
- Patrick Furrer
- Simon Guggisberg
- Jonas Troeltsch

## Rapport

### Explication de l'implémentation

#### Interface utilisateur

Pour l'implémentation de l'interface utilisateur, nous avons choisi de ne pas utiliser Jetpack Compose et d'utiliser la méthode traditionnelle avec XML et Kotlin.

#### Contact

Pour rendre les Contacts utilisable pour faire un sync avec la database distante, nous avons ajouté 2 attributs supplémentaires : `status` et `serverId`. `status` est un enum qui peut prendre les valeurs `NEW`, `UPDATED`, `DELETED` ou `OK`. `serverId` est l'id du contact sur le serveur.

Afin de pouvoir transformer les JSON reçu en objet Contact, nous avons créé une classe ContactDTO qui contient les mêmes attributs que la classe Contact mais avec des types de données différents. Nous avons ensuite créé une méthode `toContact()` qui permet de transformer un ContactDTO en Contact.

#### Remote Sync

La class `RemoteSyncManager` permet de synchroniser les contacts locaux avec le serveur. Elle contient les méthodes suivantes pour effectuer la synchronisation :

* `getOrFetchToken()`
* `getAllContacts()`
* `getContact(id: Long)`
* `saveOrRegisterContact(contact: Contact)`
* `editContact(contact: Contact)`
* `registerContact(contact: Contact)`
* `deleteContact(contact: Contact)`

Le `ContactRepository` change le status des contacts puis appelle les méthodes de `RemoteSyncManager` pour effectuer la synchronisation et les sauvgarde en local.

#### SharedPreferences pour token

Nous avons utilisé les SharedPreferences pour stocker le token de l'utilisateur.

## Implémentation des appels API 4.3
TODO: Make it proper french, it's just I do not want to forget.

Pour l'implémentation de ces méthodes nous avont choisit Java-net-url pour sa simplicité d'utilistaion

Pour ce qui est de la syncro entre le server et la DB local, nous avons choisi de d'abord effectuer les modification en local,
en utilisant des status intermédiaire telle que "Updated, New, Deleted" et lorsque le serveur a répondu, nous changeons ces status par "Ok" dans la base de donnée.

A noter que pour le Deleted, on passe d'abord le conctact par Deleted, avant de, si le serveur a répondu ok, supprimer.