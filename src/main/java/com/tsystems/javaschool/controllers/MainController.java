package com.tsystems.javaschool.controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by alex on 19.08.16.
 */
@WebServlet({"/index", "/options"})
public class MainController extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getServletPath();
        if ("/options".equals(url))
            req.getRequestDispatcher("/WEB-INF/jsp/new_option.jsp").forward(req, resp);
        else
            req.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(req, resp);
    }
}
