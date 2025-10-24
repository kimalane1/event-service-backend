# Event Registration Backend

A backend system for managing event registrations, built with Spring Boot. 
This application provides RESTful API for event handling.


## Tech Stack

- **Backend Framework**: Spring Boot 3.5.6
- **Database**: H2 
- **Build Tool**: Gradle

## Prerequisites

- Java 21
- Gradle 8.x

### Local Development Environment

1. Clone the repository:
   ```bash
   git clone https://github.com/kimalane1/event-registration-backend.git
   cd event-registration-backend
   ```
2. Run the application using the Gradle Wrapper:
   ```bash
   ./gradlew bootRun
   ```
3. By default the application will be available at `http://localhost:8080`
 
4. Database:
   - H2 in-memory database is configured by default
   - h2 console is enabled at `http://localhost:8080/h2-console`
   - tables are created automatically by Liquibase

   - Andmemudel (ERD) [docs/erd.png](./docs/erd.png)

5. Authentication:
   - Spring Security and JWT is used for authentication
   - admin credentials are:
     - username: admin
     - password: 1234
   Credentials can be changed in `application.yml`

### Docker

```bash
    docker build -t event-registration-backend .
```
```bash
    docker run -p 8080:8080 event-registration-backend
```
This runs the application in a Docker container.
Application will be available at `http://localhost:8080`






