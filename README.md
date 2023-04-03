# M.Sc, Web & Cloud Engineering - Master Thesis

# Overview

This is the backend implementation of the HRMS. It's implemented in Java, using Quarkus framework.

## Backend stack

- Quarkus
- Java
- PostgreSQL
- Liquibase
- MinIO
- Docker
- Jenkins
- GCP
- Kubernetes

## Build steps

1. ./mvnw clean package -Pproduction -Dquarkus.package.type=legacy-jar
2. docker build -f src/main/docker/Dockerfile.legacy-jar -t geokall/management:{tag} .
3. docker push geokall/management:{tag}

## Optional

The application can be deployed locally with a database instance using `docker-compose`