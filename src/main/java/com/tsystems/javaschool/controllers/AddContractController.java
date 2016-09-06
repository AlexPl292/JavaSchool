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

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by alex on 06.09.16.
 */
@WebServlet("/add_contract")
public class AddContractController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String customerId = request.getParameter("customer_id");
        String number = request.getParameter("number");
        JsonObject json = new JsonObject();

        Map<String, String> errors = new HashMap<>();
        String tmpError;
        if ((tmpError = Validator.phone(number)) != null)
            errors.put("number", tmpError);


        Contract contract = new Contract();
        if (errors.isEmpty()) {
            Tariff tariff = new TariffServiceImpl().loadByKey(Integer.parseInt(request.getParameter("tariff")));
            Customer customer = new CustomerServiceImpl().loadByKey(Integer.parseInt(customerId));

            contract.setCustomer(customer);
            contract.setNumber(number);
            contract.setTariff(tariff);
            contract.setIsBlocked(0);

            // Get list of option ids from parameter
            List<Integer> options = Arrays.stream(request.getParameterValues("options")).map(Integer::parseInt).collect(Collectors.toList());
            // Ждем, что мне ответят по транзакциям
            ContractService contractService = new ContractServiceImpl();
            contract = contractService.addNew(contract, options);
        }
        if (!errors.isEmpty()) {
            JsonElement element = new Gson().toJsonTree(errors);
            json.addProperty("success", false);
            json.add("errors", element);
        } else {
            json.addProperty("success", true);
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JsonElement element = gson.toJsonTree(contract);
            json.add("data", element);
        }


        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(json.toString());
        out.flush();
    }
}
