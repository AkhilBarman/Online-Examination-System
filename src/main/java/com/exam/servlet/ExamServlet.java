package com.exam.servlet;

import com.exam.dao.ExamDAO;
import com.exam.dao.QuestionDAO;
import com.exam.dao.ResultDAO;
import com.exam.model.Exam;
import com.exam.model.Question;
import com.exam.model.Result;
import com.exam.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Controller servlet for Student Exam operations:
 * - Listing available exams on student dashboard
 * - Starting an exam with session timer tracking
 * - Submitting answers, server-side timer enforcement, scoring, and saving results
 */
@WebServlet("/exam")
public class ExamServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ExamDAO examDAO;
    private QuestionDAO questionDAO;
    private ResultDAO resultDAO;

    @Override
    public void init() throws ServletException {
        examDAO = new ExamDAO();
        questionDAO = new QuestionDAO();
        resultDAO = new ResultDAO();
    }

    /**
     * Helper to verify if the requesting client is an authenticated STUDENT.
     */
    private boolean checkStudentAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect("login.jsp?error=unauthorized");
            return false;
        }
        User user = (User) session.getAttribute("currentUser");
        if (!user.isStudent()) {
            response.sendRedirect("login.jsp?error=unauthorized");
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!checkStudentAuth(request, response)) return;

        String action = request.getParameter("action");
        if (action == null || action.isEmpty()) {
            action = "list";
        }

        switch (action) {
            case "list":
                showStudentDashboard(request, response);
                break;

            case "take":
                startExam(request, response);
                break;

            default:
                showStudentDashboard(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!checkStudentAuth(request, response)) return;

        String action = request.getParameter("action");
        if ("submit".equals(action)) {
            submitExam(request, response);
        } else {
            response.sendRedirect("exam");
        }
    }

    private void showStudentDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        List<Exam> exams = examDAO.getAllExams();
        Set<Integer> attemptedExamIds = new HashSet<>();

        // Identify which exams this student has already taken
        for (Exam exam : exams) {
            if (resultDAO.hasStudentAttempted(currentUser.getUserId(), exam.getExamId())) {
                attemptedExamIds.add(exam.getExamId());
            }
        }

        request.setAttribute("exams", exams);
        request.setAttribute("attemptedExamIds", attemptedExamIds);
        request.getRequestDispatcher("student-dashboard.jsp").forward(request, response);
    }

    private void startExam(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        String examIdStr = request.getParameter("examId");
        if (examIdStr == null || examIdStr.trim().isEmpty()) {
            response.sendRedirect("exam?action=list");
            return;
        }

        try {
            int examId = Integer.parseInt(examIdStr.trim());

            // Enforce Rule: Prevent re-attempting the same exam twice
            if (resultDAO.hasStudentAttempted(currentUser.getUserId(), examId)) {
                response.sendRedirect("exam?action=list&error=alreadyAttempted");
                return;
            }

            Exam exam = examDAO.getExamById(examId);
            if (exam == null) {
                response.sendRedirect("exam?action=list&error=examNotFound");
                return;
            }

            List<Question> questions = questionDAO.getQuestionsByExamId(examId);
            if (questions.isEmpty()) {
                response.sendRedirect("exam?action=list&error=noQuestions");
                return;
            }

            // Store exam start time in session for server-side time verification
            session.setAttribute("exam_start_" + examId, System.currentTimeMillis());

            request.setAttribute("exam", exam);
            request.setAttribute("questions", questions);
            request.getRequestDispatcher("take-exam.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("exam?action=list");
        }
    }

    private void submitExam(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        String examIdStr = request.getParameter("examId");
        if (examIdStr == null || examIdStr.trim().isEmpty()) {
            response.sendRedirect("exam?action=list");
            return;
        }

        try {
            int examId = Integer.parseInt(examIdStr.trim());

            // Prevent duplicate submission check
            if (resultDAO.hasStudentAttempted(currentUser.getUserId(), examId)) {
                response.sendRedirect("exam?action=list&error=alreadyAttempted");
                return;
            }

            Exam exam = examDAO.getExamById(examId);
            if (exam == null) {
                response.sendRedirect("exam?action=list&error=examNotFound");
                return;
            }

            // Server-side time limit enforcement
            Long startTime = (Long) session.getAttribute("exam_start_" + examId);
            long durationMillis = (long) exam.getDurationMinutes() * 60 * 1000L;
            long gracePeriodMillis = 60 * 1000L; // 60 seconds grace period for network delays

            if (startTime != null && (System.currentTimeMillis() - startTime) > (durationMillis + gracePeriodMillis)) {
                session.removeAttribute("exam_start_" + examId);
                response.sendRedirect("exam?action=list&error=timeExceeded");
                return;
            }

            // Calculate score by evaluating student's answers
            List<Question> questions = questionDAO.getQuestionsByExamId(examId);
            int score = 0;
            int totalMarks = 0;

            for (Question q : questions) {
                totalMarks += q.getMarks();
                String selectedOption = request.getParameter("q_" + q.getQuestionId());
                if (selectedOption != null && selectedOption.trim().equalsIgnoreCase(q.getCorrectOption().trim())) {
                    score += q.getMarks();
                }
            }

            // If exam has configured total marks, ensure alignment
            if (totalMarks == 0) {
                totalMarks = exam.getTotalMarks();
            }

            // Persist the result
            Result result = new Result(currentUser.getUserId(), examId, score, totalMarks);
            int resultId = resultDAO.addResult(result);

            // Clear session start time
            session.removeAttribute("exam_start_" + examId);

            if (resultId > 0) {
                response.sendRedirect("result?id=" + resultId);
            } else {
                response.sendRedirect("exam?action=list&error=saveFailed");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect("exam?action=list");
        }
    }
}
