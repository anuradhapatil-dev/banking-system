
## 📘 Feature: Account Creation & Account Information Retrieval

### ✅ Overview

This module allows customers to:
- **Create a new account** with initial deposit
- **Retrieve account details** using account number

## 🧱 Architecture Breakdown

| Layer         | Component                    | Description |
|---------------|------------------------------|-------------|
| Controller    | `AccountController`          | Exposes REST APIs for account creation and retrieval |
| Service       | `AccountServiceImpl`         | Core business logic for account operations |
| Handler       | `AccountCreationHandler`     | Handles complex creation steps (validation, customer reuse, idempotency) |
| Repository    | `AccountRepository`, `CustomerRepository` | Database interaction layer |
| Validators    | `AccountRequestValidator`, `PhoneNumberValidator`, `PasswordValidator` | Input validation |
| Utility       | `AccountNumberGenerator`     | Generates unique account numbers |
| Idempotency   | `IdempotencyService`         | Ensures request is processed only once |

## 🔐 Validations

- Unique `(phone + account type)` combination
- Idempotency key check
- Password encoding before persisting
- Phone and password format validations

## 📄 API Contracts

### 1. `POST /api/accounts`

**Request**:
```json
{
  "name": "Anu",
  "phone": "9999999999",
  "password": "pass123",
  "type": "SAVINGS",
  "initialDeposit": 1000,
  "idempotencyKey": "key123"
}
```

**Response**:
```json
{
  "accountNumber": "ACC001",
  "message": "Account created successfully",
  "statusCode": 201,
  "timestamp": "2025-06-12T10:32:12Z"
}
```

### 2. `GET /api/accounts/{accountNumber}`

**Response**:
```json
{
  "accountNumber": "ACC001",
  "balance": 1000,
  "accountType": "SAVINGS",
  "customerName": "Anu",
  "phone": "9999999999",
  "timestamp": "2025-06-12T10:35:00Z"
}
```

## 🧪 Test Coverage Summary

### ✅ Service Layer (`AccountServiceImplTest`)
- `shouldReturnAccountDetailsSuccessfully()`
- `shouldThrowExceptionWhenAccountNotFound()`

### ✅ Handler Layer (`AccountCreationHandlerTest`)
- `shouldCreateAccountSuccessfully()`
- `shouldThrowWhenDuplicateAccountExists()`
- `shouldThrowWhenIdempotencyKeyIsDuplicate()`
- `shouldReuseExistingCustomer()`

### ✅ Controller Layer
- `createAccount_shouldDelegateToHandlerAndReturnResponse()`
- `getAccountDetails_shouldDelegateToHandlerAndReturnInfo()`

### ✅ Repository Layer (`AccountRepositoryTest`)
- `findByAccountNumber_ShouldReturnAccount()`

## 🧾 Decision Log

| Decision | Rationale |
|----------|-----------|
| Split `createAccount` logic into handler | To isolate responsibilities and keep `ServiceImpl` thin |
| Used `@DataJpaTest` for repository | Enables in-memory database testing without bootstrapping the full app |
| Introduced `IdempotencyService` | Prevents duplicate account creation via retries |
| Used `ReflectionTestUtils` for manual injection in unit tests | Due to constructor ambiguity in handler class |
| Used DTOs instead of entities in controller layer | Ensures clean, secure API contracts and separation from database schema |