# Banking App Case Study

## Tech Stack

- Java 17
- Spring Boot

## Project

``
git clone git@github.com:ahmedsalihh/bankingApp.git
``

## Documentation

 - http://localhost:8080/swagger-ui/index.html


## Database
 - http://localhost:8080/h2-console


## Docker

 After project cloned, follow below steps to run application with docker;
 
**docker had to installed on your computer!!**
 - go to project folder on command line
 - execute ``docker build -t bankingApp .``
 - after build finished run ``docker run -p8080:8080 bankingApp``