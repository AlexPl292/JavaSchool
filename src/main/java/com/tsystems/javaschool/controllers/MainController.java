package com.tsystems.javaschool.controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by alex on 19.08.16.
 */
@WebServlet({"/index", "/options", "/show_customers", "/show_tariffs"})
public class MainController extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getServletPath();
        if ("/options".equals(url))
            request.getRequestDispatcher("/WEB-INF/jsp/new_option.jsp").forward(request, response);
        else
            request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);

    }
}
