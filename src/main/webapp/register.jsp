<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Registration - Online Examination System</title>
</head>
<body>
    <h2>Student Registration</h2>
    <hr>

    <c:if test="${not empty errorMessage}">
        <p><strong>Error: ${errorMessage}</strong></p>
    </c:if>

    <form action="register" method="post">
        <table>
            <tr>
                <td>Full Name:</td>
                <td><input type="text" name="name" value="<c:out value='${name}'/>" required></td>
            </tr>
            <tr>
                <td>Email:</td>
                <td><input type="email" name="email" value="<c:out value='${email}'/>" required></td>
            </tr>
            <tr>
                <td>Password:</td>
                <td><input type="password" name="password" required></td>
            </tr>
            <tr>
                <td></td>
                <td><input type="submit" value="Register"></td>
            </tr>
        </table>
    </form>

    <hr>
    <p>Already have an account? <a href="login.jsp">Login here</a></p>
    <p><a href="index.jsp">Back to Home</a></p>
</body>
</html>
