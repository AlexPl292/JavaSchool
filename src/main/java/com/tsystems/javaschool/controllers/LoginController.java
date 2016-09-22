package com.tsystems.javaschool.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsystems.javaschool.db.entities.Staff;
import com.tsystems.javaschool.db.entities.User;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alex on 10.09.16.
 *
 */
//@WebServlet({"/login", "/sign_out", "/change_password"})
public class LoginController extends HttpServlet {

    private static final Logger logger = Logger.getLogger(LoginController.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/sign_out".equals(path)) {  // Sign out sends by "get" href
            HttpSession session = request.getSession();
            session.invalidate();
            try {
                response.sendRedirect("/login");
            } catch (IOException e){
                logger.error("Redirect exception", e);
            }
        } else if ("/change_password".equals(path)) {
            try {
                request.getRequestDispatcher("/WEB-INF/jsp/password_change.jsp").forward(request, response);
            } catch (IOException e) {
                logger.error("Forward exception", e);
            } catch (ServletException e) {
                logger.error("Servlet exception", e);
            }
        } else {
            try {
                request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
            } catch (IOException e) {
                logger.error("Forward exception", e);
            } catch (ServletException e) {
                logger.error("Servlet exception", e);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        HttpSession session = request.getSession();
        if ("/login".equals(path)) {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            JsonObject json = new JsonObject();


            User user = User.login(email, password);
            if (user != null) {
                session.setAttribute("id", user.getId());
                if (user instanceof Staff) // Define to which kind of user belongs this
                    session.setAttribute("user", "admin");
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
            } catch (IOException e) {
                logger.error("Get writer exception!", e);
            }
        } else if ("/change_password".equals(path)) {
            String oldPassword = request.getParameter("oldPassword");
            String newPassword = request.getParameter("newPassword");
            String repeatNewPassword = request.getParameter("repeatNewPassword");

            JsonObject json = new JsonObject();
            Map<String, String> errors = new HashMap<>();

            if (oldPassword == null || newPassword == null || repeatNewPassword == null) {
                errors.put("General", "Input is empty");
            } else if (newPassword.length() < 8) {
                errors.put("General", "Password is shorter than 8 characters");
            } else if (!newPassword.equals(repeatNewPassword)) {
                errors.put("General", "Passwords are different!");
            }

            String res = "";
            if (errors.isEmpty()) {
                res = User.updatePassword((Integer) session.getAttribute("id"), oldPassword, newPassword);
                if (!res.equals("Success!"))
                    errors.put("General", res);
            }

            if (!errors.isEmpty()) {
                JsonElement element = new Gson().toJsonTree(errors);
                json.addProperty("success", false);
                json.add("errors", element);
            } else {
                json.addProperty("success", true);
                json.addProperty("data", res);
            }


            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.print(json.toString());
                out.flush();
            } catch (IOException e) {
                logger.error("Get writer exception!", e);
            }
        }
    }
}
