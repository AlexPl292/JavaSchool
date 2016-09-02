package com.tsystems.javaschool.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsystems.javaschool.business.services.implementations.OptionServiceImpl;
import com.tsystems.javaschool.business.services.interfaces.OptionService;
import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.db.entities.Tariff;

import javax.persistence.EntityGraph;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by alex on 26.08.16.
 */
@WebServlet("/add_option")
public class AddOptionController extends HttpServlet {

    OptionService service = new OptionServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/index").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Option newOption = new Option();
        newOption.setName(request.getParameter("name"));
        newOption.setCost(new BigDecimal(request.getParameter("cost")));
        newOption.setConnectCost(new BigDecimal(request.getParameter("connect_cost")));
        newOption.setDescription(request.getParameter("description"));

        HashMap<String, String[]> dependencies = new HashMap<>();

        String[] requiredFrom;
        String[] requiredMe;
        String[] forbiddenWith;
        String[] forTariffs;
        if ((requiredFrom = request.getParameterValues("requiredFrom")) == null)
            requiredFrom = new String[0];
        if ((requiredMe = request.getParameterValues("requiredMe")) == null)
            requiredMe = new String[0];
        if ((forbiddenWith = request.getParameterValues("forbiddenWith")) == null)
            forbiddenWith = new String[0];
        if ((forTariffs = request.getParameterValues("forTariffs")) == null)
            forbiddenWith = new String[0]; // TODO а вот тут должна быть ошибка
        // Минимум одна опция должна быть выбрана

        dependencies.put("requiredFrom", requiredFrom);
        dependencies.put("requiredMe", requiredMe);
        dependencies.put("forbiddenWith", forbiddenWith);
        dependencies.put("forTariffs", forTariffs);

        service.addWithDependencies(newOption, dependencies);
    }
}
