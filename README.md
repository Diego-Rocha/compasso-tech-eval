# compasso-tech-eval

This project is mandatory as part of Compasso's recruitment and selection policy.

[Original requirement](./MISSION.md)

# Requirements
 - JDK >= 8
 - Docker (to run mongodb in dev)

## Console (Bash)

Note to windows users: use [git bash](https://gitforwindows.org/) :)

```bash

# dev quick start!
#   init mongo
#   build'n'run the app
#   will be avaliable on http://localhost:8080 :)
> ./quick_start.sh

# Generates a jar ready to deploy!
> ./gradlew bootJar

## EXTRAS ##

# run all tests and...
# checks if code coverage is > 90% on principal packages and...
# will generate a report in: build/reports/jacoco/test/html/index.html
> ./gradlew check

```
## Checklist

- [x] Working API (**Required**)
    - [x] City
        - [x] model
            - id
            - name
            - state
        - [x] save
        - [x] find by name
        - [x] find by state
        - Extras
            - [x] get by id
            - [x] delete
            - [x] delete all
    - [x] Client
        - [x] model
            - id
            - name
            - gender
            - birthDate
            - age (transient dto only)
            - city (FK)
        - [x] save
        - [x] find by name
        - [x] find by id
        - [x] delete
        - [x] update name
        - Extras
            - [x] get by id
            - [x] delete
            - [x] delete all
- [x] Tests (**Required**)
    - [x] Code Coverage >= 90%
    - [x] Code Quality must be 'A': [![codebeat badge](https://codebeat.co/badges/d3ae9ec4-5f2e-4508-b4a4-fb4856aa440b)](https://codebeat.co/a/diego-rocha-2/projects/github-com-diego-rocha-compasso-tech-eval-master)
- [x] API Docs with Swagger

## Package Structure

inside the main package 'io.diego.compasso.tech.eval' we have:
- controller: spring rest controllers
- service: spring services
- repository: spring repositories
- config: additional spring config
- business: contain the main application logic
- converter: convert objects like entities, dtos, etc...
    - obs.: can be replaced by modelMapper in the majority of the cases...
    but i like to control this :)
- model: simple models like entity, dto and enums

## Stack
 - Java
 - Spring Boot
 - Gradle
 - Swagger
 - Lombok
 
# Future work
 - needs better exceptionHandler to user
 - state to a entity? or enum? or maybe...
    - Provide a payload of cities and states (e.g: correios, IBGE, etc..)
 - separate 'city' and 'client' as separate microservices? but really don't need it now
 - need better Text Search: Index, Full-Text, etc...
 - tune swagger to provide better doc.
 - add spring rest docs and generate pdf/static html docs (complementar to swagger)