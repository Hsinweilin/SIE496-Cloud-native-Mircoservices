#!/bin/bash

# Build configserver service
echo "Building configserver service..."
cd configserver
mvn clean package
docker build --build-arg JAR_FILE=target/configserver-0.0.2-SNAPSHOT.jar -t ostock/configserver:0.0.2-SNAPSHOT .
cd ..

# Build user-service service
echo "Building user-service..."
cd user-service
mvn clean package
docker build --build-arg JAR_FILE=target/user-service-0.0.1-SNAPSHOT.jar -t ostock/user-service:0.0.1-SNAPSHOT .
cd ..

#Build inventory-order-service service
echo "Building inventory-order-service..."
cd inventory-order-service
mvn clean package
docker buildx build --build-arg JAR_FILE=target/inventory-order-service-0.0.1-SNAPSHOT.jar -t ostock/inventory-order-service:0.0.1-SNAPSHOT .
cd ..

#build payment-service service
echo "Building payment-service..."
cd payment-service
mvn clean package
docker build --build-arg JAR_FILE=target/payment-service-0.0.1-SNAPSHOT.jar -t ostock/payment-service:0.0.1-SNAPSHOT .
cd ..

#build review-service service
echo "Building review-service..."
cd review-service
mvn clean package
docker build --build-arg JAR_FILE=target/review-service-0.0.1-SNAPSHOT.jar -t ostock/review-service:0.0.1-SNAPSHOT .
cd ..

# build eureka
echo "Building eureka..."
cd eurekaserver
mvn clean package
docker buildx build --build-arg JAR_FILE=target/eurekaserver-0.0.2-SNAPSHOT.jar -t ostock/eurekaserver:0.0.2-SNAPSHOT .
cd ..


echo "Building gatewayserver..."
cd gatewayserver
mvn clean package
docker buildx build --build-arg JAR_FILE=target/gatewayserver-0.0.2-SNAPSHOT.jar -t ostock/gatewayserver:0.0.2-SNAPSHOT .
cd ..

echo "Build and image creation complete."
