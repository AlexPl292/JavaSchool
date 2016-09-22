package com.tsystems.javaschool.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsystems.javaschool.business.dto.TariffDto;
import com.tsystems.javaschool.business.services.implementations.TariffServiceImpl;
import com.tsystems.javaschool.business.services.interfaces.TariffService;
import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.util.StatusResponse;
import com.tsystems.javaschool.util.Validator;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.RollbackException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
@Controller
public class AddTariffController {

    private final transient TariffService service;
    private static final Logger logger = Logger.getLogger(AddTariffController.class);

    @Autowired
    public AddTariffController(TariffService service) {
        this.service = service;
    }

    @GetMapping("/admin/add_tariff")
    public String getNewTariffPage() {
        return "new_tariff";
    }

    @PostMapping(value = "/admin/add_tariff", produces="application/json")
    @ResponseBody
    public StatusResponse addNewTariff(@Valid @RequestBody TariffDto tariff, BindingResult bindingResult) {
        StatusResponse response = new StatusResponse();

        if (!bindingResult.hasErrors()) {
            response.setSuccess(true);
        } else {
            response.addBindingResult(bindingResult);
            response.setSuccess(false);
        }

        return response;
/*        String name = request.getParameter("name");
        String cost = request.getParameter("cost");
        String desc = request.getParameter("description");*/

        // Validation
        /*
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
//            String[] options = request.getParameterValues("options");
            List<Integer> optionIds = new ArrayList<>();
            if (options != null)
                optionIds = Arrays.stream(options).map(Integer::parseInt).collect(Collectors.toList());
            try {
                service.addNew(tariff, optionIds);
            } catch (RollbackException e) {
                Throwable th = ExceptionUtils.getRootCause(e);
                errors.put("General", th.getMessage());
                logger.error("Exception while tariff creating", th);
            }
        }
        if (!errors.isEmpty()) {
            JsonElement element = new Gson().toJsonTree(errors);
            json.addProperty("success", false);
            json.add("errors", element);
        } else {
            json.addProperty("success", true);
        }


        return json.toString();
        */
    }
}

