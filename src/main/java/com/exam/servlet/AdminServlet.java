package com.exam.servlet;

import com.exam.dao.ExamDAO;
import com.exam.dao.QuestionDAO;
import com.exam.dao.ResultDAO;
import com.exam.dao.UserDAO;
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
import java.util.List;

/**
 * Controller servlet for Admin operations:
 * - Managing exams (list, add, delete)
 * - Adding MCQ questions to exams
 * - Viewing all student results
 */
@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ExamDAO examDAO;
    private QuestionDAO questionDAO;
    private ResultDAO resultDAO;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        examDAO = new ExamDAO();
        questionDAO = new QuestionDAO();
        resultDAO = new ResultDAO();
        userDAO = new UserDAO();
    }

    /**
     * Helper to verify if the requesting client is an authenticated ADMIN.
     */
    private boolean checkAdminAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect("login.jsp?error=unauthorized");
            return false;
        }
        User user = (User) session.getAttribute("currentUser");
        if (!user.isAdmin()) {
            response.sendRedirect("login.jsp?error=unauthorized");
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!checkAdminAuth(request, response)) return;

        String action = request.getParameter("action");
        if (action == null || action.isEmpty()) {
            action = "dashboard";
        }

        switch (action) {
            case "dashboard":
                showDashboard(request, response);
                break;

            case "addExam":
                request.getRequestDispatcher("add-exam.jsp").forward(request, response);
                break;

            case "addQuestion":
                showAddQuestionForm(request, response);
                break;

            case "deleteExam":
                deleteExam(request, response);
                break;

            case "allResults":
                showAllResults(request, response);
                break;

            default:
                showDashboard(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!checkAdminAuth(request, response)) return;

        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("admin");
            return;
        }

        switch (action) {
            case "addExam":
                handleAddExam(request, response);
                break;

            case "addQuestion":
                handleAddQuestion(request, response);
                break;

            default:
                response.sendRedirect("admin");
                break;
        }
    }

    private void showDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Exam> exams = examDAO.getAllExams();
        List<User> students = userDAO.getAllStudents();
        request.setAttribute("exams", exams);
        request.setAttribute("students", students);
        request.getRequestDispatcher("admin-dashboard.jsp").forward(request, response);
    }

    private void showAddQuestionForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String examIdStr = request.getParameter("examId");
        if (examIdStr == null || examIdStr.trim().isEmpty()) {
            response.sendRedirect("admin");
            return;
        }

        try {
            int examId = Integer.parseInt(examIdStr.trim());
            Exam exam = examDAO.getExamById(examId);
            if (exam == null) {
                response.sendRedirect("admin?error=examNotFound");
                return;
            }
            request.setAttribute("exam", exam);
            request.getRequestDispatcher("add-question.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("admin");
        }
    }

    private void handleAddExam(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String title = request.getParameter("title");
        String durationStr = request.getParameter("duration");
        String totalMarksStr = request.getParameter("totalMarks");

        if (title == null || title.trim().isEmpty() ||
            durationStr == null || durationStr.trim().isEmpty() ||
            totalMarksStr == null || totalMarksStr.trim().isEmpty()) {
            request.setAttribute("errorMessage", "All fields are required to create an exam.");
            request.getRequestDispatcher("add-exam.jsp").forward(request, response);
            return;
        }

        try {
            int duration = Integer.parseInt(durationStr.trim());
            int totalMarks = Integer.parseInt(totalMarksStr.trim());

            if (duration <= 0 || totalMarks <= 0) {
                request.setAttribute("errorMessage", "Duration and Total Marks must be positive numbers.");
                request.getRequestDispatcher("add-exam.jsp").forward(request, response);
                return;
            }

            Exam exam = new Exam(title.trim(), duration, totalMarks);
            int examId = examDAO.addExam(exam);

            if (examId > 0) {
                response.sendRedirect("admin?success=examAdded");
            } else {
                request.setAttribute("errorMessage", "Could not create exam due to a database error.");
                request.getRequestDispatcher("add-exam.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid numeric values entered.");
            request.getRequestDispatcher("add-exam.jsp").forward(request, response);
        }
    }

    private void handleAddQuestion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String examIdStr = request.getParameter("examId");
        String questionText = request.getParameter("questionText");
        String optionA = request.getParameter("optionA");
        String optionB = request.getParameter("optionB");
        String optionC = request.getParameter("optionC");
        String optionD = request.getParameter("optionD");
        String correctOption = request.getParameter("correctOption");
        String marksStr = request.getParameter("marks");

        if (examIdStr == null || questionText == null || optionA == null || optionB == null ||
            optionC == null || optionD == null || correctOption == null || marksStr == null ||
            questionText.trim().isEmpty() || optionA.trim().isEmpty() || optionB.trim().isEmpty() ||
            optionC.trim().isEmpty() || optionD.trim().isEmpty() || correctOption.trim().isEmpty()) {

            request.setAttribute("errorMessage", "All question fields are required.");
            showAddQuestionForm(request, response);
            return;
        }

        try {
            int examId = Integer.parseInt(examIdStr.trim());
            int marks = Integer.parseInt(marksStr.trim());
            if (marks <= 0) marks = 1;

            Question question = new Question(
                    examId,
                    questionText.trim(),
                    optionA.trim(),
                    optionB.trim(),
                    optionC.trim(),
                    optionD.trim(),
                    correctOption.trim().toUpperCase(),
                    marks
            );

            boolean added = questionDAO.addQuestion(question);
            if (added) {
                // Update exam total marks to match sum of questions
                int totalMarks = questionDAO.calculateTotalMarksForExam(examId);
                examDAO.updateExamTotalMarks(examId, totalMarks);

                response.sendRedirect("admin?success=questionAdded");
            } else {
                request.setAttribute("errorMessage", "Failed to add question to database.");
                showAddQuestionForm(request, response);
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("admin");
        }
    }

    private void deleteExam(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String examIdStr = request.getParameter("examId");
        if (examIdStr != null && !examIdStr.trim().isEmpty()) {
            try {
                int examId = Integer.parseInt(examIdStr.trim());
                examDAO.deleteExam(examId);
            } catch (NumberFormatException e) {
                // Ignore invalid ID
            }
        }
        response.sendRedirect("admin?success=examDeleted");
    }

    private void showAllResults(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Result> results = resultDAO.getAllResults();
        request.setAttribute("results", results);
        request.getRequestDispatcher("all-results.jsp").forward(request, response);
    }
}
