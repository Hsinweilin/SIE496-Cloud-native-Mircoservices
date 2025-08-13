---
mode: agent
tools: ["codebase"]
---

# Create Comprehensive Unit Tests for a Microservice

## What This Prompt Does

When you specify a microservice, I will:

1. Analyze the microservice structure
2. Generate appropriate unit tests for each layer following best practices

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

### 1. Create folders for each layer unit tests and check if the dependeny exit in pom.xml file

- Create `repository`, `service`, and `controller` test folders under `src/test/java/com/example/<microservice>/`, if already exist, skip this step
- Check dependencies in pom.xml, make sure to include
```
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>4.0.0</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-test-autoconfigure</artifactId>
    <version>2.2.3.RELEASE</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-context</artifactId>
    <version>2.2.3.RELEASE</version>
</dependency>
```

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

Note: if some of the step is already implemented, skip to the next one
- Service unit tests using `@ExtendWith(MockitoExtension.class)`
- Mocked repository dependencies
- Tests for all public business methods
- Exception handling tests
- Add comments on each test to explain the purpose

### 4. Create Controller Layer Tests

Note: if some of the step is already implemented, skip to the next one
- Add required dependency to pom.xml
- REST controller tests using `@WebMvcTest`
- Mocked service layer
- Request/response validation
- HTTP status code verification
- JSON response validation
- Add comments on each test to explain the purpose
- Create an `application-test.properties` file in `src/test/resources/` with Test configuration for Keycloak


## Key Points to know when creating test cases for Controller Layer

1. **@Order(1)** on the TestSecurityConfig is crucial to prevent conflicts with the main application's SecurityConfig

2. **@Primary** annotations ensure your test beans are prioritized over the main application beans

3. **@ActiveProfiles("test")** ensures your test-specific properties are loaded

4. **@AutoConfigureMockMvc(addFilters = false)** can be used to disable security filters if needed for some tests

5. **@WithMockUser** is used to simulate authenticated users with specific roles

6. Use **jsonPath()** to verify the response content structure

7. Mock all external dependencies with **@MockBean**
