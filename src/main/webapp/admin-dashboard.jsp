<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard - Online Examination System</title>
</head>
<body>
    <h2>Admin Dashboard</h2>
    <p>Logged in as: <strong><c:out value="${sessionScope.currentUser.name}"/></strong> (<c:out value="${sessionScope.currentUser.email}"/>) | <a href="logout">Logout</a></p>
    <hr>

    <c:if test="${param.success == 'examAdded'}">
        <p><strong>Success: New exam created successfully!</strong></p>
    </c:if>
    <c:if test="${param.success == 'questionAdded'}">
        <p><strong>Success: Question added and exam total marks updated!</strong></p>
    </c:if>
    <c:if test="${param.success == 'examDeleted'}">
        <p><strong>Success: Exam and its associated questions/results deleted successfully!</strong></p>
    </c:if>
    <c:if test="${param.error == 'examNotFound'}">
        <p><strong>Error: Exam not found.</strong></p>
    </c:if>

    <p>
        <a href="admin?action=addExam"><strong>+ Add New Exam</strong></a> | 
        <a href="result?action=allResults"><strong>View All Student Results</strong></a>
    </p>

    <h3>Manage Examinations</h3>
    <c:choose>
        <c:when test="${not empty exams}">
            <table border="1" cellpadding="5" cellspacing="0">
                <thead>
                    <tr>
                        <th>Exam ID</th>
                        <th>Exam Title</th>
                        <th>Duration (Minutes)</th>
                        <th>Total Marks</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="exam" items="${exams}">
                        <tr>
                            <td>${exam.examId}</td>
                            <td><c:out value="${exam.title}"/></td>
                            <td>${exam.durationMinutes}</td>
                            <td>${exam.totalMarks}</td>
                            <td>
                                <a href="admin?action=addQuestion&examId=${exam.examId}">Add Question</a> | 
                                <a href="admin?action=deleteExam&examId=${exam.examId}" onclick="return confirm('Are you sure you want to delete this exam?');">Delete Exam</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <p>No exams created yet. Click "Add New Exam" to create one.</p>
        </c:otherwise>
    </c:choose>

    <hr>
    <h3>Registered Students</h3>
    <c:choose>
        <c:when test="${not empty students}">
            <p>Total Registered Students: <strong>${students.size()}</strong></p>
            <table border="1" cellpadding="5" cellspacing="0">
                <thead>
                    <tr>
                        <th>Student ID</th>
                        <th>Student Name</th>
                        <th>Email Address</th>
                        <th>Account Type</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="s" items="${students}">
                        <tr>
                            <td>${s.userId}</td>
                            <td><strong><c:out value="${s.name}"/></strong></td>
                            <td><c:out value="${s.email}"/></td>
                            <td>${s.role}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <p>No students have registered yet. New student registrations will automatically appear here.</p>
        </c:otherwise>
    </c:choose>
</body>
</html>
