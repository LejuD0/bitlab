#docker-compose.yml
version: "3.9"

services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: main_service_db
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres-data:/var/lib/postgresql/data

  keycloak-db:
    image: postgres:15
    environment:
      POSTGRES_DB: keycloak
      POSTGRES_USER: kc_user
      POSTGRES_PASSWORD: kc_pass
    volumes:
      - keycloak-db:/var/lib/postgresql/data

  keycloak:
    image: quay.io/keycloak/keycloak:24.0.3
    command: start-dev
    environment:
      KEYCLOAK_ADMIN: admin
      KEYCLOAK_ADMIN_PASSWORD: admin
      KC_DB: postgres
      KC_DB_URL: jdbc:postgresql://keycloak-db/keycloak
      KC_DB_USERNAME: kc_user
      KC_DB_PASSWORD: kc_pass
    ports:
      - "8080:8080"
    depends_on:
      - keycloak-db

  minio:
    image: minio/minio
    command: server /data --console-address ":9001"
    environment:
      MINIO_ROOT_USER: minioadmin
      MINIO_ROOT_PASSWORD: minioadmin
    ports:
      - "9000:9000"
      - "9001:9001"
    volumes:
      - minio-data:/data

  main-service:
    build:
      context: .
      dockerfile: Dockerfile
    depends_on:
      - postgres
      - keycloak
      - minio
    ports:
      - "8081:8081"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/main_service_db
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgres
      SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI: http://keycloak:8080/realms/bitlab
      SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWK_SET_URI: http://keycloak:8080/realms/bitlab/protocol/openid-connect/certs
      MINIO_URL: http://minio:9000
      MINIO_ACCESS_KEY: minioadmin
      MINIO_SECRET_KEY: minioadmin
      MINIO_BUCKET: my-bucket

volumes:
  postgres-data:
  keycloak-db:
  minio-data:
  
#Dockerfile
FROM openjdk:17-jdk-slim
COPY main-service-clean-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]


  
