package com.tsystems.javaschool.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsystems.javaschool.business.services.implementations.CustomerServiceImpl;
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
@WebServlet({"/show_customers", "/show_tariffs"})
public class ShowTableController extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        GenericService service;

        String url = request.getServletPath();

        if ("/show_customers".equals(url)) {
            service = new CustomerServiceImpl();
        } else if ("/show_tariffs".equals(url)) {
            service = new TariffServiceImpl();
        } else {
            service = new CustomerServiceImpl();
        }

        long recordsTotal = service.countOfEntries();

        List entitiesList;
        JsonObject json = new JsonObject();
        String page = request.getParameter("page");
        int draw;

        if ("first".equals(page)) {
            draw = 1;
        } else if ("last".equals(page)) {
            draw = (int) Math.ceil(recordsTotal / 10.0);
        } else {
            if (page != null)
                draw = Integer.parseInt(page);
            else
                draw = 1; // TODO странная ошибка: иногда page приходит null. Более того, даже эта строчка не всегда помогает
        }
        entitiesList = service.getNEntries(10, (draw - 1) * 10);

        json.addProperty("draw", draw);
        json.addProperty("recordsTotal", recordsTotal);
        JsonElement element = new Gson().toJsonTree(entitiesList);
        json.add("data", element);


        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(json.toString());
        out.flush();
    }
}
