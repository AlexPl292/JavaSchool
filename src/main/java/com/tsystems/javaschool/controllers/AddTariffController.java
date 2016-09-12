package com.tsystems.javaschool.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsystems.javaschool.business.services.implementations.TariffServiceImpl;
import com.tsystems.javaschool.business.services.interfaces.TariffService;
import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.util.Validator;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.persistence.RollbackException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by alex on 21.08.16.
 * Add new tariff
 * Returns json with either success:true, or success:false and object with errors
 */
@WebServlet("/admin/add_tariff")
public class AddTariffController extends HttpServlet {

    private final transient TariffService service = TariffServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/new_tariff.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, String> errors = new HashMap<>();
        JsonObject json = new JsonObject();

        String name = request.getParameter("name");
        String cost = request.getParameter("cost");
        String desc = request.getParameter("description");

        // Validation
        String tmpError;
        if (name == null)
            errors.put("name", "Enter name of new tariff");
        if ((tmpError = Validator.cost(cost)) != null)
            errors.put("cost", tmpError);

        if (errors.isEmpty()) {
            Tariff tariff = new Tariff();
            tariff.setName(name);
            tariff.setCost(new BigDecimal(cost));
            tariff.setDescription(desc);
            String[] options = request.getParameterValues("options");
            List<Integer> optionIds = new ArrayList<>();
            if (options != null)
                optionIds = Arrays.stream(options).map(Integer::parseInt).collect(Collectors.toList());
            try {
                service.addNew(tariff, optionIds);
            } catch (RollbackException e) {
                Throwable th = ExceptionUtils.getRootCause(e);
                errors.put("General", th.getMessage());
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
