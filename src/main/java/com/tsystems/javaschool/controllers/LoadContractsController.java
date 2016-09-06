package com.tsystems.javaschool.controllers;

import com.tsystems.javaschool.business.services.implementations.ContractServiceImpl;
import com.tsystems.javaschool.business.services.implementations.CustomerServiceImpl;
import com.tsystems.javaschool.business.services.interfaces.ContractService;
import com.tsystems.javaschool.business.services.interfaces.CustomerService;
import com.tsystems.javaschool.db.entities.Contract;

import javax.persistence.EntityGraph;
import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alex on 06.09.16.
 */
@WebServlet("/load_contracts_of_customer")
public class LoadContractsController extends HttpServlet{

    CustomerService service = new CustomerServiceImpl();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));

        service.loadByKey(id);
    }
}
