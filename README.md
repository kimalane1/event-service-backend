# Event Registration Backend

A backend system for managing event registrations, built with Spring Boot. 
This application provides RESTful APIs for user authentication, event overview and registration handling.


## Tech Stack

- **Backend Framework**: Spring Boot 3.5.6
- **Security**: Spring Security with JWT
- **Database**: H2 
- **Build Tool**: Gradle
- **Validation**: Hibernate Validator

## Prerequisites

- Java 21
- Gradle 8.14.13
- Git

### Local Development

1. Clone the repository:
   ```bash
   git clone https://github.com/kimalane1/event-registration-backend.git
   cd event-registration-backend
   ```

2.  Database:
   - H2 in-memory database is configured by default
   - h2 console is enabled at `http://localhost:8080/h2-console`
   - tables are created automatically by Liquibase

3. Build and run the application:
   ```bash
   ./gradlew bootRun
   ```

4. The application will be available at `http://localhost:8080`





