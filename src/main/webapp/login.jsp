<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - Online Examination System</title>
</head>
<body>
    <h2>User Login</h2>
    <hr>

    <c:if test="${not empty errorMessage}">
        <p><strong>Error: ${errorMessage}</strong></p>
    </c:if>

    <c:if test="${param.registered == 'true'}">
        <p><strong>Success: Registration successful for <c:out value="${not empty param.registeredName ? param.registeredName : 'your account'}"/>! Please enter your password to log in.</strong></p>
    </c:if>

    <c:if test="${param.logout == 'true'}">
        <p><strong>Notification: You have been logged out successfully.</strong></p>
    </c:if>

    <c:if test="${param.error == 'unauthorized'}">
        <p><strong>Error: Session expired or unauthorized access. Please log in.</strong></p>
    </c:if>

    <form action="login" method="post">
        <table>
            <tr>
                <td>Email:</td>
                <td><input type="email" name="email" value="<c:out value='${not empty param.registeredEmail ? param.registeredEmail : email}'/>" required></td>
            </tr>
            <tr>
                <td>Password:</td>
                <td><input type="password" name="password" required></td>
            </tr>
            <tr>
                <td></td>
                <td><input type="submit" value="Login"></td>
            </tr>
        </table>
    </form>

    <hr>
    <p>Don't have an account? <a href="register.jsp">Register as Student</a></p>
    <p><a href="index.jsp">Back to Home</a></p>
</body>
</html>
