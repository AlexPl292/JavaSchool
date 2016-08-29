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

        if ("requiredFrom".equals(request.getParameter("type"))) {
            graph.addAttributeNodes("required", "forbidden", "requiredMe");
            Map<String, Object> hints = new HashMap<>();
            hints.put("javax.persistence.loadgraph", graph);

            Option connectedToOption = service.loadWithDependecies(id, hints);

            notForbidden = connectedToOption.getRequired();
            notRequiredMe = notRequiredFrom = connectedToOption.getForbidden();

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
}
