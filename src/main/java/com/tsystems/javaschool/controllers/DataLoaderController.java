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
import org.apache.log4j.Logger;

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
//@WebServlet({"/admin/load_customers", "/load_tariffs", "/load_options_table", "/admin/load_contracts"})
public class DataLoaderController extends HttpServlet {

    private static final Logger logger = Logger.getLogger(DataLoaderController.class);

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

        if ("/admin/load_customers".equals(url)) {  // Get service depends on path
            service = CustomerServiceImpl.getInstance();
        } else if ("/load_tariffs".equals(url)) {
            service = new TariffServiceImpl(null);// = TariffServiceImpl.getInstance();
        } else if ("/load_options_table".equals(url)) {
            service = OptionServiceImpl.getInstance();
        } else if ("/admin/load_contracts".equals(url)) {
            service = ContractServiceImpl.getInstance();
        } else {
            service = CustomerServiceImpl.getInstance();
        }

        if (updateCount) {  // Should we update count of all users or not
            recordsTotal = service.count(kwargs);
            json.addProperty("recordsTotal", recordsTotal);
        }


        if ("first".equals(page)) {  // Define accessible page
            draw = 1;
        } else if ("last".equals(page)) {
            recordsTotal = service.count(kwargs);
            draw = (int) Math.ceil(recordsTotal / 10.0);
        } else {
            if (page != null) { // TODO странная ошибка: иногда page приходит null. Более того, даже эта строчка не всегда помогает
                try {
                    draw = Integer.parseInt(page);
                } catch (NumberFormatException e) {
                    logger.error("Exception while page converting", e);
                    draw = 1;
                }
            }
        }

        kwargs.put("maxEntries", 10);
        kwargs.put("firstIndex", (draw - 1) * 10);

        if ("-1".equals(page))
            entitiesList = service.loadAll();
        else {
            entitiesList = service.load(kwargs);
        }

        JsonElement element = gson.toJsonTree(entitiesList);
        json.addProperty("draw", draw);
        json.add("data", element);


        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(json.toString());
            out.flush();
        } catch (IOException e) {
            logger.error("Get writer exception!", e);
        }
    }
}
