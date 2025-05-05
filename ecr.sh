#!/bin/bash

AWS_REGION="us-east-1"
PREFIX="sie496"

services=(
  configserver
  inventory-order-service
  payment-service
  review-service
  gatewayserver
  eurekaserver
  keycloak
  zookeeper
  kafkaserver
  redisserver
  zipkin
)

for service in "${services[@]}"; do
  repo="$PREFIX/$service"
  echo "Creating ECR repository: $repo"
  aws ecr create-repository \
    --repository-name "$repo" \
    --region "$AWS_REGION" \
    --image-scanning-configuration scanOnPush=true \
    --tags Key=Service,Value=$service \
    || echo "Repository $repo may already exist, skipping..."
done
