# Online Examination System 🎓

A full-stack Java web application built using **JSP, Servlets, JDBC, and MySQL** following the **MVC (Model-View-Controller)** design pattern. It provides a complete online examination platform with role-based access control, dynamic student registration, timed MCQ assessments, auto-evaluation, and result tracking.

---

## 📌 Repository Description
> **Short Description:** A complete, lightweight Java web application for conducting online multiple-choice examinations featuring JSP, Servlets, JDBC, MySQL, role-based authentication, and a client/server-side timed exam engine.

---

## 🚀 Key Features

### 👤 Student Features
- **Dynamic Registration & Authentication:** Students register with their own full name, email, and password before logging in.
- **Dynamic Profile Display:** The student's registered name is showcased across all pages (dashboard, exam screen, scorecard, and history).
- **Available Exams:** Browse active examinations with duration and mark specifications.
- **Timed MCQ Engine:**
  - One-page MCQ assessment interface.
  - Client-side countdown timer in plain JavaScript that automatically submits when time expires.
  - Server-side start-time verification to prevent delayed submissions.
- **Single Attempt Rule:** Prevents students from attempting the same exam more than once.
- **Instant Evaluation & Scorecard:** Automatic grading on submit showing marks obtained, total marks, percentage, and **PASS / FAIL** status (40% benchmark).
- **My Results:** View complete history of all completed exams with dates and scores.

### 🛡️ Admin Features
- **Admin Dashboard:** Overview of all created exams and all dynamically registered students.
- **Exam Management:** Create new exams (title, duration in minutes, total marks) and delete existing ones.
- **Question Management:** Add multiple-choice questions (question text, 4 options, correct answer, marks) to any exam.
- **All Student Results:** View consolidated scorecards for all student attempts across all exams.

---

## 🛠️ Technology Stack

| Layer | Technology |
|---|---|
| **Language** | Java 8+ (Tested on OpenJDK 17) |
| **Architecture** | MVC (Model-View-Controller) |
| **Backend** | Java Servlets (`javax.servlet` API 4.0.1) |
| **View (UI)** | JSP (JavaServer Pages), JSTL, Plain HTML (No CSS, No external JS frameworks) |
| **Database** | MySQL 8.0+ via plain JDBC (`PreparedStatement`, `try-with-resources`) |
| **Build Tool** | Apache Maven (`war` packaging) |
| **Server** | Apache Tomcat 9 (Embedded CLI runner + Standard WAR deployment) |

---

## 📂 Project Structure

```
Online-Examination-System/
├── pom.xml                                    # Maven configuration & dependencies
├── schema.sql                                 # MySQL database tables and seed data
├── run.bat                                    # 1-Click Windows Command Prompt runner
├── run.ps1                                    # 1-Click Windows PowerShell runner
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── exam/
        │           ├── Main.java              # Embedded Tomcat 9 CLI runner
        │           ├── dao/                   # Database Access Layer (JDBC)
        │           │   ├── DBConnection.java  # Central DB connection manager
        │           │   ├── ExamDAO.java
        │           │   ├── QuestionDAO.java
        │           │   ├── ResultDAO.java
        │           │   └── UserDAO.java
        │           ├── model/                 # Plain Old Java Objects (POJOs)
        │           │   ├── Exam.java
        │           │   ├── Question.java
        │           │   ├── Result.java
        │           │   └── User.java
        │           └── servlet/               # MVC Controllers
        │               ├── AdminServlet.java
        │               ├── ExamServlet.java
        │               ├── LoginServlet.java
        │               ├── LogoutServlet.java
        │               ├── RegisterServlet.java
        │               └── ResultServlet.java
        └── webapp/                            # JSP Views
            ├── WEB-INF/
            │   └── web.xml                    # Deployment descriptor
            ├── add-exam.jsp
            ├── add-question.jsp
            ├── admin-dashboard.jsp
            ├── all-results.jsp
            ├── index.jsp
            ├── login.jsp
            ├── my-results.jsp
            ├── register.jsp
            ├── result.jsp
            ├── student-dashboard.jsp
            └── take-exam.jsp
```

---

## ⚙️ Setup & Installation

### 1. Database Configuration
1. Open MySQL Workbench or MySQL CLI:
   ```sql
   source /path/to/schema.sql;
   ```
2. Verify / update credentials in `src/main/java/com/exam/dao/DBConnection.java`:
   ```java
   private static final String DB_URL = "jdbc:mysql://localhost:3306/online_exam_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
   private static final String DB_USER = "root";
   private static final String DB_PASSWORD = "your_mysql_password";
   ```

### 2. Run Directly in Terminal (No IDE Required)
Run the provided script:
```powershell
.\run.ps1
```
*(Or in Command Prompt: `run.bat`)*

Or with Maven:
```bash
mvn compile exec:java
```

Once started, open:
👉 **`http://localhost:8080/`**

### 3. Run in IDE (Eclipse / IntelliJ)
- **Eclipse:** `File` -> `Import...` -> `Existing Maven Projects` -> Select project folder -> Run on Tomcat 9.
- **IntelliJ:** `Open` -> Select `pom.xml` -> Configure Tomcat Server Run Configuration with `OnlineExamSystem:war exploded` artifact.

---

## 🔑 Default Credentials

- **Admin Account:**
  - **Email:** `admin@exam.com`
  - **Password:** `admin123`
- **Student Access:**
  - Dynamic registration via `/register.jsp`. Each registered student logs in with their chosen credentials.

---

## 📄 License
This project is developed for educational and academic mini-project purposes.
