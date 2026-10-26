<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add Question - Online Examination System</title>
</head>
<body>
    <h2>Add MCQ Question</h2>
    <p>Target Exam: <strong><c:out value="${exam.title}"/></strong> (Exam ID: ${exam.examId})</p>
    <hr>

    <c:if test="${not empty errorMessage}">
        <p><strong>Error: ${errorMessage}</strong></p>
    </c:if>

    <form action="admin" method="post">
        <input type="hidden" name="action" value="addQuestion">
        <input type="hidden" name="examId" value="${exam.examId}">

        <table>
            <tr>
                <td valign="top">Question Text:</td>
                <td><textarea name="questionText" rows="4" cols="50" required></textarea></td>
            </tr>
            <tr>
                <td>Option A:</td>
                <td><input type="text" name="optionA" size="50" required></td>
            </tr>
            <tr>
                <td>Option B:</td>
                <td><input type="text" name="optionB" size="50" required></td>
            </tr>
            <tr>
                <td>Option C:</td>
                <td><input type="text" name="optionC" size="50" required></td>
            </tr>
            <tr>
                <td>Option D:</td>
                <td><input type="text" name="optionD" size="50" required></td>
            </tr>
            <tr>
                <td>Correct Option:</td>
                <td>
                    <select name="correctOption" required>
                        <option value="A">Option A</option>
                        <option value="B">Option B</option>
                        <option value="C">Option C</option>
                        <option value="D">Option D</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>Marks for Question:</td>
                <td><input type="number" name="marks" min="1" max="100" value="5" required></td>
            </tr>
            <tr>
                <td></td>
                <td><input type="submit" value="Save Question"></td>
            </tr>
        </table>
    </form>

    <hr>
    <p><a href="admin?action=dashboard">Back to Admin Dashboard</a></p>
</body>
</html>
