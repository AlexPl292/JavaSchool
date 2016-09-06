package com.tsystems.javaschool.controllers;

import com.tsystems.javaschool.business.services.implementations.ContractServiceImpl;
import com.tsystems.javaschool.business.services.interfaces.ContractService;
import com.tsystems.javaschool.db.entities.Contract;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by alex on 07.09.16.
 */
@WebServlet({"/blockContract", "/unblockContract"})
public class BlockController extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ContractService service = new ContractServiceImpl();
        Integer id = Integer.parseInt(request.getParameter("id"));

        String url = request.getServletPath();
        if ("/blockContract".equals(url))
            service.setBlock(id, 2);
        else
            service.setBlock(id, 0);
    }
}
