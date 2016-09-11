package com.tsystems.javaschool.controllers;

import com.tsystems.javaschool.business.services.implementations.CustomerServiceImpl;
import com.tsystems.javaschool.business.services.interfaces.CustomerService;
import com.tsystems.javaschool.db.entities.Contract;
import com.tsystems.javaschool.db.entities.Customer;

import javax.persistence.EntityGraph;
import javax.persistence.Subgraph;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * Created by alex on 11.09.16.
 */
@WebServlet("/customer")
public class CustomerController extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer id = (Integer) session.getAttribute("id");
        CustomerService service = CustomerServiceImpl.getInstance();

        EntityGraph<Customer> graph = service.getEntityGraph();
        Subgraph<Contract> tariffSubgraph = graph.addSubgraph("contracts");
        tariffSubgraph.addAttributeNodes("usedOptions");

        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);

        Customer customer = service.loadByKey(id, hints);
        Set<Contract> contractSet = new TreeSet<>(Comparator.comparing(Contract::getId).reversed());
        contractSet.addAll(customer.getContracts());
        customer.setContracts(contractSet);
        request.setAttribute("customer", customer);
        request.getRequestDispatcher("/WEB-INF/jsp/customer.jsp").forward(request, response);
    }
}
