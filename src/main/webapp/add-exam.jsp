<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add Exam - Online Examination System</title>
</head>
<body>
    <h2>Add New Examination</h2>
    <hr>

    <c:if test="${not empty errorMessage}">
        <p><strong>Error: ${errorMessage}</strong></p>
    </c:if>

    <form action="admin" method="post">
        <input type="hidden" name="action" value="addExam">
        <table>
            <tr>
                <td>Exam Title:</td>
                <td><input type="text" name="title" size="40" required></td>
            </tr>
            <tr>
                <td>Duration (in Minutes):</td>
                <td><input type="number" name="duration" min="1" max="300" value="15" required></td>
            </tr>
            <tr>
                <td>Initial Total Marks:</td>
                <td><input type="number" name="totalMarks" min="1" max="1000" value="25" required></td>
            </tr>
            <tr>
                <td></td>
                <td><input type="submit" value="Create Exam"></td>
            </tr>
        </table>
    </form>

    <hr>
    <p><a href="admin?action=dashboard">Back to Admin Dashboard</a></p>
</body>
</html>
