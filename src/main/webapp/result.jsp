<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Exam Result - Online Examination System</title>
</head>
<body>
    <h2>Examination Result Scorecard</h2>
    <hr>

    <table border="1" cellpadding="8" cellspacing="0">
        <tr>
            <td><strong>Exam Title</strong></td>
            <td><c:out value="${result.examTitle}"/></td>
        </tr>
        <tr>
            <td><strong>Student Name</strong></td>
            <td><c:out value="${result.userName}"/></td>
        </tr>
        <tr>
            <td><strong>Score Obtained</strong></td>
            <td>${result.score}</td>
        </tr>
        <tr>
            <td><strong>Total Marks</strong></td>
            <td>${result.totalMarks}</td>
        </tr>
        <tr>
            <td><strong>Percentage</strong></td>
            <td>${result.percentage}%</td>
        </tr>
        <tr>
            <td><strong>Status</strong></td>
            <td>
                <strong>${result.status}</strong> 
                (Benchmark: 40% or more to PASS)
            </td>
        </tr>
        <tr>
            <td><strong>Submission Date & Time</strong></td>
            <td>${result.takenOn}</td>
        </tr>
    </table>

    <hr>
    <p>
        <c:choose>
            <c:when test="${sessionScope.currentUser.admin}">
                <a href="admin?action=dashboard">Back to Admin Dashboard</a> | 
                <a href="result?action=allResults">View All Results</a>
            </c:when>
            <c:otherwise>
                <a href="exam?action=list">Back to Student Dashboard</a> | 
                <a href="result?action=myResults">View My Past Results</a>
            </c:otherwise>
        </c:choose>
    </p>
</body>
</html>
