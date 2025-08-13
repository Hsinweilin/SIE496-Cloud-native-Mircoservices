# Creating Controller Tests with Keycloak Security Integration

This guide provides a step-by-step approach to creating controller tests with Keycloak security integration for microservices in our Spring Boot cloud-native architecture.

## Prerequisites

- Spring Boot 2.2.3.RELEASE
- Spring Security
- Keycloak adapter dependencies
- JUnit 5
- Spring Security Test

## Step 1: Add Required Dependencies

Ensure your `pom.xml` includes these dependencies:

```xml
<dependencies>
    <!-- Spring Boot Test -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    
    <!-- Spring Security Test -->
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-test</artifactId>
        <scope>test</scope>
    </dependency>
    
    <!-- Keycloak -->
    <dependency>
        <groupId>org.keycloak</groupId>
        <artifactId>keycloak-spring-boot-starter</artifactId>
    </dependency>
</dependencies>
```

## Step 2: Create Test Properties File

Create an `application-test.properties` file in `src/test/resources/`:

```properties
# Enable bean definition overriding
spring.main.allow-bean-definition-overriding=true

# Test configuration for Keycloak
keycloak.auth-server-url=http://localhost:8080/auth
keycloak.realm=test-realm
keycloak.resource=test-client
keycloak.public-client=true
keycloak.bearer-only=true
keycloak.principal-attribute=preferred_username

# Disable security for testing
keycloak.enabled=true
```

## Step 3: Create Controller Test Class Structure

Use this template for your controller test class:

```java
package com.optimagrowth.service.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.test.web.servlet.MockMvc;

// Add the controller you want to test
@WebMvcTest(YourController.class)
@org.springframework.test.context.ActiveProfiles("test")
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class YourControllerTest {

    // Define test security configuration
    @TestConfiguration
    @EnableWebSecurity
    @ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
    @org.springframework.core.annotation.Order(1)  // Important: This gives higher priority than main SecurityConfig
    public static class TestSecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            super.configure(http);
            http.authorizeRequests()
                .anyRequest().permitAll();  // For testing, permit all requests
            http.csrf().disable();
        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            KeycloakAuthenticationProvider provider = keycloakAuthenticationProvider();
            provider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
            auth.authenticationProvider(provider);
        }

        @Bean
        @Override
        @Primary
        protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
            return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
        }

        @Bean
        @Primary
        public KeycloakSpringBootConfigResolver keycloakConfigResolver() {
            return new KeycloakSpringBootConfigResolver();
        }

        @Bean
        @Primary
        public AdapterConfig adapterConfig() {
            AdapterConfig adapterConfig = new AdapterConfig();
            adapterConfig.setRealm("test-realm");
            adapterConfig.setResource("test-client");
            adapterConfig.setAuthServerUrl("http://localhost:8080/auth");
            return adapterConfig;
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private YourService yourService;  // Mock your service

    private YourModel testModel;  // Your model object
    private String testModelJson;  // JSON representation of your model

    @BeforeEach
    void setUp() {
        // Initialize your test data
        testModel = new YourModel();
        // Set properties on your model
        
        // Create JSON representation
        testModelJson = "{\"property1\": \"value1\", \"property2\": \"value2\"}";
    }

    // Write your test methods here
}
```

## Step 4: Write Test Methods

Write specific test methods for your controller endpoints:

### GET Endpoint Test

```java
@Test
@DisplayName("GET /your/endpoint/{id} - Found")
void getByIdShouldReturnModelWhenFound() throws Exception {
    // Arrange
    when(yourService.getById(1L)).thenReturn(testModel);

    // Act & Assert
    mockMvc.perform(get("/your/endpoint/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.property1").value("value1"))
        .andExpect(jsonPath("$.property2").value("value2"));
}

@Test
@DisplayName("GET /your/endpoint/{id} - Not Found")
void getByIdShouldReturn404WhenNotFound() throws Exception {
    // Arrange
    when(yourService.getById(999L)).thenReturn(null);

    // Act & Assert
    mockMvc.perform(get("/your/endpoint/999"))
        .andExpect(status().isNotFound());
}
```

### POST Endpoint Test

```java
@Test
@WithMockUser(roles = "ADMIN")  // Mock a user with ADMIN role
@DisplayName("POST /your/endpoint - Created")
void createShouldCreateAndReturnModel() throws Exception {
    // Arrange
    when(yourService.create(any(YourModel.class))).thenReturn(testModel);

    // Act & Assert
    mockMvc.perform(post("/your/endpoint")
            .contentType(MediaType.APPLICATION_JSON)
            .content(testModelJson))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.property1").value("value1"))
        .andExpect(jsonPath("$.property2").value("value2"));
}
```

### PUT Endpoint Test

```java
@Test
@WithMockUser(roles = "ADMIN")
@DisplayName("PUT /your/endpoint/{id} - Updated")
void updateShouldUpdateAndReturnModel() throws Exception {
    // Arrange
    when(yourService.update(eq(1L), any(YourModel.class))).thenReturn(testModel);

    // Act & Assert
    mockMvc.perform(put("/your/endpoint/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(testModelJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.property1").value("value1"))
        .andExpect(jsonPath("$.property2").value("value2"));
}

@Test
@WithMockUser(roles = "ADMIN")
@DisplayName("PUT /your/endpoint/{id} - Not Found")
void updateShouldReturn404WhenNotFound() throws Exception {
    // Arrange
    when(yourService.update(eq(999L), any(YourModel.class))).thenReturn(null);

    // Act & Assert
    mockMvc.perform(put("/your/endpoint/999")
            .contentType(MediaType.APPLICATION_JSON)
            .content(testModelJson))
        .andExpect(status().isNotFound());
}
```

### DELETE Endpoint Test

```java
@Test
@WithMockUser(roles = "ADMIN")
@DisplayName("DELETE /your/endpoint/{id} - Deleted")
void deleteShouldDeleteAndReturnSuccess() throws Exception {
    // Arrange
    when(yourService.delete(1L)).thenReturn("Successfully deleted");

    // Act & Assert
    mockMvc.perform(delete("/your/endpoint/1"))
        .andExpect(status().isOk())
        .andExpect(content().string("Successfully deleted"));
}

@Test
@WithMockUser(roles = "ADMIN")
@DisplayName("DELETE /your/endpoint/{id} - Not Found")
void deleteShouldReturn404WhenNotFound() throws Exception {
    // Arrange
    when(yourService.delete(999L)).thenReturn("Not found");

    // Act & Assert
    mockMvc.perform(delete("/your/endpoint/999"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Not found"));
}
```

## Step 5: Run Tests

Run your tests with:

```bash
mvn test -Dtest=YourControllerTest
```

## Key Points to Remember

1. **@Order(1)** on the TestSecurityConfig is crucial to prevent conflicts with the main application's SecurityConfig

2. **@Primary** annotations ensure your test beans are prioritized over the main application beans

3. **@ActiveProfiles("test")** ensures your test-specific properties are loaded

4. **@AutoConfigureMockMvc(addFilters = false)** can be used to disable security filters if needed for some tests

5. **@WithMockUser** is used to simulate authenticated users with specific roles

6. Use **jsonPath()** to verify the response content structure

7. Mock all external dependencies with **@MockBean**

## Troubleshooting

### Bean Definition Conflicts

If you encounter bean definition conflicts, ensure:
- `spring.main.allow-bean-definition-overriding=true` in application-test.properties
- All conflicting beans in TestSecurityConfig are annotated with @Primary
- TestSecurityConfig has a different @Order value than the main SecurityConfig
