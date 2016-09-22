package com.tsystems.javaschool.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsystems.javaschool.business.services.implementations.ContractServiceImpl;
import com.tsystems.javaschool.business.services.implementations.CustomerServiceImpl;
import com.tsystems.javaschool.business.services.implementations.TariffServiceImpl;
import com.tsystems.javaschool.business.services.interfaces.ContractService;
import com.tsystems.javaschool.db.entities.Contract;
import com.tsystems.javaschool.db.entities.Customer;
import com.tsystems.javaschool.db.entities.Tariff;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by alex on 06.09.16.
 */
//@WebServlet("/admin/add_contract")
public class AddContractController extends HttpServlet {

    private final transient ContractService contractService = ContractServiceImpl.getInstance();
    private static final Logger logger = Logger.getLogger(AddContractController.class);


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String customerIdStr = request.getParameter("customer_id");
        String number = request.getParameter("number");
        JsonObject json = new JsonObject();

        Map<String, String> errors = new HashMap<>();
        String tmpError;
        if ((tmpError = Validator.phone(number)) != null)
            errors.put("number", tmpError);

        Integer tariffId = 0;
        Integer customerId = 0;
        try {
            tariffId = Integer.parseInt(request.getParameter("tariff"));
            customerId = Integer.parseInt(customerIdStr);
        } catch (NumberFormatException e) {
            errors.put("Number format", "Error in tariff or customer id");
            logger.error("Exception while id converting", e);
        }

        Contract contract = new Contract();
        if (errors.isEmpty()) {
//            Tariff tariff = TariffServiceImpl.getInstance().loadByKey(tariffId);
            Tariff tariff = null;
            Customer customer = CustomerServiceImpl.getInstance().loadByKey(customerId);

            contract.setCustomer(customer);
            contract.setNumber(number);
            contract.setTariff(tariff);
            contract.setIsBlocked(0);
            contract.setBalance(new BigDecimal(100));

            // Get list of option ids from parameter
            String[] optionsIdStr = request.getParameterValues("options");
            List<Integer> options;
            if (optionsIdStr != null) {
                options = Arrays.stream(optionsIdStr).map(Integer::parseInt).collect(Collectors.toList());
            } else {
                options = new ArrayList<>();
            }
            try {
                contract = contractService.addNew(contract, options);
            } catch (RollbackException e) {
                Throwable th = ExceptionUtils.getRootCause(e);
                errors.put("General", th.getMessage());
                logger.error("Exception while contract creating", th);
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
        } catch (IOException e){
            logger.error("Get writer exception!", e);
        }
    }
}
