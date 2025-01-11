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

* `enroll()`
* `getToken()`
* `getAllContacts()`
* `getContact(id: Long)`
* `saveOrRegisterContact(contact: Contact)`
* `editContact(contact: Contact)`
* `registerContact(contact: Contact)`
* `deleteContact(contact: Contact)`

Le `ContactRepository` change le status des contacts puis appelle les méthodes de `RemoteSyncManager` pour effectuer la synchronisation et les sauvgarde en local.

Pour déterminer si une requête est un succès nous vérifions le code de réponse HTTP. Cela nous permet de vérifier si la requête est passé au niveau du serveur.
Dans ce cas là, le status du contact passe en "Ok".

`getToken()` retourne le token obtenue dans la liveData. A noté que avec notre implémentation actuelle, nous ne vérifions pas si il est nul, vu que la MainActivity s'assure qu'un token soit mis en place.

Si l'on souhaite augmenter la robutesse, le RemoteSyncManager pourrait vérifier si le token est null et si c'est le cas, enroll. Vu que la MainActivity observe la liveData, il devrait être mis a jour dans le SharedPreference.
Nous avons décider de garder la version actuelle pour garder le concept de eager loadiding. 

Une implémenttation comme celle-ci serait plus robuste
```kotlin
private fun getToken() : String {
        var token = uuid.getData().value
        if (token == null) {
            token = enroll()
        }
        return token
    }
```
A noter que dans `enroll()` le remoteSyncManager post la nouvelle valeur dans le liveData.
Cela signifie aussi que notre implémentation actuelle ne permet pas d'avoir plusieurs Uuid diférente. 
Transformer notre Uuid livedata en tableau serait une approche pour permettre ceci, mais cela n'est pas nécessaire dans le cadre de ce projet.

#### SharedPreferences pour token

Nous avons utilisé les SharedPreferences pour stocker le token de l'utilisateur. Pour faciliter les interactions avec l'UUID nous avons décider de le mettre dans un livedata dans une classe dédiée,
Ainsi si nous souhaiton l'utilser dans d'autre composant, il est facile d'y accéder.

Pour garantir que l'UUID soit partager correctement, nous avons décidé d'utiliser un compagnon object, pour qu'elle soit partagée parmis tout les instances.

L'activité principale s'occupe de charger l'UUID du cache (SharedPreference). S'il il n'existe pas, elle fait un appel a enrol (défini dans RemotSyncManager) pour obtenir un nous token.
De plus elle observe la livedata pour mettre a jour le cache en cas de changement de valeur du token, typiquement si on décide de supprimer tout les données et partir a neuf (quand on appuie sur le bouton d'enroll)

#### ContactAdapter

Nous avons modifier le ContactAdapter, plus précisement  le ViewHolder pour que les objets dans la liste aient une couleur différente en fonction de leur status par rapport au serveur distant

Ainsi les objets créer mais pas encore sur le serveur sont bleus

Ceux éditer en local mais pas sur le seveur orange

Ceux supprimer en local rouge (ils disparaissent uniquement lorsque la syncronisation est un succès)

Les objets verts sont ceux dont le status est syncrone. 

## Implémentation des appels API 4.3
TODO: Make it proper french, it's just I do not want to forget.

Pour l'implémentation de ces méthodes nous avont choisit Java-net-url pour sa simplicité d'utilistaion

Pour ce qui est de la syncro entre le server et la DB local, nous avons choisi de d'abord effectuer les modification en local,
en utilisant des status intermédiaire telle que "Updated, New, Deleted" et lorsque le serveur a répondu, nous changeons ces status par "Ok" dans la base de donnée.

A noter que pour le Deleted, on passe d'abord le conctact par Deleted, avant de, si le serveur a répondu ok, supprimer.