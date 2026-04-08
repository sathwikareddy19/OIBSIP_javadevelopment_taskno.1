# Online Reservation System (Java Swing)

A **Core Java + Swing** desktop application that simulates an online railway reservation system.  
Users can **login/register**, **book tickets**, get an auto-generated **PNR**, **view reservations** by PNR, and **cancel tickets** by PNR.

## Features

- **Login / Register**
  - Validates Login ID + Password
  - Create new account from the Register screen
  - Register window close (X) safely returns to Login after confirmation
- **Reservation (Book Ticket)**
  - Passenger name + age
  - Train selection with **auto-filled train number and train name**
  - Class type (AC / Sleeper / General)
  - **Journey date selection using a calendar picker**
  - Source and destination
  - Generates a unique **PNR** after successful booking
  - Option to **Copy PNR** or **Save Ticket** (receipt text file)
- **View Reservation**
  - Search by PNR to view full ticket details
- **Cancellation**
  - Search by PNR to show details, then confirm cancellation
  - Cancellation updates seat availability
- **Dashboard Enhancements**
  - Shows **Last booked PNR** for the logged-in user
  - Optional **Delete Account** feature (admin account is protected)

## Default Login

- **Login ID**: `admin`
- **Password**: `admin123`

You can also create a new account using **Register**.

## PNR Notes

- PNR is **auto-generated at booking time** in the format `PNR000001`, `PNR000002`, etc.
- Use the PNR for:
  - **View Reservation (PNR Search)**
  - **Cancellation**
- After booking, you can:
  - **Copy PNR** to clipboard
  - **Save Ticket** to a receipt file

## Ticket Receipt Save Location

When you click **Save Ticket**, the application creates a folder named `pnr-receipts` and saves a file like:

- `pnr-receipts/PNR000001.txt`

The app tries to save in the **project folder** first. If that fails, it falls back to the user home directory.

## Tech Stack

- **Java (Core Java)**
- **Java Swing** (GUI)
- **OOP + Collections** (in-memory storage)
- **java.time** (date handling)
- **Java NIO** (receipt file saving)

## How to Run (Windows)

### Option A: Using Command Prompt / PowerShell

Open a terminal in the project folder and run:

```bash
javac *.java
java Main
```

### Option B: Using an IDE (recommended)

1. Open the project folder in an IDE (e.g., IntelliJ IDEA / Eclipse / VS Code).
2. Run `Main.java`.

## Project Structure (key files)

- `Main.java` — entry point
- `LoginFrame.java` — login UI
- `RegisterFrame.java` — create account UI
- `DashboardFrame.java` — main dashboard (includes last booked PNR + delete account)
- `ReservationFrame.java` — booking form + calendar + receipt options
- `ViewReservationFrame.java` — PNR search view
- `CancellationFrame.java` — PNR search + cancel
- `DataStore.java` — application data + business logic (users/trains/reservations/PNR)
- `DatePickerDialog.java` — calendar date picker
- `PnrReceiptUtil.java` — copy/save receipt helper

## Important Note (Data Persistence)

This project currently uses **in-memory storage** (no database).  
If you restart the application, users/reservations reset (receipts saved to files remain available).

