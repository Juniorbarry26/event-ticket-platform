# Event Ticket Platform API

A comprehensive Spring Boot REST API for managing event tickets, built with Spring Security, JWT authentication, and Keycloak integration.

## Features

- **Event Management**: Create, update, and manage events
- **Ticket Types**: Define different ticket categories and pricing
- **Ticket Sales**: Purchase and manage tickets
- **Ticket Validation**: Manual and QR code-based ticket validation
- **User Authentication**: Keycloak-based authentication with JWT tokens
- **Role-Based Access Control**: Organizer, Staff, and User roles
- **API Documentation**: OpenAPI/Swagger documentation

## Architecture

### Technology Stack
- **Java 17** with Spring Boot 4.0.2
- **Spring Security** with JWT authentication
- **Keycloak** for identity and access management
- **Spring Data JPA** with PostgreSQL/H2 databases
- **MapStruct** for object mapping
- **Lombok** for reducing boilerplate code
- **Swagger/OpenAPI** for API documentation
- **ZXing** for QR code generation

### Security Model
- **Keycloak**: Primary identity provider
- **JWT Tokens**: Custom JWT generation for API access
- **Role-Based Access**: 
  - `ORGANIZER`: Create and manage events
  - `STAFF`: Validate tickets at events
  - `USER`: Purchase tickets and view events

## Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+
- Docker and Docker Compose
- PostgreSQL (or use H2 for development)

### 1. Start Infrastructure Services

```bash
docker compose up -d
```

This starts:
- **PostgreSQL**: Database server (port 5432)
- **Keycloak**: Identity provider (port 9090)

### 2. Configure Keycloak

1. **Access Admin Console**: http://localhost:9090/admin/
   - Username: `admin`
   - Password: `admin`

2. **Create Realm**:
   - Name: `event-ticket-platform`

3. **Create Roles**:
   - `ORGANIZER`
   - `STAFF` 
   - `USER`

4. **Create Client**:
   - Client ID: `event-ticket-app`
   - Access Type: `confidential`
   - Enable Standard Flow and Direct Access Grants

5. **Create Users**:
   - Add users with appropriate roles
   - Set passwords and enable accounts

### 3. Run the Application

```bash
./mvnw spring-boot:run
```

The API will be available at: http://localhost:8080

### 4. Access API Documentation

Visit: http://localhost:8080/swagger-ui.html

## API Endpoints

### Public Endpoints
- `GET /api/v1/published-events` - View published events

### Authentication
- `POST /api/v1/auth/login` - Authenticate and get JWT token

### Organizer Endpoints (requires ORGANIZER role)
- `POST /api/v1/events` - Create new event
- `PUT /api/v1/events/{id}` - Update event
- `POST /api/v1/events/{id}/ticket-types` - Add ticket types to event

### Staff Endpoints (requires STAFF role)
- `POST /api/v1/ticket-validations` - Validate tickets (manual or QR code)

### User Endpoints (requires authentication)
- `POST /api/v1/tickets/{ticketTypeId}` - Purchase tickets

## Authentication Flow

### 1. Get JWT Token
```bash
curl -X POST "http://localhost:9080/api/v1/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "your-username",
    "password": "your-password"
  }'
```

### 2. Use Token for API Calls
```bash
curl -H "Authorization: Bearer YOUR_JWT_TOKEN" \
     http://localhost:8080/api/v1/events
```

## Database Configuration

### PostgreSQL (Production)
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/event-ticket-platform
spring.datasource.username=postgres
spring.datasource.password=Avond778@
spring.jpa.hibernate.ddl-auto=update
```

### H2 (Development)
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop
spring.h2.console.enabled=true
```

## Project Structure

```
src/main/java/com/alsaineybarry/tickets/
├── config/                 # Security and configuration
├── controllers/            # REST API endpoints
├── domain/                 # Domain models and DTOs
│   ├── dtos/              # Data transfer objects
│   └── entities/          # JPA entities
├── filters/                # Security filters
├── mappers/               # MapStruct mappers
├── repositories/          # JPA repositories
├── services/              # Business logic
│   └── impl/              # Service implementations
└── util/                  # Utility classes
```

## Key Components

### Controllers
- `EventController`: Event management operations
- `TicketController`: Ticket purchasing and management
- `TicketTypeController`: Ticket type definitions
- `TicketValidationController`: Ticket validation (manual/QR)
- `AuthController`: Authentication endpoints
- `PublishedEventController`: Public event viewing

### Services
- `TicketValidationService`: Ticket validation logic
- `QrCodeService`: QR code generation and validation
- `CustomJwtService`: JWT token management

### Security
- `CustomSecurityConfig`: Security configuration
- `CustomJwtAuthenticationFilter`: JWT validation filter
- `UserProvisioningFilter`: Automatic user creation from Keycloak

## Development

### Running Tests
```bash
./mvnw test
```

### Building the Application
```bash
./mvnw clean package
```

### Docker Build
```bash
docker build -t event-ticket-platform .
```

## Configuration

### Application Properties
Key configuration options in `application.properties`:

```properties
# JWT Configuration
jwt.secret=mySecretKey
jwt.expiration=86400

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/event-ticket-platform

# Keycloak Configuration
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:9090/realms/event-ticket-platform
```

## API Examples

### Create Event (Organizer)
```bash
curl -X POST "http://localhost:8080/api/v1/events" \
  -H "Authorization: Bearer ORGANIZER_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Summer Music Festival",
    "description": "Annual music festival",
    "venue": "Central Park",
    "eventDate": "2024-07-15T18:00:00",
    "organizerId": "organizer-uuid"
  }'
```

### Validate Ticket (Staff)
```bash
curl -X POST "http://localhost:8080/api/v1/ticket-validations" \
  -H "Authorization: Bearer STAFF_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "method": "QR_CODE",
    "id": "ticket-uuid"
  }'
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## License

This project is licensed under the MIT License.

## Support

For issues and questions:
- Create an issue in the repository
- Check the API documentation at `/swagger-ui.html`
- Review the Keycloak configuration guide

