package com.tsystems.javaschool.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsystems.javaschool.business.services.implementations.CustomerServiceImpl;
import com.tsystems.javaschool.business.services.interfaces.CustomerService;
import com.tsystems.javaschool.db.entities.Customer;

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

    CustomerService service = new CustomerServiceImpl();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/show_customers.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long recordsTotal = service.countOfCustomers();
        List<Customer> cust;
        JsonObject json = new JsonObject();
        String page = request.getParameter("page");
        int draw;
        if ("first".equals(page)) {
            draw = 1;
        } else if ("last".equals(page)) {
            draw = (int) Math.ceil(recordsTotal / 10.0);
        } else {
            draw = Integer.parseInt(page);
        }
        cust = service.getNCustomers(10, (draw-1)*10);

        json.addProperty("draw", draw);
        json.addProperty("recordsTotal", recordsTotal);
        JsonElement element = new Gson().toJsonTree(cust);
        json.add("data", element);


        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(json.toString());
        out.flush();
    }
}
