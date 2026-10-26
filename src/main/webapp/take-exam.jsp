<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Take Exam - <c:out value="${exam.title}"/></title>
</head>
<body>
    <h2>Exam: <c:out value="${exam.title}"/></h2>
    <p>
        Duration: <strong>${exam.durationMinutes} Minutes</strong> | 
        Total Marks: <strong>${exam.totalMarks}</strong> | 
        Student: <strong><c:out value="${sessionScope.currentUser.name}"/></strong>
    </p>
    <p>Time Remaining: <strong id="timer">--:--</strong></p>
    <hr>

    <form id="examForm" action="exam" method="post">
        <input type="hidden" name="action" value="submit">
        <input type="hidden" name="examId" value="${exam.examId}">

        <c:forEach var="q" items="${questions}" varStatus="status">
            <p><strong>Question ${status.count}: <c:out value="${q.questionText}"/> (${q.marks} Marks)</strong></p>
            <p>
                <input type="radio" name="q_${q.questionId}" value="A" id="q_${q.questionId}_a">
                <label for="q_${q.questionId}_a">A) <c:out value="${q.optionA}"/></label><br>

                <input type="radio" name="q_${q.questionId}" value="B" id="q_${q.questionId}_b">
                <label for="q_${q.questionId}_b">B) <c:out value="${q.optionB}"/></label><br>

                <input type="radio" name="q_${q.questionId}" value="C" id="q_${q.questionId}_c">
                <label for="q_${q.questionId}_c">C) <c:out value="${q.optionC}"/></label><br>

                <input type="radio" name="q_${q.questionId}" value="D" id="q_${q.questionId}_d">
                <label for="q_${q.questionId}_d">D) <c:out value="${q.optionD}"/></label>
            </p>
            <hr>
        </c:forEach>

        <p>
            <input type="submit" value="Submit Exam" onclick="return confirm('Are you sure you want to finish and submit the exam?');">
        </p>
    </form>

    <!-- Plain JavaScript countdown timer with auto-submit on expiry -->
    <script>
        var totalSeconds = ${exam.durationMinutes} * 60;
        var timerDisplay = document.getElementById("timer");
        var form = document.getElementById("examForm");

        function updateCountdown() {
            var minutes = Math.floor(totalSeconds / 60);
            var seconds = totalSeconds % 60;
            if (seconds < 10) {
                seconds = "0" + seconds;
            }
            timerDisplay.innerText = minutes + ":" + seconds;

            if (totalSeconds <= 0) {
                clearInterval(timerInterval);
                alert("Time is up! Your exam is being submitted automatically.");
                form.submit();
            }
            totalSeconds--;
        }

        updateCountdown();
        var timerInterval = setInterval(updateCountdown, 1000);
    </script>
</body>
</html>
