[![codebeat badge](https://codebeat.co/badges/d3ae9ec4-5f2e-4508-b4a4-fb4856aa440b)](https://codebeat.co/a/diego-rocha-2/projects/github-com-diego-rocha-compasso-tech-eval-master)

# compasso-tech-eval

This project is mandatory as part of Compasso's recruitment and selection policy.

[Original requirement](./MISSION.md)

# Requirements
 - JDK 8
 - Docker (mongodb)

## Console (Bash)
```bash

# dev quick start!
# init mongo, build'n'run the app :)
> ./quick_start.sh

# Generates a jar ready to deploy!
> ./gradlew bootJar

## EXTRAS ##
# Generates the code coverage report. ouput in:build/reports/jacoco/test/html/index.html
> ./gradlew jacocoTestReport

# Checks if code coverage is > 90% on packages 'controller, service, business and converter'
> ./gradlew jacocoTestCoverageVerification

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
- [ ] API Docs with Swagger

## Package Structure

inside the main package 'io.diego.compasso.tech.eval' we have:
- controller: spring rest controllers
- service: spring services
- repository: spring repositories
- config: additional spring config
- business: contain the main application logic
- converter: convert objects like entities, dtos, etc...
- model: simple models like entity, dto and enums

## Stack
 - Java
 - Spring Boot
 - Gradle
 - Swagger
 - Lombok
 
# Future work
 - needs better exceptionHandler to user (e.g: json search parse errors)
 - State to a entity? or enum? or maybe...
    - Provide a payload of cities and states (e.g: correios, IBGE, etc..)
 - Separate 'city' and 'client' as separate microservices? maybe...
 - need better Text Search: Index, Full-Text, etc...