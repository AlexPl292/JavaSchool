package com.tsystems.javaschool.controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by alex on 21.08.16.
 */
@WebServlet("/show_customers")
public class ShowCustomersController extends HttpServlet{

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/show_customers.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String json;
        if ("1".equals(request.getParameter("page"))) {
            json = "{\"draw\":1, \"recordsTotal\": 14, \"data\":[[\"h\",\"e\"],[\"h\",\"e\"],[\"h\",\"e\"]]}";
        } else {
            json = "{\"draw\":2, \"recordsTotal\": 14, \"data\":[[\"j\",\"h\"],[\"j\",\"h\"],[\"j\",\"l\"]]}";
        }

            response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
}
