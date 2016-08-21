package com.tsystems.javaschool.controllers;

import com.tsystems.javaschool.business.services.implementations.TariffServiceImpl;
import com.tsystems.javaschool.business.services.interfaces.TariffService;
import com.tsystems.javaschool.db.entities.Tariff;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by alex on 21.08.16.
 */
@WebServlet("/add_tariff")
public class AddTariffController extends HttpServlet{

    private TariffService service = new TariffServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Tariff tariff = new Tariff();
        tariff.setName(request.getParameter("name"));
        tariff.setCost(new BigDecimal(request.getParameter("cost")));
        tariff.setDescription(request.getParameter("description"));

        service.addTariff(tariff);
    }
}
