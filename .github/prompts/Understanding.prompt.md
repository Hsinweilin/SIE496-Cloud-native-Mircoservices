---
mode: agent
tools: ['codebase', 'fileSearch', 'textSearch']
---

# SIE496 Cloud-Native Microservices E-commerce System

This project is a comprehensive cloud-native microservices e-commerce system built with Spring Boot 2.2.3 and Spring Cloud. Let me explain the key components and architecture:

## Architecture Overview

The system follows a microservices architecture with these core components:

1. **Infrastructure Services**:
   - **Config Server**: Centralized configuration management
   - **Eureka Server**: Service discovery and registration
   - **Gateway Server**: API Gateway for routing and load balancing

2. **Business Microservices**:
   - **User Service**: User management 
   - **Payment Service**: Payment processing
   - **Inventory-Order Service**: Inventory and order management
   - **Review Service**: Product review functionality

## Technology Stack

- **Core**: Spring Boot 2.2.3, Spring Cloud, Java 11
- **Build Tool**: Maven
- **Databases**: PostgreSQL (primary), Redis (caching)
- **Messaging**: Apache Kafka
- **Security**: Keycloak (OAuth2/OpenID Connect)
- **Observability**: Zipkin (distributed tracing)
- **Deployment**: Docker, Kubernetes (AWS EKS)

## Key Architectural Patterns

- Service independence with dedicated databases
- Centralized configuration via Config Server
- Service discovery through Eureka
- API Gateway pattern for external traffic
- Event-driven communication using Kafka
- Circuit breakers for resilience
- OAuth2/OpenID Connect security model

## Deployment Infrastructure

The system is containerized with Docker and orchestrated with Kubernetes, specifically targeting AWS EKS. It includes proper monitoring, logging, and observability components.
