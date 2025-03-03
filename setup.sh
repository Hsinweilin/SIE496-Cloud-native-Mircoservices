#!/bin/bash

# Build configserver service
echo "Building configserver service..."
cd configserver
mvn clean package
docker build --build-arg JAR_FILE=target/configserver-0.0.1-SNAPSHOT.jar -t ostock/configserver:0.0.1-SNAPSHOT .
cd ..

# Build user-service service
echo "Building user-service..."
cd user-service
mvn clean package
docker build --build-arg JAR_FILE=target/user-service-0.0.1-SNAPSHOT.jar -t ostock/user-service:0.0.1-SNAPSHOT .
cd ..

# Build inventory-order-service service
echo "Building inventory-order-service..."
cd inventory-order-service
mvn clean package
docker buildx build --build-arg JAR_FILE=target/inventory-order-service-0.0.1-SNAPSHOT.jar -t ostock/inventory-order-service:0.0.1-SNAPSHOT .
cd ..

echo "Build and image creation complete."
