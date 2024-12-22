# Labo 6

Auteurs :
- Patrick Furrer
- Simon Guggisberg
- Jonas Troeltsch

## Rapport

## Implémentation des appels API 4.3
TODO: Make it proper french, it's just I do not want to forget.

Pour l'implémentation de ces méthodes nous avont choisit Java-net-url pour sa simplicité d'utilistaion

Pour ce qui est de la syncro entre le server et la DB local, nous avons choisi de d'abord effectuer les modification en local,
en utilisant des status intermédiaire telle que "Updated, New, Deleted" et lorsque le serveur a répondu, nous changeons ces status par "Ok" dans la base de donnée.

A noter que pour le Deleted, on passe d'abord le conctact par Deleted, avant de, si le serveur a répondu ok, supprimer.