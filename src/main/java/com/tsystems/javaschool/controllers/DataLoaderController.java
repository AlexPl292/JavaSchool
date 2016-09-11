package com.tsystems.javaschool.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsystems.javaschool.business.services.implementations.ContractServiceImpl;
import com.tsystems.javaschool.business.services.implementations.CustomerServiceImpl;
import com.tsystems.javaschool.business.services.implementations.OptionServiceImpl;
import com.tsystems.javaschool.business.services.implementations.TariffServiceImpl;
import com.tsystems.javaschool.business.services.interfaces.GenericService;
import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.db.implemetations.OptionDaoImpl;
import com.tsystems.javaschool.db.implemetations.TariffDaoImpl;

import javax.persistence.EntityGraph;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by alex on 23.08.16.
 * <p>
 * Loading data. Also with pagination.
 * request parameters:
 * page (int) - page of pagination
 * updateCount (bool) - make query to update count of entries or not
 * search (string) - search query
 * <p>
 * response parameters:
 * draw - page in pagination
 * data - returned data
 */
@WebServlet({"/admin/load_customers", "/load_tariffs", "/load_options_table", "/admin/load_contracts"})
public class DataLoaderController extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        GenericService service;
        String url = request.getServletPath();
        Boolean updateCount = Boolean.valueOf(request.getParameter("updateCount"));
        List entitiesList;
        JsonObject json = new JsonObject();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String page = request.getParameter("page");
        int draw = 1;
        long recordsTotal = -1;
        Map<String, Object> kwargs = new HashMap<>();
        String searchQuery = request.getParameter("search");
        kwargs.put("search", searchQuery);

        if ("/admin/load_customers".equals(url)) {
            service = CustomerServiceImpl.getInstance();
        } else if ("/load_tariffs".equals(url)) {
            service = TariffServiceImpl.getInstance();
        } else if ("/load_options_table".equals(url)) {
            service = OptionServiceImpl.getInstance();
        } else if ("/admin/load_contracts".equals(url)) {
            service = ContractServiceImpl.getInstance();
        } else {
            service = CustomerServiceImpl.getInstance();
        }

        if (updateCount) {
            recordsTotal = service.count(kwargs);
            json.addProperty("recordsTotal", recordsTotal);
        }


        if ("first".equals(page)) {
            draw = 1;
        } else if ("last".equals(page)) {
            recordsTotal = service.count(kwargs);
            draw = (int) Math.ceil(recordsTotal / 10.0);
        } else {
            if (page != null) // TODO странная ошибка: иногда page приходит null. Более того, даже эта строчка не всегда помогает
                draw = Integer.parseInt(page);
        }

        kwargs.put("maxEntries", 10);
        kwargs.put("firstIndex", (draw - 1) * 10);

        if ("-1".equals(page))
            entitiesList = service.loadAll();
        else {
            entitiesList = service.load(kwargs);
/*            if ("/load_tariffs".equals(url)) {
                json.addProperty("hasPopover", true);
                json.addProperty("popoverHeader", "Available options");
                JsonObject popoverJson = new JsonObject();
                for (Object tariff : entitiesList) {
                    popoverJson.add(((Tariff) tariff).getId().toString(), gson.toJsonTree(((Tariff) tariff).getPossibleOptions()));
                }
                json.add("popoverData", popoverJson);
            }*/
        }

        JsonElement element = gson.toJsonTree(entitiesList);
        json.addProperty("draw", draw);
        json.add("data", element);


        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(json.toString());
            out.flush();
        }
    }
}
