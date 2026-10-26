package com.exam.servlet;

import com.exam.dao.UserDAO;
import com.exam.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller servlet for handling Student Registration.
 * Validates form inputs, ensures unique email, and creates new student accounts.
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Basic validation: ensure all fields are provided
        if (name == null || name.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {

            request.setAttribute("errorMessage", "All fields are required. Please fill in all details.");
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        name = name.trim();
        email = email.trim();
        password = password.trim();

        // Check if email already exists
        if (userDAO.isEmailExists(email)) {
            request.setAttribute("errorMessage", "Email is already registered. Please login or use a different email.");
            request.setAttribute("name", name);
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // Register new student user
        User student = new User(name, email, password, "STUDENT");
        boolean isSuccess = userDAO.registerUser(student);

        if (isSuccess) {
            String encodedName = java.net.URLEncoder.encode(name, "UTF-8");
            String encodedEmail = java.net.URLEncoder.encode(email, "UTF-8");
            response.sendRedirect("login.jsp?registered=true&registeredName=" + encodedName + "&registeredEmail=" + encodedEmail);
        } else {
            request.setAttribute("errorMessage", "Registration failed due to a server error. Please try again.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}
