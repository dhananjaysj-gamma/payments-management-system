# 💳 Payments Management System

A **console-based Java application** designed to manage incoming and outgoing payments within an organization. The system includes features like user registration, login, payment creation, status updates, report generation, and audit trail tracking.

---

## 📌 Project Overview

This Payments Management System helps organizations efficiently track and manage payments. It is built using core Java and follows a layered architecture:

- **Entities**
- **DTOs (Data Transfer Objects)**
- **DAO Layer**
- **Service Layer**
- **Main Application (Console Interface)**

---

## 🧠 Logic Walkthrough

### 1. 👤 User Module

- Users can **register** with name, email, and password.
- Users can **log in** using their email and password.
- Upon successful login, the system **tracks the current user session** for contextual operations.

### 2. 💰 Payment Module

- Only the **Finance Manager** can add payments.
- A payment contains:
  - Amount
  - Type: `INCOMING` or `OUTGOING`
  - Status: `PENDING`, `COMPLETED`, or `FAILED`
  - Payment date
  - Category ID
  - Optional description
- Payments are linked to the user who created them via the `createdBy` field.
- Users can view **all their payments**.

### 3. 🔄 Payment Status Update

- Only the **Finance Manager** can update the payment status.
- Updating a payment status automatically generates an **audit log** with change details.

### 4. 📊 Report Generation

- Finance Manager can generate **monthly** or **quarterly** reports.
- Reports include:
  - Total **credits**
  - Total **debits**
  - **Net balance**
- Internally handles:
  - Date filtering
  - Data aggregation for the selected time period

### 5. 📝 Audit Logging

Audit logging ensures **transparency** and **traceability** of all payment actions.

- Automatically logs every payment **INSERT**, **UPDATE**, or **DELETE**.
- Implemented via **database triggers** on the `payments` table.
- Stores:
  - Operation type (`CREATE`, `UPDATE`, `DELETE`)
  - Amount
  - Status
  - Payment ID
  - User ID
  - Payment type
  - Payment date
  - Timestamp

> ✅ This guarantees an immutable trail of changes made to any payment record.
