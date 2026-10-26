package com.exam.servlet;

import com.exam.dao.ResultDAO;
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
 * Controller servlet for Result operations:
 * - Displaying immediate scorecard after exam submission
 * - Student "My Results" history
 * - Admin "All Results" report
 */
@WebServlet("/result")
public class ResultServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ResultDAO resultDAO;

    @Override
    public void init() throws ServletException {
        resultDAO = new ResultDAO();
    }

    /**
     * Helper to verify if the user is authenticated.
     */
    private User getAuthenticatedUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect("login.jsp?error=unauthorized");
            return null;
        }
        return (User) session.getAttribute("currentUser");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getAuthenticatedUser(request, response);
        if (user == null) return;

        String action = request.getParameter("action");
        String idParam = request.getParameter("id");

        if (idParam != null && !idParam.trim().isEmpty()) {
            showSingleResult(request, response, user, Integer.parseInt(idParam.trim()));
            return;
        }

        if ("myResults".equals(action)) {
            showMyResults(request, response, user);
        } else if ("allResults".equals(action)) {
            if (!user.isAdmin()) {
                response.sendRedirect("login.jsp?error=unauthorized");
                return;
            }
            showAllResults(request, response);
        } else {
            if (user.isAdmin()) {
                response.sendRedirect("admin");
            } else {
                response.sendRedirect("exam");
            }
        }
    }

    private void showSingleResult(HttpServletRequest request, HttpServletResponse response, User user, int resultId)
            throws ServletException, IOException {
        Result result = resultDAO.getResultById(resultId);

        if (result == null) {
            response.sendRedirect(user.isAdmin() ? "admin" : "exam");
            return;
        }

        // Student can only view their own results; Admin can view any
        if (user.isStudent() && result.getUserId() != user.getUserId()) {
            response.sendRedirect("exam?action=list&error=unauthorized");
            return;
        }

        request.setAttribute("result", result);
        request.getRequestDispatcher("result.jsp").forward(request, response);
    }

    private void showMyResults(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        List<Result> results = resultDAO.getResultsByUserId(user.getUserId());
        request.setAttribute("results", results);
        request.getRequestDispatcher("my-results.jsp").forward(request, response);
    }

    private void showAllResults(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Result> results = resultDAO.getAllResults();
        request.setAttribute("results", results);
        request.getRequestDispatcher("all-results.jsp").forward(request, response);
    }
}
