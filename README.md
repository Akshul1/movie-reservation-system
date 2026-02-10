# 🎬 Movie Reservation System API

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A high-performance, concurrent-safe backend system for booking movie tickets. This project mimics real-world platforms like **BookMyShow** or **Fandango**, featuring role-based security, dynamic seat management, and a robust locking mechanism to prevent double-booking.

---

## 🚀 Key Features

* **🔐 Stateless Authentication:** Secure login using **JWT (JSON Web Tokens)** with custom filters.
* **👮 Role-Based Access Control (RBAC):** Distinct flows for `USER`, `ADMIN`, and `SUPER_ADMIN`.
* **⚡ Concurrency Handling:** Implements **Reentrant Locks** to ensure two users cannot book the same seat simultaneously.
* **🎥 Dynamic Scheduling:** Admins can map movies to theaters with specific start/end times and prices.
* **💺 Smart Seat Management:** Support for different seat areas (e.g., VIP, Regular) and real-time availability tracking.
* **🐳 Containerized:** Fully Dockerized for easy deployment.

---

## 🛠️ Tech Stack

* **Language:** Java 17
* **Framework:** Spring Boot 3.3
* **Database:** MySQL 8.0
* **ORM:** Spring Data JPA (Hibernate)
* **Security:** Spring Security & JJWT
* **Build Tool:** Maven
* **Deployment:** Docker

---

## 🏗️ Architecture & Design Decisions

### The "Double Booking" Problem
One of the core engineering challenges in this system was handling race conditions. If User A and User B try to book "Seat A1" at the exact same millisecond, a standard database transaction might fail or allow both.

**My Solution:**
I implemented a **`SeatLockManager`** using `ConcurrentHashMap` and `ReentrantLock`.
1.  When a booking request arrives, the system attempts to acquire a lock for the specific seat ID in memory.
2.  If the lock is acquired, the transaction proceeds to check the DB status and book the seat.
3.  If the lock is busy, the second request is rejected immediately with a `409 Conflict`, preserving data integrity without overloading the database.

---

## ⚙️ Getting Started

### Prerequisites
* Java 17+
* MySQL Server
* Maven

### Option 1: Run Locally

1.  **Clone the repository**
    ```bash
    git clone [https://github.com/Akshul1/movie-reservation-system](https://github.com/Akshul1/movie-reservation-system)
    cd movie-reservation-system
    ```

2.  **Configure Database**
    Update `src/main/resources/application.properties` if your local MySQL credentials differ:
    ```properties
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    ```

3.  **Run the App**
    ```bash
    ./mvnw spring-boot:run
    ```
    The app will start on `http://localhost:8080`.

### Option 2: Run with Docker

1.  **Build the Image**
    ```bash
    docker build -t movie-api .
    ```

2.  **Run the Container**
    You can pass database credentials as environment variables:
    ```bash
    docker run -p 8080:8080 \
      -e DB_URL=jdbc:mysql://host.docker.internal:3306/movie_db \
      -e DB_USERNAME=root \
      -e DB_PASSWORD=root \
      movie-api
    ```

---

## 🔌 API Endpoints

### Authentication
* `POST /auth/signup` - Register a new user.
* `POST /auth/authenticate` - Login and receive JWT.

### Movies & Theaters (Admin)
* `POST /api/v1/movies/create` - Add a new movie.
* `POST /api/v1/theaters/theater/create` - Add a new theater.
* `POST /api/v1/shows/show/create` - Schedule a movie in a theater.

### Reservations (User)
* `GET /api/v1/shows/all` - Browse available shows.
* `POST /api/v1/reservations/reserve` - Book tickets.
    * *Payload:* `{ "showId": 1, "seatIdsToReserve": [10, 11], "amount": 500.0 }`
* `POST /api/v1/reservations/cancel/{id}` - Cancel a booking.

---

## 🧪 Default Users (Seeding)

On the first run, the system automatically creates a Super Admin user:
* **Username:** `super_user`
* **Password:** `super_password`

---

## 🔮 Future Enhancements

* **Payment Gateway Integration:** Integrate Stripe/PayPal for real payments.
* **Redis Caching:** Cache popular endpoints (like "Get All Movies") to reduce database load.
* **Email Notifications:** Send booking confirmations via SMTP.

---

**Author:** Akshul jamwal.
