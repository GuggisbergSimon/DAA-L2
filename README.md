# Labo4

## 6.1 Quelle est la meilleure approche pour sauver, même après la fermeture de l’app, le choix de l’option de tri de la liste des notes ?

### Vous justifierez votre réponse et l’illustrez en présentant le code mettant en œuvre votre approche.

En l'état la liste d'objets est triée dans la vue. Pour sauvegarder l'état de celle-ci même après la
fermeture de l'app, il faudrait que cela se fasse soit au niveau du modèle, soit au niveau du
ModelView.

TODO code

## 6.2 L’accès à la liste des notes issues de la base de données Room se fait avec une LiveData. Est-ce que cette solution présente des limites ?

### Si oui, quelles sont-elles ? Voyez-vous une autre approche plus adaptée ?

Oui.

- Les LiveData peuvent être cast en objet mutable, ce qui peut poser des problèmes de sécurité.
- Les LiveData vont être mises à jour en bloc, il convient de modifier le DAO pour limiter l'impact
  sur la performance en ne visant que les modifications
- Mise à jour fréquente de l'UI, ce qui peut poser problème pour une quantité considérable de
  données, nombres de likes sur une app pour un réseau social, par exemple, alors que ce genre
  d'informations n'est pas nécessaire de mettre à jour tout le temps.

TODO approche alternative

## 6.3 Les notes affichées dans la RecyclerView ne sont pas sélectionnables ni cliquables. Comment procéderiez-vous si vous souhaitiez proposer une interface permettant de sélectionner une note pour l’éditer ?

Il faudrait passer par un ListView qui possèdes des callbacks tels que OnItemClickListener. Ensuite
cela commencerait un Intent vers une nouvelle vue/activités, par exemple, afin d'éditer celle-ci,
qui impacterait la DB directement sans passer par un contrat pour une valeur de retour puisque la
valeur est partagée grâce au ViewModel.
