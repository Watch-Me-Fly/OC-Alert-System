# Système d'alerte


## Les points API
* [Page principale](http://localhost:8080/)
### Les données 
* [Les personneses](http://localhost:8080/person/)
* [Les casernes](http://localhost:8080/firestation/)
* [Les dossiers médicaux](http://localhost:8080/medicalRecord/)
### Autre requêtes (exemples pré-renseignés)
* [Retourner une liste des personnes couverts par une casernes](http://localhost:8080/firestation?stationNumber=2)
* [Retourner une liste des enfants pour une addresse donnée](http://localhost:8080/childAlert?address=1509%20Culver%20St)
* [Retourner une liste des numéros de téléphone des personnes couverts par une caserne](http://localhost:8080/phoneAlert?firestation=3)
* [Les des habitants à une addresse données, plus le numéro des casernes](http://localhost:8080/fire?address=748%20Townings%20Dr)
* [Liste des foyers desservis par les casernes, regroupés par addresse](http://localhost:8080/flood/stations?stations=1,2)
* [Trouver toutes les personnes portant un nom](http://localhost:8080/personInfo?lastName=Boyd)
* [Restituer tout les addresses mai dans une ville](http://localhost:8080/communityEmail?city=Culver)

## Le projet

### Initialisations
1. [Création du projet Spring Boot via IntelliJ](https://github.com/Watch-Me-Fly/OC-Alert-System/commit/165b489574e92f4242c10254de6bac57978b396d)
2. [Ajouter des dépendences pom.xml](https://github.com/Watch-Me-Fly/OC-Alert-System/commit/d43f9b87c634d9d033b5d503ec1cc615febc324a)

### Les données
3. [Importer les données et parser le json](https://github.com/Watch-Me-Fly/OC-Alert-System/commit/3f082b17d0e978dc4f15905e086bcee942662deb)
4. [[Person] : Créer le service et tests](https://github.com/Watch-Me-Fly/OC-Alert-System/commit/752be75bd7f7071df61f6c13fb710199fe1b4815)
* 4.a. : [[Fix] - PersonController + PersonControllerTests](https://github.com/Watch-Me-Fly/OC-Alert-System/commit/f46ea8572fcec12b41f07f81a9b54b9a3cbea95e)
5. [[FireStation] : Créer le service et tests](https://github.com/Watch-Me-Fly/OC-Alert-System/commit/ab6ef3d8bb8d5b7882014935a8545a3e394b15c5)
* 5.a. : [[Fix] - correct FireStationController + FireStationControllerTest](https://github.com/Watch-Me-Fly/OC-Alert-System/commit/2b7d125405dc53b9b622d0d80b43c96db0c059a8)
6. [[Medical Records] : Créer le service et tests](https://github.com/Watch-Me-Fly/OC-Alert-System/commit/0a68dab69953bdcd214972be86814790bf2d41c4)

### Les points REST API
7. [Création d'un service universel pour gérer les méthodes communes](https://github.com/Watch-Me-Fly/OC-Alert-System/commit/0e0a55e6e98ef2092d0e3f8e1708cdb0c9c59d2b)
8. [Definir les End-points dans des controlleurs](https://github.com/Watch-Me-Fly/OC-Alert-System/commit/7abbf311cc36c8765a664d0bd2921c9ccc632490)
9. [Création des services d'alerte + interface alerte](https://github.com/Watch-Me-Fly/OC-Alert-System/commit/dcc336d1592e6b771f762821dcfd6e96c46c21ab)
10. [Gestion des URLs restants](https://github.com/Watch-Me-Fly/OC-Alert-System/commit/d488289331cb16e12229685da08242152ab0e74c)

### Logging (Log4j2)
11. [Setup log4j2](https://github.com/Watch-Me-Fly/OC-Alert-System/commit/c8368947c67b3a3309d674e3c4630b476d3987ef)
12. [Add logs to services](https://github.com/Watch-Me-Fly/OC-Alert-System/commit/7d0cd744ef37980fed769395064e8861edab80b5)
