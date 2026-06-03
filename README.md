# TascaEats 🍽️

**TascaEats** is a food delivery platform developed as part of the **Software Systems Construction** course at Faculdade de Ciências da Universidade de Lisboa. The goal was to design and implement a backend system from scratch, applying concepts taught throughout the course — layered architecture, domain modelling, REST APIs, JPA, testing, and containerisation.

The platform connects customers, restaurant administrators, and delivery persons through a REST API built with Spring Boot, a web interface built with Thymeleaf, and a desktop application built with JavaFX communicating via gRPC.

---

## Authors

| Name |
|------|
| Lana Santos |
| Bruno Antunes |
| Sofia Rodrigues |

---

## Context

Users can interact with the system under three distinct profiles — **Client**, **Restaurant Administrator**, and **Delivery Person** — sharing base authentication data but with different roles and permissions.

Core features include:
- User registration, authentication (mocked), and management
- Restaurant and menu management
- Order creation and lifecycle management with associated payment
- Delivery logistics — assigning available delivery persons to ready orders
- Order cancellation with business rule enforcement

---

## Demonstration Video

> https://drive.google.com/drive/folders/1WTw22PetVufq2TXbswLgPwXUsL_1rdIK?usp=drive_link

> **Note:** The video does not cover every implemented feature exhaustively, but walks through the main user flows across the three interfaces.

## Domain Model
![Domain Model](DiagramaTascaEats-3a-fase.png)

## Database

The application uses **PostgreSQL**. You can connect via [DBeaver](https://dbeaver.io/) with the following credentials:

| Field    | Monolith    | Microservice |
|----------|-------------|--------------|
| Host     | `localhost` |  `localhost` |
| Port     | `5432`      |  `5433`      |
| Database | `postgres`  | `deliverydb` |
| User     | `user`      |  `user`      |
| Password | `password`  |  `password`  |

> **Note:** By default, the database is recreated on each run. To persist data between executions, change `spring.jpa.hibernate.ddl-auto` in `src/main/resources/application.properties` from `create-drop` to `update`.

---

## Phase 3 — Microservices Architecture

### Overview

In Phase 3, the platform underwent a significant architectural transformation to address scalability concerns. Following **Domain-Driven Design** principles, the **Delivery Management bounded context** was extracted into an independent microservice. The system evolved from a monolithic architecture to a distributed, event-driven architecture using **Apache Kafka** for asynchronous communication.

The key objectives were:
- Extract delivery management logic into a dedicated microservice
- Implement event-driven communication between services
- Ensure eventual consistency across the system
- Maintain seamless user experience across all interfaces
- Containerize and orchestrate all services using Docker Compose

## Sample Data

For convenience, the database is automatically populated on startup via `BdLoader.java`, located in `src/main/java/.../config`. This seeds the database with sample data to make it easier to test the endpoints right away.

---

## Code Quality

Code quality in this phase was enforced using **pre-commit hooks**, ensuring that checks run automatically before each commit.

---

### Backend / Architecture

**Requirements:**
- Java 21+ (**headful** JDK — the headless variant does not support graphical interfaces)
- Maven
- Ensure the `JAVA_HOME` environment variable points to the headful JDK installation

```bash
cd JavaFX
mvn javafx:run
```

---

#### Microservices Decomposition

**DeliveryService Microservice:**
The new microservice encapsulates all delivery-related operations:
- Delivery person registration, and profile management
- Delivery lifecycle management (creation, status updates, assignment)
- Search and filtering for delivery persons
- Automatic and manual delivery person assignment to ready orders

**Monolith (TascaEats):**
The monolith now focuses on:
- User and restaurant management (except delivery persons)
- Order creation and payment processing
- Menu and product management
- Acting as an event producer/consumer for delivery operations
- Serving as the central API gateway through Nginx

**Observations**
- When the user filter doesn't specify delivery person, it's the monolith that still executes it. This still works consistently as the monolith still saves delivery person in the database with the database in the microservices.
- The automatic assignment of a delivery person to a delivery only occurs when at least one of the delivery persons in the database has their status as `AVAILABLE`, and the order status is changing from `PREPARING` to `READY`. If all delivery persons in the database have the `BUSY` status, that delivery will need to be manually asigned via Swagger, while utilizing the delivery person's id from the monolith side.

#### Event-Driven Communication

Communication between the monolith and DeliveryService occurs exclusively through **Apache Kafka** topics:

| Topic | Direction | Purpose |
|---|---|---|
| `create-delivery` | Monolith → Service | Request delivery creation for a ready order |
| `create-delivery-response` | Service → Monolith | Confirm delivery created with assignment details |
| `assign-delivery-person` | Monolith → Service | Request manual delivery person assignment |
| `delivery-person-assigned` | Service → Monolith | Confirm delivery person assigned |
| `mark-delivery-delivered` | Monolith → Service | Request delivery status update to delivered |
| `delivery-marked-delivered-response` | Service → Monolith | Confirm delivery marked as delivered |
| `delivery-person-register` | Monolith → Service | Register new delivery person |
| `delivery-person-register-response` | Service → Monolith | Confirm delivery person registration |
| `delivery-person-update` | Monolith → Service | Update delivery person profile |
| `delivery-person-update-response` | Service → Monolith | Confirm delivery person update |
| `get-my-deliveries` | Monolith → Service | Retrieve deliveries for a specific delivery person |
| `my-deliveries-response` | Service → Monolith | Return list of deliveries |

All responses are wrapped in a `KafkaResponse<T>` envelope containing:
- `success` (boolean) — indicates if operation succeeded
- `data` (T) — the actual response payload
- `errorCode` (String) — error identifier if unsuccessful
- `errorMessage` (String) — human-readable error message

#### Database Isolation

- **Monolith Database:** PostgreSQL instance maintaining orders, restaurants, menus, products, and clients
- **DeliveryService Database:** Separate PostgreSQL instance maintaining delivery persons, deliveries, and delivery assignments

Each service has exclusive access to its own database, enforcing bounded context boundaries.

#### Eventual Consistency

The system implements eventual consistency through:
- **Synchronous request-response over Kafka:** For critical operations like order-to-delivery workflows
- **Timeouts and retries:** With exponential backoff for resilience
- **Saga pattern consideration:** For complex multi-step operations requiring transactional guarantees
- **Event sourcing readiness:** Kafka topics serve as an event log for audit and recovery

### Infrastructure

#### Nginx Reverse Proxy

An Nginx reverse proxy serves as the central entry point:
- Routes REST requests from the web interface to the monolith
- Routes gRPC requests from the JavaFX application to the monolith
- Handles SSL/TLS termination (if configured)
- Provides load balancing capabilities for future scaling

Configuration allows clients to connect to a single endpoint without knowledge of internal service topology.

#### Docker Compose Orchestration

The system is fully containerized with the following services:

```yaml
Services:
  - postgres-monolith: PostgreSQL for monolith
  - postgres-delivery: PostgreSQL for delivery service
  - kafka: Apache Kafka message broker
  - zookeeper: Kafka dependency for coordination
  - monolith: Spring Boot application (TascaEats)
  - delivery-service: Spring Boot microservice
  - nginx: Reverse proxy
  - pgadmin (optional): Database administration UI
```

All services communicate within a Docker network, with external access controlled through Nginx.

#### Running Phase 3

From the project root:

```bash
docker compose up --build
```

This command:
- Builds all service images
- Creates isolated PostgreSQL databases for each service
- Starts Kafka and Zookeeper
- Initializes the monolith and DeliveryService
- Configures Nginx as the entry point

Access points:
- **Web Interface:** `http://localhost/` (routed to monolith)
- **Swagger UI:** `http://localhost/swagger-ui/` (routed to monolith)
- **Kafka UI (optional):** `http://localhost:8080/` (if included)

#### Running JavaFX with Phase 3

The JavaFX application connects through Nginx without changes to the client code:

```bash
cd JavaFX
mvn javafx:run
```

Internally, gRPC requests are routed through `localhost:80` to Nginx, which forwards them to the monolith.

### Key Implementation Details

#### Order to Delivery Workflow

1. **Order Status Changed to READY:**
   - Monolith publishes `create-delivery` event with order ID

2. **Delivery Service Processes Event:**
   - Creates delivery record with unique ID
   - Searches for available delivery persons
   - If available, automatically assigns delivery person
   - Publishes `create-delivery-response` with assignment details

3. **Monolith Receives Response:**
   - Waits for delivery creation response (10-second timeout)
   - Updates order status to `INCOMING`
   - Associates delivery person with order
   - Updates delivery person status to `BUSY`

4. **User Interfaces Reflect Changes:**
   - Web and JavaFX interfaces display updated order status
   - Delivery person sees new assignment in their app

#### Fault Tolerance

- **Timeouts:** 10-second window for critical responses; requests fail gracefully
- **Dead Letter Topics:** Failed messages route to DLT for manual inspection
- **Retry Logic:** Transient failures trigger automatic retries with backoff
- **Idempotency:** Operations designed to be safely retried without side effects

#### Data Consistency Patterns

**Strong Consistency (Critical):**
- Order creation and payment processing within monolith
- Delivery person assignment workflow (uses synchronous request-response)

**Eventual Consistency (Non-Critical):**
- Delivery status updates propagate asynchronously
- Delivery person profile updates eventual across interfaces

### Demonstration Video

A demonstration video is available showing:
- Order lifecycle from creation to delivery assignment
- Kafka event flow and microservice interaction
- Database isolation and state management
- Nginx routing of REST and gRPC traffic

> **Video Link:** [To be provided in submission]

### Project Structure (Phase 3)

```
tascaeats/
├── TascaEats/                    # Monolith application
│   ├── src/
│   ├── pom.xml
│   ├── Dockerfile
│   └── ...
├── DeliveryService/              # Microservice
│   ├── src/
│   ├── pom.xml
│   ├── Dockerfile
│   └── ...
├── Common/                        # Shared utilities and DTOs
│   ├── src/
│   └── pom.xml
├── JavaFX/                        # Desktop client
│   ├── src/
│   ├── pom.xml
│   └── ...
├── nginx.conf                     # Reverse proxy configuration
├── docker-compose.yml             # Service orchestration
└── README.md
```
