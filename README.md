# 💰 Payments Management System

A Java-based application designed to manage incoming and outgoing payments for a fintech startup. The system provides secure payment tracking, role-based access control, report generation, and an audit trail, ensuring transparency, data integrity, and compliance.

---

## 📌 Problem Statement

A fintech startup requires a simple, secure internal system to:

- Track incoming payments (e.g., client invoices) and outgoing payments (e.g., vendor settlements, salaries).
- Allow **Finance Managers** to:
  - Add new payment records (incoming/outgoing).
  - Categorize payments (Salary, Vendor Payment, Client Invoice).
  - Update payment statuses (Pending, Processing, Completed).
  - Generate monthly and quarterly financial reports (credits, debits, net balance).
- Maintain an **audit trail** for all transactions.
- Support user roles: **Admin**, **Finance Manager**, **Viewer**.
- Prioritize **security**, **data integrity**, and **traceability**.

---

## 📋 Features

### ✅ User Management
- User registration with default Viewer role.
- Admin-controlled role assignment (Admin, Finance Manager, Viewer).
- Secure login with password hashing.

### 💳 Payment Management
- Finance Managers can create payments with:
  - Type: `INCOMING`, `OUTGOING`
  - Status: `PENDING`, `PROCESSING`, `COMPLETED`
  - Category: `Salary Disbursements` , `Vendor Payments`, `Client Invoice` 
- Status updates restricted to Finance Managers.
- Users can view their own payments.

### 📈 Report Generation
- Generate **monthly** and **quarterly** reports.
- Report includes total **incoming**, **outgoing**.

### 🕵️ Audit Logging
- Automatically logging of payment actions (CREATE, UPDATE, DELETE) via triggers.
- Logs include payment ID, user ID, action, status changes, amount, type, date, and timestamp.

---

## 🛠️ Tech Stack

| Layer        | Technology                                             |
|--------------|--------------------------------------------------------|
| Backend      | Java 21                                                |
| Database     | PostgreSQL 17                                          |                                      
| Build Tool   | Maven                                                  |
| Security     | AES encryption                                         |
| DB Logging   | PostgreSQL Triggers                                    |

---

## 📂 Project Structure

- `src/main/java/com/fintech/payments/`
  - `model/` – Contains entity classes.
  - `dto/` – Holds Data Transfer Objects.
  - `dao/` – Repository interfaces (DAOs) for database access.
  - `service/` – Implements business logic and interacts with repositories.
  - `config/` – Configuration classes for database connection).
  - `enums/` – Contains enumerations.
  - `exceptions/` – Custom exception classes.
  - `util/` – Utility classes (e.g., for encryption).
  - `PaymentsApplication.java` – The main class that bootstraps the Spring Boot application.
 
---

## 🚀 Setup Instructions

### Prerequisites
- Java 21
- PostgreSQL
- Maven

### Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/dhananjaysj-gamma/payments-management-system.git
   cd payments-management-system
   ```

## 🚧 Future Enhancements

- [ ] **CSV Export for Reports**  
  Enable downloading monthly and quarterly financial reports in CSV format.

- [ ] **Invoice Management for Client Invoice Category**  
  Add features to manage invoices such as uploading documents, setting due dates, and tracking invoice status.

- [ ] **Support for Recurring Payments**  
  Introduce functionality to handle recurring payments (e.g., monthly salaries, vendor retainers).

- [ ] **Audit Logging for User Role Changes**  
  Extend audit trail to log role assignments and changes made by Admin users.

