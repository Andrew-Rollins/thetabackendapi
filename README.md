# Thetabackendapi

This is a backend for the android app https://github.com/a73s/Theta-Board

As of now it is very barebones and is more of a POC of connecting a NySQL server to an API using Spring Boot.

# Important notes and setup

First start a MySQL server and add the example table using this command.
```
CREATE TABLE `boardposts` (
  `idboardposts` int NOT NULL AUTO_INCREMENT,
  `title` varchar(45) DEFAULT NULL,
  `body` varchar(45) DEFAULT NULL,
  `author` varchar(45) DEFAULT NULL,
  `date` varchar(45) DEFAULT NULL,
  PRIMARY KEY `idboardposts`)
) 
```

You MUST fill out application.properties in src/resources

Now you can start the application.

Example test commands to show getting and submitting data.
```
curl http://localhost:8080/api/posts 
```
```
curl -Method POST -Uri "http://localhost:8080/api/posts" -Headers @{ "Content-Type" = "application/json" } -Body '{"title":"Test title","body":"Test body","author":"Tester","date":"2025-11-04"}'
```
