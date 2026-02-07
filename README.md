# Library Management System

A simple Library Management System built with **Java (SparkJava)** for the backend and **HTML/CSS/JavaScript** for the frontend.  
This project allows adding books, registering members, borrowing/returning books, and tracking transactions.

---

## Features
- Add and list books (with author details)
- Register members (Student or Teacher)
- Borrow and return books with proper validation
- Show member details including borrowed books
- Persistent data storage using JSON files
- Simple web-based frontend (UI)

---

## Requirements
Before running, ensure you have installed:
- **Java JDK**  
- **Apache Maven**  
- A modern browser (Chrome/Firefox/Edge)

---

## Installing JDK (Windows + Git Bash)

### Step 1: Download JDK
- Oracle JDK: [Oracle Downloads](https://www.oracle.com/java/technologies/downloads/)
- OpenJDK (free, recommended): [Adoptium Temurin](https://adoptium.net/)

Choose a stable version (e.g., **JDK 17** or **JDK 21**).

### Step 2: Install
Run the installer and let it install in:
```
C:\Program Files\Java\jdk-XX
```

### Step 3: Set Environment Variables
1. Open **Start Menu** → search for **Environment Variables**.
2. Click **Environment Variables…**
3. Under *System variables*:
   - Add a new variable:
     ```
     JAVA_HOME = C:\Program Files\Java\jdk-XX
     ```
   - Edit the `Path` variable → Add:
     ```
     C:\Program Files\Java\jdk-XX\bin
     ```

### Step 4: Verify
Open Git Bash or CMD and run:
```bash
java -version
javac -version
```

---

## Installing Apache Maven (Windows + Git Bash)

### Step 1: Download Maven
- Official site: [Apache Maven Downloads](https://maven.apache.org/download.cgi)  
- Download the **Binary zip archive** (not the source).  
  Example: `apache-maven-3.9.X-bin.zip`

### Step 2: Extract Maven
1. Unzip the file into a preferred location, for example:
   ```
   C:\Program Files\Apache\Maven
   ```
2. After extraction, you will see a folder like:
   ```
   C:\Program Files\Apache\Maven\apache-maven-3.9.X
   ```

### Step 3: Configure Environment Variables
1. Open **Start Menu** → type **Environment Variables** → select **Edit the system environment variables**.  
2. Click **Environment Variables…**  
3. Under *System variables*:
   - Add a new variable:
     ```
     M2_HOME = C:\Program Files\Apache\Maven\apache-maven-3.9.X
     ```
   - Edit the existing `Path` variable → Add:
     ```
     C:\Program Files\Apache\Maven\apache-maven-3.9.X\bin
     ```

### Step 4: Verify Installation
Run:
```bash
mvn -version
```

✅ Expected output:
```
Apache Maven 3.9.X
Maven home: C:\Program Files\Apache\Maven\apache-maven-3.9.X
Java version: 17.0.9, vendor: Oracle Corporation
```

---

## Project Structure
```
java-prog-group-3/
├── pom.xml                           # Maven configuration file
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/library/
│   │   │       ├── Main.java         # Entry point (starts Spark server)
│   │   │       ├── exceptions/
│   │   │       │   └── BookNotAvailableException.java
│   │   │       ├── models/
│   │   │       │   ├── Book.java
│   │   │       │   ├── Member.java
│   │   │       │   ├── Student.java
│   │   │       │   └── Teacher.java
│   │   │       └── services/
│   │   │           ├── Library.java
│   │   │           ├── FileHandler.java
│   │   │           └── RuntimeTypeAdapterFactory.java
│   │   └── resources/
│   │       └── public/
│   │           └── html/
│   │               ├── index.html    # Frontend HTML
│   │               ├── index.css     # Frontend CSS
│   │               └── index.js      # Frontend JavaScript
│   └── test/
│       └── java/                     # (Optional) Unit tests
├── data/                             # Persisted JSON data (created automatically)
│   ├── books.json
│   ├── members.json
│   └── transactions.json
└── README.md
```

---

## How to Run

### 1. Clone the repository
```bash
git clone https://github.com/albertii-alt/library_system_management.git
cd library_system_management
```

### 2. Run the backend (Java + Spark)
```bash
mvn clean compile exec:java
```
➡️ Server will start at:
```
http://localhost:8080
```

### 3. Access the frontend (UI)
Open in a browser:
```
http://localhost:8080/html/index.html
```

The frontend will communicate with the backend at `http://localhost:8080`.

---

## API Endpoints (for testing with curl/Postman)

### Health Check
```bash
curl http://localhost:8080/hello
```

### Add Book
```bash
curl -X POST http://localhost:8080/books   -H "Content-Type: application/json"   -d '{"title":"Atomic Habits","author":"James Clear"}'
```

### Add Member
```bash
curl -X POST http://localhost:8080/members   -H "Content-Type: application/json"   -d '{"name":"Alice","type":"Student"}'
```

### Borrow Book
```bash
curl -X POST http://localhost:8080/borrow   -H "Content-Type: application/json"   -d '{"memberName":"Alice","bookTitle":"Atomic Habits"}'
```

### Return Book
```bash
curl -X POST http://localhost:8080/return   -H "Content-Type: application/json"   -d '{"memberName":"Alice","bookTitle":"Atomic Habits"}'
```

### List Books
```bash
curl http://localhost:8080/books
```

### List Members
```bash
curl http://localhost:8080/members
```

### List Transactions
```bash
curl http://localhost:8080/transactions
```

---

## Notes for Instructors
1. Run the backend using Maven:
   ```bash
   cd java-prog-group-3
   mvn clean compile exec:java
   ```
2. Open the frontend at:
   ```
   http://localhost:8080/html/index.html
   ```
3. Test features through the web interface or API endpoints.
4. Data persistence is handled in the `data/` JSON files.

---

## Author
Developed by **BSIT-2 | Block-3 | Group 3** — Java Programming Project  

---

## Acknowledgement
Special thanks to **ChatGPT 5.0**
