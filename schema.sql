-- Online Examination System Database Schema and Seed Data
-- Compatible with MySQL 8.0+

CREATE DATABASE IF NOT EXISTS online_exam_db;
USE online_exam_db;

-- Drop tables in proper dependency order
DROP TABLE IF EXISTS results;
DROP TABLE IF EXISTS questions;
DROP TABLE IF EXISTS exams;
DROP TABLE IF EXISTS users;

-- 1. Users Table (Stores Admin and Registered Students)
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role ENUM('ADMIN', 'STUDENT') NOT NULL DEFAULT 'STUDENT'
);

-- 2. Exams Table (Stores Examinations)
CREATE TABLE exams (
    exam_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    duration_minutes INT NOT NULL,
    total_marks INT NOT NULL
);

-- 3. Questions Table (Stores Multiple Choice Questions for Exams)
CREATE TABLE questions (
    question_id INT AUTO_INCREMENT PRIMARY KEY,
    exam_id INT NOT NULL,
    question_text TEXT NOT NULL,
    option_a VARCHAR(255) NOT NULL,
    option_b VARCHAR(255) NOT NULL,
    option_c VARCHAR(255) NOT NULL,
    option_d VARCHAR(255) NOT NULL,
    correct_option CHAR(1) NOT NULL,
    marks INT NOT NULL DEFAULT 1,
    FOREIGN KEY (exam_id) REFERENCES exams(exam_id) ON DELETE CASCADE
);

-- 4. Results Table (Stores Student Exam Submissions & Scores)
CREATE TABLE results (
    result_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    exam_id INT NOT NULL,
    score INT NOT NULL,
    total_marks INT NOT NULL,
    taken_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (exam_id) REFERENCES exams(exam_id) ON DELETE CASCADE
);

-- =================================================================
-- Initial Setup Data
-- =================================================================

-- Default Admin Account (Students dynamically register via register.jsp)
INSERT INTO users (user_id, name, email, password, role) VALUES
(1, 'System Administrator', 'admin@exam.com', 'admin123', 'ADMIN');

-- =================================================================
-- 5 Active Examinations (Each with 5 Questions = 25 Marks)
-- =================================================================
INSERT INTO exams (exam_id, title, duration_minutes, total_marks) VALUES
(1, 'Java Programming Fundamentals', 10, 25),
(2, 'Object-Oriented Programming (OOP)', 15, 25),
(3, 'Database Management Systems & SQL', 15, 25),
(4, 'Data Structures & Algorithms', 15, 25),
(5, 'Web Technologies & Servlets', 10, 25);

-- -----------------------------------------------------------------
-- Exam 1: Java Programming Fundamentals (Questions 1 - 5)
-- -----------------------------------------------------------------
INSERT INTO questions (question_id, exam_id, question_text, option_a, option_b, option_c, option_d, correct_option, marks) VALUES
(1, 1, 'What is the default value of a boolean variable in Java?', 'true', 'false', 'null', '0', 'B', 5),
(2, 1, 'Which keyword is used to inherit a class in Java?', 'implements', 'inherits', 'extends', 'instanceof', 'C', 5),
(3, 1, 'Which component of Java is responsible for running the compiled bytecode?', 'JDK', 'JVM', 'JRE', 'JIT', 'B', 5),
(4, 1, 'Which of the following is NOT a primitive data type in Java?', 'int', 'float', 'String', 'char', 'C', 5),
(5, 1, 'Which exception is thrown when an application attempts to divide a number by zero?', 'NullPointerException', 'ArithmeticException', 'NumberFormatException', 'ArrayIndexOutOfBoundsException', 'B', 5);

-- -----------------------------------------------------------------
-- Exam 2: Object-Oriented Programming (OOP) (Questions 6 - 10)
-- -----------------------------------------------------------------
INSERT INTO questions (question_id, exam_id, question_text, option_a, option_b, option_c, option_d, correct_option, marks) VALUES
(6, 2, 'Which OOP concept refers to binding data and the methods operating on them into a single unit?', 'Abstraction', 'Encapsulation', 'Inheritance', 'Polymorphism', 'B', 5),
(7, 2, 'Which keyword in Java is used to prevent a class from being subclassed (inherited)?', 'static', 'abstract', 'final', 'private', 'C', 5),
(8, 2, 'Method overriding is an example of which type of polymorphism?', 'Compile-time polymorphism', 'Run-time dynamic polymorphism', 'Static polymorphism', 'Parametric polymorphism', 'B', 5),
(9, 2, 'Can an interface in Java 8 or later contain concrete methods with implementation?', 'Yes, using default or static keywords', 'No, never', 'Only if marked native', 'Only inside private nested classes', 'A', 5),
(10, 2, 'Which keyword is used inside a subclass constructor to explicitly call the immediate superclass constructor?', 'this', 'super', 'parent', 'base', 'B', 5);

-- -----------------------------------------------------------------
-- Exam 3: Database Management Systems & SQL (Questions 11 - 15)
-- -----------------------------------------------------------------
INSERT INTO questions (question_id, exam_id, question_text, option_a, option_b, option_c, option_d, correct_option, marks) VALUES
(11, 3, 'Which SQL clause is used to filter aggregated results produced by a GROUP BY clause?', 'WHERE', 'HAVING', 'ORDER BY', 'LIMIT', 'B', 5),
(12, 3, 'What does the letter "C" stand for in the ACID transaction properties of DBMS?', 'Concurrency', 'Consistency', 'Compatibility', 'Completeness', 'B', 5),
(13, 3, 'Which database constraint uniquely identifies each row in a database table without allowing NULL values?', 'NOT NULL', 'CHECK', 'PRIMARY KEY', 'FOREIGN KEY', 'C', 5),
(14, 3, 'Which SQL DDL command removes all rows from a table and resets auto-increment counters quickly?', 'DELETE', 'DROP', 'TRUNCATE', 'REMOVE', 'C', 5),
(15, 3, 'Which SQL JOIN returns all rows from the left table and matched rows from the right table?', 'INNER JOIN', 'LEFT JOIN', 'RIGHT JOIN', 'FULL OUTER JOIN', 'B', 5);

-- -----------------------------------------------------------------
-- Exam 4: Data Structures & Algorithms (Questions 16 - 20)
-- -----------------------------------------------------------------
INSERT INTO questions (question_id, exam_id, question_text, option_a, option_b, option_c, option_d, correct_option, marks) VALUES
(16, 4, 'Which linear data structure follows the Last-In-First-Out (LIFO) operating principle?', 'Queue', 'Stack', 'Tree', 'Graph', 'B', 5),
(17, 4, 'What is the average time complexity of searching an element in a balanced Binary Search Tree (BST)?', 'O(1)', 'O(log n)', 'O(n)', 'O(n log n)', 'B', 5),
(18, 4, 'Which data structure is typically utilized to implement Breadth-First Search (BFS) graph traversal?', 'Stack', 'Queue', 'Priority Queue', 'Binary Heap', 'B', 5),
(19, 4, 'Which sorting algorithm is based on the divide-and-conquer strategy with a guaranteed O(n log n) worst-case time complexity?', 'Bubble Sort', 'Insertion Sort', 'Merge Sort', 'Selection Sort', 'C', 5),
(20, 4, 'A connected acyclic graph containing N nodes and (N - 1) edges is known as a:', 'DAG', 'Tree', 'Bipartite Graph', 'Complete Graph', 'B', 5);

-- -----------------------------------------------------------------
-- Exam 5: Web Technologies & Servlets (Questions 21 - 25)
-- -----------------------------------------------------------------
INSERT INTO questions (question_id, exam_id, question_text, option_a, option_b, option_c, option_d, correct_option, marks) VALUES
(21, 5, 'Which HTTP request method is idempotent and primarily used for retrieving resources without side-effects?', 'POST', 'GET', 'PUT', 'DELETE', 'B', 5),
(22, 5, 'Which Servlet lifecycle method is invoked exactly once when the servlet container loads the servlet instance?', 'service()', 'init()', 'destroy()', 'doGet()', 'B', 5),
(23, 5, 'Which Servlet interface is used to maintain user conversation state across multiple HTTP requests?', 'ServletContext', 'HttpServletRequest', 'HttpSession', 'ServletConfig', 'C', 5),
(24, 5, 'What is the standard name of the web application deployment descriptor configuration file in Java EE?', 'context.xml', 'web.xml', 'server.xml', 'application.xml', 'B', 5),
(25, 5, 'In the Model-View-Controller (MVC) architecture of Java web applications, which layer is handled by Servlets?', 'Model', 'View', 'Controller', 'Persistence', 'C', 5);
