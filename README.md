# Order Management System

## Table of Contents

1. [Description](#-description)
2. [Core Technologies](#️-core-technologies)
3. [Architecture Overview](#️-architecture-overview)
4. [Communication](#-communication)
5. [Running Locally](#-running-locally)
6. [API Documentation](#-api-documentation)
7. [License](#-license)

## Description

Modular backend system based on microservices with Java and Spring Boot, designed to manage products, orders, and payments. Uses API Gateway, Eureka Discovery Server, and Config Server, with communication via Feign Clients and security with JWT.

---

## Core Technologies

- **Java 21**, **Spring Boot 3.2.8**
- **Spring Cloud 2023.0.1** (Eureka, Config Server, Gateway)
- **Spring Security + JWT (io.jsonwebtoken 0.11.5)**
- **Spring Data JPA + PostgreSQL**
- **OpenFeign**
- **SpringDoc OpenAPI (Swagger)**
- **Lombok**
- **Maven**

---

## Architecture Overview

![Diagram](https://i.ibb.co/xPDtny8/architecture-diagram.png)

| Service           | Port  | Database    | Responsibility                      |
|--------------------|-------|-------------|-------------------------------------|
| Config Server     | 8888  | -           | Centralized configuration           |
| Discovery Server  | 8761  | -           | Service registry and discovery      |
| API Gateway       | 8080  | -           | Single entry point with JWT validation |
| Auth Service      | 8081  | auth_db     | User registration, authentication, JWT issuance |
| Product Service   | 8082  | product_db  | Product catalog and inventory       |
| Order Service     | 8083  | order_db    | Order creation and management       |
| Payment Service   | 8084  | payment_db  | Payment processing and order updates |

> **Security**: All endpoints are secured with JWT-based authentication and role-based authorization (`USER`, `ADMIN`).

---

## Communication

- **OpenFeign Clients** enable synchronous communication:
  - **Order Service → Product Service**: Validate and decrease stock.
  - **Payment Service → Order Service**: Retrieve and update orders.

- **Service Discovery**: Managed via **Eureka Server** for dynamic registration and load balancing.

- **Centralized Configuration**: Hosted in **Config Server**, supporting environment-specific configs.

---

## Running Locally

1. Clone the repository:
   ```bash
   git clone https://github.com/FrankSkep/order-management-system.git
   cd order-management-system
   ```
2. Set the required environment variables:

   - JWT_SECRET_KEY
   - DB_USERNAME
   - DB_PASSWORD
   - PRODUCT_DB_URL
   - ORDER_DB_URL
   - PAYMENT_DB_URL

3. Create the required databases:
   ```sql
   CREATE DATABASE auth_db;
   CREATE DATABASE product_db;
   CREATE DATABASE order_db;
   CREATE DATABASE payment_db;
   ```
4. Start the Config Server and Discovery Server.
5. Start each microservice.
6. Finally, run the API Gateway.

---

## API Documentation

Each microservice exposes its API documentation with **Swagger** at `/swagger-ui.html`.

---

## License

This project is licensed under the **MIT License**. See [LICENSE](./LICENSE) for details.