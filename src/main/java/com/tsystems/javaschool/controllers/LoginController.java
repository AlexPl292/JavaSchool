package com.tsystems.javaschool.controllers;

import com.google.gson.JsonObject;
import com.tsystems.javaschool.business.services.implementations.StaffServiceImpl;
import com.tsystems.javaschool.business.services.interfaces.StaffService;
import com.tsystems.javaschool.db.entities.Staff;

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
public class LoginController extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        StaffService staffService = StaffServiceImpl.getInstance();
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        JsonObject json = new JsonObject();

        if (email != null && password != null) {
            Staff staff = staffService.login(email, password);
            if (staff != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", "admin");
                json.addProperty("success", true);
            } else {
                json.addProperty("success", false);
            }
        } else {
            json.addProperty("success", false);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(json.toString());
        out.flush();
    }
}
