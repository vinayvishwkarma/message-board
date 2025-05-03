# Message Board

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](#)  
[![License](https://img.shields.io/badge/license-MIT-blue)](#)

> **A scalable, versioned messaging microservice** built with Spring Boot  
> Supports REST & SOAP protocols, TDD, and clean multi-module architecture.

---

## 📖 Table of Contents

1. [Overview](#overview)
2. [Architecture & Modules](#architecture--modules)
3. [Prerequisites](#prerequisites)
4. [Setup & Build](#setup--build)
5. [Running the Services](#running-the-services)
6. [API Reference](#api-reference)
7. [Testing](#testing)
8. [Future Improvements](#future-improvements)
9. [License](#license)

---

## 🌐 Overview

This **Message Board** application lets clients:

- **Create** messages with `title`, `content`, `sender`, and `url`.
- **List** messages in two versions:
    1. **v1**: `{ title, content, sender }`
    2. **v2**: `{ title, content, sender, url }` (supports `JSON` & `XML`)

Designed as a **Maven multi-module** project:

- **TDD-first**: all features start with failing tests.
- **In-memory store** (via `ConcurrentHashMap`), pluggable for real DB later.
- **Clean separation**: core logic ↔ REST API ↔ SOAP API (bonus).

---

## 🏗️ Architecture & Modules

| Module                         | Artifact ID                   | Responsibilities                                 |
|--------------------------------|-------------------------------|--------------------------------------------------|
| **
Core**                       | `message-board-core`          | Domain models, validation, in-memory storage     |
| **REST
API**                   | `message-board-rest-api`      | Spring MVC controllers for `create` & `list`     |
| **SOAP API** *(
optional)*      | `message-board-soap-api`      | Spring-WS endpoints for `createMessage`          |

message-board/ ├── pom.xml ← parent POM (dependency & plugin management) ├── message-board-core/ ←
core logic & domain ├── message-board-rest-api/ ← RESTful microservice ├── message-board-soap-api/ ←
SOAP microservice (bonus) ├── run.sh ← build & run script └── README.md


---

## 🛠️ Prerequisites

- **Java** 17 or higher
- **Maven** 3.6+
- **Git** (to clone the repo)
- **IDE** (IntelliJ IDEA recommended)

---

## 🚀 Setup & Build

1. **Clone the repo**
   ```bash
   git clone https://github.com/your-org/message-board.git
   cd message-board

2. **Build all modules**
   ```bash
   mvn clean install

▶️ Running the Services
You can start each service independently, or use run.sh to launch all at once.

1. run.sh
   ```bash
    #!/bin/bash

    # Exit script on error
    set -e

    # Check if Maven is installed
    if ! command -v mvn &> /dev/null; then
        echo "Maven could not be found. Please install Maven."
        exit 1
    fi

    # Build the project using Maven

    echo "Building the project using Maven..."
    mvn clean install

    # Run the Spring Boot application

    echo "Starting the Spring Boot application..."

    # Run the Spring Boot application (ensure the JAR file exists in the target directory)

    if [ -f "message-board-rest-api/target/message-board-rest-api-1.0.0.jar" ]; then
        java -Dserver.port=8080 -jar message-board-rest-api/target/message-board-rest-api-1.0.0.jar
        echo "Service started successfully at http://localhost:8080"
    else
        echo "JAR file not found. Please check the build process."
        exit 1
    fi
   ```

2. Manually
    - REST API
    ```bash
   cd message-board-rest-api
   mvn spring-boot:run
    ```
    - SOAP API
    ```bash
   cd message-board-soap-api
   mvn spring-boot:run
    ```

By default:

- REST listens on http://localhost:8080

- SOAP listens on http://localhost:8081/ws

📡 API Reference

Create Message

```bash
POST /messages
Content-Type: application/json

{
  "title":   "Hello World",
  "content": "This is a test message.",
  "sender":  "Alice",
  "url":     "https://example.com/info"
}
```

- Response: 201 Created
- Validation:

    - title, content, sender → non-empty, length limits
    - url → valid URL

List Messages

```bash
GET /messages?version=1
Accept: application/json
```

v1 Response (JSON only):

```json
[
  {
    "title": "Hello World",
    "content": "This is a test message.",
    "sender": "Alice"
  }
]
```

```
GET /messages?version=2&format=xml
Accept: application/xml
```

v2 Response (JSON/XML):

```json  
[
  {
    "title":   "Hello World",
    "content": "This is a test message.",
    "sender":  "Alice",
    "url":     "https://example.com/info"
  }
]
```

🧪 Testing
Run all tests across modules:

```bash
mvn test
```

- Core: validation & in-memory service tests

- REST API: MockMvc controller tests

- SOAP API: endpoint request/response tests

## 🚀 Future Improvements

- Replace in-memory store with a real database (JPA/Hibernate).

- Add authentication & authorization (Spring Security).

- Implement additional response versions for listMessages.

- Containerize services (Docker + Kubernetes).

- Integrate CI/CD pipeline, code coverage badges.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

```