package com.tsystems.javaschool.controllers;

import com.tsystems.javaschool.business.services.implementations.ContractServiceImpl;
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
 * Created by alex on 06.09.16.
 *
 * Controller for customer access from customer or contract table
 */
@WebServlet({"/admin/customer", "/admin/contract", "/customer"})
public class ShowCustomerController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        HttpSession session = request.getSession();
        Integer id;
        if ("/customer".equals(path)) {
            id = (Integer) session.getAttribute("id");
        } else {
            id = Integer.parseInt(request.getParameter("id"));
        }

        if ("/admin/contract".equals(path)) {  // path = /admin/contract. Just get customer id and redirect to customer page
            Contract contract = ContractServiceImpl.getInstance().loadByKey(id);
            response.sendRedirect("/admin/customer?id=" + contract.getCustomer().getId());
        } else {
            CustomerService service = CustomerServiceImpl.getInstance();

            EntityGraph<Customer> graph = service.getEntityGraph();
            Subgraph<Contract> tariffSubgraph = graph.addSubgraph("contracts"); // Fetch all contracts of user
            tariffSubgraph.addAttributeNodes("usedOptions");  // And all used options of every contract

            Map<String, Object> hints = new HashMap<>();
            hints.put("javax.persistence.loadgraph", graph);

            Customer customer = service.loadByKey(id, hints);
            /*
            Because of set usage, if any field changed, order would change too. That is not good.
            So, we need to order it's manually. Why reversed? By new contract adding, we put it on the top of all contracts.
             */
            Set<Contract> contractSet = new TreeSet<>(Comparator.comparing(Contract::getId).reversed());

            contractSet.addAll(customer.getContracts());
            customer.setContracts(contractSet);
            request.setAttribute("customer", customer);
            request.getRequestDispatcher("/WEB-INF/jsp/customer.jsp").forward(request, response);
        }
    }
}
