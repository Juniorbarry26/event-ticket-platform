# Event Ticket Platform - Project Documentation

## Overview

The Event Ticket Platform is a comprehensive Spring Boot REST API designed for managing event tickets with robust authentication, role-based access control, and QR code validation capabilities. The system supports multiple user roles including organizers, staff, and regular users, providing a complete ticketing solution for events.

## Architecture

### Technology Stack

- **Java 17** with Spring Boot 4.0.2
- **Spring Security** with JWT authentication
- **Spring Data JPA** for database operations
- **PostgreSQL** (production) / **H2** (development) databases
- **MapStruct** for object mapping
- **Lombok** for reducing boilerplate code
- **Swagger/OpenAPI** for API documentation
- **ZXing** for QR code generation and validation
- **Maven** for dependency management

### Core Dependencies

```xml
Key Dependencies:
- Spring Boot Starter Web MVC
- Spring Boot Starter Security
- Spring Boot Starter Data JPA
- Spring Boot Starter Validation
- JWT Libraries (jjwt-api, jjwt-impl, jjwt-jackson)
- ZXing (QR Code generation)
- SpringDoc OpenAPI
- PostgreSQL & H2 Database Drivers
- MapStruct & Lombok
```

## Project Structure

```
src/main/java/com/alsaineybarry/tickets/
├── auth/                   # Authentication components
│   ├── AuthenticationController.java
│   ├── AuthenticationService.java
│   ├── AuthenticationRequest.java
│   ├── AuthenticationResponse.java
│   ├── RegistrationRequest.java
│   └── UserAuthResponse.java
├── config/                 # Configuration classes
│   ├── BeansConfig.java
│   ├── DataInitializer.java
│   ├── JpaConfiguration.java
│   ├── JwtAuthenticationConverter.java
│   ├── QrCodeConfig.java
│   └── SwaggerConfig.java
├── controllers/            # REST API endpoints
│   ├── EventController.java
│   ├── GlobalExceptionHandler.java
│   ├── PublishedEventController.java
│   ├── TicketController.java
│   ├── TicketTypeController.java
│   └── TicketValidationController.java
├── domain/                 # Domain models
│   ├── dtos/              # Data transfer objects
│   ├── entities/          # JPA entities
│   ├── enums/             # Enum definitions
│   └── requests/          # Request objects
├── mappers/               # MapStruct mappers
├── repositories/          # JPA repositories
├── security/              # Security components
│   ├── JwtFilter.java
│   ├── JwtService.java
│   └── UserDetailsServiceImpl.java
├── services/              # Business logic
└── util/                  # Utility classes
```

## Database Schema

### Core Entities

#### User
- **Purpose**: Represents system users with role-based access
- **Key Fields**: id, name, email, password, role
- **Relationships**: 
  - One-to-many with organized events
  - Many-to-many with attending events
  - Many-to-many with staffing events

#### Role
- **Purpose**: Defines user roles (USER, ORGANIZER, STAFF)
- **Key Fields**: id, name (enum), description
- **Values**: USER, ORGANIZER, STAFF

#### Event
- **Purpose**: Represents events with ticket sales
- **Key Fields**: id, name, start/end dates, venue, status
- **Relationships**: 
  - Many-to-one with organizer (User)
  - One-to-many with ticket types
  - Many-to-many with attendees and staff

#### TicketType
- **Purpose**: Defines ticket categories for events
- **Key Fields**: id, name, price, totalAvailable, description
- **Relationships**: 
  - Many-to-one with Event
  - One-to-many with Tickets

#### Ticket
- **Purpose**: Represents purchased tickets
- **Key Fields**: id, status, purchaseDate
- **Relationships**: 
  - Many-to-one with TicketType
  - Many-to-one with purchaser (User)
  - One-to-many with validations and QR codes

#### TicketValidation
- **Purpose**: Tracks ticket validation attempts
- **Key Fields**: id, method, status, validationDate
- **Methods**: MANUAL, QR_CODE

#### QrCode
- **Purpose**: Stores QR code data for tickets
- **Key Fields**: id, data, status, generatedAt
- **Status**: ACTIVE, USED, EXPIRED

### Entity Relationships

```
User (1) ---------> (N) Event (Organizer)
User (N) <---------> (N) Event (Attendees/Staff)
Event (1) ---------> (N) TicketType
TicketType (1) ---> (N) Ticket
User (1) ----------> (N) Ticket (Purchaser)
Ticket (1) --------> (N) TicketValidation
Ticket (1) --------> (N) QrCode
User (1) ----------> (1) Role
```

## API Endpoints

### Authentication Endpoints

#### POST /api/v1/auth/login
- **Purpose**: Authenticate users and generate JWT tokens
- **Request**: AuthenticationRequest (email, password)
- **Response**: AuthenticationResponse (JWT token, user info)
- **Access**: Public

#### POST /api/v1/auth/register
- **Purpose**: Register new users
- **Request**: RegistrationRequest (firstName, lastName, email, password)
- **Response**: AuthenticationResponse (JWT token, user info)
- **Access**: Public

### Event Management Endpoints

#### POST /api/v1/events
- **Purpose**: Create new events
- **Request**: CreateEventRequestDto
- **Response**: CreateEventResponseDto
- **Access**: ORGANIZER role required

#### GET /api/v1/events
- **Purpose**: Get organizer's events (paginated)
- **Response**: Page<ListEventResponseDto>
- **Access**: ORGANIZER role required

#### GET /api/v1/events/{eventId}
- **Purpose**: Get event details
- **Response**: GetEventDetailsResponseDto
- **Access**: ORGANIZER role required

#### PUT /api/v1/events/{eventId}
- **Purpose**: Update existing event
- **Request**: UpdateEventRequestDto
- **Response**: UpdateEventResponseDto
- **Access**: ORGANIZER role required

#### DELETE /api/v1/events/{eventId}
- **Purpose**: Delete event
- **Access**: ORGANIZER role required

### Public Event Endpoints

#### GET /api/v1/published-events
- **Purpose**: View published events
- **Response**: Published events list
- **Access**: Public

### Ticket Management Endpoints

#### GET /api/v1/tickets
- **Purpose**: Get user's tickets (paginated)
- **Response**: Page<ListTicketResponseDto>
- **Access**: Authenticated users

#### GET /api/v1/tickets/{ticketId}
- **Purpose**: Get ticket details
- **Response**: GetTicketResponseDto
- **Access**: Authenticated users (ticket owner)

#### GET /api/v1/tickets/{ticketId}/qr-codes
- **Purpose**: Generate QR code for ticket
- **Response**: PNG image bytes
- **Access**: Authenticated users (ticket owner)

### Ticket Type Endpoints

#### POST /api/v1/events/{eventId}/ticket-types
- **Purpose**: Add ticket types to events
- **Request**: CreateTicketTypeRequestDto
- **Response**: CreateTicketTypeResponseDto
- **Access**: ORGANIZER role required

### Ticket Validation Endpoints

#### POST /api/v1/ticket-validations
- **Purpose**: Validate tickets (manual or QR code)
- **Request**: TicketValidationRequestDto
- **Response**: TicketValidationResponseDto
- **Access**: ORGANIZER role required

## Security Architecture

### Authentication Flow

1. **User Registration/Login**: Users authenticate via `/api/v1/auth/login` endpoint
2. **JWT Generation**: Upon successful authentication, JWT token is generated
3. **Token Validation**: JWT filter validates tokens on protected endpoints
4. **User Context**: Authenticated user context available in controllers

### Role-Based Access Control

- **ROLE_USER**: Can purchase tickets and view events
- **ROLE_ORGANIZER**: Can create/manage events and validate tickets
- **ROLE_STAFF**: Can validate tickets at events

### JWT Implementation

- **Token Generation**: Custom JWT service creates tokens with user claims
- **Token Validation**: JwtFilter validates Bearer tokens in Authorization header
- **User Details**: Custom UserDetailsService loads user from database

### Security Components

- **JwtFilter**: Validates JWT tokens on each request
- **JwtService**: Generates and validates JWT tokens
- **UserDetailsServiceImpl**: Loads user details for authentication
- **AuthenticationService**: Handles registration and authentication logic

## Business Logic

### Event Management

- **Event Creation**: Organizers can create events with ticket types
- **Event Status**: Events have status tracking (DRAFT, PUBLISHED, CANCELLED)
- **Ticket Sales**: Events have sale start/end periods
- **Venue Management**: Events include venue information

### Ticket Sales

- **Ticket Types**: Events can have multiple ticket categories
- **Pricing**: Each ticket type has specific pricing
- **Availability**: Track total available tickets per type
- **Purchase History**: Maintain ticket purchase records

### QR Code System

- **Generation**: QR codes generated for each ticket
- **Validation**: QR codes can be scanned for ticket validation
- **Security**: QR codes contain encrypted ticket information
- **Status Tracking**: QR code status (ACTIVE, USED, EXPIRED)

### Ticket Validation

- **Manual Validation**: Staff can manually validate tickets by ID
- **QR Code Validation**: Scan QR codes for quick validation
- **Validation History**: Track all validation attempts
- **Access Control**: Only authorized staff can validate tickets

## Configuration

### Application Properties

Key configuration options:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/event-ticket-platform
spring.datasource.username=postgres
spring.datasource.password=your-password
spring.jpa.hibernate.ddl-auto=update

# JWT Configuration
jwt.secret=mySecretKey
jwt.expiration=86400

# H2 Console (Development)
spring.h2.console.enabled=true
```

### Docker Configuration

```yaml
# docker-compose.yml
services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: event-ticket-platform
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: your-password
    ports:
      - "5432:5432"
```

## Development Guidelines

### Code Standards

- **Java 17** features utilized
- **Lombok** annotations for reducing boilerplate
- **MapStruct** for object mapping
- **Spring Boot** conventions followed
- **RESTful API** design principles

### Testing

- **Unit Tests**: Service layer testing
- **Integration Tests**: Repository and controller testing
- **Security Tests**: Authentication and authorization testing

### Error Handling

- **Global Exception Handler**: Centralized error handling
- **Custom Exceptions**: Domain-specific exceptions
- **HTTP Status Codes**: Proper status code usage
- **Error Responses**: Structured error response format

## Deployment

### Build Process

```bash
# Clean and package
./mvnw clean package

# Run tests
./mvnw test

# Build Docker image
docker build -t event-ticket-platform .
```

### Environment Configuration

- **Development**: H2 database with console enabled
- **Production**: PostgreSQL with proper connection pooling
- **Security**: Environment-specific JWT secrets
- **Logging**: Configured for production monitoring

## API Documentation

### Swagger/OpenAPI

- **UI Available**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/v3/api-docs
- **Custom Tags**: Organized by functional areas
- **Authentication**: Bearer token authentication documented

### Endpoint Documentation

Each endpoint includes:
- **Summary**: Brief description
- **Description**: Detailed functionality
- **Parameters**: Request/response schemas
- **Security**: Required roles/permissions
- **Examples**: Sample requests/responses

## Monitoring and Maintenance

### Logging

- **Structured Logging**: Consistent log format
- **Security Events**: Authentication/authorization logging
- **Business Events**: Ticket sales and validation logging
- **Error Tracking**: Comprehensive error logging

### Performance

- **Database Optimization**: Proper indexing strategies
- **Caching**: QR code image caching
- **Pagination**: Large dataset handling
- **Connection Pooling**: Database connection management

## Future Enhancements

### Planned Features

- **Payment Integration**: External payment gateway integration
- **Email Notifications**: Ticket purchase and event reminders
- **Analytics Dashboard**: Event and sales analytics
- **Mobile API**: Optimized endpoints for mobile apps
- **Multi-tenancy**: Support for multiple event organizers

### Scalability Considerations

- **Microservices**: Potential service decomposition
- **Event Sourcing**: Audit trail implementation
- **Caching Strategy**: Redis integration
- **Load Balancing**: Horizontal scaling support

---

*This documentation provides a comprehensive overview of the Event Ticket Platform API. For specific implementation details, refer to the source code and inline documentation.*
