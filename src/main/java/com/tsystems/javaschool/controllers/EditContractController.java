package com.tsystems.javaschool.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsystems.javaschool.business.services.implementations.ContractServiceImpl;
import com.tsystems.javaschool.db.entities.Contract;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by alex on 09.09.16.
 *
 * Controller for contract editing. Access for admin or customer
 */
//@WebServlet({"/admin/edit_contract", "/edit_contract"})
public class EditContractController extends HttpServlet {

    private static final Logger logger = Logger.getLogger(EditContractController.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JsonObject json = new JsonObject();
        Map<String, String> errors = new HashMap<>();

        Integer contractId = 0;
        Integer tariffId = 0;
        try {
            contractId = Integer.parseInt(request.getParameter("contract_id"));
            tariffId = Integer.parseInt(request.getParameter("tariff"));
        } catch (NumberFormatException e) {
            errors.put("General", "Contract or tariff id wrong format");
            logger.error("Exception while id converting", e);
        }

        String[] optionsIdStr = request.getParameterValues("optionsEdit" + contractId);
        List<Integer> options;
        if (optionsIdStr != null) {
            options = Arrays.stream(optionsIdStr).map(Integer::parseInt).collect(Collectors.toList());
        } else {
            options = new ArrayList<>();
        }
        Contract contract = ContractServiceImpl.getInstance().loadByKey(contractId);
        if (contract.getBalance().compareTo(BigDecimal.ZERO) == 0 || contract.getIsBlocked() != 0) {
            errors.put("Edit error", "You cannot edit options!");
        }
        if (errors.isEmpty()) {
            try {
                contract = ContractServiceImpl.getInstance().updateContract(contractId, tariffId, options);
            } catch (RollbackException e) {
                Throwable th = ExceptionUtils.getRootCause(e);
                errors.put("General", th.getMessage());
                logger.error("Exception while contract updating", th);
            }
        }

        if (!errors.isEmpty()) {
            JsonElement element = new Gson().toJsonTree(errors);
            json.addProperty("success", false);
            json.add("errors", element);
        } else {
            json.addProperty("success", true);
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JsonElement element = gson.toJsonTree(contract);
            element.getAsJsonObject().add("usedOptions", gson.toJsonTree(contract.getUsedOptions()));
            json.add("data", element);
        }


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
