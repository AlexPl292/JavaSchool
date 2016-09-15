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
 * Controller for contract block and unblock
 */
@WebServlet({"/admin/blockContract", "/admin/unblockContract", "/customer/blockContract", "/customer/unblockContract"})
public class BlockController extends HttpServlet {

    private static final Logger logger = Logger.getLogger(BlockController.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ContractService service = ContractServiceImpl.getInstance();

        Integer id = 0;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            logger.error("Exception while id converting", e);
            return;
        }

        String url = request.getServletPath();
        if ("/admin/blockContract".equals(url))
            service.setBlock(id, 2);
        else if ("/customer/blockContract".equals(url))
            service.setBlock(id, 1);
        else
            service.setBlock(id, 0);
    }
}
