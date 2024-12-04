# Bank Manager Service - Spring Boot Application

## Key Features
This project implements a **Bank Management System** using Spring Boot, showcasing the following functionalities:

### 1. Account Management
- **Create Account**: Users can create a new account with unique account numbers generated automatically.
- **Balance Enquiry**: Check the account balance for a given account number.
- **Name Enquiry**: Retrieve the account holder's name using the account number.

### 2. Transactions
- **Credit Account**: Add funds to a user account and record the transaction.
- **Debit Account**: Withdraw funds from a user account, with checks for sufficient balance.
- **Funds Transfer**: Transfer funds between accounts, with alerts for both sender and recipient.

### 3. Email Notifications
- Automatic email alerts for account creation, transactions (credit and debit), and fund transfers.

### 4. Error Handling
- Handles cases where accounts do not exist, balances are insufficient, or input data is invalid, ensuring consistent and informative error responses.

### 5. Code Architecture
- Implements service, repository, and utility layers for modular and maintainable code.
- Includes well-defined DTOs for consistent data transfer across layers.

---

## Technology Stack
- **Java 17**
- **Spring Boot** (REST API)
- **Spring Data JPA** (Database integration)
- **H2/MySQL** (Database)
- **Lombok** (Boilerplate code reduction)

---

## How to Run the Project
1. Clone the repository.
2. Configure database settings in `application.properties`.
3. Run the application using:
   ```bash
   mvn spring-boot:run
4.Access the API documentation via Swagger or Postman.
