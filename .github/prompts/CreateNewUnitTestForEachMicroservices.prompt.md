---
mode: agent
tools: ['codebase']
---
# Create Comprehensive Unit Tests for a Microservice

## What This Prompt Does

When you specify a microservice, I will:
1. Analyze the microservice structure
2. Check dependencies in pom.xml
3. Generate appropriate unit tests for each layer following best practices

## Best Practices To Follow

All tests will follow these principles:
- JUnit 5 annotations and assertions
- Descriptive test names with `@DisplayName`
- Add comment in each test to explain the purpose
- Nested test classes for organization
- Independent and repeatable tests
- Arrange-Act-Assert pattern
- Proper mocking with Mockito
- Configuration for test isolation

## How to Use This Prompt

The user will call this prompt and specify the microservice, for example: "inventory-order-service"
Then, follow the steps below:

## Steps

### 1. Create folders for each layer unit tests
- Create `repository`, `service`, and `controller` test folders under `src/test/java/com/example/<microservice>/`, if already exist, skip this step

### 2. Create Repository Layer Tests
- Check the repository folder in main, if there are multiple repositories, create a test class for each repository. For example, there are `OrderRepository` and `ProductRepository`, create `OrderRepositoryTest.java` and `ProductRepositoryTest.java`
- Check if the test for each repository already exist, if so skip for that repository
- Check and Import all required dependencies
- JPA repository test classes using `@DataJpaTest`
- Autowired repository beans using `@Autowired`
- Setup test data using `@BeforeEach`
- Tests for all custom finder methods, if tests exist, skip such custom finder methods
- Proper database setup and cleanup
- Both positive and negative test cases
- Add comments on each test to explain the purpose

### 3. Create Service Layer Tests
- Service unit tests using `@ExtendWith(MockitoExtension.class)`
- Mocked repository dependencies
- Tests for all public business methods
- Exception handling tests
- Add comments on each test to explain the purpose

### 4. Create Controller Layer Tests
- REST controller tests using `@WebMvcTest`
- Mocked service layer
- Request/response validation
- HTTP status code verification
- JSON response validation
- Add comments on each test to explain the purpose


