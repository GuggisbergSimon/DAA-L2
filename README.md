# L2

1.
- Que se passe-t-il si l’utilisateur appuie sur « back » lorsqu’il se trouve sur la seconde Activité ?
- Veuillez réaliser un diagramme des changements d’état des deux Activités pour les utilisations
suivantes, vous mettrez en évidence les différentes instances de chaque Activité :
  - L’utilisateur ouvre l’application, clique sur le bouton éditer, renseigne son prénom et
sauve.
  - L’utilisateur ouvre l’application en mode portrait, clique sur le bouton éditer, bascule en
mode paysage, renseigne son prénom et sauve.
- Que faut-il mettre en place pour que vos Activités supportent la rotation de l’écran ? Est-ce
nécessaire de le réaliser pour les deux Activités, quelle est la différence ?
Hint : Que se passe-t-il si on bascule la première Activité après avoir saisi son prénom ?
Comment peut-on éviter ce comportement indésirable ? Quelle est la différence avec la
seconde Activité ?

2.
- Les deux Fragments fournis implémentent la restauration de leur état. Si on enlève la
sauvegarde de l’état sur le ColorFragment sa couleur sera tout de même restaurée, comment
pouvons-nous expliquer cela ?
- Si nous plaçons deux fois le CounterFragment dans l’Activité, nous aurons deux instances
indépendantes de celui-ci. Comment est-ce que la restauration de l’état se passe en cas de
rotation de l’écran ?

3.
- A l’initialisation de l’Activité, comment peut-on faire en sorte que la première étape s’affiche
automatiquement ?
- Comment pouvez-vous faire en sorte que votre implémentation supporte la rotation de
l’écran ? Nous nous intéressons en particulier au maintien de l’état de la pile de Fragments et
de l’étape en cours lors de la rotation.
- Dans une transaction sur le Fragment, quelle est la différence entre les méthodes add et
replace ?