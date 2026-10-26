<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>All Results - Online Examination System</title>
</head>
<body>
    <h2>All Students' Examination Results</h2>
    <p><a href="admin?action=dashboard">Back to Admin Dashboard</a> | <a href="logout">Logout</a></p>
    <hr>

    <c:choose>
        <c:when test="${not empty results}">
            <table border="1" cellpadding="6" cellspacing="0">
                <thead>
                    <tr>
                        <th>Result ID</th>
                        <th>Student Name</th>
                        <th>Student Email</th>
                        <th>Exam Title</th>
                        <th>Score</th>
                        <th>Total Marks</th>
                        <th>Percentage</th>
                        <th>Status</th>
                        <th>Date Taken</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="res" items="${results}">
                        <tr>
                            <td>${res.resultId}</td>
                            <td><c:out value="${res.userName}"/></td>
                            <td><c:out value="${res.userEmail}"/></td>
                            <td><c:out value="${res.examTitle}"/></td>
                            <td>${res.score}</td>
                            <td>${res.totalMarks}</td>
                            <td>${res.percentage}%</td>
                            <td><strong>${res.status}</strong></td>
                            <td>${res.takenOn}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <p>No student results recorded yet.</p>
        </c:otherwise>
    </c:choose>
</body>
</html>
