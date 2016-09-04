package com.tsystems.javaschool.controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by alex on 02.09.16.
 */
@WebServlet("/load_content")
public class ContentLoaderController extends HttpServlet{

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getParameter("path");
        if ("/show_customers".equals(path))
            request.getRequestDispatcher("/WEB-INF/jsp/show_customers.jsp").forward(request, response);
        else if ("/add_customer".equals(path))
            request.getRequestDispatcher("/WEB-INF/jsp/new_customer.jsp").forward(request, response);
        else if ("/add_tariff".equals(path))
            request.getRequestDispatcher("/WEB-INF/jsp/new_tariff.jsp").forward(request, response);
        else if ("/show_tariffs".equals(path))
            request.getRequestDispatcher("/WEB-INF/jsp/show_tariffs.jsp").forward(request, response);
        else if ("/add_option".equals(path))
            request.getRequestDispatcher("/WEB-INF/jsp/new_option.jsp").forward(request, response);
        else if ("/show_options".equals(path))
            request.getRequestDispatcher("/WEB-INF/jsp/show_options.jsp").forward(request, response);
    }
}
