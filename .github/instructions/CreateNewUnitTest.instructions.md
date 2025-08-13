---
applyTo: '**/*Test.java'

## Guidelines for Creating New Unit Tests

### General Principles
- Write tests for every public method and business logic.
- Use descriptive test names and `@DisplayName` for clarity.
- Group related tests using nested classes or suites.
- Only use JUnit 5 (`@Test`, `@Nested`, `@DisplayName`).

### Spring Boot Microservices
- Use `@DataJpaTest` for repository layer tests.
- Use `@ExtendWith(MockitoExtension.class)` for service layer tests.
- Use `@WebMvcTest(UserController.class)` for controller layer tests.
- Use H2 in-memory database for fast, isolated tests.
- Clean up test data before each test (`deleteAll()` or similar).
- Avoid manual ID setting when using auto-generated IDs.

### Assertions & Structure
- Use `assertEquals`, `assertNotNull`, `assertThrows`, etc. from JUnit.
- Test both positive and negative scenarios.
- Validate edge cases and error handling.

### Best Practices
- Keep tests independent and repeatable.
- Use Arrange-Act-Assert pattern for test clarity.
- Add comments for complex test logic.
- Ensure tests run with `mvn test` and pass consistently.

### Example Test Skeleton for Repository Layer
```java
@DataJpaTest
class ExampleRepositoryTest {
    @Autowired
    private ExampleRepository exampleRepository;

    @BeforeEach
    void setUp() {
        exampleRepository.deleteAll();
        // ...setup test data...
    }

    @Test
    @DisplayName("should find entity by name")
    void shouldFindEntityByName() {
        // ...test logic...
    }
}
```
## Example Test Skeleton for Service Layer
```
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock private UserRepository userRepository;
    @InjectMocks private UserService userService;

    @Test
    void findUserByName_returnsUser() {
        when(userRepository.findByUsername("alice"))
            .thenReturn(Optional.of(new User(1L, "alice", "a@example.com")));

        User result = userService.findUserByName("alice");

        assertThat(result.getUsername()).isEqualTo("alice");
        verify(userRepository).findByUsername("alice");
    }
}
```
### Example Test Skeleton for Controller Layer
```
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockBean private UserService userService;

    @Test
    void getUser_returnsOk() throws Exception {
        when(userService.findUserByName("alice"))
            .thenReturn(new User(1L, "alice", "a@example.com"));

        mockMvc.perform(get("/users/alice"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.username").value("alice"));
    }
}
```

