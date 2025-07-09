# Order Management System

## Table of Contents

1. [Description](#description)
2. [Core Technologies](#core-technologies)
3. [Architecture Overview](#architecture-overview)
4. [Communication](#communication)
5. [Running Locally](#running-locally)
6. [API Documentation](#api-documentation)
7. [License](#license)

## Description

A **demo project** showcasing a well-designed microservices ecosystem using Java and Spring Boot. This system implements a complete order management workflow with product, order, and payment processing simulation, emphasizing **microservices best practices** and modern Spring Cloud patterns.

**Key Learning Objectives:**
- Microservices architecture design and implementation
- Service discovery and centralized configuration
- Inter-service communication patterns
- Security implementation across distributed systems
- API Gateway pattern and request routing

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

This project showcases multiple **microservices communication patterns**:

- **Synchronous Communication via OpenFeign**:
  - **Order Service → Product Service**: Stock validation and inventory updates
  - **Payment Service → Order Service**: Order retrieval and status updates

- **Service Discovery**: Automatic service registration and discovery via **Eureka Server**

- **Centralized Configuration**: Dynamic configuration management through **Config Server**

- **API Gateway Pattern**: Single entry point with request routing and security enforcement


---

## Running Locally

1. Clone the repository:
   ```bash
   git clone https://github.com/FrankSkep/orders-microservices.git
   cd orders-microservices
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

Each microservice exposes comprehensive API documentation via **Swagger UI**:
- Access at: `http://localhost:{port}/swagger-ui.html`
- Interactive API documentation
- Complete request/response schemas

---

## License

This project is licensed under the **MIT License**. See [LICENSE](./LICENSE) for details.
