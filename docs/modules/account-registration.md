### 📘 **Module: Account Registration – Service Layer Design & Validation**

**Context**
We are implementing the `AccountServiceImpl` class for account registration in our banking system. This is a Tier-1 core module and must follow real-world banking constraints, avoiding artificial rules.

---

#### ✅ Functional Requirements

* Allow users to register for a bank account with the following input:

    * Full name
    * Phone number
    * Account type (e.g., SAVINGS, CURRENT)
    * Password
* A single user (based on phone) can register for multiple account types (e.g., one savings and one current).
* The **account number must be unique** across the system.

---

#### 🔒 Validation Rules

| Field       | Validation                                                       |
| ----------- | ---------------------------------------------------------------- |
| Phone       | Must be exactly 10 digits                                        |
| Password    | Must contain 1 uppercase, 1 digit, 1 special character, 8+ chars |
| Account No. | Must be system-generated and unique (check before insert)        |

---

#### ⚠️ Exceptions

| Scenario                 | Exception Thrown                |
| ------------------------ | ------------------------------- |
| Duplicate account number | `AccountAlreadyExistsException` |
| Invalid phone format     | `IllegalArgumentException`      |
| Weak password            | `IllegalArgumentException`      |

---

#### 🧠 Implementation Summary (`AccountServiceImpl.java`)

```java
public Account createAccount(AccountRequestDTO requestDto) {
    // 1. Validate input
    // 2. Check if account number is unique
    // 3. Save account to DB
}
```

Helper methods:

* `isValidPhoneNumber()`
* `isValidPassword()`

---

#### 🧪 Unit Tests Written

* `createAccount_Success()`
* `createAccount_AccountAlreadyExistsException()`
* `createAccount_InvalidPhoneNumber()`
* `createAccount_InvalidPassword()`

---

#### 📌 Decisions Logged

* Phone number is **not** unique; allows multiple accounts per user.
* Password validation is basic for now; will move to encoder & policy module later.
* Account number generation will be system-driven in future (UUID or algo).

---