# Application de Gestion de Stock

## Prérequis

1. **Java JDK 11** ou supérieur
2. **Apache Maven** installé et configuré
3. **MySQL Server** installé et en cours d'exécution

## Configuration de la base de données

1. Assurez-vous que MySQL est démarré
2. Modifiez le fichier `src/main/resources/application.properties` si nécessaire :
   - `hibernate.connection.username` : votre nom d'utilisateur MySQL (par défaut: root)
   - `hibernate.connection.password` : votre mot de passe MySQL (par défaut: vide)
   - `hibernate.connection.url` : modifiez le port si nécessaire (par défaut: 3306)

La base de données `gestion_stock` sera créée automatiquement au premier lancement.

## Compilation du projet

```bash
mvn clean compile
```

## Exécution de l'application

### Méthode 1 : Utiliser Maven Exec Plugin

```bash
mvn exec:java
```

### Méthode 2 : Compiler et exécuter avec java

```bash
# Compiler le projet
mvn clean package

# Exécuter avec java
java -cp target/classes:target/dependency/* ma.projet.test.TestApplication
```

### Méthode 3 : Utiliser votre IDE

1. Ouvrez le projet dans votre IDE (IntelliJ IDEA, Eclipse, etc.)
2. Naviguez vers `src/test/java/ma/projet/test/TestApplication.java`
3. Clic droit sur la classe et choisissez "Run"

## Structure du projet

- **Entités** : `src/main/java/ma/projet/classes/`
- **DAO** : `src/main/java/ma/projet/dao/`
- **Services** : `src/main/java/ma/projet/service/`
- **Utilitaires** : `src/main/java/ma/projet/util/`
- **Tests** : `src/test/java/ma/projet/test/`

## Fonctionnalités testées

Le programme `TestApplication` teste :
1. Création de catégories
2. Création de produits
3. Création de commandes
4. Création de lignes de commande
5. Affichage des produits par catégorie
6. Recherche de produits commandés entre deux dates
7. Affichage des produits d'une commande (format spécifié)
8. Recherche de produits avec prix > 100 DH (requête nommée)

