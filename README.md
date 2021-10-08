# Finances

<hr>

### About

This project is a semester degree project of Science Computer.
This project is a finance management application

<hr>

## Build project

To build this project is necessary maven 3.6.0 or greater and Java 11, use command: ```mvn clean install```
<br>
To  check project coverage is necessary Docker and docker-compose and use command ```docker-compose -f docker-compose-sonar.yaml up -d```
enter in web browser and go to address localhost:9000 use ```admin``` for user and password, 
change password to admin1 and run ``` mvn clean package verify sonar:sonar -Dsonar.login=admin -Dsonar.password=admin1``` to check test coverage. 

<hr>

### Database

This project use Postgres database, to start a local postgres database use  ```docker-compose up -d``` to start the database.
To use another host, username, password and port use environments variables:

```
POSTGRESQL_HOST=host
POSTGRES_PORT=port
POSTGRES_DATABASE_NAME=databasename
POSTGRES_USERNAME=database_user
POSTGRES_PASSWORD=database_password
```

In first start of application is created the tables and is inserted user profiles and movement types

# DOCUMENTATION

To se the documentation (swagger ui) start the application go to web browser and go to address ```http://localhost:8080/documentation```
