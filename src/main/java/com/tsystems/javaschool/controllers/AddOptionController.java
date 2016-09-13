package com.tsystems.javaschool.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsystems.javaschool.business.services.implementations.OptionServiceImpl;
import com.tsystems.javaschool.business.services.interfaces.OptionService;
import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.util.Validator;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import javax.persistence.RollbackException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alex on 26.08.16.
 * Add new Option
 * Returns json with either success:true, or success:false and object with errors
 */
@WebServlet("/admin/add_option")
public class AddOptionController extends HttpServlet {

    private final transient OptionService service = OptionServiceImpl.getInstance();
    private final static Logger logger = Logger.getLogger(AddOptionController.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/new_option.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, String> errors = new HashMap<>();
        JsonObject json = new JsonObject();

        String name = request.getParameter("name");
        String cost = request.getParameter("cost");
        String connectCost = request.getParameter("connect_cost");
        String desc = request.getParameter("description");

        // Validation
        String tmpError;
        if (name == null)
            errors.put("name", "Enter name of new option");
        if ((tmpError = Validator.cost(cost)) != null)
            errors.put("cost", tmpError);
        if ((tmpError = Validator.cost(connectCost)) != null)
            errors.put("connectCost", tmpError);
        if (request.getParameterValues("forTariffs") == null)
            errors.put("tariffs", "Choose at least one tariff");

        if (errors.isEmpty()) {
            Option newOption = new Option();
            newOption.setName(name);
            newOption.setCost(new BigDecimal(cost));
            newOption.setConnectCost(new BigDecimal(connectCost));
            newOption.setDescription(desc);

            HashMap<String, String[]> dependencies = new HashMap<>();

            String[] requiredFrom;
            String[] forbiddenWith;
            String[] forTariffs;
            if ((requiredFrom = request.getParameterValues("requiredFrom")) == null)
                requiredFrom = new String[0];
            if ((forbiddenWith = request.getParameterValues("forbiddenWith")) == null)
                forbiddenWith = new String[0];
            forTariffs = request.getParameterValues("forTariffs");

            dependencies.put("requiredFrom", requiredFrom);
            dependencies.put("forbiddenWith", forbiddenWith);
            dependencies.put("forTariffs", forTariffs);

            try {
                service.addNew(newOption, dependencies);
            } catch (RollbackException e) {
                Throwable th = ExceptionUtils.getRootCause(e);
                errors.put("General", th.getMessage());
                logger.error("Exception while option creating", th);
            }
        }

        if (!errors.isEmpty()) {
            JsonElement element = new Gson().toJsonTree(errors);
            json.addProperty("success", false);
            json.add("errors", element);
        } else {
            json.addProperty("success", true);
        }


        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(json.toString());
            out.flush();
        }
    }
}
