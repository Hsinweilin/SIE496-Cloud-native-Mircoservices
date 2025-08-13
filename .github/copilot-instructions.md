# GitHub Copilot Instructions for SIE496 Cloud-Native Microservices E-commerce System

## Project Overview

This is a cloud-native microservices e-commerce system built with **Spring Boot 2.2.3** and **Spring Cloud**. The architecture follows enterprise patterns with multiple independent services, centralized configuration, service discovery, API gateway, and comprehensive security integration.

## Technology Stack

### Core Framework

- **Spring Boot 2.2.3** - Main application framework
- **Spring Cloud** - Microservices infrastructure
- **Maven** - Build and dependency management
- **Java 11** - Runtime environment

### Microservices Architecture

- **Config Server** - Centralized configuration management
- **Eureka Server** - Service discovery and registration
- **Gateway Server** - API Gateway with routing and load balancing
- **Business Services**: User Service, Payment Service, Inventory-Order Service, Review Service

### Infrastructure & Security

- **Keycloak** - Identity and access management (OAuth2/OpenID Connect)
- **PostgreSQL** - Primary database
- **Redis** - Caching layer
- **Apache Kafka** - Event streaming and messaging
- **Zipkin** - Distributed tracing
- **Docker** - Containerization
- **Kubernetes** - Orchestration (with AWS EKS support)

## Project Structure

```
├── configserver/          # Centralized configuration server
├── eurekaserver/          # Service discovery server
├── gatewayserver/         # API Gateway
├── user-service/          # User management microservice
├── payment-service/       # Payment processing microservice
├── inventory-order-service/ # Inventory and order management
├── review-service/        # Product review microservice
├── docker-compose.yml     # Local development environment
├── k8s*.yaml             # Kubernetes deployment manifests
└── Postman/              # API testing collections
```

## Coding Standards & Patterns

### Spring Boot Application Structure

- Main application classes use `@SpringBootApplication`
- Enable service discovery with `@EnableEurekaClient`
- Use `@EnableFeignClients` for inter-service communication
- Apply `@RefreshScope` for dynamic configuration updates

### Configuration Patterns

- External configuration via Spring Cloud Config Server
- Environment-specific profiles (dev, prod)
- Use `@ConfigurationProperties` for strongly-typed configuration
- Externalize properties with `@Value` annotations

### Security Implementation

- Keycloak integration for OAuth2/OpenID Connect
- `SecurityConfig` classes extend `KeycloakWebSecurityConfigurerAdapter`
- Use `KeycloakRestTemplate` for authenticated service-to-service calls
- Component scanning for Keycloak security components

### Inter-Service Communication

- Feign clients for declarative REST client calls
- Load-balanced RestTemplate with `@LoadBalanced`
- Circuit breaker patterns for resilience
- Correlation IDs for distributed tracing

### Data Access Patterns

- JPA/Hibernate for database access
- Repository pattern with Spring Data JPA
- Database migration with Flyway/Liquibase
- Connection pooling and transaction management

## Development Guidelines

### When Working with Microservices

1. **Service Independence**: Each service should be independently deployable
2. **Configuration**: Always use Config Server for external configuration
3. **Service Discovery**: Register all services with Eureka
4. **API Design**: Follow REST principles and use proper HTTP status codes
5. **Error Handling**: Implement global exception handlers
6. **Logging**: Use structured logging with correlation IDs

### Docker & Containerization

- Multi-stage Dockerfiles for optimized image builds
- Use OpenJDK 11 slim base images
- Proper layering for better caching
- Health checks for container orchestration

### Kubernetes Deployment

- Use ARM64-compatible images where specified
- Implement proper resource limits and requests
- Include readiness and liveness probes
- Use ConfigMaps and Secrets for configuration
- Apply proper service mesh patterns

### Database Design

- Follow microservice database-per-service pattern
- Use PostgreSQL for ACID transactions
- Implement proper indexing strategies
- Consider event sourcing for audit trails

### Testing Strategies

- Unit tests for business logic
- Integration tests for service interactions
- Contract testing between services
- End-to-end testing with Postman collections
- Performance testing with stress testing tools

## Code Generation Guidelines

### When Creating New Services

- Start with Spring Boot starter template
- Include necessary Spring Cloud dependencies
- Implement health check endpoints
- Add proper logging configuration
- Include Docker and Kubernetes manifests

### Security Implementation

- Always integrate with Keycloak for authentication
- Use role-based authorization (`@PreAuthorize`)
- Implement proper CORS configuration
- Secure inter-service communication

### API Development

- Use OpenAPI/Swagger for API documentation
- Implement proper validation with Bean Validation
- Use DTOs for data transfer between layers
- Include proper error responses

### Configuration Management

- Store all configuration in Config Server
- Use profiles for environment-specific settings
- Implement configuration refresh capabilities
- Secure sensitive configuration properties

## AWS & Cloud Integration

- ECR for container registry (339712796116.dkr.ecr.us-east-1.amazonaws.com)
- EKS for Kubernetes orchestration
- Application Load Balancer for external access
- CloudWatch for monitoring and logging
- Parameter Store for secrets management

## Monitoring & Observability

- Zipkin for distributed tracing
- Micrometer for metrics collection
- Spring Boot Actuator for health checks
- Centralized logging with ELK stack
- Application performance monitoring

## Best Practices

1. **Fail Fast**: Validate inputs early and provide clear error messages
2. **Circuit Breakers**: Implement resilience patterns for external calls
3. **Caching**: Use Redis for frequently accessed data
4. **Async Processing**: Use Kafka for event-driven architecture
5. **Documentation**: Maintain API documentation and architectural decisions
6. **Testing**: Ensure comprehensive test coverage at all levels

## Common Patterns to Follow

- **Service Registry Pattern**: All services register with Eureka
- **API Gateway Pattern**: Route all external traffic through Gateway Server
- **Configuration Server Pattern**: Centralize configuration management
- **Database per Service**: Each microservice owns its data
- **Event-Driven Architecture**: Use Kafka for service communication
- **CQRS**: Separate read and write models where appropriate

