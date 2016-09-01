package com.tsystems.javaschool.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsystems.javaschool.business.services.implementations.OptionServiceImpl;
import com.tsystems.javaschool.business.services.interfaces.OptionService;
import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.db.entities.Tariff;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by alex on 01.09.16.
 */
@WebServlet("/load_options_by_tariffs")
public class OptionLoaderController extends HttpServlet{

    OptionService service = new OptionServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Integer> tariffs = Arrays.stream(request.getParameterValues("forTariffs")).map(Integer::parseInt).collect(Collectors.toList());
        List<Option> options = service.loadOptionsByTariffs(tariffs);

        JsonObject json = new JsonObject();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        JsonElement element = gson.toJsonTree(options);
        json.add("data", element);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(json.toString());
        out.flush();
    }
}
