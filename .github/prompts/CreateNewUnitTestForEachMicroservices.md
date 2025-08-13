---
mode: agent
tools: ["codebase"]
---

# Create Comprehensive Unit Tests for a Microservice

## What This Prompt Does

When you specify a microservice, I will:

1. Analyze the microservice structure
2. Check dependencies in pom.xml
3. Generate appropriate unit tests for each layer following best practices

## How to Use This Prompt

The user will call this prompt and specify the microservice, for example:
"inventory-order-service"
Then, follow the steps below:

##

For each specified microservice, I will generate:

### 1. Repository Layer Tests

- JPA repository test classes using `@DataJpaTest`
- Tests for all custom finder methods
- Proper database setup and cleanup
- Both positive and negative test cases

### 2. Service Layer Tests

- Service unit tests using `@ExtendWith(MockitoExtension.class)`
- Mocked repository dependencies
- Tests for all public business methods
- Exception handling tests

### 3. Controller Layer Tests

- REST controller tests using `@WebMvcTest`
- Mocked service layer
- Request/response validation
- HTTP status code verification
- JSON response validation

## Best Practices Followed

All tests will follow these principles:

- JUnit 5 annotations and assertions
- Descriptive test names with `@DisplayName`
- Nested test classes for organization
- Independent and repeatable tests
- Arrange-Act-Assert pattern
- Proper mocking with Mockito
- Configuration for test isolation

## Example Usage

"Generate unit tests for user-service"

I will analyze the user-service's structure and dependencies, then generate appropriate test classes for repositories, services, and controllers based on the existing code.
