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
            service.addNew(tariff);
        } else {
            response.addBindingResult(bindingResult);
        }

        return response;
    }
}

