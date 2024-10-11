# Labo 2

_**Furrer Patrick, Guggisberg Simon et Troeltsch Jonas**_

## Partie 1

### Que se passe-t-il si l’utilisateur appuie sur « back » lorsqu’il se trouve sur la seconde Activité ?

L'utilisateur revient à l'activité précédente sur la pile, ici la première.

### Veuillez réaliser un diagramme des changements d’état des deux Activités pour les utilisations suivantes, vous mettrez en évidence les différentes instances de chaque Activité :

#### L’utilisateur ouvre l’application, clique sur le bouton éditer, renseigne son prénom et sauve.

![Diagramme2](./diagrams/1.svg)

#### L’utilisateur ouvre l’application en mode portrait, clique sur le bouton éditer, bascule en mode paysage, renseigne son prénom et sauve.

![Diagramme2](./diagrams/2.svg)


### Que faut-il mettre en place pour que vos Activités supportent la rotation de l’écran ? Est-ce nécessaire de le réaliser pour les deux Activités, quelle est la différence ?
_Hint : Que se passe-t-il si on bascule la première Activité après avoir saisi son prénom ? Comment peut-on éviter ce comportement indésirable ? Quelle est la différence avec la seconde Activité ?_

Il faut mettre en place la sauvegarde/chargement de l'état de l'activité, ici le nom de l'utilisateur. Ceci se fait au travers de la méthode onCreate et onRestoreInstanceState, via un bundle.

## Partie 2

### Les deux Fragments fournis implémentent la restauration de leur état. Si on enlève la sauvegarde de l’état sur le ColorFragment sa couleur sera tout de même restaurée, comment pouvons-nous expliquer cela ? 

Car les informations sont enregistrées dans le Bundle, qui est passé en argument à la fonction onCreate. Ceci se fait de manière indépendant de l'usage de onSaveInstanceState.

### Si nous plaçons deux fois le CounterFragment dans l’Activité, nous aurons deux instances indépendantes de celui-ci. Comment est-ce que la restauration de l’état se passe en cas de rotation de l’écran ? 

Chaque instance de ces Fragments sont indépendantes et possèdent un identifiant unique. Ceci permet de sauvegarder les informations de manière indépendante dans le Bundle et donc récupérer les informations de manière individuelle.

## Partie 3

### A l’initialisation de l’Activité, comment peut-on faire en sorte que la première étape s’affiche automatiquement ?

Il suffit de rajouter le fragment dans le layout de l'activité, ou de le rajouter lors de la création de l'activité.

### Comment pouvez-vous faire en sorte que votre implémentation supporte la rotation de l’écran ? Nous nous intéressons en particulier au maintien de l’état de la pile de Fragments et de l’étape en cours lors de la rotation.

En implémentant onSaveInstanceState et onRestoreInstanceState, on peut sauvegarder l'état du  fragment, à la manière du fragment Counter donné en exemple pour le point 2.

### Dans une transaction sur le Fragment, quelle est la différence entre les méthodes add et replace ?

Replace supprime le fragment précédent, add le rajoute au layout, sans l'afficher au dessus du précédent.


### TODO
* une description de l’implémentation de chaque partie en justifiant d’éventuels
* la liste des manipulations effectuées avec les résultats associés.