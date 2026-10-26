<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Dashboard - Online Examination System</title>
</head>
<body>
    <h2>Student Dashboard</h2>
    <p>Welcome, <strong><c:out value="${sessionScope.currentUser.name}"/></strong> (<c:out value="${sessionScope.currentUser.email}"/>) | 
       <a href="result?action=myResults">My Results</a> | 
       <a href="logout">Logout</a>
    </p>
    <hr>

    <c:if test="${param.error == 'alreadyAttempted'}">
        <p><strong>Notice: You have already attempted this exam. Each exam can only be taken once.</strong></p>
    </c:if>
    <c:if test="${param.error == 'timeExceeded'}">
        <p><strong>Error: Exam duration exceeded. Submission was rejected due to timeout.</strong></p>
    </c:if>
    <c:if test="${param.error == 'noQuestions'}">
        <p><strong>Notice: This exam has no questions available yet. Please check back later.</strong></p>
    </c:if>
    <c:if test="${param.error == 'examNotFound'}">
        <p><strong>Error: The requested exam was not found.</strong></p>
    </c:if>

    <h3>Available Exams</h3>
    <c:choose>
        <c:when test="${not empty exams}">
            <table border="1" cellpadding="5" cellspacing="0">
                <thead>
                    <tr>
                        <th>Exam ID</th>
                        <th>Exam Title</th>
                        <th>Duration (Minutes)</th>
                        <th>Total Marks</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="exam" items="${exams}">
                        <tr>
                            <td>${exam.examId}</td>
                            <td><c:out value="${exam.title}"/></td>
                            <td>${exam.durationMinutes} min</td>
                            <td>${exam.totalMarks}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${attemptedExamIds.contains(exam.examId)}">
                                        Already Attempted
                                    </c:when>
                                    <c:otherwise>
                                        <a href="exam?action=take&examId=${exam.examId}">Take Exam</a>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <p>No exams are available at this moment.</p>
        </c:otherwise>
    </c:choose>
</body>
</html>
