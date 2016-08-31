package com.tsystems.javaschool.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsystems.javaschool.business.services.implementations.CustomerServiceImpl;
import com.tsystems.javaschool.business.services.implementations.OptionServiceImpl;
import com.tsystems.javaschool.business.services.implementations.TariffServiceImpl;
import com.tsystems.javaschool.business.services.interfaces.GenericService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by alex on 23.08.16.
 */
@WebServlet({"/load_customers", "/load_tariffs", "/load_options"})
public class DataLoaderController extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        GenericService service;
        String url = request.getServletPath();
        Boolean updateCount = Boolean.valueOf(request.getParameter("updateCount"));
        List entitiesList;
        JsonObject json = new JsonObject();
        String page = request.getParameter("page");
        int draw = 1;
        long recordsTotal = -1;
        String searchQuery = request.getParameter("search");

        if ("/load_customers".equals(url)) {
            service = new CustomerServiceImpl();
        } else if ("/load_tariffs".equals(url)) {
            service = new TariffServiceImpl();
        } else if ("/load_options".equals(url)) {
            service = new OptionServiceImpl();
        } else {
            service = new CustomerServiceImpl();
        }

        if (updateCount) {
            recordsTotal = service.countOfEntries(searchQuery);
            json.addProperty("recordsTotal", recordsTotal);
        }


        if ("first".equals(page)) {
            draw = 1;
        } else if ("last".equals(page)) {
            if (recordsTotal == -1)
                recordsTotal = service.countOfEntries(searchQuery);
            draw = (int) Math.ceil(recordsTotal / 10.0);
        } else {
            if (page != null) // TODO странная ошибка: иногда page приходит null. Более того, даже эта строчка не всегда помогает
                draw = Integer.parseInt(page);
        }
        if ("-1".equals(page))
            entitiesList = service.loadAll();
        else
            entitiesList = service.getNEntries(10, (draw - 1) * 10, searchQuery);

        JsonElement element = new Gson().toJsonTree(entitiesList);
        json.addProperty("draw", draw);
        json.add("data", element);


        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(json.toString());
        out.flush();
    }
}
