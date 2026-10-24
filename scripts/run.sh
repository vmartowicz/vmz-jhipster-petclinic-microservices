#!/usr/bin/env bash

docker compose kill || echo "No docker containers are running"

echo "Running apps"
cd ../petclinic
sudo docker compose -f src/main/docker/consul.yml up -d
#./mvnw clean && mkdir -p target && ./mvnw > target/petclinic-gateway.log 2>&1 &
#sleep 20
cd ../customer
#./mvnw clean && mkdir -p target && ./mvnw > target/customer-service.log 2>&1 &

cd ../vet
#./mvnw clean && mkdir -p target && ./mvnw > target/vet-service.log 2>&1 &

cd ../visit
#./mvnw clean && mkdir -p target && ./mvnw > target/visit-service.log 2>&1 &
