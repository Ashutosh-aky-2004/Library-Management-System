# 📚 Library Management System (Java)

A simple and efficient **Library Management System** built in **Java**, designed to manage books, members, and transactions (issue/return of books).  
It demonstrates strong **Object-Oriented Programming (OOP)** principles and can be extended to use databases or GUIs.

---

## 🚀 Features

- **Add, View, and Remove Books**
- **Register and Manage Members**
- **Issue and Return Books**
- **Search for Books or Members**
- **View Issued Book Records**
- **Console-based interface** (easy to navigate)
- Clean OOP design with classes for `Book`, `Member`, and `Library`

---

## 🧩 Project Structure

```
LibraryManagementSystem/
│
├── src/
│   ├── Book.java
│   ├── Member.java
│   ├── Library.java
│   ├── Transaction.java
│   └── Main.java
│
├── README.md
└── (optional) database.sql   # for JDBC version
```

---

## ⚙️ How It Works

### 🏗 Classes
- **Book** → Represents a single book with ID, title, author, and availability status.  
- **Member** → Represents a library user (student or teacher).  
- **Library** → Manages collections of books and members, handles issuing and returning.  
- **Transaction** → Tracks book issues and returns with timestamps.  
- **Main** → Entry point of the application (menu-driven console interface).

---

## 🧠 Concepts Used

- Object-Oriented Programming (Encapsulation, Inheritance, Polymorphism)
- Collections Framework (`ArrayList`, `HashMap`)
- Exception Handling
- Input/Output Streams
- Basic File Handling (for persistence, optional)
- Optional: JDBC integration (MySQL)

---

## 💻 How to Run

1. Clone this repository  
   ```bash
   git clone https://github.com/Ashutosh-aky-2004/LibraryManagementSystem.git
   cd LibraryManagementSystem
   ```

2. Compile the program  
   ```bash
   javac src/*.java
   ```

3. Run the program  
   ```bash
   java src/Main
   ```

---

## 🗄️ Optional: Database Integration

You can connect this system to a **MySQL database** using **JDBC**.  
Update your `Library.java` or `DatabaseHelper.java` with the following details:

```java
String url = "jdbc:mysql://localhost:3306/librarydb";
String user = "root";
String password = "yourpassword";
```

Use `database.sql` to create tables for books, members, and transactions.

---

## 🧩 Future Enhancements

- Add **Swing / JavaFX GUI**
- Add **Admin login system**
- Generate **book issue reports**
- Email notifications for due dates
- Integration with cloud databases

---


## 🖼 Sample Menu (Console UI)

```
===== Library Management System =====
1. Add Book
2. View All Books
3. Register Member
4. Issue Book
5. Return Book
6. Search Book
7. Exit
Enter your choice: 
```

---

```
Book issued successfully!
Book ID: B102
Member: Rahul Sharma
Due Date: 15-11-2025
```

---

```
Thank you for using the Library Management System!
```

