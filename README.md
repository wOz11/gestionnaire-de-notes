# Gestionnaire de notes

Application de gestion de notes développée en Java (SAÉ 1.01 — BUT Informatique, IUT de Lens).
L'interface s'affiche dans le navigateur via la bibliothèque **Navigator** fournie par l'IUT (`nav.jar`).

## Fonctionnalités
- Ajout d'étudiants et de matières (avec coefficients)
- Saisie des notes, gestion des absences
- Calcul des moyennes par étudiant et par matière
- Classement général et par matière
- Affichage/masquage de matières
- Sauvegarde et chargement des données dans un fichier

## Lancer le projet
Nécessite un JDK (Java 21+).

```bash
javac -cp .:nav.jar Test.java
java  -cp .:nav.jar Test
```

Sous Windows, remplace `:` par `;` dans le classpath :
```bash
javac -cp .;nav.jar Test.java
java  -cp .;nav.jar Test
```

## Auteur
Noah Ntambwe (KNA)
