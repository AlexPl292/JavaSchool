package com.tsystems.javaschool.controllers;

import com.google.gson.JsonObject;
import com.tsystems.javaschool.db.entities.Staff;
import com.tsystems.javaschool.db.entities.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by alex on 10.09.16.
 */
@WebServlet("/login")
public class LoginController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        JsonObject json = new JsonObject();


        User user = User.login(email, password);
        if (user != null) {
            HttpSession session = request.getSession();
            if (user instanceof Staff)
                session.setAttribute("user", "manager");
            else
                session.setAttribute("user", "customer");
            json.addProperty("success", true);
        } else {
            json.addProperty("success", false);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(json.toString());
            out.flush();
        }
    }
}
