# 💰 Payments Management System

A Java Spring Boot application for managing incoming and outgoing payments for fintech startups. The system ensures secure payment tracking, role-based access control, report generation, and a comprehensive audit trail for transparency, data integrity, and compliance.

---

## 📌 Problem Statement

A fintech startup needs a secure internal system to manage all payment flows with traceability and reporting.
Create a Spring Boot REST API backend to manage users and payment records with status tracking and categorization.

---

## 📋 Features

### ✅ User Management
- Admin can register users with a temporary password.
- Users update their password on first login.
- Admin-controlled role assignment (Admin, Finance Manager, Viewer).
- Secure login with password hashing.
- JWT-based authentication and role-based authorization for secure access

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
- Automatically logging of payment actions (CREATE, UPDATE, DELETE) via  DB triggers.
- Logs include payment ID, user ID, action, status changes, amount, type, date, and timestamp.

---

# 🛠️ Tech Stack

| Layer        | Technology            |
|--------------|-----------------------|
| Backend      | Java 17, Spring Boot  |
| Database     | PostgreSQL 17         |                                      
| Build Tool   | Maven                 |
| Security     | JWT, Spring Security  |
| DB Logging   | PostgreSQL Triggers   |

---

## 🚀 Setup Instructions

### Prerequisites
- Java 17
- PostgreSQL
- Maven
- Spring Boot2

### Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/dhananjaysj-gamma/payments-management-system.git
   cd payments-management-system
   ```