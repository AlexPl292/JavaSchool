package com.tsystems.javaschool.controllers;

import com.tsystems.javaschool.business.services.implementations.CustomerServiceImpl;
import com.tsystems.javaschool.business.services.interfaces.CustomerService;
import com.tsystems.javaschool.db.entities.Customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by alex on 06.09.16.
 */
@WebServlet("/customer")
public class ShowCustomerController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        CustomerService service = CustomerServiceImpl.getInstance();
        Customer customer = service.loadByKey(id);
        request.setAttribute("customer", customer);
        request.getRequestDispatcher("/WEB-INF/jsp/customer.jsp").forward(request, response);
    }
}
