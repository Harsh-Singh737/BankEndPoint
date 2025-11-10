# Banking System CRUD Application

## Table of Contents
- [Project Overview](#project-overview)  
- [Technologies Used](#technologies-used)  
- [Database Schema](#database-schema)  
- [Setup & Installation](#setup--installation)  
- [Running the Application](#running-the-application)  
- [API Endpoints](#api-endpoints)  
- [Usage Examples](#usage-examples)  
- [Important Notes](#important-notes)  
- [Future Enhancements](#future-enhancements)  

---

### Project Overview

 - The Banking System CRUD Application is a Spring Boot-based backend application for managing customers, accounts, transactions, and transfers in a banking system.

 - It provides complete CRUD operations and secure JWT-based authentication for admin routes.

 - It supports:

 - Managing customers, accounts, transactions, and fund transfers.

 - Recording deposits, withdrawals, and transfers between accounts.

 - Updating account balances safely.

 - JWT-secured Admin APIs for managing and monitoring data.

 - RESTful API for integration with a frontend or external services.

This project is designed to help you learn Spring Boot, JPA, MySQL, and JWT Security in a modular, production-style structure.

---

## Technologies Used
- **Backend Framework:** Spring Boot 3.x  
- **Database:** MySQL  
- **ORM:** JPA / Hibernate  
- **Java Version:** 17+  
- **Dependency Management:** Maven  
- **Lombok** for boilerplate code reduction  

---

## Database Schema
The application uses three main tables:

### Customers
| Column Name   | Type       | Description          |
|---------------|-----------|--------------------|
| customer_id   | BIGINT     | Primary Key         |
| first_name    | VARCHAR    | Customer's first name|
| last_name     | VARCHAR    | Customer's last name |
| email         | VARCHAR    | Email               |
| phone         | VARCHAR    | Phone number        |
| address       | VARCHAR    | Customer address    |
| created_at    | TIMESTAMP  | store data and time |

### Accounts
| Column Name   | Type       | Description               |
|---------------|------------|---------------------------|
| account_id    | BIGINT     | Primary Key               |
| account_number| VARCHAR    | Unique account number     |
| account_type  | VARCHAR    | Type (Saving/Current)     |
| balance       | DOUBLE     | Account balance           |
| customer_id   | BIGINT     | Foreign key from Customer |

### Transactions
| Column Name       | Type       | Description               |
|------------------ |------------|---------------------------|
| transaction_id    | BIGINT     | Primary Key               |
| transaction_type  | VARCHAR    | Deposit / Withdrawal      |
| amount            | DOUBLE     | Transaction amount        |
| description       | VARCHAR    | Optional description      |
| transaction_date  | DATETIME   | Date & time of transaction|
| account_id        | BIGINT     | Foreign key from Account  |



### Transfers
| Column Name       | Type           | Description               |
|------------------|-----------------|---------------------------|
| transfer_id      | BIGINT          | Primary Key               |
| from_account_id  | BIGINT          | sender_account_id         |
| to_account_id    | BIGINT          | receiver_account_id       |
| amount           | DOUBLE          | transferred amount        |
| transfer_date    | DATETIME        | Date & time of transfer   |
| remarks          | VARCHAR(50)     | Optional description      |

---

### 🔐 Authentication & Security

 - The application uses Spring Security with JWT for authentication and authorization.

 - Admin credentials are stored in memory (not in the database).

 - Upon successful login, the admin receives a JWT token.

 - All endpoints under /api/admin/** are secured and require a valid JWT.
   
 - Other public APIs (like customers, accounts, transactions) remain open for testing and learning purposes.

## Setup & Installation

### Prerequisites
- Java JDK 17+  
- MySQL Database  
- Maven  
- IDE (IntelliJ IDEA, Eclipse, VS Code)  

### Steps
1. **Clone the repository**  
```bash
git clone https://github.com/yourusername/bankingsystem-crud.git
cd bankingsystem-crud
```

## ⚙️ Setup Instructions

### Step 2: Create MySQL Database
Run the following SQL command in your MySQL terminal or any client (like MySQL Workbench):

```sql
CREATE DATABASE database_name;
```
---

### Step 3: Open the file src/main/resources/application.properties and add the following configuration:
```sql
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/bankingdb
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```
