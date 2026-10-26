-- Online Examination System Database Schema and Initial Data
-- Compatible with MySQL 8.0+

CREATE DATABASE IF NOT EXISTS online_exam_db;
USE online_exam_db;

-- Drop tables in proper dependency order
DROP TABLE IF EXISTS results;
DROP TABLE IF EXISTS questions;
DROP TABLE IF EXISTS exams;
DROP TABLE IF EXISTS users;

-- 1. Users Table
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role ENUM('ADMIN', 'STUDENT') NOT NULL DEFAULT 'STUDENT'
);

-- 2. Exams Table
CREATE TABLE exams (
    exam_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    duration_minutes INT NOT NULL,
    total_marks INT NOT NULL
);

-- 3. Questions Table
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

-- 4. Results Table
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

-- Default Admin Account (Students must dynamically register before logging in)
INSERT INTO users (user_id, name, email, password, role) VALUES
(1, 'System Administrator', 'admin@exam.com', 'admin123', 'ADMIN');

-- Sample Exam: Java Basics
INSERT INTO exams (exam_id, title, duration_minutes, total_marks) VALUES
(1, 'Java Programming Fundamentals', 10, 25);

-- 5 Sample Questions for Exam 1 (5 marks each = 25 marks total)
INSERT INTO questions (question_id, exam_id, question_text, option_a, option_b, option_c, option_d, correct_option, marks) VALUES
(1, 1, 'What is the default value of a boolean variable in Java?', 'true', 'false', 'null', '0', 'B', 5),
(2, 1, 'Which keyword is used to inherit a class in Java?', 'implements', 'inherits', 'extends', 'instanceof', 'C', 5),
(3, 1, 'Which component of Java is responsible for running the compiled bytecode?', 'JDK', 'JVM', 'JRE', 'JIT', 'B', 5),
(4, 1, 'Which of the following is NOT a primitive data type in Java?', 'int', 'float', 'String', 'char', 'C', 5),
(5, 1, 'Which exception is thrown when an application attempts to divide a number by zero?', 'NullPointerException', 'ArithmeticException', 'NumberFormatException', 'ArrayIndexOutOfBoundsException', 'B', 5);
