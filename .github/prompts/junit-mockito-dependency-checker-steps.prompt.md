# JUnit and Mockito Dependency Checker (Numbered Steps)

## Overview

This prompt guides you through a step-by-step process to ensure all microservices in the SIE496 Cloud-Native Microservices E-commerce System have JUnit and Mockito testing dependencies properly configured in their pom.xml files.

## Steps

1. **Identify Microservices**

   - List all microservice directories:
     - user-service
     - payment-service
     - inventory-order-service
     - review-service

2. **Check for JUnit and Mockito Dependencies**
   - For each microservice, open its `pom.xml` and look for the following dependencies:

```xml
<!-- JUnit and Testing Dependencies -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
    <exclusions>
        <exclusion>
            <groupId>org.junit.vintage</groupId>
            <artifactId>junit-vintage-engine</artifactId>
        </exclusion>
    </exclusions>
</dependency>

<!-- Mockito (additional if needed) -->
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
```

3. **Verify with Maven**
   - In each microservice directory, run:

```bash
mvn dependency:tree | grep -E "(junit|mockito)"
```

- Confirm that JUnit and Mockito dependencies are present in the output.

4. **Add Missing Dependencies**

   - If any dependency is missing, add the required XML snippet from Step 2 to the `<dependencies>` section of the corresponding `pom.xml` file.

5. **Install and Verify**
   - After updating the `pom.xml`, run:

```bash
mvn clean install
```

- Ensure the build completes successfully.

6. **Run Tests**
   - Run the test suite for each microservice:

```bash
mvn test
```

- Confirm that tests execute without errors and JUnit/Mockito are working.

Let's do this step by step
