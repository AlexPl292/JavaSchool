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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by alex on 19.08.16.
 */
@WebServlet("/add_customer")
public class AddCustomerController extends HttpServlet {

    private CustomerService service = new CustomerServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Customer newCustomer = new Customer();
        newCustomer.setName(request.getParameter("name"));
        newCustomer.setSurname(request.getParameter("surname"));
        Date birthday;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            birthday = sdf.parse(request.getParameter("birthday"));
        } catch (ParseException e) {
            birthday = new Date(0);
        }
        newCustomer.setDate_of_birth(birthday);
        newCustomer.setPassport_data(request.getParameter("passport"));
        newCustomer.setAddress(request.getParameter("address"));
        newCustomer.setEmail(request.getParameter("email"));
        newCustomer.setIs_blocked(0);
        service.addCustomer(newCustomer);
    }
}
