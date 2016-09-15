package com.tsystems.javaschool.controllers;

import com.tsystems.javaschool.business.services.implementations.ContractServiceImpl;
import com.tsystems.javaschool.business.services.interfaces.ContractService;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by alex on 07.09.16.
 *
 * Delete contract controller
 */
@WebServlet({"/admin/delete_contract", "/customer/delete_contract"})
public class DeleteController extends HttpServlet {

    private static final Logger logger = Logger.getLogger(DeleteController.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ContractService service = ContractServiceImpl.getInstance();

        Integer id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
            service.remove(id);
        } catch (NumberFormatException e) {
            logger.error("Exception while id converting", e);
        }
    }
}
