# Finances

<hr>

### About

This project is a semester degree project of Science Computer.
This project is a finance management application

<hr>

## Build project

To build this project is necessary maven 3.6.0 or greater and Java 11, use command: ```mvn clean install```
<br>
To  check project coverage is necessary Docker and docker-compose and use command ```docker-compose up -d```
enter in web browser and go to address localhost:9000 use ```admin``` for user and password, 
change password to admin1 and run ``` mvn clean package verify sonar:sonar -Dsonar.login=admin -Dsonar.password=admin1``` to check test coverage. 