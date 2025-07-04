# 📝 Orders Management System - Microservices

## 📌 Description

Modular backend system based on **microservices with Java and Spring Boot**, designed to manage products, orders, and payments. Uses **API Gateway, Eureka Discovery Server, and Config Server**, with communication via **Feign Clients** and security with **JWT**.

---

## ⚙️ Technologies

* Java 21
* Spring Boot 3
* Spring Cloud (Eureka, Config, Gateway, OpenFeign)
* Spring Security JWT
* PostgreSQL
* Maven

---

## 🏗️ Architecture

![Diagram](https://i.ibb.co/Gv529Xcv/architecture.png)

* **API Gateway**: Single entry point with JWT validation.
* **Config Server**: Centralized configuration.
* **Eureka Server**: Service discovery.
* **Auth Service**: User registration and login (JWT issuance).
* **Product Service**: Product and category management.
* **Order Service**: Order creation and management.
* **Payment Service**: Payment processing and order updates.

---

## 🔐 Security

* JWT is validated by the API Gateway.
* Each microservice implements its own **JwtService** and **JwtAuthenticationFilter** to set up the security context and protect endpoints using Spring Security.

---

## 🔄 Communication

* **Feign Clients** for inter-service calls:

  * `Order-Service` → `Product-Service`: Validate and decrease stock.
  * `Payment-Service` → `Order-Service`: Retrieve and update orders.

---

## 🚀 Running locally

1. Clone the repository:

   ```bash
   git clone https://github.com/FrankSkep/order-management-system.git
   cd order-management-system
   ```
2. Start Config Server and Discovery Server.
3. Start each microservice.
4. Finally, run the API Gateway.

---

## 📄 API Documentation

Each microservice exposes its API documentation with **Swagger** at `/swagger-ui.html`.

---

## 📝 License

This project is licensed under the MIT License. See [LICENSE](./LICENSE) for details.
