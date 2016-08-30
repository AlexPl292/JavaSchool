package com.tsystems.javaschool.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsystems.javaschool.business.services.implementations.OptionServiceImpl;
import com.tsystems.javaschool.business.services.interfaces.OptionService;
import com.tsystems.javaschool.db.entities.Option;

import javax.persistence.EntityGraph;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by alex on 26.08.16.
 */
@WebServlet("/add_option")
public class AddOptionController extends HttpServlet {

    OptionService service = new OptionServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = Integer.parseInt(request.getParameter("data"));

        EntityGraph<Option> graph = service.getEntityGraph();
        Set<Option> notForbidden = new HashSet<>();
        Set<Option> notRequiredFrom = new HashSet<>();
        Set<Option> notRequiredMe = new HashSet<>();

        graph.addAttributeNodes("required", "forbidden", "requiredMe");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);

        Option connectedToOption = service.loadWithDependecies(id, hints);

        if ("requiredFrom".equals(request.getParameter("type"))) {
            notForbidden = connectedToOption.getRequired();
            notRequiredMe = notRequiredFrom = connectedToOption.getForbidden();  // Look carefully! There are two operations
        } else if ("requiredMe".equals(request.getParameter("type"))) {
            notRequiredFrom = connectedToOption.getForbidden();
            notForbidden = connectedToOption.getRequiredMe();
            notForbidden.addAll(connectedToOption.getRequired());
        } else {// "if forbiddenMe
            notRequiredMe = connectedToOption.getRequired();
            notRequiredFrom = connectedToOption.getRequiredMe();
            notRequiredMe.addAll(connectedToOption.getRequiredMe());
        }

        JsonObject json = new JsonObject();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        JsonElement elementNotForbidden = gson.toJsonTree(notForbidden);
        JsonElement elementNotRequiredFrom = gson.toJsonTree(notRequiredFrom);
        JsonElement elementNotRequiredMe = gson.toJsonTree(notRequiredMe);
        json.add("not_forbidden", elementNotForbidden);
        json.add("not_required_from", elementNotRequiredFrom);
        json.add("not_required_me", elementNotRequiredMe);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(json.toString());
        out.flush();
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
        if ((requiredFrom = request.getParameterValues("requiredFrom")) == null)
            requiredFrom = new String[0];
        if ((requiredMe = request.getParameterValues("requiredMe")) == null)
            requiredMe = new String[0];
        if ((forbiddenWith = request.getParameterValues("forbiddenWith")) == null)
            forbiddenWith = new String[0];


        dependencies.put("requiredFrom", requiredFrom);
        dependencies.put("requiredMe", requiredMe);
        dependencies.put("forbiddenWith", forbiddenWith);

        service.addWithDependencies(newOption, dependencies);
    }
}
