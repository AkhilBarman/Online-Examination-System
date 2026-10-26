package com.exam.servlet;

import com.exam.dao.UserDAO;
import com.exam.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller servlet for handling user authentication (Login).
 * Redirects users based on their role: ADMIN to admin dashboard, STUDENT to student dashboard.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // If already logged in, redirect to appropriate home
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("currentUser") != null) {
            User user = (User) session.getAttribute("currentUser");
            if (user.isAdmin()) {
                response.sendRedirect("admin");
            } else {
                response.sendRedirect("exam");
            }
            return;
        }
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Basic validation for empty fields
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Email and Password cannot be empty.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        User user = userDAO.validateUser(email.trim(), password.trim());

        if (user != null) {
            // Authentication successful: create session
            HttpSession session = request.getSession(true);
            session.setAttribute("currentUser", user);

            // Role-based redirection
            if (user.isAdmin()) {
                response.sendRedirect("admin");
            } else {
                response.sendRedirect("exam");
            }
        } else {
            // Authentication failed
            if (!userDAO.isEmailExists(email.trim())) {
                request.setAttribute("errorMessage", "No account found with this email. Please register as a student first.");
            } else {
                request.setAttribute("errorMessage", "Incorrect password. Please try again.");
            }
            request.setAttribute("email", email);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
