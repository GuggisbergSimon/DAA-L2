# DAA Lab 03

Auteurs :

- Patrick Furrer
- Simon Guggisberg
- Jonas Troeltsch

## 4.1 Pour le champ remark, destiné à accueillir un texte pouvant être plus long qu’une seule ligne,

### Quelle configuration particulière faut-il faire dans le fichier XML pour que son comportement soit correct ?

Nous pensons notamment à la possibilité de faire des retours à la ligne, d’activer
le correcteur orthographique et de permettre au champ de prendre la taille nécessaire.

```
android:layout_width="wrap_content"
android:layout_height="wrap_content"
android:inputType="textMultiLine|textAutoCorrect"
```

- Pour que le champ de texte puisse prendre la taille nécessaire, à l'aide de wrap_content
- Pour que le texte soit sur plusieurs lignes et qu'il soit corrigé automatiquement,
  respectivement.

## 4.2 Pour afficher la date sélectionnée via le DatePicker nous pouvons utiliser un DateFormat permettant par exemple d’afficher 12 juin 1996 à partir d’une instance de Date.

Le formatage des dates peut être relativement différent en fonction des langues, la traduction des
mois par exemple, mais également des habitudes régionales différentes : la même date en anglais
britannique serait 12th June 1996 et en anglais américain June 12, 1996.

### Comment peut-on gérer cela au mieux ?

En se basant sur le format de date de l'utilisateur, on peut utiliser la locale de l'utilisateur
pour formater la date.

```kotlin
val dateFormat =
    java.text.DateFormat.getDateInstance(java.text.DateFormat.LONG, Locale.getDefault())
dateEditText.setText(dateFormat.format(calendar.time))
```

## 4.3 Veuillez choisir une question en fonction de votre choix d’implémentation :

### Si vous avez utilisé le DatePickerDialog du SDK. En cas de rotation de l’écran du smartphone lorsque le dialogue est ouvert, une exception android.view.WindowLeaked sera présente dans les logs, à quoi est-elle due ?

//TODO

## 4.4 Lors du remplissage des champs textuels, vous pouvez constater que le bouton « suivant » présent sur le clavier virtuel permet de sauter automatiquement au prochain champ à saisir

### Est-ce possible de spécifier son propre ordre de remplissage du questionnaire ?

Oui, cela est possible avec nextFocusDown.
Les propriétés nextFocusLeft, nextFocusRight, nextFocusUp permettent la navigation au clavier dans
d'autres directions.

```
android:nextFocusDown="@+id/nextField"
```

### Arrivé sur le dernier champ, est-il possible de faire en sorte que ce bouton soit lié au bouton de validation du questionnaire ?

Hint : Le champ remark, multilignes, peut provoquer des effets de bords en fonction du clavier
virtuel utilisé sur votre smartphone. Vous pouvez l’échanger avec le champ e-mail pour faciliter vos
recherches concernant la réponse à cette question.

Il faut ajouter cette propriété dans l'EditText du dernier champ, celui-ci remarques :
```
android:imeOptions="actionDone"
```

```kotlin
val lastField = findViewById<EditText>(R.id.lastField)
lastField.setOnEditorActionListener { _, actionId, _ ->
    if (actionId == EditorInfo.IME_ACTION_DONE) {
        okButton.performClick()
        true
    } else {
        false
    }
}
```

## 4.5 Pour les deux Spinners (nationalité et secteur d’activité)

### Comment peut-on faire en sorte que le premier choix corresponde au choix null, affichant par exemple le label « Sélectionner » ?

En ajoutant un élément vide au début de la liste des éléments du Spinner.

```kotlin
val items = resources.getStringArray(array).toMutableList()
items.add(0, getString(R.string.select))
val adapter = ArrayAdapter(
    this,
    android.R.layout.simple_spinner_item,
    items
)
adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
spinner.adapter = adapter
```

### Comment peut-on gérer cette valeur pour ne pas qu’elle soit confondue avec une réponse ?

En considérant l'index 0 du Spinner comme étant null.

```kotlin
val selected = if (spinner.selectedItemPosition == 0) null else spinner.selectedItem.toString()
```
