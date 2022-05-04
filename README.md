# pm

This document contains description of version 1.0 of API for pm application. pm is a REST server
application written in Java (version 11) using spring boot framework.

## requests and responses

pm server uses http(s) protocol for requests. Responses of API methods are represented as objects in JSON
format.

The methods of API can be separated into two categories:

1. user unaware methods - methods that don't require identification of user;
2. user aware methods - methods that can be invoked only on behalf of a user.

### User unaware methods:

    PUT http://host:8080/api/user/create-password
    Accept: application/json Content-Type: application/json

    PUT http://host:8080/api/user/update-password
    Accept: application/json
    Content-Type: application/json

    POST http://host:8080/login
    Content-Type: application/json
    
    {
    "email": "bvujicic@gmail.com",
    "password": "26N64AcWYszk"
    }

swagger and openapi documentation

- http://host:8080/swagger-ui.html
- http://host:8080/v3/api-docs
- http://host:8080/v3/api-docs.yaml

#### login example

    POST http://localhost:8080/login
    Content-Type: application/json
    
    {
    "email": "branimir.vujicic.uva@gmail.com",
    "password": "test"
    }

response:

    {
      "id": 24,
      "email": "branimir.vujicic.uva@gmail.com",
      "active": true,
      "name": "Branimir Vujicic",
      "alphabet": "LATIN",
      "settings": null,
      "roles": [
        "USER"
      ],
      "rights": [],
      "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJicmFuaW1pci52dWppY2ljLnV2YUBnbWFpbC5jb20iLCJyb2xlcyI6WyJVU0VSIl0sInJpZ2h0cyI6W10sImlhdCI6MTYzNTExMDMwNCwiZXhwIjoxNjM1MTk2NzA0fQ.z1dnm1jVbdAMQiNFdiZ277VWmHl0QCEFGw8St9jwNVc1fAGlbsA8wd0UC43lxafKHO0wqZYnuRdz2TKxgytnrQ",
      "authorities": [
        {
          "authority": "USER"
        }
      ]
    }

### User aware methods

All method not listed above must be called in behalf of a User.

example:

    GET  http://localhost:8080/api/user/me
    Accept: application/json
    Authorization: Bearer {{accessToken}}

response :

    {
      "email": "bvujicic@gmail.com",
      "name": "bane",
      "position": "CEO",
      "company": null,
      "unit": "management",
      "lastLoginTime": null,
      "active": true,
      "roles": [
        "ADMIN",
        "USER"
      ],
      "rights": [
      ],
      "createdOn": null
    }

## Error responses format

    {
        "errorCode": 10200,
        "status": "NOT_FOUND",
        "type": "SERVICE_ERROR",
        "message": "Unit not found: 12345",
        "path": "/api/unit/12345",
        "errors": []
    }

Response code: 404;

### error type

    GENERAL_ERROR : Unclassified error 
    INTERNAL_SERVER_ERROR : Internal server error 
    VALIDATION_ERROR : Validation error 
    BAD_REQ_INVALID_PASSWORD : Invalid auth 
    BAD_REQ_INVALID_AUTHORIZATION : Invalid auth 
    FORBIDDEN : Forbidden 
    SERVICE_ERROR : Unclassified error 
    UNSUPPORTED_MEDIA_TYPE_ERROR : Unsupported channels type error 
    HTTP_MESSAGE_NOT_READABLE_ERROR : Http message not readable 
    DUPLICATE_KEY : Duplicate DB key 
    NOT_FOUND : Resource not found

### error code

    GENERAL_ERROR = 10000;                           
    INTERNAL_ERROR = 10001;                         
    INVALID_ARGUMENT = 10002;                       
    INVALID_CREDENTIALS = 10003;                    
    ACCESS_DENIED = 10005;                          
    FORBIDDEN = 10006;                              
    NOT_FOUND = 10007;                              
    IN_USE = 10008;                                 
    ERROR_SENDING_EMAIL = 10009;                    
    SETUP_ALREADY_COMPLETED = 10010;
    
    ACCOUNT_DISABLED = 10101;                       
    INVALID_TOKEN = 10102;                          
    INVALID_RECOVERY_KEY = 10103;                   
    RECOVERY_KEY_EXPIRED = 10104;                   
    NOT_AUTHORIZED = 10105;                         
    UNKNOWN_USER = 10106;                           
    PASSWORD_DOES_NOT_MATCH = 10107;                
    USERNAME_EXISTS = 10108;                        
    PROJECT_EXISTS = 10109;                         
    TAG_EXISTS = 10110;
    
    INVALID_EMAIL = 10201;                          
    INVALID_URL = 10202;
    
    UPLOAD_EXCEPTION = 10301;                       
    INVALID_DOCUMENT_TYPE = 10302;

## API examples Intellij IDEA http file

    src/test/resources/http/admin-local.http

## building application

In order to build application machine must have java 11 installed .

in application root folder execute

linux / mac :

    ./gradlew build

windows :

    gradlew.bat build

application jar is located in build/libs.

## configuring application

for running application in dev environment file application-dev.properties must be edited

following lines must be edited according to database setup. Assumed database name is "eprotocol" and server timezone =
Europe/Belgrade and host is "localhost"

database credentials must also be provided

    spring.datasource.url=jdbc:mysql://localhost:3306/eprotocol?serverTimezone=Europe/Belgrade
    spring.datasource.username=eprotocol
    spring.datasource.password=w6Ur9XQtR7FM

initial data for database

    init/sql/setup.sql

## installing application as service

https://www.baeldung.com/spring-boot-app-as-a-service

## starting application

### starting application from project root

linux / mac :

    ./gradlew bootRun

windows :

    gradlew.bat bootRun

### starting application from custom location with profile "dev"

    java -jar -Dspring.profiles.active=dev eprotocol-0.0.1-SNAPSHOT.jar

## starting app from docker


### docker compose with mysql docker

the simplest way of starting app is using

    docker-compose up

application will build app from source and start application on port 6868

[swagger page](http://localhost:6868/swagger-ui.html)
