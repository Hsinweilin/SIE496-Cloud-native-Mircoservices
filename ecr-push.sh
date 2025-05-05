#!/bin/bash

AWS_ACCOUNT_ID=339712796116
REGION=us-east-1
REPO_PREFIX=sie496

# Define service-name to image mappings
SERVICES=(
  "configserver|ostock/configserver:0.0.2-SNAPSHOT"
  "inventory-order-service|ostock/inventory-order-service:0.0.1-SNAPSHOT"
  "payment-service|ostock/payment-service:0.0.1-SNAPSHOT"
  "review-service|ostock/review-service:0.0.1-SNAPSHOT"
  "gatewayserver|ostock/gatewayserver:0.0.2-SNAPSHOT"
  "eurekaserver|ostock/eurekaserver:0.0.2-SNAPSHOT"
)

# Authenticate Docker to ECR
aws ecr get-login-password --region $REGION | docker login --username AWS --password-stdin "$AWS_ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com"

# Iterate through services
for ITEM in "${SERVICES[@]}"; do
  SERVICE_NAME=$(echo "$ITEM" | cut -d'|' -f1)
  LOCAL_IMAGE=$(echo "$ITEM" | cut -d'|' -f2)
  ECR_IMAGE="$AWS_ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com/$REPO_PREFIX/$SERVICE_NAME"

  echo "📦 Tagging $LOCAL_IMAGE as $ECR_IMAGE"
  docker tag "$LOCAL_IMAGE" "$ECR_IMAGE"

  echo "🚀 Pushing $ECR_IMAGE to ECR"
  docker push "$ECR_IMAGE"
done
